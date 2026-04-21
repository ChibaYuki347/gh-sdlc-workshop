# 契約更新通知機能 — 実装計画

## 1. 変更対象ファイル一覧

### 新規作成

| # | ファイルパス | 目的 |
|---|---|---|
| 1 | `service/RenewalNotificationService.java` | 更新通知のビジネスロジック |
| 2 | `controller/RenewalNotificationController.java` | REST API エンドポイント |
| 3 | `test/.../service/RenewalNotificationServiceTest.java` | Service層の単体テスト |
| 4 | `test/.../controller/RenewalNotificationControllerTest.java` | Controller層の統合テスト |

### 既存修正

| # | ファイルパス | 変更内容 |
|---|---|---|
| 5 | `repository/CustomerRepository.java` | 更新間近の顧客を取得するカスタムクエリ2件を追加 |

### 変更なし（参照のみ）

| ファイル | 理由 |
|---|---|
| `model/Customer.java` | 既存の `policyEndDate`, `policyStatus`, `agentName` をそのまま使用 |
| `model/Policy.java` | 今回は `customers` テーブルのみで完結（スキーマ変更不要） |
| `service/CustomerService.java` | God Class の肥大化を防ぐため、変更しない |
| `controller/CustomerController.java` | 既存APIに影響を与えない |

## 2. アーキテクチャ方針

| 判断 | 理由 |
|---|---|
| **新規 Service クラスに分離** | 既存の `CustomerService` は God Class（保険料計算・レポート・通知等が混在）。新しい責務を追加せず、`RenewalNotificationService` として独立させる |
| **`customers` テーブルのみ使用** | 現状の `Customer` モデルに `policyEndDate`, `policyStatus`, `agentName` が含まれているため、`Policy` テーブルとのJOINは不要 |
| **DB側で絞り込み** | 既存の `searchCustomers()` のような全件取得→Javaフィルタの非効率パターンを避け、JPQLで絞り込む |
| **残日数はService層で計算** | `daysUntilExpiry` はDBで計算するとDB依存が発生するため、Service層で算出する |
| **新規エンドポイント `/api/notifications/renewals`** | 既存の `/api/customers` とは異なるリソースのため、別パスとする |

## 3. 実装ステップ

依存関係を考慮し、以下の順序で実装する。

### Step 1: Repository — カスタムクエリの追加

**対象**: `repository/CustomerRepository.java`

```java
@Query("SELECT c FROM Customer c WHERE c.policyStatus = 1 " +
       "AND c.policyEndDate IS NOT NULL " +
       "AND c.policyEndDate BETWEEN :today AND :expiryDate " +
       "ORDER BY c.policyEndDate ASC")
List<Customer> findUpcomingRenewals(
    @Param("today") Date today,
    @Param("expiryDate") Date expiryDate);

@Query("SELECT c FROM Customer c WHERE c.policyStatus = 1 " +
       "AND c.policyEndDate IS NOT NULL " +
       "AND c.policyEndDate BETWEEN :today AND :expiryDate " +
       "AND c.agentName = :agentName " +
       "ORDER BY c.policyEndDate ASC")
List<Customer> findUpcomingRenewalsByAgent(
    @Param("today") Date today,
    @Param("expiryDate") Date expiryDate,
    @Param("agentName") String agentName);
```

**依存**: なし（最初に実装）

### Step 2: Service — ビジネスロジックの実装

**対象**: 新規 `service/RenewalNotificationService.java`

主な責務:
- `daysBeforeExpiry` から対象期間（今日 〜 今日+N日）を算出
- `agentName` の有無に応じてリポジトリのクエリを切り替え
- 各顧客の `daysUntilExpiry`（残日数）を計算
- レスポンス用の `Map<String, Object>` を組み立てる（`totalCount`, `daysBeforeExpiry`, `renewals`）
- `daysBeforeExpiry` のバリデーション（1〜365の範囲チェック）

**依存**: Step 1（Repository）

### Step 3: Controller — REST エンドポイントの実装

**対象**: 新規 `controller/RenewalNotificationController.java`

```
@RestController
@RequestMapping("/api/notifications")
```

- `GET /api/notifications/renewals` エンドポイント
- `@RequestParam(defaultValue = "30") int daysBeforeExpiry`
- `@RequestParam(required = false) String agentName`
- バリデーションエラー時は `400 Bad Request` を返却
- 空の `agentName` はフィルタなしとして扱う

**依存**: Step 2（Service）

### Step 4: 単体テスト — Service層

**対象**: 新規 `test/.../service/RenewalNotificationServiceTest.java`

- Repository をモック化し、Service のロジックを検証
- テストケース: 正常系（対象あり）、対象0件、担当者フィルタ、残日数計算、バリデーションエラー

**依存**: Step 2（Service）

### Step 5: 統合テスト — Controller層

**対象**: 新規 `test/.../controller/RenewalNotificationControllerTest.java`

- `@SpringBootTest` + `MockMvc` でエンドポイントを検証
- テストケース: 200 OK レスポンス、パラメータ指定、400 Bad Request、空結果

**依存**: Step 3（Controller）

## 4. テスト戦略

### 単体テスト（`RenewalNotificationServiceTest`）

| # | テストケース | 検証内容 |
|---|---|---|
| 1 | 更新間近の顧客が存在する | 正しい件数・ソート順で返却される |
| 2 | 対象顧客が0件 | 空リスト + `totalCount: 0` が返る |
| 3 | `agentName` フィルタ指定 | 該当担当者の顧客のみ返る |
| 4 | `agentName` が空文字/null | 全担当者が対象になる |
| 5 | `daysUntilExpiry` の計算 | 当日=0日、翌日=1日を正しく算出 |
| 6 | `daysBeforeExpiry` が0以下 | `IllegalArgumentException` がスローされる |
| 7 | `daysBeforeExpiry` が365超過 | `IllegalArgumentException` がスローされる |

### 統合テスト（`RenewalNotificationControllerTest`）

| # | テストケース | 検証内容 |
|---|---|---|
| 1 | `GET /api/notifications/renewals` | 200 OK + JSON構造の検証 |
| 2 | `?daysBeforeExpiry=60` | パラメータが反映される |
| 3 | `?agentName=佐藤花子` | フィルタが機能する |
| 4 | `?daysBeforeExpiry=0` | 400 Bad Request |
| 5 | `?daysBeforeExpiry=500` | 400 Bad Request |
| 6 | 既存API `GET /api/customers` | 影響がないことを確認 |

### テスト環境

- **JUnit 5** + **Spring Boot Test**（`spring-boot-starter-test` は `pom.xml` に既存）
- **H2 インメモリDB** + テスト用データ（`data.sql` が自動投入される）
- Mockito による Repository のモック化（単体テスト）

## 5. リスクと制約

| リスク | 影響 | 対策 |
|---|---|---|
| `Customer` モデルの `policyEndDate` が `java.util.Date` | 日付計算で時刻成分が混入する可能性 | `Calendar` または日付のみの比較で対応。将来的には `LocalDate` への移行を推奨 |
| `customers` テーブルに保険契約情報が混在（レガシー設計） | 本来は `policies` テーブルを参照すべきだが、現状の設計に従う | 仕様書に「既存テーブルのみ使用」と明記済み。将来のリファクタリング時に `Policy` テーブルへ移行 |
| 大量データ時のパフォーマンス | DB側で絞り込むため問題なし | `policyEndDate` + `policyStatus` の複合インデックスを将来的に検討 |
| 既存APIへの影響 | 新規パス `/api/notifications/` のため影響なし | 統合テストで既存エンドポイントの動作も確認 |

## 6. 未決事項（Open Questions）

| # | 項目 | 補足 |
|---|---|---|
| 1 | **ページネーション** | 対象顧客が大量の場合、ページネーションが必要か？（初期リリースでは全件返却とする） |
| 2 | **`policies` テーブルとの統合** | 現在は `customers` テーブルの `policyEndDate` を使用。将来的に `policies` テーブルの `endDate` に移行するか？ |
| 3 | **認証・認可** | 営業担当者が自分の顧客のみ閲覧可能にする制限は必要か？（現状は認証なし） |
| 4 | **通知の自動実行** | 定期バッチ（スケジューラ）で自動通知を行うか？（Nice to Have として仕様に記載済み、初期リリースではAPI提供のみ） |
