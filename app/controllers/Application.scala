package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  // step1: form where user choose his/her name
  def take = Action {
    Redirect(routes.Application.take1())
  }

  def take1 = Action {
    Ok(views.html.take.step1())
  }

  // step2: form where user inputs the book to take
  def take2 = Action {
    Ok(views.html.take.step2())
  }

  def putback = Action {
    Ok(views.html.putback())
  }

  def library = Action {
    Ok(views.html.library())
  }

  def administration = Action {
    Ok(views.html.administration())
  }
}