import models.{User, UserRepository}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.UserService

import scala.concurrent.Future
import scala.util.Success

class UserServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  test("return a user when we push a new user") {
      val userRepository = mock[UserRepository]
      val user = new User(Option(1), "is700845@iteso.mx", "$2a$10$u4CWJ75wJcQfrr7QPOhXSOue9/ALtPH0Cyvld7slfNVjK71A.XqtO")
      when(userRepository.add(any()))
        .thenReturn(Future(Success(user)))

      val userService = new UserService(userRepository)

      userService.add("is700845@iteso.mx", "UltraSecretPassword").map { s =>
        s.get.id shouldBe Some(1)
        s.get.email shouldBe "is700845@iteso.mx"
        s.get.password shouldBe "$2a$10$u4CWJ75wJcQfrr7QPOhXSOue9/ALtPH0Cyvld7slfNVjK71A.XqtO"
      }
  }
}