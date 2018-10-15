import models.{Lesson, LessonRepository}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.{LessonService}

import scala.concurrent.Future

class LessonServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  val lessonRepository = mock[LessonRepository]
  val lessons: List[Lesson] = List(new Lesson(Option(1), "Lesson1", "Descripción lesson1", 1),
    new Lesson(Option(1), "Lesson2", "Descripción lesson2", 1))

  test("return lessons List") {
    when(lessonRepository.getLessons(1)).thenReturn(Future(lessons))
    val lessonService = new LessonService(lessonRepository)
    lessonService.getLessons(1).map {
      lessonsRes => lessonsRes shouldBe lessons
    }
  }
  test("return lesson"){
    when(lessonRepository.getLesson(1)).thenReturn(Future(lessons))
    val lessonService = new LessonService(lessonRepository)
    lessonService.getLesson(1).map{
      lessonRes => lessonRes shouldBe lessons
    }
  }
}