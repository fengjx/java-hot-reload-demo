#!/usr/bin/env bash

DIST_DIR=.dist

mkdir -p $DIST_DIR

rm -rf $DIST_DIR/*

mvn -v
mvn clean package -Dmaven.test.skip=true -U

cp ./agent/target/agent-jar-with-dependencies.jar $DIST_DIR/agent.jar
cp ./app/target/app-jar-with-dependencies.jar $DIST_DIR/app.jar
cp ./attach/target/attach-jar-with-dependencies.jar $DIST_DIR/attach.jar
