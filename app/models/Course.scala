package models

import play.api.libs.json.{Json, Writes}

import scala.concurrent.Future

case class Course(id: Option[Int], title: String, description: String)

object Course {
  implicit val courseWrites = new Writes[Course] {
    def writes(course: Course) = Json.obj(
      "id" -> course.id,
      "title" -> course.title,
      "description" -> course.description
    )
  }
}
trait CourseRepository {
  def getCourses(): Future[Seq[Course]]
  def getCourse(courseid: Int): Future[Seq[Course]]
}