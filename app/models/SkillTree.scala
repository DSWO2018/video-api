package models

import play.api.libs.json.{JsPath, Json, Reads, Writes}
import play.api.libs.functional.syntax._

import scala.concurrent.Future
import scala.util.Try

case class SkillTree(id: Option[Int], title: String, description: String, credits: Int)

object SkillTree {
  implicit val skillTreeWrites = new Writes[SkillTree] {
    def writes(skillTree: SkillTree) = Json.obj(
      "id" -> skillTree.id,
      "title" -> skillTree.title,
      "description" -> skillTree.description,
      "credits" -> skillTree.credits
    )
  }
  implicit val skillTreeReads: Reads[SkillTree] = (
    (JsPath \ "id").readNullable[Int] and
      (JsPath \ "title").read[String] and
      (JsPath \ "description").read[String] and
      (JsPath \ "credits").read[Int]
    )(SkillTree.apply _)

}
trait SkillTreeRepository {
  def add(skillTree: SkillTree): Future[Try[SkillTree]]
  def update(skillTree: SkillTree): Future[Int]
  def get(id: Int): Future[Option[SkillTree]]
  def list(): Future[Seq[SkillTree]]
}