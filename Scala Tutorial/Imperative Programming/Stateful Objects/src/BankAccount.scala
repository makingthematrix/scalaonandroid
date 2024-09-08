class BankAccount {
  private var balance = 0
  def deposit(amount: Int): Int = {
    if (amount > 0) balance = /*add the expression for the updated balance*/
    balance
  }
  def withdraw(amount: Int): Int =
    if (0 < amount && amount <= balance) {
      balance = /*add the expression for the updated balance*/
      balance
    } else throw new Error("insufficient funds")
}