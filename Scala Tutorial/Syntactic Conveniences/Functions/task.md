
## Functions as Objects

We have seen that Scala's numeric types and the `Boolean`
type can be implemented like normal classes.

But what about functions?

In fact, function values *are* treated as objects in Scala.

The function type `A => B` is just an abbreviation for the class
`scala.Function1[A, B]`, which is defined as follows:

    package scala
    trait Function1[A, B] {
        def apply(x: A): B
    }

So functions are objects with `apply` methods.

There are also traits `Function2`, `Function3`, ... for functions which take more parameters (currently up to 22).

### Expansion of Function Values

An anonymous function such as

      (x: Int) => x * x

is expanded to:

      class AnonFun extends Function1[Int, Int] {
        def apply(x: Int) = x * x
      }
      new AnonFun

or, shorter, using *anonymous class syntax*:

      new Function1[Int, Int] {
        def apply(x: Int) = x * x
      }

### Expansion of Function Calls

A function call, such as `f(a, b)`, where `f` is a value of some class
type, is expanded to:

      f.apply(a, b)

So the OO-translation of

      val f = (x: Int) => x * x
      f(7)

would be:

      val f = new Function1[Int, Int] {
        def apply(x: Int) = x * x
      }
      f.apply(7)

### Functions and Methods

Note that a method such as

      def f(x: Int): Boolean = …

is not itself a function value.

But if `f` is used in a place where a function type is expected, it is
converted automatically to the function value:

      (x: Int) => f(x)

### `for` expressions 

You probably noticed that several data types of the standard library
have methods named `map`, `flatMap`, and `filter`.

These methods are so common in practice that Scala supports a dedicated
syntax: *for expressions*.

### `map`

Thus, instead of writing the following:

      xs.map(x => x + 1)

You can write:

      for (x <- xs) yield x + 1

You can read it as “for every value that I name ‘x’, in ‘xs’, return ‘x + 1’”.

### `filter`

Also, instead of writing the following:

      xs.filter(x => x % 2 == 0)

You can write:

      for (x <- xs if x % 2 == 0) yield x

The benefit of this syntax becomes more apparent when it is combined
with the previous one:

      for (x <- xs if x % 2 == 0) yield x + 1
    
      // Equivalent to the following:
      xs.filter(x => x % 2 == 0).map(x => x + 1)

### `flatMap`

Finally, instead of writing the following:

      xs.flatMap(x => ys.map(y => (x, y)))

You can write:

      for (x <- xs; y <- ys) yield (x, y)

You can read it as “for every value ‘x’ in ‘xs’, and then for
every value ‘y’ in ‘ys’, return ‘(x, y)’”.

## Exercise

Complete the function so that it becomes an equivalent of the following de-sugared code:

      xs.filter { x =>
        x % 2 == 0
      }.flatMap { x =>
        ys.map { y =>
          (x, y)
        }
      }


