## Type Bounds

Consider the method `selection`, which takes two animals as parameters
and returns the one with the highest `fitness` value:

What would be the best type you can give to `selection`? Maybe:

      def selection(a1: Animal, a2: Animal): Animal

In most situations this is fine, but can one be more precise?

One might want to express that `selection`
takes `Zebra`s to `Zebra`s and `Reptile`s to `Reptile`s.

### Upper Bounds 

A way to express this is:

      def selection[A <: Animal](a1: A, a2: A): A =
        if (a1.fitness > a2.fitness) a1 else a2

Here, “`<: Animal`” is an *upper bound* of the type parameter `A`.

It means that `A` can be instantiated only to the types that conform to `Animal`.

Generally, the notation

 - `A <: B` means: *`A` is a subtype of `B`*, and
 - `A >: B` means: *`A` is a supertype of `B`*, or *`B` is a subtype of `A`*.

### Lower Bounds

You can also use a lower bound for a type variable.

      A >: Reptile

The type parameter `A` can range only over the *supertypes* of `Reptile`.

So `A` could be one of `Reptile`, `Animal`, `AnyRef`, or `Any`.

(We will see  where lower bounds are useful later on.)

### Mixed Bounds 

Finally, it is also possible to mix a lower bound with an upper bound.


## Exercise

Complete the type bounds in `selection` parameters to restrict `A` to any type on the interval between `Zebra` and `Animal`.
