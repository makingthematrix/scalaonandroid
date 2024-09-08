
## Tuples

We saw earlier that case classes are useful to aggregate information.
However, sometimes you want to aggregate information without having to define
a complete case class for it. In such a case you can use *tuples*.



In the example in the code editor, the type `(Int, String)` represents a pair whose
first element is an `Int` and whose second element is a `String`.

Similarly, the value `(i, s)` is a pair whose first element is `i` and
whose second element is `s`.

More generally, a type `(T1, …, Tn)` is a *tuple type* of n elements
whose i'th element has type `Ti`.

And a value `(t1, … tn)` is a *tuple value* of n elements.

### Manipulating Tuples

You can retrieve the elements of a tuple by using a *tuple pattern*.

Alternatively, you can retrieve the 1st element with the `_1` member,
the 2nd element with the `_2` member, etc.

## Exercise

Complete the `pair` method definition to return the tuple including the parameters passed.
