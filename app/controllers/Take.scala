package controllers

import models.SlickDAO
import play.api.mvc._



object Take extends Controller {

  // step1: form where user choose his/her name
  def take = Action {
    Ok(views.html.take.step1(Forms.personForm.discardingErrors))
  }

  def step1 = Action { implicit request =>
    Forms.personForm.bindFromRequest.fold(
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
        Ok(views.html.take.step2(person)(Forms.bookIdForm.discardingErrors))
      }
    }
  }

  // step2: form where user inputs the book to take
  def step3(personId: Int) = Action { implicit request =>
    SlickDAO.getPersonById(personId) match {
      case None => Redirect(routes.Take.take())
      case Some(person) => {
        Forms.bookIdForm.bindFromRequest.fold(
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