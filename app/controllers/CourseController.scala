package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.SlickCourseRepository
import services.CourseService

import scala.concurrent.{ExecutionContext, Future}
@Singleton
class CourseController @Inject()(curseRepo: SlickCourseRepository, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {
  var service = new CourseService(curseRepo)
  def getCourses() = Action.async{
    service.getCourses().map(courses =>
      if(courses.length>0){
        Ok(Json.obj("status" -> OK, "response" ->Json.obj("items" -> Json.toJson(courses))))
      }else{
        NotFound(Json.obj("status" -> NOT_FOUND, "response" -> "No existen cursos"))
      })
  }
}