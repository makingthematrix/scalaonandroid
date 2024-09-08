
## Multiple Parameters

Separate several parameters with commas:

    def sumOfSquares(x: Double, y: Double) = square(x) + square(y)

## Parameters and Return Types 

Function parameters come with their type, which is given after a colon:

    def power(x: Double, y: Int): Double = ...

If a return type is given, it follows the parameter list.

## Val vs Def

The right-hand side of a `def` definition is evaluated on each use.

The right-hand side of a `val` definition is evaluated at the point of the definition
itself. Afterwards, the name refers to the value.

    val x = 2
    val y = square(x)

For instance, `y` above refers to `4`, not `square(2)`.

## Evaluation of Function Applications

Applications of parametrized functions are evaluated in a way similar to
operators:

 1. Evaluate all function arguments, from left to right.
 2. Replace the function application by the function's right-hand side and, at the same time
 3. Replace the formal parameters of the function by the actual arguments.

## Example

    sumOfSquares(3, 2+2)
    sumOfSquares(3, 4)
    square(3) + square(4)
    3 * 3 + square(4)
    9 + square(4)
    9 + 4 * 4
    9 + 16
    25

## The substitution model

This scheme of expression evaluation is called the *substitution model*.

The idea underlying this model is that all evaluation does is *reduce
an expression to a value*.

It can be applied to all expressions as long as they have no side effects.

The substitution model is formalized in the λ-calculus, which gives
a foundation for functional programming.

## Termination 

Does every expression reduce to a value (in a finite number of steps)?

No. Here is a counter-example:

    def loop: Int = loop

    loop

## Value Definitions and Termination

The difference between `val` and `def` becomes apparent when the right-hand
side does not terminate. Given

      def loop: Int = loop

A definition

      def x = loop

is OK, but a value

      val x = loop

will lead to an infinite loop.

## Changing the evaluation strategy

The interpreter reduces function arguments to values before rewriting the
function application.

One could alternatively apply the function to unreduced arguments.

For instance:

    sumOfSquares(3, 2+2)
    square(3) + square(2+2)
    3 * 3 + square(2+2)
    9 + square(2+2)
    9 + (2+2) * (2+2)
    9 + 4 * (2+2)
    9 + 4 * 4
    25

## Call-by-name and call-by-value 

The first evaluation strategy is known as *call-by-value*,
the second is known as *call-by-name*.

Both strategies reduce to the same final values
as long as

 - the reduced expression consists of pure functions, and
 - both evaluations terminate.

Call-by-value has the advantage that it evaluates every function argument
only once.

Call-by-name has the advantage that a function argument is not evaluated if the
corresponding parameter is unused in the evaluation of the function body.

Scala normally uses call-by-value.

## Exercise

Complete the following definition of the `triangleArea` function,
which takes the base and height of a triangle as parameters and returns
its area.
