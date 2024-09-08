
## Common Operations on Lists

Transform the elements of a list using `map`:

      List(1, 2, 3).map(x => x + 1) == List(2, 3, 4)

Filter elements using `filter`:

      List(1, 2, 3).filter(x => x % 2 == 0) == List(2)

Transform each element of a list into a list and flatten the
results into a single list using `flatMap`:

      val xs =
        List(1, 2, 3).flatMap { x =>
          List(x, 2 * x, 3 * x)
        }
      xs == List(1, 2, 3, 2, 4, 6, 3, 6, 9)

### Optional Values

We represent an optional value of type `A` with the type `Option[A]`.
This is useful to implement, for instance, in partially defined
functions:

      // The `sqrt` function is not defined for negative values
      def sqrt(x: Double): Option[Double] = …

An `Option[A]` can either be `None` (if there is no value) or `Some[A]`
(if there is a value):

      def sqrt(x: Double): Option[Double] =
        if (x < 0) None else Some(…)

### Manipulating Options

It is possible to decompose options with pattern matching:

      def foo(x: Double): String =
        sqrt(x) match {
          case None => "no result"
          case Some(y) => y.toString
        }

## Exercise

1. Transform an optional value with `map`.
2. Filter values with `filter`.
3. Use `flatMap` to transform a successful value into an optional value.
