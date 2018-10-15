import models.{UniRepository, University}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.UniService

import scala.concurrent.Future

class UniServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  val uniRepository = mock[UniRepository]
  val iteso = new University(Option(1), "ITESO", "Logo_ITESO")
  val ibero = new University(Option(1), "IBERO", "Logo_IBERO")
  val uniList:List[University] = List(ibero, iteso)

  test("return a user list") {
    when(uniRepository.list())
      .thenReturn(Future(uniList))

    val uniService = new UniService(uniRepository)

    uniService.list().map { list =>
      list shouldBe uniList
    }
  }
}