package controllers

import play.api.mvc._

object Administration extends Controller {

  def index = Action {
    Ok(views.html.administration.index())
  }
}