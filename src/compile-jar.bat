rm *.class
rm Sanctum.jar
javac -classpath "../lib/libs/JOAL/jogamp-all-platforms/jar/*;../lib/jsonlib/jackson-core-2.9.8.jar;../lib/jsonlib/jackson-databind-2.9.8.jar;../lib/jsonlib/jackson-annotations-2.9.8.jar;." *.java
jar -cfvm Sanctum.jar jar-manifest.txt *.class
rm *.class
