
##  Higher-Order Functions 

Functional languages treat functions as *first-class values*.

This means that, like any other value, a function
can be passed as a parameter and returned as a result.

This provides a flexible way to compose programs.

Functions that take other functions as parameters or return functions
as a result are called *higher order functions*.

###  Motivation 

Consider the following programs.

Taking the sum of the integers between `a` and `b`:

      def sumInts(a: Int, b: Int): Int =
        if (a > b) 0 else a + sumInts(a + 1, b)

Taking the sum of the cubes of all the integers between `a`
and `b`:

      def cube(x: Int): Int = x * x * x
    
      def sumCubes(a: Int, b: Int): Int =
        if (a > b) 0 else cube(a) + sumCubes(a + 1, b)

Taking the sum of the factorials of all the integers between `a`
and `b`:

      def sumFactorials(a: Int, b: Int): Int =
        if (a > b) 0 else factorial(a) + sumFactorials(a + 1, b)

Note how similar these methods are.
Can we factor out the common pattern?

###  Summing with Higher-Order Functions 

Let's define:

      def sum(f: Int => Int, a: Int, b: Int): Int =
        if (a > b) 0
        else f(a) + sum(f, a + 1, b)

We can then write:

      def id(x: Int): Int = x
      def sumInts(a: Int, b: Int)       = sum(id, a, b)
      def sumCubes(a: Int, b: Int)      = sum(cube, a, b)
      def sumFactorials(a: Int, b: Int) = sum(factorial, a, b)

##  Function Types 

The type `A => B` is the type of a *function* that
takes an argument of type `A` and returns a result of
type `B`.

So, `Int => Int` is the type of functions that map integers to integers.

Similarly, `(A1, A2) => B` is the type of functions that take two arguments
(of types `A1` and `A2`, respectively) and return a result of type `B`.

More generally, `(A1, ..., An) => B` is the type of functions that take `n`
arguments (of types `A1` to `An`) and return a result of type `B`.

###  Anonymous Functions 

Passing functions as parameters leads to the creation of many small functions.

Sometimes it is tedious to have to define (and name) these functions using `def`.

Compare to strings: we do not need to define a string using `val`. Instead of:

      val str = "abc"; println(str)

we can directly write:

      println("abc")

because strings exist as *literals*. Analogously, we can use function
literals, which let us write a function without giving it a name.

These are called *anonymous functions*.

### Anonymous Function Syntax

Example of a function that raises its argument to the third power:

      (x: Int) => x * x * x

Here, `(x: Int)` is the *parameter* of the function, and
`x * x * x` is its *body*.

The type of the parameter can be omitted if it can be inferred by the
compiler from the context.

If there are several parameters, they are separated by commas:

      (x: Int, y: Int) => x + y

### Anonymous Functions are Syntactic Sugar

An anonymous function `(x1: T1, …, xn: Tn) => e`
can always be expressed using `def` as follows:

      { def f(x1: T1, …, xn: Tn) = e ; f }

where `f` is an arbitrary, fresh name (that has not yet been used in the program).

One can therefore say that anonymous functions are *syntactic sugar*.

### Summation with Anonymous Functions 

Using anonymous functions, we can write sums in a shorter way:

      def sumInts(a: Int, b: Int)  = sum(x => x, a, b)
      def sumCubes(a: Int, b: Int) = sum(x => x * x * x, a, b)

##  Exercise 

The `sum` function uses linear recursion. Complete the tail-recursive
version.
