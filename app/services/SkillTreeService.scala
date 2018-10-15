package services

import models.{SkillTree, SkillTreeRepository}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class SkillTreeService(skillTreeRepository: SkillTreeRepository)(implicit ec: ExecutionContext) {

  def add(skillTree: SkillTree): Future[Try[SkillTree]] = {
    skillTreeRepository.add(skillTree)
  }

}
