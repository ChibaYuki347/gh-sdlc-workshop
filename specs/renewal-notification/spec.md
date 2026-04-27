# 機能仕様書 — 契約更新通知機能

> **ステータス**: Draft
> **作成日**: 2026-04-27
> **対象プロジェクト**: 保険CRMアプリケーション

---

## 1. 概要

### 機能名
契約更新通知機能（Renewal Notification API）

### 目的
保険契約の更新日（`endDate`）が近づいている顧客を自動的にリストアップし、営業担当者が更新漏れを防止できるようにする。

### 背景
- 保険契約には更新日（`endDate`）があり、更新日を過ぎると契約が失効（`policyStatus = 2`）する
- 現状は営業担当者が手動で更新日を確認しているため、月に数件の更新漏れによる失効が発生している
- 更新対象の顧客を自動で抽出するAPIを提供し、業務効率化と失効防止を実現する

---

## 2. ユーザーストーリー

### メインストーリー
```
As a 営業担当者,
I want 契約更新日が30日以内に迫っている顧客のリストを取得したい,
So that 更新漏れによる契約失効を未然に防止できる.
```

### サブストーリー
```
As a 営業マネージャー,
I want 担当者別に更新対象をフィルタリングしたい,
So that チーム内の対応状況を把握し、適切にフォローできる.
```

---

## 3. 機能要件

### 3.1 必須要件（Must Have）
- [ ] 更新日が指定日数以内（デフォルト30日）の**有効な契約**（`policyStatus = 1`）を持つ顧客一覧を返すAPIエンドポイント
- [ ] 営業担当者名（`agentName`）によるフィルタリング機能
- [ ] レスポンスに顧客情報・契約情報・更新までの残日数を含める
- [ ] 更新日が近い順（昇順）でソートして返却する

### 3.2 推奨要件（Should Have）
- [ ] 日数パラメータをカスタマイズ可能にする（デフォルト: 30日）
- [ ] レスポンスに対象件数（`totalCount`）を含める
- [ ] 更新日が過去（既に期限切れだがまだ `policyStatus = 1` のまま）の契約も含める

### 3.3 将来対応（Nice to Have）
- [ ] 契約種別（`policyType`）によるフィルタリング
- [ ] メール通知の自動送信機能
- [ ] ダッシュボード画面への更新通知ウィジェット追加

---

## 4. API 設計

### エンドポイント

```
GET /api/customers/renewals
```

### リクエストパラメータ

| パラメータ | 型 | 必須 | デフォルト | 説明 |
|-----------|-----|------|----------|------|
| `daysUntilRenewal` | int | No | 30 | 更新日までの日数しきい値。現在日から指定日数以内に更新日が到来する契約を抽出する |
| `agentName` | String | No | - | 営業担当者名でフィルタ。部分一致（LIKE）で検索する |

### 正常レスポンス（200 OK）

```json
{
  "renewals": [
    {
      "customerId": 2,
      "firstName": "花子",
      "lastName": "鈴木",
      "fullName": "花子 鈴木",
      "email": "suzuki@example.com",
      "phone": "03-2345-6789",
      "policyNumber": "ZM-2024-002",
      "policyType": "MEDICAL",
      "policyStatus": 1,
      "policyStatusText": "有効",
      "premiumAmount": 8500.0,
      "policyStartDate": "2024-03-15",
      "policyEndDate": "2025-03-15",
      "daysUntilRenewal": 12,
      "agentName": "佐藤花子",
      "agentEmail": "sato@example.co.jp"
    }
  ],
  "totalCount": 1,
  "searchCriteria": {
    "daysUntilRenewal": 30,
    "agentName": null
  }
}
```

### エラーレスポンス

| ステータスコード | 条件 | レスポンス例 |
|----------------|------|------------|
| 400 Bad Request | `daysUntilRenewal` が負の値または0以下 | `{"error": "daysUntilRenewal must be a positive integer"}` |
| 400 Bad Request | `daysUntilRenewal` が365を超える | `{"error": "daysUntilRenewal must not exceed 365"}` |
| 500 Internal Server Error | サーバー内部エラー | `{"error": "Internal server error"}` |

### リクエスト例

```
# デフォルト（30日以内の更新対象を取得）
GET /api/customers/renewals

# 60日以内の更新対象を取得
GET /api/customers/renewals?daysUntilRenewal=60

# 佐藤花子が担当する30日以内の更新対象を取得
GET /api/customers/renewals?agentName=佐藤花子

# 高橋次郎が担当する90日以内の更新対象を取得
GET /api/customers/renewals?daysUntilRenewal=90&agentName=高橋次郎
```

---

## 5. データモデル

### 関連テーブル

本機能は既存テーブルのみを使用し、新規テーブルの追加は不要。

#### `customers` テーブル（既存）

| カラム | 型 | 用途 |
|--------|-----|------|
| `id` | Long | 顧客ID |
| `first_name` | String | 名 |
| `last_name` | String | 姓 |
| `email` | String | メールアドレス |
| `phone` | String | 電話番号 |
| `policy_number` | String | 契約番号 |
| `policy_type` | String | 契約種別（LIFE, MEDICAL, AUTO, FIRE） |
| `policy_status` | int | 契約ステータス（0=申請中, 1=有効, 2=失効, 3=解約） |
| `premium_amount` | double | 保険料 |
| `policy_start_date` | Date | 契約開始日 |
| `policy_end_date` | Date | 契約終了日（更新日） |
| `agent_name` | String | 営業担当者名 |
| `agent_email` | String | 営業担当者メール |

### クエリ条件

```sql
SELECT * FROM customers
WHERE policy_status = 1                          -- 有効な契約のみ
  AND policy_end_date IS NOT NULL                -- 更新日が設定されている
  AND policy_end_date <= DATEADD('DAY', :days, CURRENT_DATE)  -- 指定日数以内
  AND (:agentName IS NULL OR agent_name LIKE CONCAT('%', :agentName, '%'))  -- 担当者フィルタ（部分一致）
ORDER BY policy_end_date ASC                     -- 更新日が近い順
```

### 新規フィールド

新規カラムの追加は不要。レスポンス専用の算出フィールドとして以下を追加する:

| フィールド | 型 | 説明 |
|-----------|-----|------|
| `daysUntilRenewal` | int | 更新日までの残日数（`policy_end_date - CURRENT_DATE`）。負の値は期限超過を示す |

---

## 6. エッジケース

| # | ケース | 期待される動作 |
|---|--------|--------------|
| 1 | 更新対象の顧客が0件 | 空の `renewals` 配列と `totalCount: 0` を返す |
| 2 | `policyEndDate` が NULL の顧客 | 抽出対象から除外する |
| 3 | `policyStatus` が有効（1）以外の契約 | 抽出対象から除外する（失効・解約済みは対象外） |
| 4 | `daysUntilRenewal` に0以下の値を指定 | 400 Bad Request を返す |
| 5 | `daysUntilRenewal` に366以上の値を指定 | 400 Bad Request を返す |
| 6 | `agentName` に存在しない担当者名を指定 | 空の結果を返す（エラーにはしない） |
| 7 | 更新日が本日ちょうどの契約 | `daysUntilRenewal = 0` として抽出対象に含める |
| 8 | 更新日が過去の有効契約（ステータス未更新） | `daysUntilRenewal` が負の値として抽出対象に含める |
| 9 | `agentName` パラメータに空文字を指定 | フィルタなし（全担当者）として扱う |

---

## 7. 受け入れ条件

### AC-1: 基本取得
- [ ] `GET /api/customers/renewals` を呼び出すと、更新日が30日以内の有効な契約を持つ顧客リストが返却される
- [ ] レスポンスに `renewals` 配列、`totalCount`、`searchCriteria` が含まれる
- [ ] 結果は `policyEndDate` の昇順（更新日が近い順）でソートされている

### AC-2: 日数指定
- [ ] `daysUntilRenewal=60` を指定すると、60日以内の更新対象が返却される
- [ ] `daysUntilRenewal=0` を指定すると、400エラーが返却される

### AC-3: 担当者フィルタ
- [ ] `agentName=佐藤花子` を指定すると、佐藤花子が担当する顧客のみが返却される
- [ ] `agentName=佐藤` のように部分一致で検索でき、佐藤花子が担当する顧客が返却される
- [ ] 存在しない担当者名を指定すると、空のリストが返却される

### AC-4: ステータスフィルタ
- [ ] `policyStatus = 1`（有効）の契約のみが抽出される
- [ ] 失効（2）、解約（3）、申請中（0）の契約は含まれない

### AC-5: daysUntilRenewal 算出
- [ ] 各顧客の `daysUntilRenewal` が正しく計算されている（`policyEndDate - 現在日`）
- [ ] 更新日が過去の場合、負の値が返却される

### AC-6: エラーハンドリング
- [ ] 不正なパラメータに対して400エラーと適切なメッセージが返却される
- [ ] サーバーエラー時に500エラーが返却される

---

## 8. 非機能要件

### パフォーマンス
- 1000件以下のデータ規模で、レスポンス時間が500ms以内であること
- `policy_end_date` と `policy_status` に対するクエリが効率的に実行されること（将来的にインデックス追加を検討）

### セキュリティ
- SQLインジェクション対策: Spring Data JPAのパラメータバインディングを使用すること
- `agentName` パラメータの入力値を適切にサニタイズすること
- 認証・認可は既存の仕組みに準拠する（現時点では未実装）

### 互換性
- 既存の `/api/customers` エンドポイント群に影響を与えないこと
- レスポンスのJSON形式は既存APIと統一感を持たせること

### テスト
- JUnit 5 + Spring Boot Test でユニットテスト・統合テストを作成すること
- 正常系・異常系・境界値の各ケースをカバーすること

---

## 実装ガイド

### 変更対象ファイル（想定）

| ファイル | 変更内容 |
|---------|---------|
| `CustomerRepository.java` | 更新対象を検索するクエリメソッドの追加 |
| `CustomerService.java` | 更新通知ビジネスロジックの追加 |
| `CustomerController.java` | `GET /api/customers/renewals` エンドポイントの追加 |

### 実装上の注意
- 現在の `Customer` モデルは `java.util.Date` を使用しているため、日数計算では `Date` ベースで処理する（`LocalDate` への移行は将来課題）
- `policyEndDate` が `NULL` のレコードを確実に除外すること
- レスポンス用のDTOクラスの作成は任意（既存パターンに合わせて `Map` でも可）
