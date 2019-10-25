#!/bin/sh
CLSSPTH="/home/eti/Dokumente/HTWG/Master/4_WS201920/Masterarbeit/code/antlr-4.7.2-complete.jar:$CLASSPATH"
java -cp "$CLSSPTH" org.antlr.v4.Tool "$*"
echo "Generated Lexer and Parser"
javac -cp "$CLSSPTH" *.java
echo "Compiled Lexer and Parser"
tail -n +28 e"$*" > e"$*"-rulesOnly.g4
java -cp "$CLSSPTH" org.antlr.v4.gui.TestRig bnf rulelist -tokens -tree -gui < e"$*"-rulesOnly.g4
echo "Built parse tree"

rm *.class *.java *.tokens *.interp e"$*"-rulesOnly.g4
echo "Cleaned up"
