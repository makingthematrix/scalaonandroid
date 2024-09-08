
## Error Handling

This subsection introduces types that are useful to handle failures.

### Try

`Try[A]` represents a computation that attempted to return an `A`. It can
either be:
 - a `Success[A]`,
 - or a `Failure`.

The key difference between `None` and `Failure`s is that the latter provide
the reason for the failure:

      def sqrt(x: Double): Try[Double] =
        if (x < 0) Failure(new IllegalArgumentException("x must be positive"))
        else Success(…)

### Manipulating `Try[A]` values

Like options and lists, `Try[A]` is an algebraic data type, so it can
be decomposed using pattern matching.

`Try[A]` also has `map`, `filter` and `flatMap`. They behave the same
as with `Option[A]`, except that any exception thrown
during their execution is converted into a `Failure`.

### Either

`Either` can also be useful to handle failures. Basically, the type
`Either[A, B]` represents a value that can either be of type `A` or
of type `B`. It can be decomposed in two cases: `Left` or `Right`.

You can use one case to represent the failure and the other to represent
the success. What makes it different from `Try` is that you can choose a
type other than `Throwable` to represent the exception. Another difference
is that exceptions that occur when transforming `Either` values are
not converted into failures.

      def sqrt(x: Double): Either[String, Double] =
        if (x < 0) Left("x must be positive")
        else Right(…)

### Manipulating `Either[A, B]` Values

Since Scala 2.12, `Either` has `map` and `flatMap`. These methods
transform the `Right` case only. We say that `Either` is “right-biased”:

      Right(1).map((x: Int) => x + 1) shouldBe Right(2)
      Left("foo").map((x: Int) => x + 1) shouldBe Left("foo")

`Either` also has a `filterOrElse` method that turns a `Right` value
into a `Left` value if it does not satisfy a given predicate:

      Right(1).filterOrElse(x => x % 2 == 0, "Odd value") shouldBe Left("Odd value")

However, prior to Scala 2.12, `Either` was “unbiased”. You had to explicitly
specify which “side” (`Left` or `Right`) you wanted to `map`.

## Exercise

Complete the ```tripleEither()``` function so that it maps the successful results with the ```triple()``` function.