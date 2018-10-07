package controllers

import javax.inject.{Inject, Singleton}
import models.User
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}
import repository.SlickUserRepository
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(userRepo: SlickUserRepository, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  val service = new UserService(userRepo)

  def addUser(): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue]  =>
    val placeResult = request.body.validate[User]
    placeResult.fold(
      errors => {
        Future(BadRequest(Json.obj("status" ->BAD_REQUEST, "message" -> JsError.toJson(errors))))
      },
      user => {
        service.addUser(user).map { anyUser =>
          Ok(Json.toJson(anyUser))
        }
      }
    )
  }
}
