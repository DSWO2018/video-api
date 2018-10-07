import models.{User, UserRepository}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

class UserServiceSpec extends PlaySpec with MockitoSugar with ScalaFutures {
  "User" should {
    "create a user object from JSON" in {
      val data = Json.obj("email" ->"is700845@iteso.mx", "password" -> "UltraSecretPassword")
      val user = data.validate[User]
      user.get.mustBe(User(None, "is700845@iteso.mx", "UltraSecretPassword"))
    }
    "reject a JSON with invalid email or short password" in {
      val data = Json.obj("email" ->"is700845itesomx", "password" -> "123456")
      val user = data.validate[User]
      user.isError mustBe true
    }
  }

  "UserService" should {
    "return a user when we push a new user" in { implicit ec: ExecutionContext =>
      val userRepository = mock[UserRepository]
      val user = new User(Option(1), "is700845@iteso.mx", "UltraSecretPassword")
      when(userRepository.addUser(user))
        .thenReturn(Future(user))

      val userService = new UserService(userRepository)

      whenReady(userService.addUser(user)) { s =>
        s.email mustBe "is700845@iteso.mx"
        s.password mustBe "UltraSecretPassword"
      }
    }
  }
}