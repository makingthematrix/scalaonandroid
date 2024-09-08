object TypeBounds{
  class Animal

  class Reptile extends Animal

  class Mammal extends Animal

  class Zebra extends Mammal

  def selection[A >: /*insert a correct lower bound Type to include Zebra*/ <: /*insert an upper bound type to include Animal*/](a: A): A =
    a

  val unknownAnimal = new Animal
  val reptile = new Reptile
  val mammal = new Mammal
  val zebra = new Zebra

  def main(args: Array[String]): Unit = {
    println(selection(unknownAnimal))
    println(selection(reptile ))
    println(selection(mammal))
    println(selection(zebra))
  }
}