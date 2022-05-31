@echo off
cd Management-Server

if NOT exist DoNotCreateThisFile.txt (
    .\mvnw.cmd package -DskipTests
    xcopy /Y target\*-with-dependencies.jar ms.jar*
    java -jar ms.jar
)