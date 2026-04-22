# API仕様書 — 保険CRM REST API

> 📝 この仕様書は実装コードから生成されています。最新の実装と差異がある場合は、Copilot Hookにより自動で更新が促されます。

**最終更新**: 自動メンテナンス対象ドキュメント

---

## 概要

保険CRM管理システムのREST APIです。顧客情報の管理、保険料の試算、契約ステータスの変更、顧客検索、月次レポートの生成を行います。

- **ベースURL**: `http://localhost:8080`
- **APIプレフィックス**: `/api/customers`
- **データ形式**: JSON（`Content-Type: application/json`）

---

## 目次

1. [顧客管理API](#1-顧客管理api)
   - [顧客一覧取得](#11-get-apicustomers)
   - [顧客詳細取得](#12-get-apicustomersid)
   - [顧客新規登録](#13-post-apicustomers)
   - [顧客情報更新](#14-put-apicustomersid)
   - [顧客削除](#15-delete-apicustomersid)
2. [検索API](#2-検索api)
   - [顧客検索](#21-get-apicustomerssearch)
3. [保険料API](#3-保険料api)
   - [保険料試算](#31-get-apicustomerspremiumcalculate)
4. [契約管理API](#4-契約管理api)
   - [ステータス変更](#41-put-apicustomersidstatus)
5. [レポートAPI](#5-レポートapi)
   - [月次レポート](#51-get-apicustomersreportmonthly)
6. [データモデル](#6-データモデル)
7. [ステータスコード一覧](#7-ステータスコード一覧)

---

## 1. 顧客管理API

### 1.1 GET /api/customers

**顧客一覧の取得**

登録されているすべての顧客情報を取得します。

#### リクエスト

パラメータなし

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | 顧客一覧の取得成功 |

```json
[
  {
    "id": 1,
    "firstName": "太郎",
    "lastName": "山田",
    "email": "yamada@example.com",
    "phone": "090-1234-5678",
    "address": "東京都千代田区1-1-1",
    "policyNumber": "POL-0001",
    "policyType": "LIFE",
    "policyStatus": 1,
    "premiumAmount": 5500.0,
    "policyStartDate": "2024-01-01",
    "policyEndDate": "2034-01-01",
    "agentName": "佐藤花子",
    "agentEmail": "sato@example.com",
    "createdAt": "2024-01-01T00:00:00.000+00:00",
    "updatedAt": "2024-01-15T10:30:00.000+00:00",
    "fullName": "太郎 山田",
    "policyStatusText": "有効"
  }
]
```

---

### 1.2 GET /api/customers/{id}

**顧客詳細の取得**

指定されたIDの顧客情報を取得します。

#### リクエスト

| パラメータ | 型   | 必須 | 説明       |
|-----------|------|------|-----------|
| id        | Long | ✅   | 顧客ID（パスパラメータ） |

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | 顧客詳細の取得成功 |
| 500 Internal Server Error | 指定IDの顧客が存在しない場合（※注意） |

```json
{
  "id": 1,
  "firstName": "太郎",
  "lastName": "山田",
  "email": "yamada@example.com",
  "phone": "090-1234-5678",
  "address": "東京都千代田区1-1-1",
  "policyNumber": "POL-0001",
  "policyType": "LIFE",
  "policyStatus": 1,
  "premiumAmount": 5500.0,
  "policyStartDate": "2024-01-01",
  "policyEndDate": "2034-01-01",
  "agentName": "佐藤花子",
  "agentEmail": "sato@example.com",
  "createdAt": "2024-01-01T00:00:00.000+00:00",
  "updatedAt": "2024-01-15T10:30:00.000+00:00",
  "fullName": "太郎 山田",
  "policyStatusText": "有効"
}
```

> ⚠️ **既知の制約**: 存在しないIDを指定した場合、404ではなく500エラーが返されます（`NoSuchElementException`）。将来的に適切な404レスポンスへの改善が予定されています。

---

### 1.3 POST /api/customers

**顧客の新規登録**

新しい顧客情報を登録します。`createdAt` と `updatedAt` は自動的に現在日時が設定されます。

#### リクエスト

| パラメータ       | 型      | 必須 | 説明                                         |
|-----------------|---------|------|---------------------------------------------|
| firstName       | String  | ✅   | 名                                           |
| lastName        | String  | ✅   | 姓                                           |
| email           | String  | ✅   | メールアドレス                                 |
| phone           | String  | —    | 電話番号                                      |
| address         | String  | —    | 住所                                          |
| policyNumber    | String  | —    | 契約番号                                      |
| policyType      | String  | —    | 契約種別（`LIFE` / `MEDICAL` / `AUTO` / `FIRE`） |
| policyStatus    | int     | —    | 契約ステータス（0〜3）。デフォルト: 0（申請中）     |
| premiumAmount   | double  | —    | 月額保険料（円）                                |
| policyStartDate | Date    | —    | 契約開始日                                     |
| policyEndDate   | Date    | —    | 契約終了日                                     |
| agentName       | String  | —    | 担当者名                                      |
| agentEmail      | String  | —    | 担当者メールアドレス                             |

#### リクエスト例

```json
{
  "firstName": "太郎",
  "lastName": "山田",
  "email": "yamada@example.com",
  "phone": "090-1234-5678",
  "address": "東京都千代田区1-1-1",
  "policyNumber": "POL-0001",
  "policyType": "LIFE",
  "policyStatus": 0,
  "premiumAmount": 5500.0
}
```

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | 顧客登録成功（登録された顧客情報を返却） |

> ⚠️ **既知の制約**: 入力バリデーションが実装されていないため、不正なデータでも登録が成功する場合があります。

---

### 1.4 PUT /api/customers/{id}

**顧客情報の更新**

指定されたIDの顧客情報を更新します。`updatedAt` は自動的に現在日時に更新されます。

#### リクエスト

| パラメータ | 型   | 必須 | 説明                |
|-----------|------|------|-------------------|
| id        | Long | ✅   | 顧客ID（パスパラメータ） |

リクエストボディは「顧客新規登録」と同じJSON形式です。

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | 更新成功（更新後の顧客情報を返却） |
| 500 Internal Server Error | 指定IDの顧客が存在しない場合 |

---

### 1.5 DELETE /api/customers/{id}

**顧客の削除**

指定されたIDの顧客情報を削除します。

#### リクエスト

| パラメータ | 型   | 必須 | 説明                |
|-----------|------|------|-------------------|
| id        | Long | ✅   | 顧客ID（パスパラメータ） |

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | 削除成功（レスポンスボディなし） |

> ⚠️ **注意**: 削除操作は取り消しできません。

---

## 2. 検索API

### 2.1 GET /api/customers/search

**顧客検索**

キーワードによる顧客検索を行います。以下のフィールドに対して部分一致検索を行います。
- 姓（lastName）
- 名（firstName）
- メールアドレス（email）
- 契約番号（policyNumber）

#### リクエスト

| パラメータ | 型     | 必須 | 説明         |
|-----------|--------|------|-------------|
| keyword   | String | ✅   | 検索キーワード |

#### リクエスト例

```
GET /api/customers/search?keyword=山田
```

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | 検索結果（顧客情報の配列） |

```json
[
  {
    "id": 1,
    "firstName": "太郎",
    "lastName": "山田",
    "email": "yamada@example.com",
    "fullName": "太郎 山田",
    "policyStatusText": "有効"
  }
]
```

> 検索結果が0件の場合は空配列 `[]` が返されます。

---

## 3. 保険料API

### 3.1 GET /api/customers/premium/calculate

**保険料試算**

保険種別・年齢・喫煙の有無に基づいて月額保険料を試算します。税込金額（10%）で返却されます。

#### リクエスト

| パラメータ   | 型      | 必須 | 説明                                         |
|-------------|---------|------|---------------------------------------------|
| policyType  | String  | ✅   | 保険種別（`LIFE` / `MEDICAL` / `AUTO` / `FIRE`） |
| age         | int     | ✅   | 年齢                                         |
| isSmoker    | boolean | —    | 喫煙者フラグ（デフォルト: `false`）              |

#### リクエスト例

```
GET /api/customers/premium/calculate?policyType=LIFE&age=35&isSmoker=true
```

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | 試算結果 |

```json
{
  "policyType": "LIFE",
  "age": 35,
  "isSmoker": true,
  "monthlyPremium": 9900.0
}
```

#### 保険料計算ロジック

| 保険種別 | 基本保険料 | 年齢係数 | 喫煙者係数 |
|---------|-----------|---------|-----------|
| LIFE（生命保険）    | ¥5,000 | 30歳超: ×1.2 / 40歳超: ×1.8 / 60歳超: ×2.5 | ×1.5 |
| MEDICAL（医療保険） | ¥3,000 | 30歳超: ×1.3 / 40歳超: ×2.0 / 60歳超: ×3.0 | ×1.8 |
| AUTO（自動車保険）  | ¥8,000 | 25歳未満: ×2.0 / 65歳超: ×1.5               | 適用なし |
| FIRE（火災保険）    | ¥2,000 | 適用なし                                      | 適用なし |

> すべての保険料に消費税10%が加算されます。結果は円未満四捨五入です。

---

## 4. 契約管理API

### 4.1 PUT /api/customers/{id}/status

**契約ステータスの変更**

指定された顧客の契約ステータスを変更します。

#### リクエスト

| パラメータ | 型   | 必須 | 説明                              |
|-----------|------|------|----------------------------------|
| id        | Long | ✅   | 顧客ID（パスパラメータ）              |
| status    | int  | ✅   | 新しいステータス値（クエリパラメータ）   |

#### ステータス値

| 値 | 意味   |
|----|--------|
| 0  | 申請中  |
| 1  | 有効    |
| 2  | 失効    |
| 3  | 解約    |

#### リクエスト例

```
PUT /api/customers/1/status?status=1
```

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | ステータス変更成功（更新後の顧客情報を返却） |
| 500 Internal Server Error | 指定IDの顧客が存在しない場合 |

> ⚠️ **既知の制約**: ステータス値の範囲チェック（0〜3）が実装されていないため、不正な値も受け付けます。

---

## 5. レポートAPI

### 5.1 GET /api/customers/report/monthly

**月次レポートの取得**

顧客・契約に関する月次サマリーレポートを生成して返却します。

#### リクエスト

パラメータなし

#### レスポンス

| ステータス | 説明 |
|-----------|------|
| 200 OK    | レポート取得成功 |

```json
{
  "totalCustomers": 50,
  "activeContracts": 35,
  "expiredContracts": 8,
  "totalMonthlyPremium": 192500.0,
  "averagePremium": 5500.0,
  "generatedAt": "Mon Jan 15 10:30:00 JST 2024"
}
```

#### レスポンスフィールド

| フィールド           | 型      | 説明                             |
|---------------------|---------|----------------------------------|
| totalCustomers      | int     | 総顧客数                          |
| activeContracts     | int     | 有効契約数（ステータス=1）           |
| expiredContracts    | int     | 失効契約数（ステータス=2）           |
| totalMonthlyPremium | double  | 有効契約の月間保険料合計（円）        |
| averagePremium      | double  | 有効契約の平均保険料（円）           |
| generatedAt         | String  | レポート生成日時                    |

---

## 6. データモデル

### 6.1 Customer（顧客）

テーブル名: `customers`

| フィールド       | 型        | 説明                                         |
|-----------------|-----------|---------------------------------------------|
| id              | Long      | 顧客ID（自動採番）                             |
| firstName       | String    | 名                                           |
| lastName        | String    | 姓                                           |
| email           | String    | メールアドレス                                 |
| phone           | String    | 電話番号                                      |
| address         | String    | 住所                                          |
| policyNumber    | String    | 契約番号                                      |
| policyType      | String    | 契約種別（`LIFE` / `MEDICAL` / `AUTO` / `FIRE`） |
| policyStatus    | int       | 契約ステータス（0: 申請中 / 1: 有効 / 2: 失効 / 3: 解約） |
| premiumAmount   | double    | 月額保険料（円）                                |
| policyStartDate | Date      | 契約開始日                                     |
| policyEndDate   | Date      | 契約終了日                                     |
| agentName       | String    | 担当営業者名                                   |
| agentEmail      | String    | 担当営業者メールアドレス                         |
| createdAt       | Timestamp | 作成日時                                      |
| updatedAt       | Timestamp | 更新日時                                      |

**派生フィールド（レスポンスに含まれる）**:

| フィールド        | 型     | 説明                      |
|------------------|--------|--------------------------|
| fullName         | String | フルネーム（firstName + lastName） |
| policyStatusText | String | ステータス文字列（「申請中」「有効」等） |

### 6.2 Policy（保険契約）

テーブル名: `policies`

| フィールド      | 型        | 説明                                                |
|----------------|-----------|---------------------------------------------------|
| id             | Long      | 契約ID（自動採番）                                    |
| customerId     | Long      | 顧客ID                                              |
| policyNumber   | String    | 契約番号                                             |
| type           | String    | 契約種別（`LIFE` / `MEDICAL` / `AUTO` / `FIRE`）      |
| status         | int       | 契約ステータス（0: 申請中 / 1: 有効 / 2: 失効 / 3: 解約） |
| premiumAmount  | double    | 月額保険料（円）                                      |
| coverageAmount | double    | 保障金額（円）                                        |
| startDate      | Date      | 契約開始日                                           |
| endDate        | Date      | 契約終了日                                           |
| notes          | String    | 備考                                                |
| createdAt      | Timestamp | 作成日時                                             |

---

## 7. ステータスコード一覧

| コード | 意味                     | 発生場面                                |
|-------|--------------------------|----------------------------------------|
| 200   | OK — 成功                 | すべてのAPI正常レスポンス                  |
| 400   | Bad Request              | リクエストパラメータ不正時                  |
| 404   | Not Found                | 存在しないエンドポイントへのアクセス          |
| 500   | Internal Server Error    | 存在しない顧客IDの参照時、その他サーバーエラー |

---

## 補足事項

### 認証・認可
現在、認証・認可は未実装です。すべてのAPIは認証なしでアクセス可能です。

### CORS
CORS設定は特に行われていません。同一オリジンからのアクセスを前提としています。

### 既知の制約事項

1. **入力バリデーション**: リクエストボディのバリデーション（必須チェック、形式チェック等）が未実装
2. **エラーレスポンス形式**: エラー時のレスポンス形式が統一されていない
3. **404処理**: 存在しないリソースへのアクセス時に500エラーが返される（本来は404が適切）
4. **ページネーション**: 顧客一覧取得にページネーション機能がない
5. **ステータス値検証**: ステータス変更APIで不正な値（0〜3以外）のチェックが未実装
