cd Server
.\mvnw.bat package
move target\*-with-dependencies.jar server.jar
java -jar server.jar
