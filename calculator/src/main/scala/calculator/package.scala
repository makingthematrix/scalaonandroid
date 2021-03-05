package object calculator {
  @inline def returning[T](obj: T)(f: T => Unit): T = {
    f(obj)
    obj
  }
}
