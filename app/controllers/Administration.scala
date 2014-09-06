package controllers

import models.SlickDAO
import play.api.mvc._

object Administration extends Controller {

  def index = Action {
    Ok(views.html.administration.index())
  }

  /*
   *  addition section
   */
  def addBook = Action {
    Ok(views.html.administration.addBook(SlickDAO.getAllThemes())(Forms.bookForm.discardingErrors))
  }

  def submitAddBook = Action { implicit request => {
    Forms.bookForm.bindFromRequest.fold(
      errors => {
        Ok(views.html.administration.addBook(SlickDAO.getAllThemes())(errors))
      },
      book => {
        val bookId: Int = SlickDAO.insertBook(book)
        val theme = SlickDAO.getAllThemes().filter(_.themeName == book.bookType).head
        SlickDAO.addThemeToBook(SlickDAO.getBookById(bookId).get, theme)
        Redirect(routes.Administration.addBookFinish(book.barCode))
      })
  }}

  def addBookFinish(bookBarCode : String) = Action {
    Ok(views.html.administration.addBookFinish(bookBarCode))
  }

  /*
   *  deletion section
   */
  def deleteBook = Action {
    Ok(views.html.administration.deleteBook(Forms.bookIdForm.discardingErrors))
  }

  def submitDeleteBook = Action { implicit request => {
    Forms.bookIdForm.bindFromRequest.fold(
      errors => {
        Ok(views.html.administration.deleteBook(errors))
      },
      book => {
        SlickDAO.deleteBookByBarcode(book.barCode)
        Redirect(routes.Administration.deleteBookFinish(book.barCode))
      })
  }}

  def deleteBookFinish(bookBarCode : String) = Action {
    Ok(views.html.administration.deleteBookFinish(bookBarCode))
  }
}