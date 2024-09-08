
## Method Calls

Another way to make complex expressions out of simpler expressions is to call
*methods* on expressions:

  - What is the size of the text “Hello, Scala!”?

        "Hello, Scala!".size


Methods are *applied* on expressions using the *dot notation*.

The object on which the method is applied is named the *target object*.

  - What is the range of numbers between 1 and 10?
    
        1.to(10)


Methods can have *parameters*. They are supplied between parentheses.

The `abs` method returns the absolute value of a
number, and the `toUpperCase` method returns the target `String` in
upper case.

#### Exercise

Complete the invocations for the described methods.

<div class="hint">The <code>upperCaseMethod()</code> should return "HELLO, SCALA!"</div>
<div class="hint">The <code>absMethod()</code> should return 42.</div>