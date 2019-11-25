#!/bin/sh
CLSSPTH="/home/eti/Dokumente/HTWG/Master/4_WS201920/Masterarbeit/code/antlr-4.7.2-complete.jar:$CLASSPATH"
java -cp "$CLSSPTH" org.antlr.v4.Tool bnf.g4
echo "Generated Lexer and Parser"
javac -cp "$CLSSPTH" *.java
echo "Compiled Lexer and Parser"
java -cp "$CLSSPTH" org.antlr.v4.gui.TestRig bnf rulelist -tokens -tree -gui < "$*"
echo "Built parse tree"

rm -f *.class *.java *.tokens *.interp "$*"-rulesOnly.g4
echo "Cleaned up"
