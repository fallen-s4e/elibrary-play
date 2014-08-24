package controllers

import controllers.Application._
import models.{SlickDAO, SlickDAOImpl, Person, Messages}
import models.Messages.Erorrs
import play.api.mvc._

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import scalaz._

import views._

import models._



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
}