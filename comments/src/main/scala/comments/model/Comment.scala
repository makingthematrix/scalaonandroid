package comments.model

import java.util.UUID

final case class Comment(author:  String = "", content: String = "", unique: UUID = UUID.randomUUID())
