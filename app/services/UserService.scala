package services

import models.{User, UserRepository}
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

class UserService(userRepository: UserRepository)(implicit ec: ExecutionContext) {

  def add(email: String, password: String): Future[Try[User]] = {
    val hashedPassword = getHash(password)
    val anyUser = new User(Some(0), email, hashedPassword)
    userRepository.add(anyUser)
  }

  def isAuthenticated(email: String, password: String): Future[Boolean] = {
    val result = userRepository.get(email)
    result.map { usr =>
      checkHash(password, usr.get.password)
    }
  }

  def getHash(str: String) : String = {
    BCrypt.hashpw(str, BCrypt.gensalt())
  }
  def checkHash(str: String, strHashed: String): Boolean = {
    BCrypt.checkpw(str, strHashed)
  }

}