# Copilot Hook: PostToolUse — 実装変更時に仕様書更新を促す
#
# このスクリプトは Copilot Agent がファイルを編集した後に自動実行されます。
# Controller, Model, Template の変更を検知し、仕様書の更新を促すメッセージを返します。

$ErrorActionPreference = "Stop"

$InputJson = [Console]::In.ReadToEnd()

# 変更されたファイルパスを取得
try {
    $parsed = $InputJson | ConvertFrom-Json
    $FilePath = if ($parsed.toolInput.filePath) { $parsed.toolInput.filePath }
                elseif ($parsed.toolInput.file_path) { $parsed.toolInput.file_path }
                elseif ($parsed.toolArgs) { $parsed.toolArgs }
                else { "" }
} catch {
    $FilePath = ""
}

if (-not $FilePath) {
    Write-Output '{}'
    exit 0
}

function Out-Json($msg) {
    $escaped = $msg -replace '\\', '\\\\' -replace '"', '\"'
    Write-Output "{`"systemMessage`": `"$escaped`"}"
}

# Controller (REST API) の変更を検知
if ($FilePath -match 'controller/.*Controller\.java$') {
    if ($FilePath -match 'WebController') {
        Out-Json "⚠️ UI Controller が変更されました。specs/ui-spec.md の画面仕様を確認し、変更内容を反映してください。新しいルート、パラメータ変更、画面遷移の追加がある場合は仕様書を更新してください。"
    } else {
        Out-Json "⚠️ REST API Controller が変更されました。specs/api-spec.md のAPI仕様を確認し、変更内容を反映してください。新しいエンドポイント、リクエスト/レスポンス形式の変更がある場合は仕様書を更新してください。"
    }
    exit 0
}

# Model (エンティティ) の変更を検知
if ($FilePath -match 'model/.*\.java$') {
    Out-Json "⚠️ データモデルが変更されました。specs/api-spec.md のデータモデルセクション、および specs/ui-spec.md の関連する画面表示項目を確認し、仕様書を更新してください。"
    exit 0
}

# Thymeleaf テンプレートの変更を検知
if ($FilePath -match 'templates/.*\.html$') {
    Out-Json "⚠️ UI テンプレートが変更されました。specs/ui-spec.md の該当画面の仕様を確認し、表示項目やユーザー操作の変更を反映してください。"
    exit 0
}

# Service の変更を検知
if ($FilePath -match 'service/.*Service\.java$') {
    Out-Json "⚠️ ビジネスロジックが変更されました。機能仕様に影響がある場合は、specs/ 配下の関連する仕様書を確認してください。"
    exit 0
}

# その他のファイル → 何もしない
Write-Output '{}'
exit 0
