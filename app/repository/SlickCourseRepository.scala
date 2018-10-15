package repository

import javax.inject.{Inject, Singleton}
import models.{Course, CourseRepository}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SlickCourseRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                     (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with CourseRepository {

  import profile.api._

  private val courses = TableQuery[CoursesTable]
  def getCourses(): Future[Seq[Course]] = {
    db.run {
      courses.result
    }
  }
  class CoursesTable(tag: Tag) extends Table[Course](tag, "courses"){
    def id = column[Int]("id", O.AutoInc, O.PrimaryKey)
    def title = column[String]("title")
    def description = column[String]("description")
    def * = (id.?, title, description) <> ((Course.apply _).tupled, Course.unapply)
  }
}