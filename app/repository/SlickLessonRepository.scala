package repository

import javax.inject.{Inject, Singleton}
import models.{Course, Lesson, LessonRepository}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SlickLessonRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                     (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with LessonRepository {

  import profile.api._

  private val lessons = TableQuery[LessonsTable]
  def getLessons(courseId: Int): Future[Seq[Lesson]] = {
    db.run {
      lessons.filter(_.course_id === courseId).result
    }
  }
  def getLesson(lessonId: Int): Future[Seq[Lesson]] = {
    db.run {
      lessons.filter(_.id === lessonId).result
    }
  }
  class LessonsTable(tag: Tag) extends Table[Lesson](tag, "lessons"){
    def id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    def title = column[String]("title")
    def description = column[String]("description")
    def course_id = column[Int]("course_id")
    def * = (id.?, title, description, course_id) <> ((Lesson.apply _).tupled, Lesson.unapply)
  }
}