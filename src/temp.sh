#/bin/bash

javac -cp ".:../lib/jackson-core-2.9.8.jar:../lib/jackson-databind-2.9.8.jar:../lib/jackson-annotations-2.9.8.jar" JsonTest.java MyValue.java
echo "Compiled successfully"
java -cp ".:../lib/jackson-core-2.9.8.jar:../lib/jackson-databind-2.9.8.jar:../lib/jackson-annotations-2.9.8.jar" JsonTest 

