import models.{SkillTree, SkillTreeRepository}
import org.mockito.Matchers.any
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.SkillTreeService

import scala.concurrent.Future
import scala.util.Success

class SkillTreeServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  val stRepository = mock[SkillTreeRepository]
  val st = new SkillTree(Option(1), "Back end", "Awesome backend", 300)

  test("return a skill tree when we push a new skill tree") {
    when(stRepository.add(any()))
      .thenReturn(Future(Success(st)))

    val stService = new SkillTreeService(stRepository)

    stService.add(new SkillTree(Option(1), "Back end", "Awesome backend", 300)).map { s =>
      s.get.id shouldBe Some(1)
      s.get.title shouldBe "Back end"
      s.get.description shouldBe "Awesome backend"
      s.get.credits shouldBe 300
    }
  }

  test("update skill tree") {
    when(stRepository.get(1))
      .thenReturn(Future(Some(st)))

    when(stRepository.get(0))
      .thenReturn(Future(None))

    when(stRepository.update(any()))
      .thenReturn(Future(1))

    val stService = new SkillTreeService(stRepository)

    stService.update(1, st).map {st =>
      st shouldBe 1
    }
    stService.update(0, st).map {st =>
      st shouldBe 0
    }
  }
}