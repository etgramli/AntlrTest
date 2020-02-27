# DSL Generator
Generates a set of Java interfaces from a EBNF grammar file.

## Usage
This program can be packaged as a jar file with the maven target 'package' and be executed with the following command line options.

### Command line options
Short option | Long option | Expected argument | Required | Description
-------------|-------------|-------------------|----------|------------
h            | help        |                   | False    | Prints help screen
d            | directory   | directory         | True     | Target directory in that the interfaces will be saved
p            | package     | package           | True     | Java package in that the Interfaces will be located 
g            | grammar     | file              | True     | EBNF grammar in text file to generate interfaces from
