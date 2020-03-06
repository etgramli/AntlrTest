# DSL Generator
Generates a set of Java interfaces from a EBNF grammar file.

## Usage
This program can be packaged as a jar file with the maven target 'package' and be executed with the following command line options.

### Command line options
Short option | Long option | Expected argument | Required | Example | Description
-------------|-------------|-------------------|----------|---------|------------
h | help      |           | False |                        | Prints help screen
d | directory | directory | True  | /home/user/target      | Target directory in that the interfaces will be saved
p | package   | package   | True  | com.myname.project     | Java package in that the Interfaces will be located 
g | grammar   | file      | True  | /home/user/grammar.bnf | EBNF grammar in text file to generate interfaces from

## Use dependency
You can use this project as a dependency using maven. [Link](https://github.com/etgramli/AntlrTest/packages)

Use this token to access the maven dependency: 9dc232ed15a45f21ec42dd06d6c3ae8cdd257a5f
