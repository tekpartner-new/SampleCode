@echo off
setlocal enabledelayedexpansion

set EXAMPLES_DIR=%~dp0..

set EXAMPLE_PATH_BEGIN=com\tangosol\examples
set SCRIPT_NAME=%0

call set_example_env %1
if defined RET (
    goto %RET%
    )
    
call set_env SCRIPT_NAME
if defined RET (
    goto %RET%
    )

@rem Perform build
echo building %EXAMPLE% from %EXAMPLE_PATH%

if NOT EXIST %CLASSES_DIR% mkdir %CLASSES_DIR%

dir %EXAMPLES_DIR%\src\%POF_PATH%\*.java /s /b > pof-list.txt
"%JAVA_HOME%"\bin\javac -d %CLASSES_DIR% -source 1.5 -cp %CP% @pof-list.txt

del pof-list.txt

dir %EXAMPLES_DIR%\src\%EXAMPLE_PATH%\*.java /s /b > example-list.txt
"%JAVA_HOME%"\bin\javac -d %CLASSES_DIR% -source 1.5 -cp %CP% @example-list.txt
del example-list.txt

echo To run this example execute 'bin\run %EXAMPLE%'

:exit
