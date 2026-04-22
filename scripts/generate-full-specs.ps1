# Copilot Hook: Stop — Agent セッション終了時に仕様書の整合性チェックを促す
#
# Agent セッションが終了する際に、実装と仕様書の整合性を確認するメッセージを返します。

$ErrorActionPreference = "Stop"

$InputJson = [Console]::In.ReadToEnd()

# git diff で変更されたファイルを確認
try {
    $ChangedFiles = git diff --name-only HEAD 2>$null
} catch {
    $ChangedFiles = ""
}

$HasCodeChanges = $false
$HasSpecChanges = $false

if ($ChangedFiles -match 'app/src/main/') {
    $HasCodeChanges = $true
}

if ($ChangedFiles -match 'specs/') {
    $HasSpecChanges = $true
}

# 実装変更があるが仕様書更新がない場合に警告
if ($HasCodeChanges -and -not $HasSpecChanges) {
    $msg = "セッション終了前の確認: 実装コードに変更がありますが、仕様書（specs/）が更新されていません。specs/api-spec.md および specs/ui-spec.md を確認し、変更内容を反映してください。"
    $escaped = $msg -replace '\\', '\\\\' -replace '"', '\"'
    Write-Output "{`"systemMessage`": `"$escaped`"}"
    exit 0
}

Write-Output '{}'
exit 0
