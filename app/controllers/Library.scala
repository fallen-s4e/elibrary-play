package controllers

import models.{DummyRows, SlickDAO}
import play.api.mvc._

object Library extends Controller {

  def index = Action {
    Ok(views.html.library.index(SlickDAO.getAllThemeGroups()))
  }

  def byThemeGroup(themeGroup : String) = Action {
    Ok(views.html.library.bythemegroup(SlickDAO.getThemesByThemegroup(themeGroup)))
  }

  def byTheme(theme : String) = Action {
    Ok(views.html.library.bytheme(theme, SlickDAO.getBooksByTheme(theme)))
  }

  def takeBook(bookId : Int) = Action {
    Ok("lib : take book")
  }

  //---------------- unused
  def submit = Action { implicit request =>
    Forms.bookForm.bindFromRequest.fold(
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