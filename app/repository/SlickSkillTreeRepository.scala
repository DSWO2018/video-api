package repository

import javax.inject.{Inject, Singleton}
import models._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Singleton
class SlickSkillTreeRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
                                  (implicit executionContext: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] with SkillTreeRepository {

  import profile.api._

  private val SKTable = TableQuery[SkillTreeTable]

  def add(skillTree: SkillTree): Future[Try[SkillTree]] = {
    db.run(((SKTable returning SKTable.map(_.id)
      into ((skillTree,id) => skillTree.copy(id=Some(id)))
      ) += SkillTree(None, skillTree.title, skillTree.description, skillTree.credits)).asTry)
  }


  def update(skillTree: SkillTree): Future[Int] = {
    db.run(SKTable.filter(_.id === skillTree.id).update(skillTree))
  }

  def get(id: Int): Future[Option[SkillTree]] = {
    db.run(SKTable.filter(_.id === id).result.headOption)
  }

  def list(): Future[Seq[SkillTree]] = db.run {
    SKTable.result
  }

  class SkillTreeTable(tag: Tag) extends Table[SkillTree](tag, "skill_tree") {

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
    def title = column[String]("title")
    def description = column[String]("description")
    def credits = column[Int]("credits")

    def * = (id.?, title, description, credits) <> ((SkillTree.apply _).tupled, SkillTree.unapply)
  }
}