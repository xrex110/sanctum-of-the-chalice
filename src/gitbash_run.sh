#!/bin/bash

#javac -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*;../lib/jsonlib/jackson-core-2.9.8.jar;../lib/jsonlib/jackson-databind-2.9.8.jar;../lib/jsonlib/jackson-annotations-2.9.8.jar;." *.java
#java -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*;../lib/jsonlib/jackson-core-2.9.8.jar;../lib/jsonlib/jackson-databind-2.9.8.jar;../lib/jsonlib/jackson-annotations-2.9.8.jar;." Sanctum
#rm *.class

rm -rf ../bin/*/*.class

javac -d ../bin/ -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*;../lib/jsonlib/jackson-core-2.9.8.jar;../lib/jsonlib/jackson-databind-2.9.8.jar;../lib/jsonlib/jackson-annotations-2.9.8.jar;." ./main/Sanctum.java
java -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*;../lib/jsonlib/jackson-core-2.9.8.jar;../lib/jsonlib/jackson-databind-2.9.8.jar;../lib/jsonlib/jackson-annotations-2.9.8.jar;../bin/" main.Sanctum

rm -rf ../bin/*/*.class
