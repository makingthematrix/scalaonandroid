
## Nested functions

It's good functional programming style to split up a task into several small functions.

But the names of functions like `sqrtIter`, `improve`, and `isGoodEnough` (defined in the
previous section) matter only for the *implementation* of `sqrt`, not for its *usage*.

Normally we would not like users to access these functions directly.

We can achieve this and at the same time avoid "name-space pollution" by
putting the auxiliary functions inside `sqrt`.

###  The `sqrt` Function, Take 2 

    def sqrt(x: Double) = {
        def sqrtIter(guess: Double, x: Double): Double =
        if (isGoodEnough(guess, x)) guess
        else sqrtIter(improve(guess, x), x)

        def improve(guess: Double, x: Double) =
        (guess + x / guess) / 2

        def isGoodEnough(guess: Double, x: Double) =
          abs(square(guess) - x) < 0.001
    
        sqrtIter(1.0, x)
    }

## Blocks in Scala

 A block is delimited by braces `{ ... }`.

      {
        val x = f(3)
        x * x
      }
 
 It contains a sequence of definitions or expressions.\
 The last element of a block is an expression that defines its value.\
 This return expression can be preceded by auxiliary definitions.\
 Blocks are expressions themselves; a block may appear anywhere an expression can.

### Blocks and Visibility 

 The definitions inside a block are only visible from within the block.\
 The definitions inside a block *shadow* the definitions of the same names
   outside the block.

## Exercise: Scope Rules 

Complete the code in the code editor.
What is the value of `result` in the program?

