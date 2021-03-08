#!/bin/sh
origDir=$(pwd)
if [ $# -eq 0 ]; then
    echo "Usage: $0 type"
    echo "Available types: client, server, ms, all"
    exit
else type=$1
fi

build_server() {
  echo "Building server..."
  cd Server/ || exit
  gradle build || exit
  cd "$origDir" || exit
  tar xvf Server/build/distributions/Server.tar || exit
}
build_client() {
  echo "Building client..."
  echo "Change IP from 127.0.0.1?(y/N)"
  read -r changeIP
  if [ "$changeIP" = "y" ]; then
    echo "Enter desired IP address:"
    read -r newIP
    echo "target_ip_addr:$newIP" > Client/src/main/resources/client.conf
  fi
  cd Client/ || exit
  gradle build || exit
  cd "$origDir" || exit
  tar xvf Client/build/distributions/Client.tar || exit
  cp Client/build/libs/client-1.0.0.jar Client/bin/2009scape.jar || exit
  echo "Client jarfile can be found at Client/bin/2009scape.jar"
}
build_ms() {
  echo "Building management server..."
  cd Management-Server/ || exit
  gradle build || exit
  cd "$origDir" || exit
  tar xvf Management-Server/build/distributions/Management-Server.tar || exit
}


if [ "$type" = "client" ]; then
    build_client
elif [ "$type" = "server" ]; then
    build_server
elif [ "$type" = "ms" ]; then
    build_ms
elif [ "$type" = "all" ]; then
    build_ms
    build_server
    build_client
else echo "Invalid type. Should be one of: client, server, ms, all"
fi

