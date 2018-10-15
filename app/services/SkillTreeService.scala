package services

import models.{SkillTree, SkillTreeRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class SkillTreeService(skillTreeRepository: SkillTreeRepository)(implicit ec: ExecutionContext) {

  def add(skillTree: SkillTree): Future[Try[SkillTree]] = {
    skillTreeRepository.add(skillTree)
  }

  def update(id: Int, skillTree: SkillTree): Future[Int] = {
    val oldSt = skillTreeRepository.get(id)
    oldSt.flatMap { st =>
      if (!st.isEmpty) {
        skillTreeRepository.update(new SkillTree(Some(id), skillTree.title, skillTree.description, skillTree.credits))
      } else {
        Future(0)
      }
    }
  }

}
