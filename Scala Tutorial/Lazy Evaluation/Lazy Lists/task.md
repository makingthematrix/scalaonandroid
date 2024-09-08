
## Motivation

Consider the following program, which finds the second prime number between 1000 and 10000:

      ((1000 to 10000) filter isPrime)(1)

This is *much* shorter than the recursive alternative:


      def nthPrime(from: Int, to: Int, n: Int): Int =
        if (from >= to) throw new Error("no prime")
        else if (isPrime(from))
          if (n == 1) from else nthPrime(from + 1, to, n - 1)
        else nthPrime(from + 1, to, n)
      
      def secondPrime(from: Int, to: Int) = nthPrime(from, to, 2)

But from a standpoint of performance, the first version is pretty bad: it constructs
*all* prime numbers between `1000` and `10000` in a list, but only ever looks at
the first two elements of that list.

Reducing the upper bound would speed things up, but it creates a risks of missing the
second prime number altogether.

## Delayed Evaluation

However, we can make the short code efficient by using a trick:

- Avoid computing the tail of a sequence until it is needed for the evaluation
  result (which might be never).

This idea is implemented in a new class, the `LazyList`.

LazyLists are similar to lists, but their elements are evaluated only ''on demand''.

## Defining LazyLists

LazyLists are defined from the `LazyList.cons` constructor.

For instance,

      val xs = LazyList.cons(1, LazyList.cons(2, LazyList.empty))

## LazyList Ranges

Let's try to write a function that returns a `LazyList` representing a range of numbers
between `lo` and `hi`:

      def llRange(lo: Int, hi: Int): LazyList[Int] =
        if (lo >= hi) LazyList.empty
        else LazyList.cons(lo, llRange(lo + 1, hi))

Compare it to a similar function that produces a list:

      def listRange(lo: Int, hi: Int): List[Int] =
        if (lo >= hi) Nil
        else lo :: listRange(lo + 1, hi)

The functions have almost identical structure, yet they evaluate quite differently.

- `listRange(start, end)` will produce a list with `end - start` elements and return it.
- `llRange(start, end)` returns a single object of type `LazyList` with `start` as the head element.
    - The other elements are only computed when they are needed, where “needed” means that someone calls `tail` on the stream.

## Methods on LazyLists

`LazyList` supports almost all methods of `List`.

For instance, to find the second prime number between 1000 and 10000:

      (llRange(1000, 10000) filter isPrime)(1)

The one major exception is `::`.

`x :: xs` always produces a list, never a lazy list.

There is, however, an alternative operator `#::`, which produces a lazy list:

      x #:: xs  ==   LazyList.cons(x, xs)

`#::` can be used in expressions as well as in patterns.

## Implementation of LazyLists

The implementation of lazy lists is quite close to that of lists.

Here's the class `LazyList`:

      final class LazyList[+A] ... extends ... {
        override def isEmpty: Boolean = ...
        override def head: A = ...
        override def tail: LazyList[A] = ...
        …
      }

As for lists, all other methods can be defined in terms of these three.

Concrete implementations of streams are defined in the `LazyList.State` companion object.
Here's a first draft:

      private object State {
        object Empty extends State[Nothing] {
          def head: Nothing = throw new NoSuchElementException("head of empty lazy list")
          def tail: LazyList[Nothing] = throw new UnsupportedOperationException("tail of empty lazy list")
        }
        
        final class Cons[A](val head: A, val tail: LazyList[A]) extends State[A]
      }

The only important difference between the implementations of `List` and `LazyList`
concern `tail`, the second parameter of `LazyList.cons`.

For lazy lists, this is a by-name parameter: the type of `tail` starts with `=>`. In such
a case, this parameter is evaluated by following the rules of the call-by-name model.

That's why the second argument to `LazyList.cons` is not evaluated at the point of call.

Instead, it will be evaluated each time someone calls `tail` on a `LazyList` object.

In Scala 2.13, `LazyList` (previously `Stream`) became fully lazy from head to tail. To make it possible,
methods (`filter`, `flatMap`, etc.) are implemented in a way where the head is not evaluated unless it is
explicitly indicated.

For instance, here's `filter`:

      object LazyList extends SeqFactory[LazyList] {
        …
        private def filterImpl[A](ll: LazyList[A], p: A => Boolean, isFlipped: Boolean): LazyList[A] = {
        // DO NOT REFERENCE `ll` ANYWHERE ELSE, OR IT WILL LEAK THE HEAD
        var restRef = ll                         // val restRef = new ObjectRef(ll)
        newLL {
            var elem: A = null.asInstanceOf[A]
            var found   = false
            var rest    = restRef                  // var rest = restRef.elem
            while (!found && !rest.isEmpty) {
                elem    = rest.head
                found   = p(elem) != isFlipped
                rest    = rest.tail
                restRef = rest                       // restRef.elem = rest
            }
            if (found) sCons(elem, filterImpl(rest, p, isFlipped)) else State.Empty
        }
      }

## Exercise

Consider the modification of `llRange` given in the code editor. When you write
`llRange(1, 10).take(3).toList`, what is the value of `rec`?

Be careful, the head is also evaluated!
