
## Conditional Expressions

To express the choice between two alternatives, Scala
uses a conditional expression `if-else`.

It looks like the `if-else` in Java but is used for expressions, not statements.

Example:
~~~
    def abs(x: Double) = if (x >= 0) x else -x
~~~
Here `x >= 0` is a *predicate* of type `Boolean`.

## Boolean Expressions 

Boolean expressions `b` can include:

    true  false      // Constants
    !b               // Negation
    b && b           // Conjunction
    b || b           // Disjunction

They can also contain the usual comparison operations:
      e <= e, e >= e, e < e, e > e, e == e, e != e.

## Rewrite rules for Booleans

Here are reduction rules for Boolean expressions (`e` is an arbitrary expression):

    !true       -->   false
    !false      -->   true
    true && e   -->   e
    false && e  -->   false
    true || e   -->   true
    false || e  -->   e

Note that `&&` and `||` do not always need their right operand to be evaluated.

We say these expressions use "short-circuit evaluation".

## Summary

You have seen simple elements of functional programing in Scala:

 - arithmetic and boolean expressions;
 - conditional expressions if-else;
 - functions with recursion.

You have learned the difference between the call-by-name and
call-by-value evaluation strategies.

You have learned a way to reason about program execution: reduce expressions using
the substitution model.

## Exercise

Complete the definition of the following method, which computes the factorial of a number.
