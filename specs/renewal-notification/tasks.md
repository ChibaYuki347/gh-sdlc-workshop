# タスク一覧 — 契約更新通知機能

> **対象計画書**: [specs/renewal-notification/plan.md](plan.md)
> **対象仕様書**: [specs/renewal-notification/spec.md](spec.md)
> **作成日**: 2026-04-27

---

## タスク依存関係

```
Task 1 (DTO作成) ─────────┐
                          ├─→ Task 3 (Service実装) → Task 4 (Controller実装) → Task 5 (統合テスト)
Task 2 (Repository拡張) ──┘
```

---

### タスク 1: レスポンス用DTOクラスの作成

**種別**: feat
**優先度**: 高
**依存**: なし

**概要**:
契約更新通知APIのレスポンスに使用するDTOクラスを新規作成する。既存エンティティには `daysUntilRenewal`（更新までの残日数）が存在しないため、レスポンス専用のデータ構造が必要。

**受け入れ条件**:
- [ ] `RenewalCustomerDto` クラスが作成され、顧客情報・契約情報・`daysUntilRenewal` フィールドを持つ
- [ ] `RenewalNotificationResponse` クラスが作成され、`renewals`（リスト）、`totalCount`、`searchCriteria` を持つ
- [ ] 日付フィールドに `@JsonFormat(pattern = "yyyy-MM-dd")` が設定されている
- [ ] コンパイルが通ること

**技術メモ**:
- 新規ファイル: `app/src/main/java/com/example/crm/dto/RenewalCustomerDto.java`
- 新規ファイル: `app/src/main/java/com/example/crm/dto/RenewalNotificationResponse.java`
- `RenewalCustomerDto` のフィールド: `customerId`, `firstName`, `lastName`, `fullName`, `email`, `phone`, `policyNumber`, `policyType`, `policyStatus`, `policyStatusText`, `premiumAmount`, `policyStartDate`, `policyEndDate`, `daysUntilRenewal`, `agentName`, `agentEmail`
- 既存コードが public フィールドパターンを使用しているため、DTOも同様のスタイルで統一する
- 詳細仕様: `specs/renewal-notification/spec.md` セクション4（レスポンス形式）

---

### タスク 2: Repository層に更新対象検索クエリを追加

**種別**: feat
**優先度**: 高
**依存**: なし

**概要**:
`CustomerRepository` に、有効な契約（`policyStatus = 1`）かつ更新日が指定期限以内の顧客を検索するJPQLクエリメソッドを追加する。担当者名でのフィルタリングにも対応する。

**受け入れ条件**:
- [ ] `findRenewalTargets(int status, Date deadline)` メソッドが追加されている
- [ ] `findRenewalTargetsByAgent(int status, Date deadline, String agentName)` メソッドが追加されている
- [ ] `policyEndDate IS NOT NULL` 条件で NULL を除外している
- [ ] 結果が `policyEndDate ASC`（更新日が近い順）でソートされている
- [ ] 既存のクエリメソッドに影響がないこと

**技術メモ**:
- 変更ファイル: `app/src/main/java/com/example/crm/repository/CustomerRepository.java`
- Spring Data JPA `@Query` アノテーションで JPQL を記述
- `@Param` でパラメータバインディング（SQLインジェクション対策）
- deadline の算出は Java 側で行い、JPQL は `<=` 比較のみ（H2 方言依存を回避）
- 詳細仕様: `specs/renewal-notification/spec.md` セクション5（クエリ条件）

---

### タスク 3: Service層に更新通知ビジネスロジックを追加

**種別**: feat
**優先度**: 高
**依存**: タスク 1, タスク 2

**概要**:
`CustomerService` に契約更新通知のビジネスロジックメソッドを追加する。指定日数から期限日を算出し、Repository で取得した顧客リストを DTO に変換して返す。`daysUntilRenewal`（残日数）の算出もこのレイヤーで行う。

**受け入れ条件**:
- [ ] `getRenewalNotifications(int days, String agentName)` メソッドが追加されている
- [ ] 現在日 + 指定日数 = deadline の算出が正しく行われる
- [ ] `agentName` の有無で Repository の呼び出しメソッドが正しく分岐する
- [ ] `daysUntilRenewal` が `policyEndDate - 現在日` で正しく計算される（過去日は負の値）
- [ ] Customer エンティティから `RenewalCustomerDto` への変換が全フィールド正しく行われる
- [ ] `policyStatusText` が数値ステータスから正しく変換される（1 → "有効"）
- [ ] `RenewalNotificationResponse` に `renewals`, `totalCount`, `searchCriteria` が正しくセットされる

**技術メモ**:
- 変更ファイル: `app/src/main/java/com/example/crm/service/CustomerService.java`
- 使用クラス: `CustomerRepository`, `RenewalCustomerDto`, `RenewalNotificationResponse`
- 日数計算: `java.util.Date` → `LocalDate` に変換し、`ChronoUnit.DAYS.between()` を使用
- deadline 算出: `java.util.Date` で `Calendar.add(Calendar.DAY_OF_MONTH, days)` または `LocalDate.plusDays()`
- 処理フロー: deadline算出 → Repository呼出 → DTO変換 → レスポンス組立
- 詳細仕様: `specs/renewal-notification/spec.md` セクション4, 5

---

### タスク 4: Controller層に更新通知APIエンドポイントを追加

**種別**: feat
**優先度**: 高
**依存**: タスク 3

**概要**:
`CustomerController` に `GET /api/customers/renewals` エンドポイントを追加する。リクエストパラメータのバリデーションを行い、Service 層のビジネスロジックを呼び出してレスポンスを返す。

**受け入れ条件**:
- [ ] `GET /api/customers/renewals` エンドポイントが追加されている
- [ ] `daysUntilRenewal` パラメータがデフォルト値 30 で受け取れる
- [ ] `agentName` パラメータがオプションで受け取れる
- [ ] `daysUntilRenewal` が 1 未満の場合、400 Bad Request とエラーメッセージを返す
- [ ] `daysUntilRenewal` が 365 超の場合、400 Bad Request とエラーメッセージを返す
- [ ] `agentName` が空文字の場合、フィルタなし（null）として扱う
- [ ] 正常時に `RenewalNotificationResponse` の JSON を返す
- [ ] 既存の `/api/customers` 配下のエンドポイントに影響がないこと

**技術メモ**:
- 変更ファイル: `app/src/main/java/com/example/crm/controller/CustomerController.java`
- 使用クラス: `CustomerService`, `RenewalNotificationResponse`
- `@GetMapping("/renewals")` + `@RequestParam(defaultValue = "30")` + `@RequestParam(required = false)`
- バリデーション: `ResponseEntity.badRequest().body(Map.of("error", "..."))` パターン
- 詳細仕様: `specs/renewal-notification/spec.md` セクション4（API設計）、セクション6（エッジケース）

---

### タスク 5: 単体テスト・統合テストの作成

**種別**: test
**優先度**: 高
**依存**: タスク 3, タスク 4

**概要**:
契約更新通知機能の品質を担保するため、Service 層の単体テストと Controller 層の統合テストを作成する。正常系・異常系・境界値の各パターンをカバーする。

**受け入れ条件**:
- [ ] `CustomerServiceTest` — デフォルト日数での正常取得テスト
- [ ] `CustomerServiceTest` — カスタム日数の反映テスト
- [ ] `CustomerServiceTest` — 担当者フィルタの分岐テスト
- [ ] `CustomerServiceTest` — 該当0件で空リスト返却テスト
- [ ] `CustomerServiceTest` — `daysUntilRenewal` 算出テスト（未来日=正、過去日=負、当日=0）
- [ ] `CustomerControllerTest` — `GET /api/customers/renewals` で 200 OK を返すテスト
- [ ] `CustomerControllerTest` — `daysUntilRenewal=0` で 400 Bad Request を返すテスト
- [ ] `CustomerControllerTest` — `daysUntilRenewal=400` で 400 Bad Request を返すテスト
- [ ] `CustomerControllerTest` — `agentName` フィルタの動作テスト
- [ ] `CustomerControllerTest` — 存在しない担当者で空リストを返すテスト
- [ ] すべてのテストが `mvn test` で成功すること

**技術メモ**:
- 新規ファイル: `app/src/test/java/com/example/crm/service/CustomerServiceTest.java`
- 新規ファイル: `app/src/test/java/com/example/crm/controller/CustomerControllerTest.java`
- Service テスト: `@ExtendWith(MockitoExtension.class)` で `CustomerRepository` をモック化
- Controller テスト: `@SpringBootTest` + `@AutoConfigureMockMvc` で H2 を使用した統合テスト
- テストデータ: `@BeforeEach` で動的にテスト用顧客データを投入（`data.sql` は変更しない）
- 詳細仕様: `specs/renewal-notification/spec.md` セクション7（受け入れ条件）、`plan.md` セクション4（テスト戦略）

---

## サマリー

| タスク | 種別 | 優先度 | 依存 | 新規/修正 | 主な変更ファイル |
|-------|------|-------|------|----------|----------------|
| 1. DTO作成 | feat | 高 | なし | 新規 2ファイル | `dto/RenewalCustomerDto.java`, `dto/RenewalNotificationResponse.java` |
| 2. Repository拡張 | feat | 高 | なし | 既存修正 | `repository/CustomerRepository.java` |
| 3. Service実装 | feat | 高 | 1, 2 | 既存修正 | `service/CustomerService.java` |
| 4. Controller追加 | feat | 高 | 3 | 既存修正 | `controller/CustomerController.java` |
| 5. テスト作成 | test | 高 | 3, 4 | 新規 2ファイル | `test/.../CustomerServiceTest.java`, `test/.../CustomerControllerTest.java` |

**合計**: 5タスク（新規4ファイル + 既存3ファイル修正）
