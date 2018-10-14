package services

import models.{User, UserRepository}
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

class UserService(userRepository: UserRepository)(implicit ec: ExecutionContext) {

  def add(email: String, password: String): Future[Try[User]] = {
    val hashedPassword = getHash(password)
    val anyUser = new User(Some(0), email, hashedPassword, null, null, null)
    userRepository.add(anyUser)
  }

  def isAuthenticated(email: String, password: String): Future[Boolean] = {
    val result = userRepository.get(email)
    result.map { usr =>
      checkHash(password, usr.get.password)
    }
  }

  def updateDescription(id: Integer, description: String): Future[Int] = {
    val oldUser = userRepository.get(id)
    oldUser.flatMap { usr =>
      if (!usr.isEmpty) {
        userRepository.update(new User(usr.get.id, usr.get.email, usr.get.password,
          usr.get.first_name, usr.get.last_name, description))

      } else {
        Future(0)
      }
    }
  }

  def getHash(str: String) : String = {
    BCrypt.hashpw(str, BCrypt.gensalt())
  }
  def checkHash(str: String, strHashed: String): Boolean = {
    BCrypt.checkpw(str, strHashed)
  }

}