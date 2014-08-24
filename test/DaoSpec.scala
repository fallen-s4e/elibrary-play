import models.{Book, IDAO, Person, SlickMemoryDAO}
import org.specs2.mutable._

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.{StaticQuery => Q}

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class DaoSpec extends Specification {

  val dummyPersons = List(
    new Person(None, "Иван", "Иванов", "Иванович"),
    new Person(None, "Сидор", "Сидоров", "Сидорович"),
    new Person(None, "Петр", "Петров", "Петрович")
  )
  
  val dummyBooks = List(
    new Book(Some(1), "bookName1", "author1", "theme1", "description1", "barcode1", "bookType1"),
    new Book(Some(2), "bookName2", "author2", "theme2", "description2", "barcode2", "bookType2"),
    new Book(Some(3), "bookName3", "author3", "theme3", "description3", "barcode3", "bookType3")
  )

  def insertPersons(slickDAO: IDAO) {
    dummyPersons.foreach(slickDAO.insertPerson(_))
  }

  def fresh() : IDAO = {
    Database.forURL(new SlickMemoryDAO().dbURL, driver = "org.h2.Driver") withSession { implicit session =>
      Q.updateNA("drop all objects").execute
    }
    new SlickMemoryDAO()
  }

  "Dao" should {
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
      val person: Option[Person] = slickDAO.getPersonByFullName(dummyPersons.head.toFullName())
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
      val firstBook: Book = dummyBooks.head
      slickDAO.insertBook(firstBook)
      Some(firstBook) === slickDAO.getBookById(firstBook.id.get)
    }
  }
}