package controllers

import models.SlickDAO
import play.api.mvc._

object Administration extends Controller {

  def index = Action {
    Ok(views.html.administration.index())
  }

  def addBook = Action {
    Ok(views.html.administration.addBook(Forms.bookForm.discardingErrors))
  }

  def submitAddBook = Action { implicit request => {
    Forms.bookForm.bindFromRequest.fold(
      errors => {
        Ok(views.html.administration.addBook(errors))
      },
      book => {
        val bookId: Int = SlickDAO.insertBook(book)
        SlickDAO.addThemeToBook(SlickDAO.getBookById(bookId).get, book.bookType)
        Redirect(routes.Administration.addBookFinish(book.barCode))
      })
  }}

  def addBookFinish(bookBarCode : String) = Action {
    Ok(views.html.administration.addBookFinish(bookBarCode))
  }
}