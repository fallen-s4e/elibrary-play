package controllers

import models.{Person, SlickDAO, Messages, Book}
import play.api.data.Form
import play.api.data.Forms._

/**
 * @author magzhan.karasayev
 * @since 8/31/14 7:45 PM
 */
object Forms {
  val bookForm: Form[Book] = Form(
    // Define a mapping that will handle Person values
    mapping(
      "bookId" -> number.verifying(
        // Add an additional constraint: book must exist
        Messages.tr("errors.bookMustExist"), bookId => {
          SlickDAO.getBookById(bookId).isDefined
        }
      )
    )
      (SlickDAO.getBookById(_).get)
      ((b: Book) => b.id)
  )

  val personForm: Form[Person] = Form(
    // Define a mapping that will handle Person values
    mapping(
      "personFIO" -> nonEmptyText.verifying(
        // Add an additional constraint: person must exist
        Messages.tr("errors.personMustExist"), personFIO => {
          SlickDAO.getPersonByFullName(personFIO).isDefined
        }
      )
    )
      (SlickDAO.getPersonByFullName(_).get)
      ((p: Person) => Some(p.toFullName()))
  )
}
