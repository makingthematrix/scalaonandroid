
## Lexical Scoping

The definitions of outer blocks are visible inside the block unless they are shadowed.
Shadowed definitions are those that are redefined in a lower scope.

Therefore, we can simplify `sqrt` by eliminating redundant occurrences of the `x` parameter, which means
the same thing everywhere:

### The `sqrt` Function, Take 3

      def sqrt(x: Double) = {
        def sqrtIter(guess: Double): Double =
          if (isGoodEnough(guess)) guess
          else sqrtIter(improve(guess))
    
        def improve(guess: Double) =
          (guess + x / guess) / 2
    
        def isGoodEnough(guess: Double) =
          abs(square(guess) - x) < 0.001
    
        sqrtIter(1.0)
      }

### Semicolons 

In Scala, semicolons at the end of lines are in most cases optional.

You could write:

      val x = 1;

but most people would omit the semicolon.

On the other hand, if there is more than one statement on a line, they need to be
separated by semicolons:

      val y = x + 1; y * y

### Semicolons and infix operators

One issue with Scala's semicolon convention is how to write expressions that span
several lines. For instance, these two lines:
```
      someLongExpression
```
```
    + someOtherExpression
```
would be interpreted as *two* expressions:
```
      someLongExpression;
```
```
      + someOtherExpression
```
There are two ways to overcome this problem.

You could write the multi-line expression in parentheses because semicolons
are never used inside `(…)`:
```
      (someLongExpression
```
```
    + someOtherExpression)
```
Or otherwise, you could write the operator on the first line because this tells the Scala
compiler that the expression is not yet finished:
```
      someLongExpression +
```
```
      someOtherExpression
```
### Top-Level Definitions

In real Scala programs, `def` and `val` definitions must be written
within a top-level *object definition*, in a .scala file:

      object MyExecutableProgram {
        val myVal = …
        def myMethod = …
      }

The above code defines an *object* named `MyExecutableProgram`. You
can refer to its *members* using the common dot notation:

      MyExecutableProgram.myMethod

The definition of `MyExecutableProgram` is *top-level* because it
is not nested within another definition.

### Packages and Imports 

Top-level definitions can be organized in *packages*.
To place a class or an object inside a package, use a package clause
at the top of your source file:

      // file foo/Bar.scala
      package foo
      object Bar { … }

      // file foo/Baz.scala
      package foo
      object Baz { … }

Definitions located in a package are visible from other definitions
located in the same package:

      // file foo/Baz.scala
      package foo
      object Baz {
        // Bar is visible because it is in the `foo` package too
        Bar.someMethod
      }

On the other hand, definitions located in other packages are not directly
visible: you must use *fully qualified names* to refer to them:

      // file quux/Quux.scala
      package quux
      object Quux {
        foo.Bar.someMethod
      }

Finally, you can import names to avoid repeating their fully qualified form:

      // file quux/Quux.scala
      package quux
      import foo.Bar
      object Quux {
        // Bar refers to the imported `foo.Bar`
        Bar.someMethod
      }

### Automatic Imports 

Some entities are automatically imported in any Scala program.

These are:

 - All members of the `scala` package;
 - All members of the  `java.lang` package;
 - All members of the singleton object `scala.Predef`.

Here are the fully qualified names of some types and functions
which you have seen so far:

```
      Int                            scala.Int
```
```
      Boolean                        scala.Boolean
```
```
      Object                         java.lang.Object
```
```
      String                         java.lang.String
```
### Writing Executable Programs 
 
Let's consider creating standalone
applications in Scala.

Each such application contains an object with a `main` method.

For instance, here is the "Hello World!" program in Scala:

      object Hello {
        def main(args: Array[String]) = println("hello world!")
      }

Once this program is compiled, you can start it from the command line with the following command:

      $ scala Hello

## Exercise
Complete the expression for `y` in the `Baz` object to make it  represent the sum of `x` fields from the objects `Foo` and `Bar`.