@echo off
SET /A "index = 1"
SET /A "count = 3"
:while
if %index% leq %count% (
   echo RUN SCRIPT %index%
   start cmd.exe /k "node stress_test.js" %index%
   SET /A "index = index + 1"
   goto :while
)