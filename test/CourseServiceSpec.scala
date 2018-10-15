import models.{Course, CourseRepository}
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{AsyncFunSuite, Matchers}
import services.CourseService

import scala.concurrent.Future

class CourseServiceSpec extends AsyncFunSuite with Matchers with MockitoSugar {
  val courseRepository = mock[CourseRepository]
  val courses: List[Course] = List(new Course(Option(1), "cursoprueba", "descripcion prueba"),
    new Course(Option(2), "curso2prueba", "descripcion2 prueba"))

  test("return courses List") {
    when(courseRepository.getCourses()).thenReturn(Future(courses))
    val courseService = new CourseService(courseRepository)
    courseService.getCourses().map {
      coursesRes => coursesRes shouldBe courses
    }
  }

  test("return course"){
    when(courseRepository.getCourse(1)).thenReturn(Future(courses))
    val courseService = new CourseService(courseRepository)
    courseService.getCourse(1).map{
      courseRes => courseRes shouldBe courses
    }
  }
}