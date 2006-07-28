@echo off

rem Root of Visual Developer Studio Common files.
set VSCommonDir=C:\PROGRA~1\MIAF9D~1\Common

rem Root of Visual Developer Studio installed files.
set MSDevDir=C:\PROGRA~1\MIAF9D~1\Common\msdev98

rem Root of Visual C++ installed files.
set MSVCDir=C:\PROGRA~1\MIAF9D~1\VC98

echo Setting environment for using Microsoft Visual C++ tools.

if "%OS%" == "" set PATH="%MSDevDir%\BIN";"%MSVCDir%\BIN";"%VSCommonDir%\TOOLS\%VcOsDir%";"%VSCommonDir%\TOOLS";"%windir%\SYSTEM";"%PATH%"
set INCLUDE=%MSVCDir%\ATL\INCLUDE;%MSVCDir%\INCLUDE;%MSVCDir%\MFC\INCLUDE;%INCLUDE%;.\include
set LIB=%MSVCDir%\LIB;%MSVCDir%\MFC\LIB;%LIB%

set VcOsDir=
set VSCommonDir=

echo Building 
cd native
nmake /f path.mak CLEAN
nmake /f path.mak CFG="Release"
copy .\Release\path.dll ..\build
echo Build complete
