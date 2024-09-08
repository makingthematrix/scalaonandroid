
## Imperative Loops

In the first sections, we saw how to write loops using recursion.

### While Loops 

We can also write loops with the `while` keyword:

      def power (x: Double, exp: Int): Double = {
        var r = 1.0
        var i = exp
        while (i > 0) { r = r * x; i = i - 1 }
        r
      }

As long as the condition of the `while` statement is `true`,
its body is evaluated.

### For Loops 

In Scala, there is a kind of `for` loop, too:

      for (i <- 1 until 3) { System.out.print(i.toString + " ") }

This displays `1 2`.

`For` loops translate similarly to `for` expressions but use the
`foreach` combinator instead of `map` and `flatMap`.

`foreach` is defined on collections with elements of type `A` as follows:

      def foreach(f: A => Unit): Unit =
        // apply `f` to each element of the collection

Example:

      for (i <- 1 until 3; j <- "abc") println(s"$i $j")

It translates to:

      (1 until 3) foreach (i => "abc" foreach (j => println(s"$i $j")))

## Exercise 

Complete the imperative implementation of `factorial` in the code editor.
