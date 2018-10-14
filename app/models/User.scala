package models

import play.api.libs.json.{JsPath, Json, Reads, Writes}
import play.api.libs.json.Reads.{email, minLength}
import play.api.libs.functional.syntax._

import scala.concurrent.Future
import scala.util.Try

case class User(id: Option[Int], email: String, password: String, first_name: String, last_name: String, description: String)

object User {
  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "email" -> user.email,
      "first_name" -> user.first_name,
      "last_name" -> user.last_name,
      "description" -> user.description
    )
  }
  implicit val userReads: Reads[User] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "email").read[String](email) and
      (JsPath \ "password").read[String](minLength[String](8)) and
      (JsPath \ "first_name").read[String] and
      (JsPath \ "last_name").read[String] and
      (JsPath \ "description").read[String]
    )(User.apply _)
}

trait UserRepository {
  def add(anyUser: User): Future[Try[User]]
  def update(anyUser: User): Future[Int]
  def get(id: Int): Future[Option[User]]
  def get(mail: String): Future[Option[User]]
  def list(): Future[Seq[User]]
}