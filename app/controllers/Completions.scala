package controllers

import models.SlickDAO

/**
 * @author magzhan.karasayev
 * @since 9/6/14 5:51 PM
 */
object Completions {

  def personCompletion() = {
    Completions.toCompletion(SlickDAO.getAllPersons().map(_.toFullName()))
  }

  private def toCompletion(completions : List[String]) : String = completions match {
    case List()  => ""
    case xs      => xs.map(String.format("\"%s\"", _)).mkString(", ")
  }
}
