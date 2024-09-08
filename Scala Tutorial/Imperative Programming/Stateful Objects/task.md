
Until now, our programs have been side-effect free.

Therefore, the concept of *time* wasn't important.

For all programs that terminate, any sequence of actions would have given the same results.

This was also reflected in the substitution model of computation.

### Reminder: Substitution Model

Programs can be evaluated by *rewriting*:
  - a name is evaluated by replacing it with the right-hand side of its definition,
  - function application is evaluated by replacing it with the function’s right-hand
    side, and, at the same time, by replacing the formal parameters by the actual
    arguments.

Say, you have the following two functions `iterate` and `square`:

      def iterate(n: Int, f: Int => Int, x: Int) =
        if (n == 0) x else iterate(n-1, f, f(x))
      def square(x: Int) = x * x

Then the call `iterate(1, square, 3)` gets rewritten as follows:

      iterate(1, square, 3)
      if (1 == 0) 3 else iterate(1-1, square, square(3))
      iterate(0, square, square(3))
      iterate(0, square, 3 * 3)
      iterate(0, square, 9)
      if (0 == 0) 9 else iterate(0-1, square, square(9))
      9

Rewriting can be done anywhere in a term, and all rewritings which
terminate lead to the same solution.

This is an important result of the λ-calculus, i.e., the theory
behind functional programming.

For instance, these two rewritings will eventually lead to the same result:

      if (1 == 0) 3 else iterate(1 - 1, square, square(3))
      iterate(0, square, square(3))
    
      // OR
      if (1 == 0) 3 else iterate(1 - 1, square, square(3))
      if (1 == 0) 3 else iterate(1 - 1, square, 3 * 3)

### Stateful Objects

One normally describes the world as a set of objects, some of which
have a state that *changes* over the course of time.

An object *has a state* if its behavior is influenced by its
history.

Example: a bank account has a state, because the answer to the question
“can I withdraw 100 CHF ?” may vary over the course of the lifetime of
the account.

## Implementation of State 

Every form of mutable state is constructed from variables.

A variable definition is written like a value definition but with the
keyword `var` in place of `val`:

      var x: String = "abc"
      var count = 111

Just like a value definition, a variable definition associates a value
with a name.

However, in the case of variable definitions, this association can be
changed later through an *assignment*:

      x = "hi"
      count = count + 1

## State in Objects 

In practice, objects with state are usually represented by objects that
have some variable members.

Here is a class modeling a bank account:

      class BankAccount {
        private var balance = 0
        def deposit(amount: Int): Int = {
          if (amount > 0) balance = balance + amount
          balance
        }
        def withdraw(amount: Int): Int =
          if (0 < amount && amount <= balance) {
            balance = balance - amount
            balance
          } else throw new Error("insufficient funds")
      }

The class `BankAccount` defines a variable `balance` that contains the
current balance of the account.

The methods `deposit` and `withdraw` change the value of the `balance`
through assignments.

Note that `balance` is `private` in the `BankAccount`
class, it therefore cannot be accessed from outside the class.

To create bank accounts, we use the common notation for object creation:

      val account = new BankAccount

## Working with Mutable Objects

Here is a program that manipulates bank accounts:

      val account = new BankAccount       // account: BankAccount = BankAccount
      account deposit 50                  //
      account withdraw 20                 // res1: Int = 30
      account withdraw 20                 // res2: Int = 10
      account withdraw 15                 // java.lang.Error: insufficient funds

Applying the same operation to an account twice in a row produces different results.
Clearly, accounts are stateful objects.

## Identity and Change 

Assignment poses the new problem of deciding whether two expressions
are "the same".

When one excludes assignments and writes:

      val x = E; val y = E

where `E` is an arbitrary expression, then it is reasonable to assume that
`x` and `y` are the same. That is to say that we could have also written:

      val x = E; val y = x

(This property is usually called *referential transparency*.)

But once we allow assignment, the two formulations become different. For example:

      val x = new BankAccount
      val y = new BankAccount

Are `x` and `y` the same?

## Operational Equivalence 

To respond to the last question, we must specify what is meant by “the same”.

The precise meaning of “being the same” is defined by the property of
*operational equivalence*.

In a somewhat informal way, this property is stated as follows:

 - Suppose we have two definitions `x` and `y`.
 - `x` and `y` are operationally equivalent if *no possible test* can
   distinguish between them.

### Testing for Operational Equivalence 

To test if `x` and `y` are the same, we must:

 - Execute the definitions followed by an arbitrary sequence `S` of operations that
   involves `x` and `y`, observing the possible outcomes.

          val x = new BankAccount
          val y = new BankAccount
          f(x, y)

 - Then, execute the definitions with another sequence `S'` obtained by
   renaming all occurrences of `y` by `x` in `S`:

          val x = new BankAccount
          val y = new BankAccount
          f(x, x)

 - If the results are different, then the expressions `x` and `y` are certainly different.
 - On the other hand, if all possible pairs of sequences `(S, S')` produce the same result,
   then `x` and `y` are the same.

Based on this definition, let's see if these expressions are different:

      val x = new BankAccount
      val y = new BankAccount

Let's follow the definitions by a test sequence:

      val x = new BankAccount
      val y = new BankAccount
      x deposit 30
      y withdraw 20                // java.lang.Error: insufficient funds

If we rename all occurrences of `y` with `x` in this sequence, the final results 
will be different. We conclude that `x` and `y` are not the same.

## Establishing Operational Equivalence 

On the other hand, if we define

      val x = new BankAccount
      val y = x

then no sequence of operations can distinguish between `x` and `y`, so
`x` and `y` are the same in this case.

## Assignment and Substitution Model

The preceding examples show that our model of computation by
substitution cannot be used.

Indeed, according to this model, one can always replace the name of a
value by the expression that defines it. For example, in

      val x = new BankAccount
      val y = x

the `x` in the definition of `y` could be replaced by `new BankAccount`.

But we have seen that this change leads to a different program!

The substitution model ceases to be valid when we add assignment.

It is possible to adapt the substitution model by introducing a *store*,
but this becomes considerably more complicated.


## Exercise
Complete the `deposit` definition to return the sum of `balance` and `amount` in case the `amount` is positive.
Complete the `withdraw` definition to return the difference between `balance` and `amount` in case the `amount` is negative.