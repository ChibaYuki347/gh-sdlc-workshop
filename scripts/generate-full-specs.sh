#!/bin/bash
# Copilot Hook: Stop — Agent セッション終了時に仕様書の整合性チェックを促す
#
# Agent セッションが終了する際に、実装と仕様書の整合性を確認するメッセージを返します。

set -euo pipefail

INPUT=$(cat)

# git status で未追跡ファイルを含む変更ファイルを確認
GIT_STATUS_OUTPUT=$(git status --porcelain 2>/dev/null || echo "")
STATUS_FILES=$(printf '%s\n' "$GIT_STATUS_OUTPUT" | sed -E 's/^...//' || echo "")

HAS_CODE_CHANGES=false
HAS_SPEC_CHANGES=false

if echo "$STATUS_FILES" | grep -qE '^app/src/main/'; then
  HAS_CODE_CHANGES=true
fi

if echo "$STATUS_FILES" | grep -qE '^specs/'; then
  HAS_SPEC_CHANGES=true
fi

# 実装変更があるが仕様書更新がない場合に警告
if [ "$HAS_CODE_CHANGES" = true ] && [ "$HAS_SPEC_CHANGES" = false ]; then
  echo '{"systemMessage": "📋 セッション終了前の確認: 実装コードに変更がありますが、仕様書（specs/）が更新されていません。specs/api-spec.md および specs/ui-spec.md を確認し、変更内容を反映してください。"}'
  exit 0
fi

echo '{}'
exit 0
