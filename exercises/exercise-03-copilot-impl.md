# 演習3: Copilot 実装演習

**対応セッション**: セッション4 - GitHub Copilotによる実装とリファクタリング
**所要時間**: 約30分

---

## 🎯 演習のゴール

- 仕様書を元に Copilot を使ってコードを生成する体験をする
- AI が生成したコードを確認・修正するスキルを身につける

---

## 📋 シナリオ

演習2で作成した「契約更新通知機能」の仕様書（`specs/renewal-notification/spec.md`）を元に、実際のコードを実装します。

---

## 📝 演習手順

### Step 1: 機能ブランチを作成する

```bash
git checkout main
git pull origin main
git checkout -b feature/team-X-renewal-notification
```

### Step 2: Copilot Agent Mode で実装する

VS Code の Copilot Chat を開き、以下のプロンプトを入力:

```
@workspace specs/renewal-notification/spec.md の仕様に基づいて、
契約更新通知機能を実装してください。

以下のファイルを作成してください:

1. app/src/main/java/com/example/crm/service/RenewalNotificationService.java
   - 更新日が指定日数以内の契約をリストアップ
   - 通知対象の顧客情報を返す

2. app/src/main/java/com/example/crm/controller/RenewalNotificationController.java
   - GET /api/notifications/renewals エンドポイント
   - パラメータ: daysBeforeExpiry (デフォルト: 30)

3. app/src/main/java/com/example/crm/model/RenewalNotification.java
   - 通知データのDTO（顧客名、契約番号、更新日、残り日数）

既存の Customer, Policy モデルと
CustomerRepository, PolicyRepository を活用してください。
Spring Boot 3.2 の規約に従ってください。
```

### Step 3: 生成されたコードを確認する

生成されたコードを以下の観点で確認してください:

```
確認チェックリスト:
□ コンパイルが通るか（mvn compile）
□ 仕様書の要件を満たしているか
□ 既存コードとの整合性はあるか
□ エラーハンドリングは適切か
□ 日付の計算ロジックは正しいか
```

### Step 4: 対話的に修正する

確認で見つかった問題を Copilot に修正を依頼:

```
プロンプト例:
「生成されたRenewalNotificationServiceに以下の改善をしてください:
1. ステータスが有効(1)の契約のみを対象にする
2. endDateがnullの契約を除外する
3. 結果を更新日の昇順でソートする」
```

### Step 5: 動作確認

```bash
cd app
mvn spring-boot:run
```

別のターミナルで API を呼び出して確認:

```bash
# 更新が30日以内の契約を取得
curl http://localhost:8080/api/notifications/renewals

# 更新が60日以内の契約を取得
curl http://localhost:8080/api/notifications/renewals?daysBeforeExpiry=60
```

### Step 6: コミット

```bash
git add .
git commit -m "feat: 契約更新通知機能を追加

- RenewalNotificationService: 更新日チェックロジック
- RenewalNotificationController: REST APIエンドポイント
- RenewalNotification DTO: 通知データモデル"
```

---

## ✅ 完了条件

- [ ] RenewalNotificationService.java が作成されている
- [ ] RenewalNotificationController.java が作成されている
- [ ] `mvn compile` が成功する
- [ ] API エンドポイントが正しく動作する
- [ ] 変更がコミットされている

---

## 🔥 チャレンジ（時間に余裕がある場合）

以下の追加機能に挑戦してみてください:

1. **ページネーション対応**: 大量の通知対象がいる場合のページング
2. **営業担当者別フィルタ**: 特定の担当者の顧客のみを取得
3. **通知履歴**: 通知済みの顧客を記録し、重複通知を防止
