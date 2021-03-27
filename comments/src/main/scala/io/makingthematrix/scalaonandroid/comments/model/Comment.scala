package io.makingthematrix.scalaonandroid.comments.model

import java.util.UUID

case class Comment(
  author:  String,
  content: String,
  unique: UUID = UUID.randomUUID()
) {
  def this() = this("", "")
}
