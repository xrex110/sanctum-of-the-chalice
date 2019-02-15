#!/bin/bash
files=`find . -type f -name "*.java"`
/usr/lib/jvm/java-8-openjdk/bin/javac $files
#/usr/lib/jvm/java-8-openjdk/bin/javac ./InputHandler.java
java Sanctum
rm -r *.class
