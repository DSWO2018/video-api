package repository

import javax.inject.{Inject, Singleton}
import models.{UniRepository, University, User, UserRepository}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Singleton
class SlickUniRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                   (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with UniRepository {

  import profile.api._

  private val Universities = TableQuery[UniTable]

  def add(uni: University): Future[Try[University]] = {
    db.run(((Universities returning Universities.map(_.id)
      into ((uni,id) => uni.copy(id=Some(id)))
      ) += University(None, uni.name, uni.logo)).asTry)
  }


  def update(uni: University): Future[Int] = {
    db.run(Universities.filter(_.id === uni.id).update(uni))
  }

  def get(id: Int): Future[Option[University]] = {
    db.run(Universities.filter(_.id === id).result.headOption)
  }

  def list(): Future[Seq[University]] = db.run {
    Universities.result
  }

  class UniTable(tag: Tag) extends Table[University](tag, "universities") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("name")
    def logo = column[String]("logo")

    def * = (id.?, name, logo) <> ((University.apply _).tupled, University.unapply)
  }
}