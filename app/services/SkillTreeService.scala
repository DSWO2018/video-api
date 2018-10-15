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
      if (st.isDefined) {
        skillTreeRepository.update(skillTree)
      } else {
        Future(0)
      }
    }
  }

}
