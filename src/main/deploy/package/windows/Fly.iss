;This file will be executed next to the application bundle image
;I.e. current directory will contain folder Fly with application files
[Setup]
AppId={{com.tsasoftdev.fly}}
AppName=Fly
AppVersion=1.1
AppVerName=Fly 1.1
AppPublisher=TJ TSA
AppComments=Fly
AppCopyright=Copyright (C) 2016
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={localappdata}\Fly
DisableStartupPrompt=Yes
DisableDirPage=Yes
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=TJ TSA
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=Fly
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=Fly\Fly.ico
UninstallDisplayIcon={app}\Fly.ico
UninstallDisplayName=Fly
WizardImageStretch=No
WizardSmallImageFile=Fly-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "Fly\Fly.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "Fly\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Fly"; Filename: "{app}\Fly.exe"; IconFilename: "{app}\Fly.ico"; Check: returnTrue()
Name: "{commondesktop}\Fly"; Filename: "{app}\Fly.exe";  IconFilename: "{app}\Fly.ico"; Check: returnFalse()

[Run]
Filename: "{app}\Fly.exe"; Description: "{cm:LaunchProgram,Fly}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\Fly.exe"; Parameters: "-install -svcName ""Fly"" -svcDesc ""Fly"" -mainExe ""Fly.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\Fly.exe "; Parameters: "-uninstall -svcName Fly -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
