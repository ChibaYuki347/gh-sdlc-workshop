# 契約更新通知機能 — タスク一覧

> **元ドキュメント**: [spec.md](spec.md) / [plan.md](plan.md)

---

### タスク 1: CustomerRepository に更新間近の顧客取得クエリを追加

**種別**: feat
**優先度**: 高
**依存**: なし

**概要**:
`CustomerRepository` に、更新日が指定期間内に迫っている有効契約の顧客を取得する JPQL カスタムクエリを2件追加する。全担当者対象と営業担当者別フィルタの2パターン。

**受け入れ条件**:
- [ ] `findUpcomingRenewals(Date today, Date expiryDate)` メソッドが追加されている
- [ ] `findUpcomingRenewalsByAgent(Date today, Date expiryDate, String agentName)` メソッドが追加されている
- [ ] `policyStatus = 1`（有効）の契約のみ対象
- [ ] `policyEndDate IS NOT NULL` で null を除外
- [ ] 結果が `policyEndDate` の昇順でソートされる
- [ ] 既存のクエリメソッドに影響がない

**技術メモ**:
- 変更対象: `app/src/main/java/com/example/crm/repository/CustomerRepository.java`
- `@Query` + `@Param` アノテーションを使用
- `java.util.Date` 型のパラメータ（既存モデルに合わせる）

---

### タスク 2: RenewalNotificationService の実装

**種別**: feat
**優先度**: 高
**依存**: タスク 1

**概要**:
契約更新通知のビジネスロジックを担う新規サービスクラスを作成する。対象期間の算出、リポジトリ呼び出し、残日数計算、レスポンス組み立て、入力バリデーションを行う。

**受け入れ条件**:
- [ ] `RenewalNotificationService` クラスが作成されている
- [ ] `getUpcomingRenewals(int daysBeforeExpiry, String agentName)` メソッドが実装されている
- [ ] `daysBeforeExpiry` から今日〜今日+N日の期間を算出する
- [ ] `agentName` が null/空文字の場合は全担当者、指定ありの場合はフィルタする
- [ ] 各顧客に `daysUntilExpiry`（残日数）を計算して付与する
- [ ] レスポンスに `totalCount`, `daysBeforeExpiry`, `renewals` を含める
- [ ] `daysBeforeExpiry` が 1〜365 の範囲外の場合 `IllegalArgumentException` をスローする
- [ ] 既存の `CustomerService` には変更を加えない

**技術メモ**:
- 新規作成: `app/src/main/java/com/example/crm/service/RenewalNotificationService.java`
- `@Service` アノテーション、`@Autowired` で `CustomerRepository` を注入
- 日付計算は `Calendar` を使用（既存コードの `java.util.Date` に合わせる）
- レスポンスは `Map<String, Object>` で返却

---

### タスク 3: RenewalNotificationController の実装

**種別**: feat
**優先度**: 高
**依存**: タスク 2

**概要**:
`GET /api/notifications/renewals` エンドポイントを提供する REST コントローラを新規作成する。リクエストパラメータの受け取りとエラーハンドリングを行う。

**受け入れ条件**:
- [ ] `GET /api/notifications/renewals` が 200 OK を返す
- [ ] `daysBeforeExpiry` パラメータ（デフォルト: 30）を受け取れる
- [ ] `agentName` パラメータ（任意）を受け取れる
- [ ] `daysBeforeExpiry` が不正値の場合 400 Bad Request を返す
- [ ] レスポンスが仕様書の JSON 形式に準拠している
- [ ] 既存の `/api/customers` エンドポイントに影響がない

**技術メモ**:
- 新規作成: `app/src/main/java/com/example/crm/controller/RenewalNotificationController.java`
- `@RestController` + `@RequestMapping("/api/notifications")`
- `@RequestParam(defaultValue = "30")` と `@RequestParam(required = false)` を使用
- `IllegalArgumentException` を `@ExceptionHandler` または try-catch で 400 に変換

---

### タスク 4: RenewalNotificationService の単体テスト

**種別**: test
**優先度**: 中
**依存**: タスク 2

**概要**:
`RenewalNotificationService` のビジネスロジックを検証する単体テストを作成する。Repository をモック化し、日付計算・バリデーション・フィルタロジックをテストする。

**受け入れ条件**:
- [ ] 更新間近の顧客が存在する場合、正しい件数で返却される
- [ ] 対象顧客が 0 件の場合、空リスト + `totalCount: 0` が返る
- [ ] `agentName` フィルタが正しく動作する
- [ ] `agentName` が null/空文字の場合、全担当者が対象になる
- [ ] `daysUntilExpiry` が正しく計算される（当日=0、翌日=1）
- [ ] `daysBeforeExpiry` が 0 以下の場合、例外がスローされる
- [ ] `daysBeforeExpiry` が 365 超過の場合、例外がスローされる

**技術メモ**:
- 新規作成: `app/src/test/java/com/example/crm/service/RenewalNotificationServiceTest.java`
- JUnit 5 + Mockito（`@ExtendWith(MockitoExtension.class)`）
- `CustomerRepository` を `@Mock` で差し替え
- テストデータは `Customer` オブジェクトを直接生成

---

### タスク 5: RenewalNotificationController の統合テスト

**種別**: test
**優先度**: 中
**依存**: タスク 3

**概要**:
REST エンドポイントの統合テストを作成する。`MockMvc` を使用して HTTP リクエスト/レスポンスの検証を行う。

**受け入れ条件**:
- [ ] `GET /api/notifications/renewals` が 200 OK + 正しい JSON を返す
- [ ] `?daysBeforeExpiry=60` でパラメータが反映される
- [ ] `?agentName=佐藤花子` でフィルタが機能する
- [ ] `?daysBeforeExpiry=0` で 400 Bad Request が返る
- [ ] `?daysBeforeExpiry=500` で 400 Bad Request が返る
- [ ] 既存 API `GET /api/customers` が正常に動作することを確認

**技術メモ**:
- 新規作成: `app/src/test/java/com/example/crm/controller/RenewalNotificationControllerTest.java`
- `@SpringBootTest` + `@AutoConfigureMockMvc`
- H2 インメモリ DB + `data.sql` の初期データを使用
- `MockMvc` で JSON レスポンスの構造を検証（`jsonPath`）

---

## タスク依存関係

```
タスク1 (Repository)
   ↓
タスク2 (Service) ──→ タスク4 (Service テスト)
   ↓
タスク3 (Controller) ──→ タスク5 (Controller テスト)
```

## 推奨実装順序

1. **タスク 1** → 2 → 3 → 4 → 5（依存順）
2. または **タスク 1 → 2 → 4**（Service完成+テスト）→ **3 → 5**（Controller完成+テスト）

> 💡 Coding Agent にアサインする場合は、タスク 1〜3 を1つの Issue にまとめ、
> タスク 4〜5 を別 Issue にすると効率的です。
