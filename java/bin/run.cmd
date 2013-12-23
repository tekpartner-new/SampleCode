@echo off
setlocal enabledelayedexpansion

set EXAMPLES_DIR=%~dp0..
set SCRIPT_NAME=%0
set EXAMPLE_PATH_BEGIN=com\tangosol\examples

call set_example_env %1
if defined RET (
    goto %RET%
    )
    
call set_env SCRIPT_NAME
if defined RET (
    goto %RET%
    )

echo redirecting Coherence logging to %EXAMPLE%.log...

"%JAVA_HOME%"\bin\java %COH_OPTS% -Xms64m -Xmx64m ^
     -Dtangosol.coherence.distributed.localstorage=false ^
     -Dtangosol.coherence.log=%EXAMPLE%.log ^
     com.tangosol.examples.%EXAMPLE%.Driver %EXAMPLE% %EXAMPLES_DIR% %2 %3 %4 %5 %6 %7

:exit
