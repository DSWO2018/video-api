package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents}
import repository.SlickUniRepository
import services.UniService

import scala.concurrent.ExecutionContext

@Singleton
class UniController @Inject()(uniRepo: SlickUniRepository, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  val service = new UniService(uniRepo)

  def list() = Action.async { implicit request =>
    service.list().map { result =>
      Ok(Json.obj("status" ->OK, "response" -> Json.toJson(result)))
    }
  }

}
