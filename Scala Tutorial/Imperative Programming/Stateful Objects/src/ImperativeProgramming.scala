object ImperativeProgramming{

  def main(args: Array[String]): Unit = {
    val x = new BankAccount
    val y = new BankAccount
    println(x deposit 30)
    println(x withdraw 20)
  }

}
