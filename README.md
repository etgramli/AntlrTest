# DSL Generator
Generates a set of Java interfaces from a EBNF grammar file.

## Usage
This program can be packaged as a jar file with the maven target 'package' and be executed with the following command line options.

### Command line options
Short option | Long option | Expected argument | Required | Example | Description
-------------|-------------|-------------------|----------|---------|------------
-h        | --help         |                   | False |                        | Prints help screen with command line options.
-d        | --directory    | directory         | True  | /home/user/target      | Target directory in that the package folder and interfaces will be saved.
-p        | --package      | package           | True  | com.myname.project     | Java package in that the interfaces will be located (a subdirectory will be created in the output directory).
-g        | --grammar      | file              | True  | /home/user/grammar.bnf | EBNF grammar in text file to generate interfaces from.
-s        | --sketch-graph | file              | False | graphName              | Writes DOT graph to the file with the given name in the target directory.

## Use dependency
You can use this project as a dependency using maven. [Link](https://github.com/etgramli/AntlrTest/packages)
