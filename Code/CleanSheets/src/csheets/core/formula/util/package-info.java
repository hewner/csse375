/**

<p>Provides utilities for performing operations on formulas. Classes that perform
operations on expression trees must implement the <code>ExpressionVisitor</code>
interfacce, either directly or by extending one of the base-classes:
<ul>
<li><code>AbstractExpressionVisitor</code> - that simply visits all nodes in the tree
<li><code>ExpressionBuilder</code> - that copies a given expression
</ul>

<p><code>ExpressionVisitor</code>s may throw <code>ExpressionVisitorException
</code>s, or sub-classes thereof.

*/
package csheets.core.formula.util;