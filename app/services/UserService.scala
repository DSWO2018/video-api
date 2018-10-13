package services

import java.sql.{SQLException, SQLIntegrityConstraintViolationException}

import models.{User, UserRepository}
import org.mindrot.jbcrypt.BCrypt

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.util.Try

class UserService(userRepository: UserRepository) {

  def add(email: String, password: String): Future[Try[User]] = {
    val hashedPassword = getHash(password)
    val anyUser = new User(Some(0), email, hashedPassword)
    userRepository.add(anyUser)
  }

  def isAuthenticated(anyUser: User): Boolean = {
    val result = userRepository.get(anyUser.email)
    val user = Await.result(result, Duration.Inf)

    user.fold(return false)(return checkHash(anyUser.password, user.get.password))
  }

  def getHash(str: String) : String = {
    BCrypt.hashpw(str, BCrypt.gensalt())
  }
  def checkHash(str: String, strHashed: String): Boolean = {
    BCrypt.checkpw(str, strHashed)
  }

}