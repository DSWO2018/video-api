package controllers

import javax.inject.{Inject, Singleton}
import models.User
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, ControllerComponents, Request}
import repository.SlickUserRepository
import services.UserService

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class UserController @Inject()(userRepo: SlickUserRepository, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  val service = new UserService(userRepo)

  def addUser(): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue]  =>
    val placeResult = request.body.validate[User]
    placeResult.fold(
      _ => {
        Future(BadRequest(Json.obj("status" ->BAD_REQUEST, "message" -> "Wrong email or password size.")))
      },
      user => {
        service.add(user.email, user.password).map{
          case Success(response) => Ok(Json.toJson(response))
          case Failure(t)        => BadRequest(Json.obj("status" ->BAD_REQUEST, "message" -> "Email already registered."))
        }
      }
    )
  }
}
