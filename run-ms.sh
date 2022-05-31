#!/bin/bash
cd Management-Server
sh mvnw package
mv target/*with-dependencies.jar /tmp/ms.jar
java -jar /tmp/ms.jar
