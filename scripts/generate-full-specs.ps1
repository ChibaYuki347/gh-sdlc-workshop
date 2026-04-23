# Copilot Hook: Stop — Agent セッション終了時に仕様書の整合性チェックを促す
#
# Agent セッションが終了する際に、実装と仕様書の整合性を確認するメッセージを返します。

$ErrorActionPreference = "Stop"

$InputJson = [Console]::In.ReadToEnd()

# git status で未追跡ファイルを含む変更ファイルを確認
try {
    $GitStatusOutput = git status --porcelain 2>$null
    if ($LASTEXITCODE -ne 0) { $GitStatusOutput = @() }
} catch {
    $GitStatusOutput = @()
}

$ChangedFiles = @($GitStatusOutput | Where-Object { $_ } | ForEach-Object { $_.Substring(3) })

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
