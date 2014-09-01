package controllers

import models._
import play.api.data.Form
import play.api.data.Forms._

/**
 * @author magzhan.karasayev
 * @since 8/31/14 7:45 PM
 */
object Forms {
  val bookIdForm: Form[Book] = Form(
    // Define a mapping that will handle Person values
    mapping(
      "barCode" -> nonEmptyText.verifying(
        // Add an additional constraint: book must exist
        Messages.tr("errors.bookMustExist"), barCode => {
          SlickDAO.getBookByBarCode(barCode).isDefined
        }
      )
    )
      (SlickDAO.getBookByBarCode(_).get)
      ((b: Book) => Some(b.barCode))
  )

  val bookForm: Form[Book] = Form(
    // Define a mapping that will handle Person values
    mapping(
      "author"        -> nonEmptyText(2, Int.MaxValue),
      "bookName"      -> nonEmptyText(2, Int.MaxValue),
      "description"   -> nonEmptyText(2, Int.MaxValue),
      "barCode"       -> nonEmptyText(2, Int.MaxValue).verifying(
        // Add an additional constraint: book must not exist
        Messages.tr("errors.barCodeIsDuplicated"), barCode => {
          !SlickDAO.getBookByBarCode(barCode).isDefined
        }
      ),
      "bookType"      -> nonEmptyText(2, Int.MaxValue)
    )
      ((author, bookName, description, barCode, bookType) => Book(None, bookName, author, description, barCode, bookType, None))
      ((b: Book) => Some((b.author, b.bookName, b.description, b.barCode, b.bookType)))
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
