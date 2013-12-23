set EXAMPLE=%1

if not defined EXAMPLE (
    echo usage: %SCRIPT_NAME% example 
    echo where ^"example^" is a directory under com.tangosol.examples
    echo. 
    echo example: %SCRIPT_NAME% contacts
    echo.  
    echo current directories:
    dir /b ..\src\%EXAMPLE_PATH_BEGIN%
    set RET=exit
  )
  