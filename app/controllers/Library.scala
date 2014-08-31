package controllers

import models.SlickDAO
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
    Ok(views.html.library.takeBook(bookId)(Forms.personForm.discardingErrors))
  }

  def submit(bookId : Int) = Action { implicit request =>
    Forms.personForm.bindFromRequest.fold(
      errors => {
        Ok(views.html.library.takeBook(bookId)(errors))
      },
      person => {
        SlickDAO.addBookToPerson(person, SlickDAO.getBookById(bookId).get)
        Redirect(routes.Take.finish(person.id.get))
      }
    )
  }

  def finish(bookId : Int) = Action {
    Ok(views.html.putback.finish(bookId))
  }
}