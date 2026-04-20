# Copilot Instructions — 保険CRMプロジェクト

## プロジェクト概要
保険契約管理（CRM）Webアプリケーション。顧客情報と保険契約の管理を行う。

## 技術スタック
- Java 17 / Spring Boot 3.2
- Spring Data JPA / H2 Database（開発用）
- Maven ビルド
- REST API（JSON）

## アーキテクチャ
```
controller/ → REST APIエンドポイント
service/    → ビジネスロジック
model/      → エンティティ（Customer, Policy）
repository/ → Spring Data JPAリポジトリ
```

## コーディング規約
- クラス名: PascalCase
- メソッド名: camelCase
- REST API: `/api/` プレフィックス、リソース名は複数形
- エラーレスポンス: 適切なHTTPステータスコードを返す
- Null安全: Optional を活用し、NullPointerException を防止
- テスト: JUnit 5 + Spring Boot Test

## 既存モデル
- `Customer`: 顧客情報（id, name, email, phone, address, registrationDate）
- `Policy`: 保険契約（id, policyNumber, policyType, policyStatus, startDate, endDate, premiumAmount, customer）

## 注意事項
- 既存コードにはレガシーパターン（God Class、マジックナンバー等）が含まれる
- リファクタリング時は段階的に改善し、既存APIの互換性を維持すること
- policyStatus は数値（0: 申請中, 1: 有効, 2: 失効, 3: 解約）で管理されている（将来的にEnum化推奨）
