@echo off
rem Compile and run the Smart Library Management System (Windows)
if not exist out mkdir out
javac -d out src\com\library\*.java src\com\library\exception\*.java src\com\library\model\*.java src\com\library\service\*.java
if errorlevel 1 (
    echo Compilation failed.
    exit /b 1
)
java -cp out com.library.Main
