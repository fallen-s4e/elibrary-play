package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def take = Action {
    Ok(views.html.take())
  }
}