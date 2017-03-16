@echo off
CD /d %~dp0\

SET JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80
SET PATH=C:\apache-maven-3.3.9\bin;%PATH%;%JAVA_HOME%\bin

echo %PATH%

call mvn eclipse:eclipse -DdownloadSources=true

call appcfg.cmd update src\main\webapp

echo Finish !