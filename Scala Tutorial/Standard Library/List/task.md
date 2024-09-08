
## List

The list is a fundamental data structure in functional programming.

A list having `x1`, …, `xn` as elements is written as `List(x1, …, xn)`:

      val fruit  = List("apples", "oranges", "pears")
      val nums   = List(1, 2, 3, 4)
      val diag3  = List(List(1, 0, 0), List(0, 1, 0), List(0, 0, 1))
      val empty  = List()

 - Lists are immutable – the elements of a list cannot be changed.
 - Lists are recursive (as you will see in the next subsection).
 - Lists are *homogeneous*: a list is intended to be composed of elements that all have the same type.

The type of a list with elements of type `T` is written `List[T]`:

      val fruit: List[String]    = List("apples", "oranges", "pears")
      val nums : List[Int]       = List(1, 2, 3, 4)
      val diag3: List[List[Int]] = List(List(1, 0, 0), List(0, 1, 0), List(0, 0, 1))
      val empty: List[Nothing]   = List()

### Constructors of Lists 

Actually, all lists are constructed from:

 - the empty list `Nil`, and
 - the construction operation `::` (pronounced *cons*): `x :: xs` gives a new list
   with the first element `x`, called the `head`, followed by the `tail` `xs`, which is itself a list of elements.

For example:

      val fruit = "apples" :: ("oranges" :: ("pears" :: Nil))
      val nums  = 1 :: (2 :: (3 :: (4 :: Nil)))
      val empty = Nil

#### Right Associativity 

Convention: Operators ending in “`:`” associate to the right.

`A :: B :: C` is interpreted as `A :: (B :: C)`.

We can thus omit the parentheses in the definition above.

      val nums = 1 :: 2 :: 3 :: 4 :: Nil

Operators ending in “`:`” are also different in the fact that they are seen as method calls of
the *right-hand* operand.

So the expression above is equivalent to:

      val nums = Nil.::(4).::(3).::(2).::(1)

### Manipulating Lists 
 
It is possible to decompose lists with pattern matching:

 - `Nil`: the `Nil` constant;
 - `p :: ps`: A pattern that matches a list with a `head` matching `p` and a
   `tail` matching `ps`.
```
      nums match {
        // Lists of `Int` that starts with `1` and then `2`
        case 1 :: 2 :: xs => …
        // Lists of length 1
        case x :: Nil => …
        // Same as `x :: Nil`
        case List(x) => …
        // The empty list, same as `Nil`
        case List() =>
        // A list that contains as only element another list that starts with `2`
        case List(2 :: xs) => …
      }
```
## Exercise: Sorting Lists

Suppose we want to sort a list of numbers in ascending order.

 -  One way to sort the list `List(7, 3, 9, 2)` is to first sort the
    tail `List(3, 9, 2)` to obtain `List(2, 3, 9)`.
 -  The next step is to insert the head `7` in the right place
    to obtain the result `List(2, 3, 7, 9)`.

This idea describes *Insertion Sort*:

      def insertionSort(xs: List[Int]): List[Int] = xs match {
        case List() => List()
        case y :: ys => insert(y, insertionSort(ys))
      }

Complete the definition of insertion sort by filling in the blanks.
