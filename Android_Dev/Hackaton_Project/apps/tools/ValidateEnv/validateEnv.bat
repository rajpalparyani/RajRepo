@echo off

set script=
set script=java -jar ValidateEnv-j2se.jar

if "%1"=="-v" goto validate_v
if "%1"=="-h" goto help 
if "%1"=="-l" goto validate_l
if "%1"=="-e" goto validate_e

:help
type README
goto end

:validate_v
%script% -v %2
goto end

:validate_l
%script% -l
goto end

:validate_e
%script% -e %2
goto end


:end

