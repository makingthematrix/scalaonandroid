package fxcalculator.logic

import fxcalculator.logic.expressions.{Assignment, Error}

final case class EvaluationResults(result: Option[Double] = None,
                                   error: Option[Error] = None,
                                   newAssignments: List[Assignment] = Nil)
