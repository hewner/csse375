#!/bin/sh
java -cp ../lib/antlr.jar antlr.Tool -o ../src/csheets/core/formula/compiler ../src/csheets/core/formula/compiler/FormulaCompiler.g
java -cp ../lib/antlr.jar antlr.Tool -o ../src/csheets/ext/assertion ../src/csheets/ext/assertion/AssertionParser.g