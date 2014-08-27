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
    new Person(Some(1), "Иван", "Иванов", "Иванович"),
    new Person(Some(2), "Сидор", "Сидоров", "Сидорович"),
    new Person(Some(3), "Петр", "Петров", "Петрович")
  )
  
  val dummyBooks = List(
    new Book(Some(1), "bookName1", "author1", "description1", "barcode1", "bookType1", None),
    new Book(Some(2), "bookName2", "author2", "description2", "barcode2", "bookType2", None),
    new Book(Some(3), "bookName3", "author3", "description3", "barcode3", "bookType3", None)
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
    "find person by id" in {
      val slickDAO: IDAO = fresh()
      insertPersons(slickDAO)
      val person: Option[Person] = slickDAO.getPersonById(dummyPersons.head.id.get)
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
    "add a book to a person" in {
      val slickDAO: IDAO = fresh()
      val firstBook: Book      = dummyBooks.head
      val firstPerson : Person = dummyPersons.head

      slickDAO.insertBook(firstBook)
      slickDAO.insertPerson(firstPerson)
      slickDAO.addBookToPerson(firstPerson, firstBook)

      slickDAO.getBookById(firstBook.id.get) should not be None
      Some(firstPerson.id.get) === slickDAO.getBookById(firstBook.id.get).get.personId
    }
  }
}