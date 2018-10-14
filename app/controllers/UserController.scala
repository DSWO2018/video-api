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
        Future(BadRequest(Json.obj("status" ->BAD_REQUEST, "response" -> "Wrong email or password size.")))
      },
      user => {
        service.add(user.email, user.password).map{
          case Success(response) => Ok(Json.obj("status" ->OK, "response" -> Json.toJson(response)))
          case Failure(t)        => BadRequest(Json.obj("status" ->BAD_REQUEST, "response" -> "Email already registered."))
        }
      }
    )
  }

  def updateDescription(id: Integer): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue]  =>
    val description = (request.body \ "description").as[String]

    service.updateDescription(id, description).map{ response =>
      if(response > 0) Ok(Json.obj("status" ->OK, "response" -> "Updated."))
      else NotFound(Json.obj("status" ->NOT_FOUND, "response" -> "User not found"))
    }
  }

  def updatePassword(id: Integer): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue] =>
    val password = (request.body \ "old_password").as[String]
    val newPassword = (request.body \ "new_password").as[String]

    service.updatePassword(id, password, newPassword).map{ response =>
      if(response > 0) Ok(Json.obj("status" ->OK, "response" -> "Updated."))
      else BadRequest(Json.obj("status" ->BAD_REQUEST, "response" -> "Bad request."))
    }
  }
}
