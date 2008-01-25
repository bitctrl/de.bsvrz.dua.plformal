@echo off

call ..\..\..\skripte-dosshell\einstellungen.bat

set cp=..\..\de.bsvrz.sys.funclib.bitctrl\de.bsvrz.sys.funclib.bitctrl-runtime.jar
set cp=%cp%;..\de.bsvrz.dua.plformal-runtime.jar
set cp=%cp%;..\de.bsvrz.dua.plformal-test.jar
set cp=%cp%;..\..\junit-4.1.jar

title Pruefungen SE4 - DUA, SWE 4.1

echo ========================================================
echo #  Pruefungen SE4 - DUA, SWE 4.1
echo #
echo #  Die Funktionalitaet wird entsprechend der
echo #  Pruefspezifikation ueberprueft.
echo ========================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.plformal.test.PlPruefungFormalTest
pause

echo ========================================================
echo #  Pruefungen SE4 - DUA, SWE 4.1
echo #
echo #  Im Folgenden werden Funktionen allgemeine Funktionen getestet
echo ========================================================
echo.

%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.plformal.allgemein.DUAUtensilienTest
pause

echo ========================================================
echo #  Pruefungen SE4 - DUA, SWE 4.1
echo #
echo #  Im Folgenden werden Funktionen der Anmeldungsverwaltung ueberprueft
echo ========================================================
echo.
%java% -cp %cp% org.junit.runner.JUnitCore de.bsvrz.dua.plformal.av.TestDAVObjektAnmeldung
pause

