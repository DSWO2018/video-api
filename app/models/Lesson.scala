package models

import play.api.libs.json.{Json, Writes}

import scala.concurrent.Future

case class Lesson(id: Option[Int], title: String, description: String, course_id: Int)

object Lesson {
  implicit val courseWrites = new Writes[Lesson] {
    def writes(lesson: Lesson) = Json.obj(
      "id" -> lesson.id,
      "title" -> lesson.title,
      "description" -> lesson.description,
      "course_id" -> lesson.course_id
    )
  }
}
trait LessonRepository {
  def getLessons(courseId: Int): Future[Seq[Lesson]]
}