if not defined COHERENCE_HOME (
    echo COHERENCE_HOME is not set!
    echo set COHERENCE_HOME to a Coherence 3.6 or later installation.
    set RET=exit
    goto :EOF
  )

if not defined JAVA_HOME (
    echo JAVA_HOME is not set!
    echo set JAVA_HOME to a JDK 1.5 or greater installation.
    set RET=exit
    goto :EOF
  )

rem remove any double quotes in the environment variables

set COHERENCE_HOME=!COHERENCE_HOME:"=!
set JAVA_HOME=!JAVA_HOME:"=!

set CLASSES_DIR=%EXAMPLES_DIR%\classes
set EXAMPLE_PATH=%EXAMPLE_PATH_BEGIN%\%EXAMPLE%
set EXAMPLE_CONFIG_DIR=%EXAMPLES_DIR%\resource\%EXAMPLE%
set POF_PATH=%EXAMPLE_PATH_BEGIN%\pof
set CONFIG_DIR=%EXAMPLES_DIR%\resource\config

rem run script uses client config (if available) but run-proxy and run-cache-server do not
if	/I [%SCRIPT_NAME%] == [run]  (
   if exist %EXAMPLE_CONFIG_DIR%\client-cache-config.xml (
       set CACHE_CONFIG_FILE_NAME=client-cache-config.xml    
   ) else (
   set CACHE_CONFIG_FILE_NAME=examples-cache-config.xml
   )
   ) else (
   set CACHE_CONFIG_FILE_NAME=examples-cache-config.xml
   )

rem if config is found in examples resource directory, use it instead of basic config
if exist %EXAMPLE_CONFIG_DIR%\%CACHE_CONFIG_FILE_NAME% (
    set CACHE_CONFIG=%EXAMPLE_CONFIG_DIR%\%CACHE_CONFIG_FILE_NAME%
    ) else (
    set CACHE_CONFIG=%CONFIG_DIR%\%CACHE_CONFIG_FILE_NAME%
    )

set CP="%EXAMPLE_CONFIG_DIR%;%CONFIG_DIR%;%COHERENCE_HOME%"\lib\coherence.jar;%CLASSES_DIR%

set COH_OPTS=%COH_OPTS% -cp %CP%
set COH_OPTS=%COH_OPTS% -Dtangosol.coherence.cacheconfig=%CACHE_CONFIG%
rem using non-default port to prevent accidentally joining other clusters 
set COH_OPTS=%COH_OPTS% -Dtangosol.coherence.clusterport=5555
