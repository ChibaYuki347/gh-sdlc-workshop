# プロジェクト・コンスティテューション — 保険CRM管理システム

> このファイルは spec-kit Brownfield Bootstrap によって生成されました。
> プロジェクトの特性に基づいた SDD（仕様駆動開発）のガイドラインを定義します。

## プロジェクト概要

- **プロジェクト名**: 保険CRM管理システム（Insurance CRM）
- **種別**: Brownfield（既存プロジェクト）
- **技術スタック**: Java 17 / Spring Boot 3.2 / Spring Data JPA / H2 / Thymeleaf / Bootstrap 3
- **ビルドツール**: Maven
- **アーキテクチャ**: MVC（Controller → Service → Repository → Entity）

## コーディング規約

- クラス名: PascalCase（例: `CustomerService`）
- メソッド名: camelCase（例: `findById`）
- REST API: `/api/` プレフィックス、リソース名は複数形（例: `/api/customers`）
- UI ルート: Thymeleaf テンプレート、日本語ラベル
- エラーレスポンス: 適切な HTTP ステータスコードを返す
- Null 安全: `Optional` を活用

## データモデル

- `Customer`: 顧客情報（id, name, email, phone, address, registrationDate）
- `Policy`: 保険契約（id, policyNumber, policyType, policyStatus, startDate, endDate, premiumAmount, customer）
- `policyStatus`: 数値管理（0: 申請中, 1: 有効, 2: 失効, 3: 解約）

## 既知のレガシーパターン（改善対象）

以下は意図的に残されたレガシーパターンです。リファクタリング時の改善ポイントとして活用してください。

1. **God Class**: `CustomerService` にビジネスロジックが集中
2. **マジックナンバー**: `policyStatus` の数値リテラル（Enum 化推奨）
3. **ハードコードされた保険料率**: `calculatePremium()` 内の固定値
4. **バリデーション不足**: 入力値の検証が不十分
5. **テスト不在**: ユニットテスト・統合テストなし
6. **非効率な検索**: `searchCustomers()` が全件取得後にフィルタリング
7. **レガシー日付API**: `java.util.Date` の使用（`java.time` 推奨）

## SDD ワークフローガイドライン

### 仕様書作成時の注意事項

1. 既存の API エンドポイント（`specs/api-spec.md`）との互換性を維持すること
2. 既存の UI 画面（`specs/ui-spec.md`）のナビゲーション構造を尊重すること
3. 新規機能は既存のアーキテクチャパターン（Controller → Service → Repository）に従うこと
4. データモデルの変更時は、既存の `Customer` ↔ `Policy` 関係を考慮すること

### テスト駆動開発（TDD）原則

- 新規機能には必ずユニットテストを作成する
- テストクラスは `src/test/java/com/example/crm/` 配下に配置
- JUnit 5 + Spring Boot Test を使用
- テスト名は日本語メソッド名も可（例: `契約更新通知が正しく取得できること()`）

### コード再利用の原則

- 既存の `CustomerService` のメソッドを活用し、重複を避ける
- 新規 Service クラスは単一責任の原則に従い分割する
- 既存の `CustomerRepository` を拡張する場合は Spring Data JPA のクエリメソッドを活用
