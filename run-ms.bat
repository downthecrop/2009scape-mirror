cd Management-Server

.\mvnw.cmd package -DskipTests
xcopy target\*-with-dependencies.jar ms.jar
java -jar ms.jar