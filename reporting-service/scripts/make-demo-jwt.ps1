param(
    [Parameter(Mandatory = $true)]
    [string]$Secret,

    [string]$Issuer    = 'toolvault-demo',
    [string]$Audience  = 'reporting-service',
    [string]$Subject   = 'reporting',
    [string[]]$Roles   = @('reporting.read'),  # adjust if your API checks roles
    [string]$Scope     = 'reports:read',       # remove if not used
    [int]$TTLSeconds   = 3600
)

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

function Base64UrlEncode([byte[]]$bytes) {
    [Convert]::ToBase64String($bytes).TrimEnd('=').Replace('+', '-').Replace('/', '_')
}

# Header
$headerObj = @{ alg = 'HS256'; typ = 'JWT' }

# Payload
$now = [DateTimeOffset]::UtcNow
$payloadObj = @{
    iss   = $Issuer
    aud   = $Audience
    sub   = $Subject
    iat   = [int]$now.ToUnixTimeSeconds()
    nbf   = [int]$now.ToUnixTimeSeconds()
    exp   = [int]$now.AddSeconds($TTLSeconds).ToUnixTimeSeconds()
    roles = $Roles
    scope = $Scope
}

# Serialize
$headerJson  = (ConvertTo-Json $headerObj -Compress)
$payloadJson = (ConvertTo-Json $payloadObj -Compress)

$headerB64   = Base64UrlEncode([Text.Encoding]::UTF8.GetBytes($headerJson))
$payloadB64  = Base64UrlEncode([Text.Encoding]::UTF8.GetBytes($payloadJson))
$unsigned    = "$headerB64.$payloadB64"

# Sign (HS256) – use the .NET constructor to avoid array expansion issues
$keyBytes    = [Text.Encoding]::UTF8.GetBytes($Secret)
$hmac        = [System.Security.Cryptography.HMACSHA256]::new($keyBytes)
$sigBytes    = $hmac.ComputeHash([Text.Encoding]::UTF8.GetBytes($unsigned))
$sigB64      = Base64UrlEncode($sigBytes)

# Emit JWT
$jwt = "$unsigned.$sigB64"
Write-Output $jwt
