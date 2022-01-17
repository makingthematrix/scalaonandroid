package calculator.eval

import munit.Location
import calculator.eval.expressions.*

class DictionaryTest extends munit.FunSuite:
  implicit val location: Location = Location.empty

  test("Add and get an expr") {
    val dict = Dictionary()
    dict.add("a", Constant(1.0))
    assertEquals(dict.get("a"), Some(Constant(1.0)))
  }

  test("Unable to reassign an expr") {
    val dict = Dictionary()
    assertEquals(dict.add("a", Constant(1.0)), true)
    assertEquals(dict.add("a", Constant(2.0)), false)
    assertEquals(dict.get("a"), Some(Constant(1.0)))
  }

  test("List names") {
    val dict = Dictionary()
    dict.add("a", Constant(1.0))
    dict.add("b", Constant(2.0))
    assertEquals(dict.expressions.keySet, Set("a", "b"))
  }

  test("Handle an attempt to get an unassigned expression") {
    val dict = Dictionary()
    dict.add("a", Constant(1.0))
    assertEquals(dict.get("b"), None)
  }

  test("Copy with updates") {
    val dict = Dictionary()
    dict.add("a", Constant(1.0))
    val newDict = dict.copy(Map("b" -> Constant(2.0)))
    assertEquals(newDict.get("b"), Some(Constant(2.0)))
  }

  test("Valid and invalid names") {
    assert(Dictionary.isValidName("a"))
    assert(Dictionary.isValidName("_a"))
    assert(Dictionary.isValidName("a_"))
    assert(Dictionary.isValidName("a1"))
    assert(Dictionary.isValidName("a_b"))
    assert(Dictionary.isValidName("$1", canBeSpecial = true))
    assert(!Dictionary.isValidName("$1"))
    assert(!Dictionary.isValidName(":"))
    assert(!Dictionary.isValidName("()"))
    assert(!Dictionary.isValidName(""))
    assert(!Dictionary.isValidName("1a"))
    assert(!Dictionary.isValidName("_"))
    assert(!Dictionary.isValidName("__"))
  }

  test("Create special assignments") {
    val dict = Dictionary()
    dict.addSpecial(Constant(1.0))
    dict.addSpecial(Constant(2.0))
    dict.addSpecial(Constant(3.0))
    assertEquals(dict.specials.size, 3)
    assertEquals(dict.specials.values.toSet, Set(Constant(1.0), Constant(2.0), Constant(3.0)))
  }
