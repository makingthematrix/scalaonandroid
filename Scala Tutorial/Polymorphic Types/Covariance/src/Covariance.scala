object Covariance {
  class Animal/*complete  the class definition with covariance and animal parameter*/

  class Reptile extends Animal

  class Mammal extends Animal

  class Zebra extends Mammal

  class AnimalVet(val animal:Animal[Mammal])

  def main(args: Array[String]): Unit = {
    val reptile = new Reptile
    val zebra = new Zebra

    val reptileAnimal = new Animal[Reptile](reptile)
    val zebraAnimal = new Animal[Zebra](zebra)

    //Uncomment the line below to see if the reptileAnimal passes type-checking
    //val reptileVet = new AnimalVet(reptileAnimal)
    val zebraVet = new AnimalVet(zebraAnimal)

    println(zebraVet)
  }
}

