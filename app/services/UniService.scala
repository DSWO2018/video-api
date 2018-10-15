package services

import models.{UniRepository, University}

import scala.concurrent.{ExecutionContext, Future}

class UniService(uniRepository: UniRepository)(implicit ec: ExecutionContext) {

  def list(): Future[Seq[University]] = {
    uniRepository.list()
  }

}