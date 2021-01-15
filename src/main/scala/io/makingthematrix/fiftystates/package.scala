package io.makingthematrix

package object fiftystates {

  @inline def returning[T](t: T)(body: T => Any): T = {
    body(t)
    t
  }

}
