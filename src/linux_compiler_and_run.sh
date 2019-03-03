#/bin/bash

#run this compile script from the src folder
#JAVAFILE="$1"
#echo -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*;../src" $JAVAFILE

rm *.class

javac -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*:../src" Sanctum.java
java -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*:../src" Sanctum

rm *.class
