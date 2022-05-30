cd Server

if NOT exist hasRan.txt (
    .\mvnw.cmd clean
    copy NUL hasRan.txt
)

.\mvnw.cmd package -DskipTests
move target\*-with-dependencies.jar server.jar
java -jar server.jar