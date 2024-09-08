## Named Parameters

It can sometimes be difficult to figure out what the meaning of
each parameter passed to a function is. Consider for instance the following
expression:

      Range(1, 10, 2)

What does it mean? We can improve the readability by using *named
parameters*.

Based on the fact that the `Range` constructor is defined as follows:

      case class Range(start: Int, end: Int, step: Int)

we can rewrite our expression as follows:

      Range(start = 1, end = 10, step = 2)

It is now clearer that this expression defines a range of numbers
from 1 to 10 by increments of 2.

### Default Values 

Method parameters can have default values. Let’s refine the `Range`
constructor:

      case class Range(start: Int, end: Int, step: Int = 1)

Here, we say that the `step` parameter has a default value, `1`.

Then, at use site, we can omit this parameter, and the compiler
will supply it for us by using its default value.


### Repeated Parameters

You can define a function that can receive an arbitrary number of
parameters (of the same type).


The `average` function defined in the code editor takes at least one `Int` parameter and then
an arbitrary number of other values and computes their average.
By forcing users to supply at least one parameter, we make it impossible
for them to compute the average of an empty list of numbers.

Sometimes you want to supply each element of a list as many parameters.
You can do that by adding a `: _*` type ascription to your list:

      val xs: List[Int] = …
      average(1, xs: _*)

### Type Aliases

In the same way as you can give meaningful names to expressions,
you can give meaningful names to *type expressions*.


## Exercise
- Complete the `step` default parameter in the `Range` definition for it to be equal to 1.
- Complete the `average` definition for it to take an arbitrary number of parameters.
- Complete the `Result` definition for its `Right` to be a tuple of integer numbers.