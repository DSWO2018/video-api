package models

import play.api.libs.json.{JsPath, Json, Reads, Writes}
import play.api.libs.json.Reads.{email, minLength}
import play.api.libs.functional.syntax._

import scala.concurrent.Future

case class User(id: Option[Int], email: String, password: String)

object User {
  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "email" -> user.email
    )
  }
  implicit val userReads: Reads[User] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "email").read[String](email) and
      (JsPath \ "password").read[String](minLength[String](8))
    )(User.apply _)
}

trait UserRepository {
  def addUser(anyUser: User): Future[User]
}