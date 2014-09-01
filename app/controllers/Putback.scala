package controllers

import models.SlickDAO
import play.api.mvc._

object Putback extends Controller {

  def index = Action {
    Ok(views.html.putback.index(Forms.bookIdForm.discardingErrors))
  }

  def submit = Action { implicit request =>
    Forms.bookIdForm.bindFromRequest.fold(
      errors => {
        Ok(views.html.putback.index(errors))
      },
      book => {
        SlickDAO.putBookBack(book)
        Redirect(routes.Putback.finish(book.id.get))
      }
    )
  }

  def finish(bookId : Int) = Action {
    Ok(views.html.putback.finish(bookId))
  }
}