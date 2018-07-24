@echo off
del /S /Q ..\res\*.class >nul
dir /B /S /O:N ..\src\csheets\*.java >c.lst
javac -cp ../src;../lib/antlr.jar -d ../res @c.lst %1 %2 %3
del c.lst