package controllers

import javax.inject.Inject
import models.SkillTree
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}
import repository.SlickSkillTreeRepository
import services.SkillTreeService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class SkillTreeController @Inject()(skRepo: SlickSkillTreeRepository, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  val service = new SkillTreeService(skRepo)

  def add(): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue]  =>
    val placeResult = request.body.validate[SkillTree]
    placeResult.fold(
      _ => {
        Future(BadRequest(Json.obj("status" ->BAD_REQUEST, "response" -> "Wrong Json format")))
      },
      skill => {
        service.add(skill).map{
          case Success(response) => Ok(Json.obj("status" ->OK, "response" -> Json.toJson(response)))
          case Failure(t)        => BadRequest(Json.obj("status" ->BAD_REQUEST, "response" -> t.getMessage))
        }
      }
    )
  }

  def update(id: Integer): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue]  =>
    val placeResult = request.body.validate[SkillTree]
    placeResult.fold(
      _ => {
        Future(BadRequest(Json.obj("status" ->BAD_REQUEST, "response" -> "Wrong Json format")))
      },
      skill => {
        service.update(id, skill).map{ response =>
          if(response > 0) Ok(Json.obj("status" ->OK, "response" -> "Updated."))
          else NotFound(Json.obj("status" ->NOT_FOUND, "response" -> "Skill not found"))
        }
      }
    )
  }

}
