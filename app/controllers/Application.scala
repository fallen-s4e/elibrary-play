package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def administration = Action {
    Ok(views.html.administration())
  }
}