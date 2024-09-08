object ClassesVsCaseClasses{
  val aliceAccount = /*create a new BankAccount here*/
  val bobAccount   = /*create a new BankAccount here*/

  val c3     = /*create a new c3 note here with a quarter duration*/
  val cThree = /*create a new c3 note here with a quarter duration*/

  def main(args: Array[String]): Unit = {
    println(aliceAccount == bobAccount)
    print(c3 == cThree)
  }
}