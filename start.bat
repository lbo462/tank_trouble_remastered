@echo off
echo Deleting every *.class files ...
del *.class
:start
echo Compiling ...
javac Main.java
echo Done Compiling!
echo Launching Main class ...
java Main
echo Programm finished.
echo Deleting every *.class files ...
del *.class
PAUSE
goto start