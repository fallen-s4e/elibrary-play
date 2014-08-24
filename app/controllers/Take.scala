package controllers

import models.{Messages, Person, SlickDAO}
import play.api.data.Forms._
import play.api.data._
import play.api.mvc._



object Take extends Controller {
  /**
   * Sign Up Form definition.
   *
   * Once defined it handle automatically, ,
   * validation, submission, errors, redisplaying, ...
   */
  val take1Form: Form[Person] = Form(

    // Define a mapping that will handle User values
    mapping(
      "personFIO" -> nonEmptyText.verifying(
        // Add an additional constraint: both passwords must match
        Messages.Erorrs.personMustExist, personFIO => {SlickDAO.getPersonByFullName(personFIO).isDefined}
      )
    )
      (SlickDAO.getPersonByFullName(_).get)
      ((p:Person) => Some(p.toFullName()))
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
      user => Redirect(routes.Take.step2())
    )
  }

  // step2: form where user inputs the book to take
  def step2 = Action { implicit request =>
    Ok(views.html.take.step2())
  }
}