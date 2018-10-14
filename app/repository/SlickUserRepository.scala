package repository

import javax.inject.{Inject, Singleton}
import models.{User, UserRepository}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Singleton
class SlickUserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                   (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with UserRepository {

  import profile.api._

  private val Users = TableQuery[UsersTable]

  def add(anyUser: User): Future[Try[User]] = {
    db.run(((Users returning Users.map(_.id)
      into ((user,id) => user.copy(id=Some(id)))
      ) += User(None, anyUser.email, anyUser.password, anyUser.first_name, anyUser.last_name, anyUser.description)).asTry)
  }


  def update(anyUser: User): Future[Int] = {
    db.run(Users.filter(_.id === anyUser.id).update(anyUser))
  }

  def get(id: Int): Future[Option[User]] = {
    db.run(Users.filter(_.id === id).result.headOption)
  }

  def get(mail: String): Future[Option[User]] = {
    db.run(Users.filter(_.email === mail).result.headOption)
  }

  def list(): Future[Seq[User]] = db.run {
    Users.result
  }

  class UsersTable(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email", O.Unique)
    def password = column[String]("password")
    def first_name = column[String]("first_name")
    def last_name = column[String]("last_name")
    def description = column[String]("description")

    def * = (id.?, email, password,
      first_name, last_name, description) <> ((User.apply _).tupled, User.unapply)
  }
}