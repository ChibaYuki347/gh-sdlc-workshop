# 実装計画 — 契約更新通知機能

> **対象仕様書**: [specs/renewal-notification/spec.md](../renewal-notification/spec.md)
> **作成日**: 2026-04-27

---

## 1. 変更対象ファイル一覧

| # | ファイル | パス | 変更種別 | 変更内容 |
|---|---------|------|---------|---------|
| 1 | `CustomerRepository.java` | `repository/CustomerRepository.java` | 既存修正 | 更新対象顧客を検索するJPQLクエリメソッドを追加 |
| 2 | `CustomerService.java` | `service/CustomerService.java` | 既存修正 | 更新通知のビジネスロジックメソッドを追加 |
| 3 | `CustomerController.java` | `controller/CustomerController.java` | 既存修正 | `GET /api/customers/renewals` エンドポイントを追加 |
| 4 | `RenewalNotificationResponse.java` | `dto/RenewalNotificationResponse.java` | **新規作成** | レスポンス用DTOクラス（renewals, totalCount, searchCriteria） |
| 5 | `RenewalCustomerDto.java` | `dto/RenewalCustomerDto.java` | **新規作成** | 更新対象顧客1件分のDTOクラス（daysUntilRenewal を含む） |
| 6 | `CustomerControllerTest.java` | `test/.../controller/CustomerControllerTest.java` | **新規作成** | 統合テスト（MockMvc） |
| 7 | `CustomerServiceTest.java` | `test/.../service/CustomerServiceTest.java` | **新規作成** | サービス層の単体テスト |
| 8 | `data.sql` | `resources/data.sql` | 既存修正 | テスト検証用に更新日が近い顧客データを追加（任意） |

> **ベースパス**: `app/src/main/java/com/example/crm/`
> **テストパス**: `app/src/test/java/com/example/crm/`

---

## 2. アーキテクチャ方針

### 2.1 既存パターンへの準拠

既存の `CustomerController → CustomerService → CustomerRepository` の3層構造に従い、新たなクラスの乱立を避ける。更新通知は「顧客ドメイン」の機能拡張として位置づける。

### 2.2 DTOの導入

既存コードはエンティティを直接返却しているが、本機能では `daysUntilRenewal` という算出フィールドが必要なため、レスポンス専用のDTOクラスを導入する。

- `RenewalCustomerDto` — 顧客1件分のデータ + `daysUntilRenewal`
- `RenewalNotificationResponse` — レスポンス全体（`renewals` 配列 + `totalCount` + `searchCriteria`）

### 2.3 クエリ戦略

Spring Data JPA の `@Query` アノテーション（JPQL）を使用する。
- `policyStatus = 1` かつ `policyEndDate IS NOT NULL` かつ `policyEndDate <= :deadline` の条件でDB側フィルタリング
- `agentName` フィルタは JPQL の条件式（`:agentName IS NULL OR agentName = :agentName`）で対応
- ソートは `ORDER BY policyEndDate ASC` でDB側で実行

### 2.4 日付処理

現行コードは `java.util.Date` を使用しているため、日数計算は `java.time.LocalDate` / `java.time.temporal.ChronoUnit` に変換して行う。エンティティの型自体は変更しない。

### 2.5 バリデーション

コントローラ層で `daysUntilRenewal` パラメータの範囲チェック（1〜365）を行い、不正な値には `ResponseEntity.badRequest()` を返す。

---

## 3. 実装ステップ

依存関係を考慮し、下位レイヤー（Repository）から上位レイヤー（Controller）へ向かって実装する。

### Step 1: DTOクラスの作成

**対象**: `RenewalCustomerDto.java`, `RenewalNotificationResponse.java`

| 作業 | 詳細 |
|------|------|
| `RenewalCustomerDto` を作成 | `customerId`, `firstName`, `lastName`, `fullName`, `email`, `phone`, `policyNumber`, `policyType`, `policyStatus`, `policyStatusText`, `premiumAmount`, `policyStartDate`, `policyEndDate`, `daysUntilRenewal`, `agentName`, `agentEmail` フィールドを定義 |
| `RenewalNotificationResponse` を作成 | `renewals`（List\<RenewalCustomerDto\>）, `totalCount`（int）, `searchCriteria`（Map or 内部クラス）を定義 |

**完了条件**: DTOクラスがコンパイルできること

### Step 2: Repository層の拡張

**対象**: `CustomerRepository.java`

| 作業 | 詳細 |
|------|------|
| JPQLクエリメソッドを追加 | `findRenewalTargets(int policyStatus, Date deadline)` — 有効な契約で、更新日が期限内の顧客を返す |
| 担当者フィルタ付きクエリを追加 | `findRenewalTargetsByAgent(int policyStatus, Date deadline, String agentName)` — 上記 + 担当者名フィルタ |

```java
@Query("SELECT c FROM Customer c WHERE c.policyStatus = :status " +
       "AND c.policyEndDate IS NOT NULL " +
       "AND c.policyEndDate <= :deadline " +
       "ORDER BY c.policyEndDate ASC")
List<Customer> findRenewalTargets(@Param("status") int status, @Param("deadline") Date deadline);

@Query("SELECT c FROM Customer c WHERE c.policyStatus = :status " +
       "AND c.policyEndDate IS NOT NULL " +
       "AND c.policyEndDate <= :deadline " +
       "AND c.agentName = :agentName " +
       "ORDER BY c.policyEndDate ASC")
List<Customer> findRenewalTargetsByAgent(@Param("status") int status, @Param("deadline") Date deadline, @Param("agentName") String agentName);
```

**完了条件**: クエリが H2 上で正しく実行されること

### Step 3: Service層のビジネスロジック追加

**対象**: `CustomerService.java`

| 作業 | 詳細 |
|------|------|
| `getRenewalNotifications(int days, String agentName)` メソッドを追加 | 期限日を算出し、Repository を呼び出し、結果を DTO に変換して返す |
| `daysUntilRenewal` の算出ロジック | `policyEndDate` と現在日の差分を `ChronoUnit.DAYS.between()` で計算 |
| Customer → RenewalCustomerDto の変換 | 各フィールドのマッピングとステータステキスト変換を行う |

**処理フロー**:
```
1. 現在日 + daysUntilRenewal 日 = deadline を算出
2. agentName の有無で Repository の呼び出しメソッドを分岐
3. 取得した Customer リストを RenewalCustomerDto リストに変換
4. RenewalNotificationResponse を組み立てて返却
```

**完了条件**: Service メソッドが正しい結果を返すこと（単体テストで検証）

### Step 4: Controller層のエンドポイント追加

**対象**: `CustomerController.java`

| 作業 | 詳細 |
|------|------|
| `GET /api/customers/renewals` を追加 | `@GetMapping("/renewals")` メソッドを定義 |
| パラメータバインディング | `@RequestParam(defaultValue = "30") int daysUntilRenewal`, `@RequestParam(required = false) String agentName` |
| バリデーション | `daysUntilRenewal` が1未満または365超の場合、400エラーを返す |
| 空文字の正規化 | `agentName` が空文字の場合は `null` に変換（フィルタなしとして扱う） |

**完了条件**: curl / HTTPクライアントでエンドポイントが正常応答すること

### Step 5: テストの作成

**対象**: `CustomerServiceTest.java`, `CustomerControllerTest.java`

| 作業 | 詳細 |
|------|------|
| サービス単体テスト | Repository をモック化し、ビジネスロジック（日数計算、DTO変換、フィルタ分岐）を検証 |
| コントローラ統合テスト | MockMvc を使い、HTTP リクエスト → レスポンスの E2E 検証（正常系・異常系・境界値） |

詳細は「4. テスト戦略」を参照。

### Step 6: データ確認・動作検証

| 作業 | 詳細 |
|------|------|
| 初期データの確認 | `data.sql` の既存データで更新対象が存在するか確認。必要に応じてテスト用データを追加 |
| 手動動作検証 | アプリ起動後、`GET /api/customers/renewals` を実行し、期待通りの結果が返ることを確認 |

---

## 4. テスト戦略

### 4.1 単体テスト（`CustomerServiceTest.java`）

| テストケース | 検証内容 |
|------------|---------|
| 正常系: デフォルト日数 | 30日以内の有効顧客が取得されること |
| 正常系: カスタム日数 | 指定日数が正しく反映されること |
| 正常系: 担当者フィルタ | agentName 指定時に Repository の分岐が正しいこと |
| 正常系: 該当0件 | 空リストと totalCount=0 が返ること |
| daysUntilRenewal 算出 | 日数差分の計算が正しいこと（未来日=正、過去日=負、当日=0） |
| DTO変換 | Customer エンティティから DTO への全フィールドマッピングが正しいこと |
| policyStatusText 変換 | ステータス値に応じたテキスト変換が正しいこと |

**テスト技法**: `@ExtendWith(MockitoExtension.class)` で Repository をモック化

### 4.2 統合テスト（`CustomerControllerTest.java`）

| テストケース | HTTP | 検証内容 |
|------------|------|---------|
| AC-1: 基本取得 | `GET /api/customers/renewals` | 200 OK、renewals 配列を含むレスポンス |
| AC-2: 日数指定 | `GET /api/customers/renewals?daysUntilRenewal=60` | 指定日数での検索結果 |
| AC-2: 不正日数（0以下） | `GET /api/customers/renewals?daysUntilRenewal=0` | 400 Bad Request |
| AC-2: 不正日数（366以上） | `GET /api/customers/renewals?daysUntilRenewal=400` | 400 Bad Request |
| AC-3: 担当者フィルタ | `GET /api/customers/renewals?agentName=佐藤花子` | 該当担当者のみ返却 |
| AC-3: 存在しない担当者 | `GET /api/customers/renewals?agentName=不存在` | 空リスト（200 OK） |
| AC-4: ステータスフィルタ | — | 有効（1）以外が含まれないこと |
| ソート検証 | — | policyEndDate 昇順であること |

**テスト技法**: `@SpringBootTest` + `@AutoConfigureMockMvc` でH2インメモリDBを使用した統合テスト

### 4.3 テストデータ戦略

`data.sql` の既存データを活用しつつ、テストクラス内で `@Sql` や `@BeforeEach` でテスト専用データを投入する。

| データパターン | policyStatus | policyEndDate | agentName | 用途 |
|--------------|-------------|---------------|-----------|------|
| 更新間近（有効） | 1 | 現在日+15日 | 佐藤花子 | 正常抽出対象 |
| 更新間近（失効） | 2 | 現在日+10日 | 佐藤花子 | ステータスフィルタ検証 |
| 更新日遠い | 1 | 現在日+60日 | 高橋次郎 | 日数フィルタ検証 |
| 更新日NULL | 1 | NULL | 佐藤花子 | NULL除外検証 |
| 更新日過去 | 1 | 現在日-5日 | 山田三郎 | 期限超過検証 |

---

## 5. リスクと制約

### 5.1 既存コードへの影響

| リスク | 影響度 | 対策 |
|-------|-------|------|
| `CustomerController` へのメソッド追加がルーティング衝突を起こす | 低 | `/renewals` パスは既存エンドポイント（`/search`, `/premium/calculate`）と競合しない |
| `CustomerService` への責務追加で God Class がさらに肥大化 | 中 | 今回は既存パターンに従い追加。将来的に `RenewalNotificationService` へ分離を推奨 |
| `CustomerRepository` のクエリ追加 | 低 | 既存クエリに影響なし。インターフェースへのメソッド追加のみ |

### 5.2 技術的制約

| 制約 | 詳細 | 対応方針 |
|------|------|---------|
| `java.util.Date` の使用 | 既存エンティティが `Date` 型。日数計算に不便 | `Date` → `LocalDate` 変換を Service 内で行う。エンティティは変更しない |
| H2 Database の方言 | `DATEADD` 関数は H2 固有。JPQL で日付演算を行う場合は注意 | JPQL の比較演算（`<=`）を使い、deadline を Java 側で算出する |
| 認証・認可なし | 現行システムに認証機構がない | 仕様書の記載通り、今回のスコープでは対応しない |
| publicフィールド | 既存エンティティが public フィールドを使用 | 既存パターンに合わせ、DTOもシンプルな構造とする |

### 5.3 パフォーマンス考慮

- 現行データ規模（数件〜数百件）ではパフォーマンス問題なし
- `policy_status` + `policy_end_date` への複合インデックスは将来課題として記録する
- Repository で DB 側フィルタ・ソートを行うため、全件取得+Java側処理のパターンは避ける

---

## 6. 未決事項（Open Questions）

| # | 質問 | 影響範囲 | 暫定方針 |
|---|------|---------|---------|
| 1 | `agentName` は完全一致検索で十分か？部分一致が必要か？ | Repository クエリ | 仕様書通り完全一致で実装。部分一致は将来対応 |
| 2 | 更新日が過去の有効契約は、ステータスを自動的に失効（2）に更新すべきか？ | Service ロジック | 今回のスコープでは「通知のみ」。ステータス自動更新は別機能として切り出す |
| 3 | レスポンスの日付フォーマットは `yyyy-MM-dd` 文字列とするか、既存同様のタイムスタンプ（epoch）とするか？ | DTO / シリアライズ | 仕様書の例に従い `yyyy-MM-dd` 形式で返す。`@JsonFormat` で制御 |
| 4 | テスト用データの `data.sql` に更新間近のデータを追加してよいか？ | 他演習への影響 | `data.sql` は変更せず、テストクラス内でデータを投入する方針を推奨 |
| 5 | `CustomerService` の肥大化をどのタイミングで分離すべきか？ | リファクタリング計画 | 今回は追加のみ。次フェーズで `RenewalNotificationService` への分離を計画 |

---

## 実装順序サマリー

```
Step 1: DTO作成          ← 依存なし、最初に着手
   ↓
Step 2: Repository拡張   ← DTOに依存しないが、Step 3の前提
   ↓
Step 3: Service実装      ← Step 1 (DTO) + Step 2 (Repository) に依存
   ↓
Step 4: Controller追加   ← Step 3 (Service) に依存
   ↓
Step 5: テスト作成       ← Step 3, 4 完了後に着手
   ↓
Step 6: 動作検証         ← 全Step完了後
```

**見積り対象ファイル数**: 新規4ファイル + 既存3ファイル修正 = 計7ファイル
