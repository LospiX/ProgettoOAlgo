$params = (cat .\nomi_istanze.txt).split(";").Trim() ;
$nomeEseguibile  = ".\KernelSearchProj.jar" ;
Write-Output $params;
Write-Output $nomeEseguibile;


#java -jar $nomeEseguibile $params
$configs = Get-ChildItem .\Configurazioni | ForEach-Object {$_.FullName}
foreach ($cfg in $configs) {
    Copy-Item $cfg .\config.txt
    foreach ($istanza in $params) {
	java -jar $nomeEseguibile $istanza
    }
}
Start-Sleep 25

Start-Sleep 10