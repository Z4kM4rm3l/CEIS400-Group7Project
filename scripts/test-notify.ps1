#requires -Version 5.1
<#!
  test-notify.ps1
  Detects Spring Boot server.port and context-path from application.yml,
  then sends three POST requests to Notification service: email, sms, event.
  Usage:
    .\scripts\test-notify.ps1 [-AppYmlPath <path>] [-ServerHost localhost] [-Port <int>] [-ContextPath <string>] [-Verbose]

  Notes:
    - Defaults: ServerHost=localhost, Port from application.yml or 8086, ContextPath from application.yml or /api
    - Uses Invoke-RestMethod with JSON bodies; prints status and response time.
!#>
param(
  [string]$AppYmlPath = (Join-Path (Get-Location) 'notification-service\src\main\resources\application.yml'),
  [string]$ServerHost = 'localhost',
  [int]$Port,
  [string]$ContextPath,
  [switch]$Verbose
)

function Get-AppConfig {
  param([string]$Path)
  $result = @{ Port = $null; ContextPath = $null }
  if (-not (Test-Path $Path)) { return $result }
  $yaml = Get-Content -Path $Path -Raw
  # Match server.port (nested or flat)
  $mPort = [regex]::Match($yaml, '(?m)^\s*server\s*:\s*[\s\S]*?^\s*port\s*:\s*(\d+)')
  if (-not $mPort.Success) { $mPort = [regex]::Match($yaml, '(?m)^\s*server\.port\s*:\s*(\d+)') }
  if ($mPort.Success) { $result.Port = [int]$mPort.Groups[1].Value }
  # Match server.servlet.context-path (nested or flat)
  $mCtx = [regex]::Match($yaml, '(?m)^\s*server\s*:\s*[\s\S]*?^\s*servlet\s*:\s*[\s\S]*?^\s*context-path\s*:\s*(\S+)')
  if (-not $mCtx.Success) { $mCtx = [regex]::Match($yaml, '(?m)^\s*server\.servlet\.context-path\s*:\s*(\S+)') }
  if ($mCtx.Success) { $result.ContextPath = $mCtx.Groups[1].Value.Trim() }
  return $result
}

# Resolve port/context-path
$app = Get-AppConfig -Path $AppYmlPath
$resolvedPort = if ($Port) { $Port } elseif ($app.Port) { $app.Port } else { 8086 }
$resolvedCtx  = if ($ContextPath) { $ContextPath } elseif ($app.ContextPath) { $app.ContextPath } else { '/api' }
if (-not $resolvedCtx.StartsWith('/')) { $resolvedCtx = '/' + $resolvedCtx }

# Build base URL (use format operator, avoid $Host automatic variable)
$base = 'http://{0}:{1}{2}' -f $ServerHost, $resolvedPort, $resolvedCtx
Write-Host ("[notify] Base URL: {0}" -f $base) -ForegroundColor Cyan

function Invoke-Notify {
  param([string]$Path, [hashtable]$Body)
  $url = "$base$Path"
  $json = ($Body | ConvertTo-Json -Depth 6)
  $sw = [System.Diagnostics.Stopwatch]::StartNew()
  try {
    $resp = Invoke-RestMethod -Method POST -Uri $url -ContentType 'application/json' -Body $json
    $sw.Stop()
    Write-Host ("[OK] POST {0} -> {1} in {2} ms" -f $Path, '202 Accepted', [int]$sw.Elapsed.TotalMilliseconds) -ForegroundColor Green
    return $true
  } catch {
    $sw.Stop()
    Write-Host ("[FAIL] POST {0} -> {1} in {2} ms" -f $Path, $_.Exception.Message, [int]$sw.Elapsed.TotalMilliseconds) -ForegroundColor Red
    return $false
  }
}

# 1) Email
$ok1 = Invoke-Notify -Path '/notify/email' -Body @{
  to      = 'demo@example.com'
  subject = 'Notification Service Smoke Test'
  body    = 'This is a test message sent at ' + (Get-Date).ToString('yyyy-MM-dd HH:mm:ss')
}

# 2) SMS (mock)
$ok2 = Invoke-Notify -Path '/notify/sms' -Body @{
  toNumber = '+15551234567'
  message  = 'SMS smoke test at ' + (Get-Date).ToString('HH:mm:ss')
}

# 3) Event (mock)
$ok3 = Invoke-Notify -Path '/notify/event' -Body @{
  eventType = 'LOW_STOCK'
  entityId  = 'SKU-ABC-123'
  metadata  = @{ currentQty = 4; threshold = 5; warehouseId = 'WH-01' }
}

if ($ok1 -and $ok2 -and $ok3) {
  Write-Host '[notify] All smoke tests passed ✅' -ForegroundColor Green
  exit 0
} else {
  Write-Host '[notify] One or more smoke tests failed ❌' -ForegroundColor Red
  exit 1
}
