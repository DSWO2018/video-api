package services

import models.{User, UserRepository}

import scala.concurrent.Future

class UserService(userRepository: UserRepository) {
  def addUser(anyUser: User): Future[User] = {
    userRepository.addUser(anyUser)
  }
}