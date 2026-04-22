#!/bin/bash
# Copilot Hook: PostToolUse — 実装変更時に仕様書更新を促す
#
# このスクリプトは Copilot Agent がファイルを編集した後に自動実行されます。
# Controller, Model, Template の変更を検知し、仕様書の更新を促すメッセージを返します。

set -euo pipefail

INPUT=$(cat)

# 変更されたファイルパスを取得
FILE_PATH=$(echo "$INPUT" | jq -r '.toolInput.filePath // .toolInput.file_path // .toolArgs // empty' 2>/dev/null || echo "")

if [ -z "$FILE_PATH" ]; then
  echo '{}'
  exit 0
fi

# Controller (REST API) の変更を検知
if echo "$FILE_PATH" | grep -qE 'controller/.*Controller\.java$'; then
  if echo "$FILE_PATH" | grep -q 'WebController'; then
    echo '{"systemMessage": "⚠️ UI Controller が変更されました。specs/ui-spec.md の画面仕様を確認し、変更内容を反映してください。新しいルート、パラメータ変更、画面遷移の追加がある場合は仕様書を更新してください。"}'
  else
    echo '{"systemMessage": "⚠️ REST API Controller が変更されました。specs/api-spec.md のAPI仕様を確認し、変更内容を反映してください。新しいエンドポイント、リクエスト/レスポンス形式の変更がある場合は仕様書を更新してください。"}'
  fi
  exit 0
fi

# Model (エンティティ) の変更を検知
if echo "$FILE_PATH" | grep -qE 'model/.*\.java$'; then
  echo '{"systemMessage": "⚠️ データモデルが変更されました。specs/api-spec.md のデータモデルセクション、および specs/ui-spec.md の関連する画面表示項目を確認し、仕様書を更新してください。"}'
  exit 0
fi

# Thymeleaf テンプレートの変更を検知
if echo "$FILE_PATH" | grep -qE 'templates/.*\.html$'; then
  echo '{"systemMessage": "⚠️ UI テンプレートが変更されました。specs/ui-spec.md の該当画面の仕様を確認し、表示項目やユーザー操作の変更を反映してください。"}'
  exit 0
fi

# Service の変更を検知
if echo "$FILE_PATH" | grep -qE 'service/.*Service\.java$'; then
  echo '{"systemMessage": "⚠️ ビジネスロジックが変更されました。機能仕様に影響がある場合は、specs/ 配下の関連する仕様書を確認してください。"}'
  exit 0
fi

# その他のファイル → 何もしない
echo '{}'
exit 0
