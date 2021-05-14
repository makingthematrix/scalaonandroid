import com.github.plokhotnyuk.jsoniter_scala.macros._
import com.github.plokhotnyuk.jsoniter_scala.core._

import scala.util.{Random, Try}

package object hellofxml2 {
  implicit val codec: JsonValueCodec[Foo] = JsonCodecMaker.make

  final case class Foo(n: Int, bar: String) {
    def toJson: String = writeToString(this)
  }

  def fromJson(jsonStr: String): Option[Foo] = Try(readFromString(jsonStr)).toOption

  def randomFoo(): Foo = Foo(Random.nextInt(), (0 to 10).map(_ => Random.nextPrintableChar()).mkString)
}
