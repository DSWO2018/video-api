package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json.{Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.{SlickLessonRepository}
import services.{LessonService}

import scala.concurrent.{ExecutionContext}
@Singleton
class LessonController @Inject()(lessonRepo: SlickLessonRepository, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {
  var service = new LessonService(lessonRepo)
  def getLesson (lessonId: Int) = Action.async{
    service.getLesson(lessonId).map(lesson =>
      if(lesson.length>0){
        Ok(Json.obj("status" -> OK, "response" ->Json.toJson(lesson(0))))
      }else{
        NotFound(Json.obj("status" -> NOT_FOUND, "response" -> "No existe ninguna lección con el ID solicitado"))
      })
  }
  def getLessons(courseId: Int) = Action.async{
    service.getLessons(courseId).map(lessons =>
      if(lessons.length>0){
        Ok(Json.obj("status" -> OK, "response" ->Json.obj("items" -> Json.toJson(lessons))))
      }else{
        NotFound(Json.obj("status" -> NOT_FOUND, "response" -> "No existen lecciones o id de curso invalido"))
      })
  }
}