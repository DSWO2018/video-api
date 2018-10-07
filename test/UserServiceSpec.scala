import models.{User, UserRepository}
import org.mockito.Mockito.when
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.UserService

import scala.concurrent.Future

class UserServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  test("return a user when we push a new user") {
      val userRepository = mock[UserRepository]
      val user = new User(Option(1), "is700845@iteso.mx", "UltraSecretPassword")
      when(userRepository.addUser(user))
        .thenReturn(Future(user))

      val userService = new UserService(userRepository)

      userService.addUser(user).map { s =>
        s.id shouldBe Some(1)
        s.email shouldBe "is700845@iteso.mx"
        s.password shouldBe "UltraSecretPassword"
      }
  }
}