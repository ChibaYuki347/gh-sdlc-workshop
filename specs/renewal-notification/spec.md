# 契約更新通知機能 — 機能仕様書

## 1. 概要

| 項目 | 内容 |
|------|------|
| **機能名** | 契約更新通知（Renewal Notification） |
| **目的** | 更新日が近い保険契約を自動的にリストアップし、営業担当者が迅速にフォローできるようにする |
| **背景** | 保険契約には更新日（`endDate`）があり、更新日を過ぎると契約が失効する。現在は営業担当者の手動確認に依存しており、月に数件の更新漏れによる失効が発生している。本機能により更新対象の可視化を行い、失効を防止する |

## 2. ユーザーストーリー

### US-1: 更新間近の顧客一覧取得
> **As a** 営業担当者  
> **I want** 更新日が近い顧客の一覧を取得したい  
> **So that** 更新手続きを事前にフォローし、契約失効を防止できる

### US-2: 担当者別フィルタ
> **As a** 営業担当者  
> **I want** 自分が担当する顧客だけに絞り込みたい  
> **So that** 自分のフォロー対象を効率的に把握できる

### US-3: 期間指定
> **As a** 営業マネージャー  
> **I want** 更新日までの日数を指定してリストを取得したい  
> **So that** 30日前・60日前など状況に応じた対応計画を立てられる

## 3. 機能要件

### Must Have（必須）
- [ ] 更新日が指定日数以内に迫っている顧客を一覧取得する REST API
- [ ] デフォルトの対象期間は30日以内
- [ ] ステータスが「有効」（`policyStatus = 1`）の契約のみ対象
- [ ] `policyEndDate` が `null` の契約は除外
- [ ] 結果を更新日（`policyEndDate`）の昇順でソート
- [ ] 営業担当者名（`agentName`）によるフィルタ

### Should Have（推奨）
- [ ] レスポンスに残日数（`daysUntilExpiry`）を含める
- [ ] 件数情報（`totalCount`）をレスポンスに含める

### Nice to Have（将来対応可）
- [ ] メール通知の自動送信
- [ ] ダッシュボード画面への統合
- [ ] 更新完了ステータスの管理

## 4. API設計

### GET `/api/notifications/renewals`

契約更新日が近い顧客リストを返す。

#### リクエストパラメータ

| パラメータ | 型 | 必須 | デフォルト | 説明 |
|---|---|---|---|---|
| `daysBeforeExpiry` | `int` | No | `30` | 更新日までの日数しきい値 |
| `agentName` | `String` | No | *(なし)* | 営業担当者名でフィルタ |

#### リクエスト例

```
GET /api/notifications/renewals?daysBeforeExpiry=30&agentName=佐藤花子
```

#### レスポンス（200 OK）

```json
{
  "totalCount": 2,
  "daysBeforeExpiry": 30,
  "renewals": [
    {
      "customerId": 2,
      "firstName": "花子",
      "lastName": "鈴木",
      "email": "suzuki@example.com",
      "phone": "03-2345-6789",
      "policyNumber": "ZM-2024-002",
      "policyType": "MEDICAL",
      "premiumAmount": 8500,
      "policyEndDate": "2025-03-15",
      "daysUntilExpiry": 12,
      "agentName": "佐藤花子",
      "agentEmail": "sato@example.co.jp"
    }
  ]
}
```

#### エラーレスポンス

| ステータス | 条件 | レスポンス例 |
|---|---|---|
| `400 Bad Request` | `daysBeforeExpiry` が0以下 | `{"error": "daysBeforeExpiry must be a positive integer"}` |
| `400 Bad Request` | `daysBeforeExpiry` が365超過 | `{"error": "daysBeforeExpiry must not exceed 365"}` |

## 5. データモデル

### 既存テーブル（変更なし）

本機能は既存の `customers` テーブルのみを使用し、**スキーマ変更は不要**。

| テーブル | 使用フィールド | 用途 |
|---|---|---|
| `customers` | `id`, `firstName`, `lastName`, `email`, `phone` | 顧客基本情報 |
| `customers` | `policyNumber`, `policyType`, `premiumAmount` | 契約情報 |
| `customers` | `policyEndDate` | 更新日判定 |
| `customers` | `policyStatus` | 有効契約（`1`）のフィルタ |
| `customers` | `agentName`, `agentEmail` | 営業担当者フィルタ |

### 必要なリポジトリクエリ

`CustomerRepository` に以下のカスタムクエリを追加:

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

## 6. エッジケース

| # | ケース | 期待動作 |
|---|---|---|
| 1 | 対象顧客が0件 | 空配列を返す（`totalCount: 0`, `renewals: []`） |
| 2 | `policyEndDate` が `null` | 対象から除外 |
| 3 | `policyStatus` が 1 以外（申請中・失効・解約） | 対象から除外 |
| 4 | `policyEndDate` が過去日（既に期限切れ） | 対象から除外（`today` 以降のみ） |
| 5 | `daysBeforeExpiry` が 0 以下 | 400 Bad Request を返す |
| 6 | `daysBeforeExpiry` が 365 を超過 | 400 Bad Request を返す |
| 7 | `agentName` に該当する担当者がいない | 空配列を返す（エラーではない） |
| 8 | `agentName` が空文字 | フィルタなし（全担当者対象）として扱う |
| 9 | 当日が更新日の顧客 | 対象に含める（`daysUntilExpiry: 0`） |

## 7. 受け入れ条件

- [ ] `GET /api/notifications/renewals` が 200 OK を返す
- [ ] デフォルト（パラメータなし）で `policyStatus = 1` かつ `policyEndDate` が今日から30日以内の顧客のみ返る
- [ ] `daysBeforeExpiry=60` を指定すると60日以内の顧客が返る
- [ ] `agentName=佐藤花子` を指定すると該当担当者の顧客のみ返る
- [ ] `policyEndDate` が `null` の顧客は含まれない
- [ ] 結果が `policyEndDate` の昇順でソートされている
- [ ] 各レスポンス要素に `daysUntilExpiry`（残日数）が含まれる
- [ ] `daysBeforeExpiry` に不正値を渡すと 400 Bad Request が返る
- [ ] 該当顧客が0件の場合、空配列と `totalCount: 0` が返る
- [ ] 既存の `/api/customers` 系エンドポイントに影響がない
- [ ] 単体テスト（Service層）が作成されている
- [ ] 統合テスト（Controller層）が作成されている

## 8. 非機能要件

| 観点 | 要件 |
|---|---|
| **パフォーマンス** | DBクエリで絞り込みを行い、全件取得後のJavaフィルタリングを避ける |
| **セキュリティ** | SQLインジェクション対策として Spring Data JPA のパラメータバインディングを使用 |
| **互換性** | 既存APIのエンドポイント・レスポンス形式に影響を与えない |
| **テスト容易性** | サービス層に日付計算ロジックを集約し、単体テスト可能にする |
| **保守性** | 新規サービスクラス（`RenewalNotificationService`）に実装し、既存の `CustomerService` の肥大化を避ける |