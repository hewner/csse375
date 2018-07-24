/**

Provides classes for representing formulas.
A cell in a spreadsheet can contain a formula (an expression).
Expressions are represented as abstract syntax trees,
consisting of a number of nodes, each of which are also expressions.
The existing types of expressions are literals, references, operations
(unary and binary) and function calls.

*/
package csheets.core.formula;