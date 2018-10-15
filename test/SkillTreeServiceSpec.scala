import models.{SkillTree, SkillTreeRepository}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.SkillTreeService

import scala.concurrent.Future
import scala.util.Success

class SkillTreeServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  val skRepository = mock[SkillTreeRepository]
  val sk = new SkillTree(Option(1), "Back end", "Awesome backend", 300)

  test("return a skill tree when we push a new skill tree") {
    when(skRepository.add(any()))
      .thenReturn(Future(Success(sk)))

    val skService = new SkillTreeService(skRepository)

    skService.add(new SkillTree(Option(1), "Back end", "Awesome backend", 300)).map { s =>
      s.get.id shouldBe Some(1)
      s.get.title shouldBe "Back end"
      s.get.description shouldBe "Awesome backend"
      s.get.credits shouldBe 300
    }
  }
}