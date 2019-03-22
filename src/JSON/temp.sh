#/bin/bash

javac -cp ".:../../lib/jsonlib/jackson-core-2.9.8.jar:../../lib/jsonlib/jackson-databind-2.9.8.jar:../../lib/jsonlib/jackson-annotations-2.9.8.jar" *.java
echo "Compiled successfully"
java -cp ".:../../lib/jsonlib/jackson-core-2.9.8.jar:../../lib/jsonlib/jackson-databind-2.9.8.jar:../../lib/jsonlib/jackson-annotations-2.9.8.jar" JsonTest 

rm *.class
