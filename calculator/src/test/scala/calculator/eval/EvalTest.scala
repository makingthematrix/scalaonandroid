package calculator.eval

class EvalTest extends munit.FunSuite {
  test("Number") {
    assertEqualsDouble(Eval("4").evaluate, 4.0, 0.001)
    assertEqualsDouble(Eval("-3").evaluate, -3.0, 0.001)
    assertEqualsDouble(Eval("4.12").evaluate, 4.12, 0.001)
    assertEqualsDouble(Eval("0").evaluate, 0.0, 0.00001)
  }

  test("Special case - minus zero") {
    assertEqualsDouble(Eval("-0").evaluate, 0.0, 0.00001)
  }

  test("Add") {
    assertEqualsDouble(Eval("1+1").evaluate, 2.0, 0.001)
    assertEqualsDouble(Eval("1+2+3").evaluate, 6.0, 0.001)
  }

  test("Substract") {
    assertEqualsDouble(Eval("4-2").evaluate, 2.0, 0.001)
    assertEqualsDouble(Eval("-1+1").evaluate, 0.0, 0.001)
    assertEqualsDouble(Eval("1+-1").evaluate, 0.0, 0.001)
    assertEqualsDouble(Eval("0-1").evaluate, -1.0, 0.001)
    assertEqualsDouble(Eval("4-2-3").evaluate, -1.0, 0.001)
    assertEqualsDouble(Eval("-4-2").evaluate, -6.0, 0.001)
  }

  test("Multiply") {
    assertEqualsDouble(Eval("4*2").evaluate, 8.0, 0.001)
    assertEqualsDouble(Eval("0*2").evaluate, 0.0, 0.001)
    assertEqualsDouble(Eval("4*0").evaluate, 0.0, 0.001)
    assertEqualsDouble(Eval("4*2*3").evaluate, 24.0, 0.001)
    assertEqualsDouble(Eval("4*0.5").evaluate, 2.0, 0.001)
    assertEqualsDouble(Eval("4.2*0.5").evaluate, 2.1, 0.001)
  }

  test("Multiply and Add") {
    assertEqualsDouble(Eval("4*2+1").evaluate, 9.0, 0.001)
    assertEqualsDouble(Eval("4+2*1").evaluate, 6.0, 0.001)
    assertEqualsDouble(Eval("1+4*2+1").evaluate, 10.0, 0.001)
    assertEqualsDouble(Eval("0*2+1").evaluate, 1.0, 0.001)
    assertEqualsDouble(Eval("1+4*0").evaluate, 1.0, 0.001)
    assertEqualsDouble(Eval("4*2+1*3").evaluate, 11.0, 0.001)
    assertEqualsDouble(Eval("4*0.5+1").evaluate, 3.0, 0.001)
    assertEqualsDouble(Eval("1.11+4.2*0.5").evaluate, 3.21, 0.001)
  }

  test("Multiply and Substract") {
    assertEqualsDouble(Eval("4*2-1").evaluate, 7.0, 0.001)
    assertEqualsDouble(Eval("4-2*1").evaluate, 2.0, 0.001)
    assertEqualsDouble(Eval("1-4*2-1").evaluate, -8.0, 0.001)
    assertEqualsDouble(Eval("0*2-1").evaluate, -1.0, 0.001)
    assertEqualsDouble(Eval("1-4*0").evaluate, 1.0, 0.001)
    assertEqualsDouble(Eval("4*2-1*3").evaluate, 5.0, 0.001)
    assertEqualsDouble(Eval("4*0.5-1").evaluate, 1.0, 0.001)
    assertEqualsDouble(Eval("1.11-4.2*0.5").evaluate, -0.99, 0.001)
  }

  test("Multiply negative numbers") {
    assertEqualsDouble(Eval("-4*-2").evaluate, 8.0, 0.001)
    assertEqualsDouble(Eval("2*-2").evaluate, -4.0, 0.001)
    assertEqualsDouble(Eval("-4*2").evaluate, -8.0, 0.001)
  }

  test("Divide") {
    assertEqualsDouble(Eval("4/2").evaluate, 2.0, 0.001)
    assertEqualsDouble(Eval("0/2").evaluate, 0.0, 0.001)
    assertEqualsDouble(Eval("4/0").evaluate, Double.NaN, 0.001)
    assertEqualsDouble(Eval("4/2/8").evaluate, 0.25, 0.001)
    assertEqualsDouble(Eval("4.2/0.5").evaluate, 8.4, 0.001)
    assertEqualsDouble(Eval("0.0/0.0").evaluate, Double.NaN, 0.001)
  }

  test("Divide and Multiply") {
    assertEqualsDouble(Eval("4/2*8").evaluate, 16.0, 0.001)
    assertEqualsDouble(Eval("4*2/8").evaluate, 1.0, 0.001)
    assertEqualsDouble(Eval("-4/2*8").evaluate, -16.0, 0.001)
    assertEqualsDouble(Eval("4*-2/8").evaluate, -1.0, 0.001)
    assertEqualsDouble(Eval("4*2/-8").evaluate, -1.0, 0.001)
  }

  test("Divide and Add") {
    assertEqualsDouble(Eval("4/2+1").evaluate, 3.0, 0.001)
    assertEqualsDouble(Eval("4+2/1").evaluate, 6.0, 0.001)
    assertEqualsDouble(Eval("1+4/2+1").evaluate, 4.0, 0.001)
    assertEqualsDouble(Eval("0/2+1").evaluate, 1.0, 0.001)
    assertEqualsDouble(Eval("1+4/0").evaluate, Double.NaN, 0.001)
    assertEqualsDouble(Eval("4/2+1/3").evaluate, 2.33333, 0.001)
    assertEqualsDouble(Eval("4/0.5+1").evaluate, 9.0, 0.001)
    assertEqualsDouble(Eval("1.11+4.2/0.5").evaluate, 9.51, 0.001)
  }

  test("Divide and Substract") {
    assertEqualsDouble(Eval("4/2-1").evaluate, 1.0, 0.001)
    assertEqualsDouble(Eval("4-2/1").evaluate, 2.0, 0.001)
    assertEqualsDouble(Eval("1-4/2-1").evaluate, -2.0, 0.001)
    assertEqualsDouble(Eval("0/2-1").evaluate, -1.0, 0.001)
    assertEqualsDouble(Eval("1-4/0").evaluate, Double.NaN, 0.001)
    assertEqualsDouble(Eval("4/2-1/3").evaluate, 1.666667, 0.001)
    assertEqualsDouble(Eval("4/0.5-1").evaluate, 7.0, 0.001)
    assertEqualsDouble(Eval("1.11-4.2/0.5").evaluate, -7.29, 0.001)
  }

  test("Divide negative numbers") {
    assertEqualsDouble(Eval("-4/-2").evaluate, 2.0, 0.001)
    assertEqualsDouble(Eval("2/-2").evaluate, -1.0, 0.001)
    assertEqualsDouble(Eval("-4/2").evaluate, -2.0, 0.001)
  }

  test("Bubble minus up") {
    assertEqualsDouble(Eval("-3*3*4*-2").evaluate, 72.0, 0.001)
    assertEqualsDouble(Eval("3+4-3*3*4*-2").evaluate, 79.0, 0.001)
  }

  test("parentheses") {
    assertEqualsDouble(Eval("(1+4)*(2+1)").evaluate, 15.0, 0.001)
    assertEqualsDouble(Eval("((1+2)*3)").evaluate, 9.0, 0.001)
  }
}
