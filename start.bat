@echo off
:start
echo Deleting every *.class files ...
del *.class
echo Compiling ...
javac Main.java
echo Done Compiling!
echo Launching Main class ...
java Main
echo Programm finished.
PAUSE
goto start
