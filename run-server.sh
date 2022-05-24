#!/bin/bash
cd Server
sh mvnw package
mv target/*with-dependencies.jar /tmp/server.jar
java -jar /tmp/server.jar
