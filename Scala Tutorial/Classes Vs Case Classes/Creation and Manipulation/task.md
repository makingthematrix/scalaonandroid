
In the previous sections, we have seen how case classes could be
used to achieve information aggregation and also how classes
could be used to achieve data abstraction or to define stateful
objects.

What is the relationship between classes and case classes? How
do they differ?

## Creation and Manipulation

Remember the class definition of `BankAccount`:

      class BankAccount {
    
        private var balance = 0
    
        def deposit(amount: Int): Unit = {
          if (amount > 0) balance = balance + amount
        }
    
        def withdraw(amount: Int): Int =
          if (0 < amount && amount <= balance) {
            balance = balance - amount
            balance
          } else throw new Error("insufficient funds")
      }

And the case class definition of `Note`:

      case class Note(name: String, duration: String, octave: Int)

Let’s create some instances of `BankAccount` and `Note` and manipulate them.

## Exercise

- Deposit `100` to the account.
- Create a `C3` `Note` instance.