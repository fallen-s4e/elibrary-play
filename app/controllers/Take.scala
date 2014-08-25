package controllers

import models.{Book, Messages, Person, SlickDAO}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._



object Take extends Controller {

  val take1Form: Form[Person] = Form(
    // Define a mapping that will handle Person values
    mapping(
      "personFIO" -> nonEmptyText.verifying(
        // Add an additional constraint: person must exist
        Messages.Erorrs.personMustExist, personFIO => {
          SlickDAO.getPersonByFullName(personFIO).isDefined
        }
      )
    )
      (SlickDAO.getPersonByFullName(_).get)
      ((p: Person) => Some(p.toFullName()))
  )

  val take2Form: Form[Book] = Form(
    // Define a mapping that will handle Person values
    mapping(
      "bookId" -> number.verifying(
        // Add an additional constraint: book must exist
        Messages.Erorrs.bookMustExist, bookId => {
          SlickDAO.getBookById(bookId).isDefined
        }
      )
    )
      (SlickDAO.getBookById(_).get)
      ((b: Book) => b.id)
  )

  // step1: form where user choose his/her name
  def take = Action {
    Ok(views.html.take.step1(take1Form.discardingErrors))
  }

  def step1 = Action { implicit request =>
    take1Form.bindFromRequest.fold(
      // Form has errors, redisplay it
      errors => Ok(views.html.take.step1(errors)),

      // We got a valid User value, display the summary
      person => Redirect(routes.Take.step2(person.id.get))
    )
  }

  // step2: form where user inputs the book to take
  def step2(personId: Int) = Action { implicit request =>
    SlickDAO.getPersonById(personId) match {
      case None => Redirect(routes.Take.take())
      case Some(person) => {
        Ok(views.html.take.step2(person)(take2Form.discardingErrors))
      }
    }
  }

  // step2: form where user inputs the book to take
  def step3(personId: Int) = Action { implicit request =>
    SlickDAO.getPersonById(personId) match {
      case None => Redirect(routes.Take.take())
      case Some(person) => {
        take2Form.bindFromRequest.fold(
          errors => {
            Ok(views.html.take.step2(person)(errors))
          },
          book => {
            SlickDAO.addBookToPerson(person, book)
            Redirect(routes.Take.finish(personId))
          }
        )
      }
    }
  }

  def finish(personId: Int) = Action { implicit request =>
    SlickDAO.getPersonById(personId) match {
      case None => Redirect(routes.Take.take())
      case Some(person) => Ok(views.html.take.finish(person))
    }
  }
}