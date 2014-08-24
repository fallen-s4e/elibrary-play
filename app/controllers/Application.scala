package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
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