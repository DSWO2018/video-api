import models.{User, UserRepository}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.UserService

import scala.concurrent.Future
import scala.util.Success

class UserServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  val userRepository = mock[UserRepository]
  val user = new User(Option(1), "is700845@iteso.mx", "$2a$10$u4CWJ75wJcQfrr7QPOhXSOue9/ALtPH0Cyvld7slfNVjK71A.XqtO",
  null, null, null)

  test("return a user when we push a new user") {
      when(userRepository.add(any()))
        .thenReturn(Future(Success(user)))

      val userService = new UserService(userRepository)

      userService.add("is700845@iteso.mx", "UltraSecretPassword").map { s =>
        s.get.id shouldBe Some(1)
        s.get.email shouldBe "is700845@iteso.mx"
        s.get.password shouldBe "$2a$10$u4CWJ75wJcQfrr7QPOhXSOue9/ALtPH0Cyvld7slfNVjK71A.XqtO"
      }
  }

  test("validate login") {
    when(userRepository.get("is700845@iteso.mx"))
      .thenReturn(Future(Some(user)))

    val userService = new UserService(userRepository)

    userService.isAuthenticated("is700845@iteso.mx", "badPassword").map(f => f shouldBe false)
    userService.isAuthenticated("is700845@iteso.mx", "UltraSecretPassword").map(f => f shouldBe true)
  }

  test("update description") {
    when(userRepository.get(1))
      .thenReturn(Future(Some(user)))

    when(userRepository.get(0))
      .thenReturn(Future(None))

    when(userRepository.update(any()))
      .thenReturn(Future(1))

    val userService = new UserService(userRepository)

    userService.updateDescription(1, "New description").map {user =>
      user shouldBe 1
    }
    userService.updateDescription(0, "New description").map {user =>
      user shouldBe 0
    }
  }

  test("update password") {
    when(userRepository.get(1))
      .thenReturn(Future(Some(user)))

    when(userRepository.get(0))
      .thenReturn(Future(None))

    when(userRepository.update(any()))
      .thenReturn(Future(1))

    val userService = new UserService(userRepository)

    userService.updatePassword(1, "UltraSecretPassword", "NewSecretPassword").map {user =>
      user shouldBe 1
    }

    userService.updatePassword(1, "askbdnahjsbda", "NewSecretPassword").map {user =>
      user shouldBe 0
    }

    userService.updatePassword(0, "UltraSecretPassword", "NewSecretPassword").map {user =>
      user shouldBe 0
    }
  }
}