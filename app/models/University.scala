package models

import play.api.libs.json.{JsPath, Json, Reads, Writes}
import play.api.libs.functional.syntax._

import scala.concurrent.Future
import scala.util.Try

case class University(id: Option[Int], name: String, logo: String)

object University {
  implicit val userWrites = new Writes[University] {
    def writes(university: University) = Json.obj(
      "id" -> university.id,
      "name" -> university.name,
      "logo" -> university.logo
    )
  }
  implicit val userReads: Reads[University] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "name").read[String] and
      (JsPath \ "logo").read[String]
    )(University.apply _)
}

trait UniRepository {
  def add(university: University): Future[Try[University]]
  def update(university: University): Future[Int]
  def get(id: Int): Future[Option[University]]
  def list(): Future[Seq[University]]
}