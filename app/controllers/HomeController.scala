package controllers

import javax.inject._
import models.User
import play.api.mvc._
import play.api.libs.json._
import repository.SlickUserRepository
import services.UserService

import scala.concurrent.{ExecutionContext, Future}

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(userRepo: SlickUserRepository, cc: ControllerComponents)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  val service = new UserService(userRepo)

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
      Ok(Json.obj("status" ->"OK", "response" -> "Hello world."))
  }

  def who = Action { request =>
    request.session.get("user").map { user =>
      Ok(Json.obj("status" ->OK, "response" -> user))
    }.getOrElse {
      Unauthorized(Json.obj("status" ->UNAUTHORIZED, "response" -> "Unauthorized"))
    }
  }

  def login(): Action[JsValue] = Action.async(parse.json) { implicit request: Request[JsValue]  =>
    val placeResult = request.body.validate[User]
    placeResult.fold(
      errors => {
        Future(BadRequest(Json.obj("status" ->BAD_REQUEST, "response" -> JsError.toJson(errors))))
      },
      user => {
        service.isAuthenticated(user.email, user.password).map { auth =>
          if(auth) {
            Ok(Json.obj("status" ->OK, "response" -> "Authorized")).withSession(
              "user" -> user.email)
          } else {
            Unauthorized(Json.obj("status" ->UNAUTHORIZED, "response" -> "Bad login"))
          }
        }
      }
    )
  }
}
