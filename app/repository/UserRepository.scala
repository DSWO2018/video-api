package repository

import javax.inject.{Inject, Singleton}
import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  private val Users = TableQuery[UsersTable]

  def create(anyUser: User): Future[User] = db.run {
    (Users returning Users.map(_.id)
      into ((user,id) => user.copy(id=Some(id)))
      ) += User(None, anyUser.email, anyUser.password)
  }

  private class UsersTable(tag: Tag) extends Table[User](tag, "users") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email", O.Unique)
    def password = column[String]("password")

    def * = (id.?, email, password) <> ((User.apply _).tupled, User.unapply)
  }

}