
 ##Scala Tutorial

 The following set of sections provides a quick tutorial on the Scala language.

 The contents is based on the MOOCS [Functional Programming Principles in Scala](https://www.coursera.org/learn/progfun1/home)
 and [Functional Program Design in Scala](https://www.coursera.org/learn/progfun2/home).

 The target audience is people who already have *some* experience of programming and who are familiar with
 the JVM.

 ##Elements of Programming

 Programming languages give programmers ways to express computations.

 Every non-trivial programming language provides:

  - primitive expressions representing the simplest elements;
  - ways to *combine* expressions;
  - ways to *abstract* expressions, which introduce a name for an expression by which it can then be referred to.

 ## Primitive Expressions

 Here are some examples of *primitive expressions*:

  - The number “1”:

    `1`
 
  - The boolean value "true":

    `true`
 
  - The text "Hello, Scala!":

    `"Hello, Scala!"`
 
 (Note the usage of double quotes `"`.)

 ## Compound Expressions 

 More complex expressions can be expressed by *combining* simpler expressions
 using *operators*. They can therefore express more complex computations:

  - How much is one plus two?

    `1 + 2`
 
  - What is the result of the concatenation of the texts “Hello, ” and “Scala!”?

    `"Hello, " ++ "Scala!"`
 
 ## Evaluation 

 A non-primitive expression is evaluated as follows:

  1. Take the leftmost operator.
  2. Evaluate its operands (left before right).
  3. Apply the operator to the operands.

  The evaluation process stops once it results in a value.

 ### Example

 Here is the evaluation of an arithmetic expression:

    (1 + 2) * 3
    3 * 3
    9
 
 ## Exercise
 
Complete the expression in `sayHello()` to return "Hello, Scala!".
Complete the expression in `sumTheNumbers()` to return `4`.
