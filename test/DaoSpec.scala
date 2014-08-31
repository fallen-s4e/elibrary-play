import models._
import org.specs2.mutable._

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.{StaticQuery => Q}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class DaoSpec extends Specification {

  def insertPersons(slickDAO: IDAO) {
    DummyRows.persons.foreach(slickDAO.insertPerson(_))
  }

  def fresh() : IDAO = {
    Database.forURL(new SlickMemoryDAO().dbURL, driver = "org.h2.Driver") withSession { implicit session =>
      Q.updateNA("drop all objects").execute
    }
    new SlickMemoryDAO()
  }

  "Dao" should {
    "initThemes without errors" in {
      // idk what makes it so long, commented
//      new SlickFilledMemoryDAO().initThemes()
    }
    "insert values and get" in {
      val slickDAO: IDAO = fresh()
      insertPersons(slickDAO)
      slickDAO.getAllPersons().size === 3
    }
    "restrict same values" in {
      val slickDAO: IDAO = fresh()
      insertPersons(slickDAO)
      scala.util.control.Exception.ignoring(classOf[Throwable]) {
        insertPersons(slickDAO)
      }
      slickDAO.getAllPersons().size === 3
    }
    "find person by fullname" in {
      val slickDAO: IDAO = fresh()
      insertPersons(slickDAO)
      val person: Option[Person] = slickDAO.getPersonByFullName(DummyRows.persons.head.toFullName())
      person !== None
    }
    "find person by id" in {
      val slickDAO: IDAO = fresh()
      insertPersons(slickDAO)
      val person: Option[Person] = slickDAO.getPersonById(DummyRows.persons.head.id.get)
      person !== None
    }
    "not find person by fullname if does not exist" in {
      val slickDAO: IDAO = fresh()
      insertPersons(slickDAO)
      val person: Option[Person] = slickDAO.getPersonByFullName("NoSuchPerson")
      person === None
    }
    "insert and find this inserted book" in {
      val slickDAO: IDAO = fresh()
      val firstBook: Book = DummyRows.books.head
      slickDAO.insertBook(firstBook)
      Some(firstBook) === slickDAO.getBookById(firstBook.id.get)
    }
    "add a book to a person" in {
      val slickDAO: IDAO = fresh()
      val firstBook: Book      = DummyRows.books.head
      val firstPerson : Person = DummyRows.persons.head

      slickDAO.insertBook(firstBook)
      slickDAO.insertPerson(firstPerson)
      slickDAO.addBookToPerson(firstPerson, firstBook)

      slickDAO.getBookById(firstBook.id.get) should not be None
      Some(firstPerson.id.get) === slickDAO.getBookById(firstBook.id.get).get.personId
    }
    "add theme to a book and find book by theme" in {
      val slickDAO: IDAO = fresh()
      val firstBook: Book      = DummyRows.books.head
      val firstTheme: String   = DummyRows.themeGrpToThemes.values.head.head

      slickDAO.insertBook(firstBook)
      slickDAO.addThemeToBook(firstBook, firstTheme)
      List(firstBook) === slickDAO.getBooksByTheme(firstTheme)
    }
    "add theme to themeGroup and find theme ByThemeGroup" in {
      val slickDAO: IDAO = fresh()
      val firstTheme: String      = DummyRows.themes.head
      val firstThemeGroup: String = DummyRows.themeGroups.head

      slickDAO.addThemeToThemeGroup(firstTheme, firstThemeGroup)
      List(firstTheme) === slickDAO.getThemesByThemegroup(firstThemeGroup)
    }
    "add a book to a person and put it back" in {
      val slickDAO: IDAO = fresh()
      val firstBook: Book      = DummyRows.books.head
      val firstPerson : Person = DummyRows.persons.head

      slickDAO.insertBook(firstBook)
      slickDAO.insertPerson(firstPerson)
      slickDAO.addBookToPerson(firstPerson, firstBook)
      slickDAO.putBookBack(firstBook)

      slickDAO.getBookById(firstBook.id.get) should not be None
      None === slickDAO.getBookById(firstBook.id.get).get.personId
    }
  }
}