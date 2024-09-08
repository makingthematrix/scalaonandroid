
## Computing the Square Root of a Value 

In this section, we will define a square root calculation method:

    /** Calculates the square root of parameter x */
    def sqrt(x: Double): Double = ...

The classical way to achieve this is by successive approximations using
Newton's method.

## Method

To compute `sqrt(x)`:

 - Start with an initial *estimate* `y` (let's pick `y = 1`).
 - Repeatedly improve the estimate by taking the mean of `y` and `x/y`.

Example:

    Estimation          Quotient              Mean
    1                   2 / 1 = 2             1.5
    1.5                 2 / 1.5 = 1.333       1.4167
    1.4167              2 / 1.4167 = 1.4118   1.4142
    1.4142              ...                   ...

## Implementation in Scala

First, we define a method which computes one iteration step:

      def sqrtIter(guess: Double, x: Double): Double =
        if (isGoodEnough(guess, x)) guess
        else sqrtIter(improve(guess, x), x)

Note that `sqrtIter` is *recursive*, its right-hand side calls itself.

Recursive methods need an explicit return type in Scala.

For non-recursive methods, the return type is optional.

Second, we define a method `improve` to improve the estimate and a test to check for termination:

      def improve(guess: Double, x: Double) =
        (guess + x / guess) / 2

      def isGoodEnough(guess: Double, x: Double) =
        abs(guess * guess - x) < 0.001

Third, we define the `sqrt` function:

      def sqrt(x: Double) = sqrtIter(1.0, x)

## Exercise
Complete the definitions of the methods `sqrtIter` and `improve` in the code editor. 
