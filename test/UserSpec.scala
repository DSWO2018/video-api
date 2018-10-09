import models.User
import org.scalatest.mockito.MockitoSugar
import org.scalatestplus.play._
import org.scalatest.concurrent.ScalaFutures
import play.api.libs.json.Json

class UserSpec extends PlaySpec with MockitoSugar with ScalaFutures {
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
}