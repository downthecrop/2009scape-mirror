#!/bin/bash

cd Server
if [ ! -f hasRun.txt ]; then
  sh mvnw clean
  touch hasRun.txt
fi
sh mvnw package -DskipTests
mv target/*with-dependencies.jar /tmp/server.jar
java -jar /tmp/server.jar
