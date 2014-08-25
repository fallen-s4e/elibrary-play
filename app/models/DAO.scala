package models


import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted
import scala.slick.lifted.TableQuery

/**
 * @author magzhan.karasayev
 * @since 8/23/14 10:21 PM
 */
trait IDAO {
  def getAllPersons() : List[Person]
  def getPersonByFullName(fullName : String) : Option[Person]
  def getPersonById(id : Int) : Option[Person]
  def insertPerson(person : Person) : Unit

  def insertBook(book : Book) : Unit
  def getBookById(bookId : Int) : Option[Book]

  def addBookToPerson(person : Person, book : Book) : Unit
}

/** this class is for DAO unit tests */
class SlickMemoryDAO
  extends SlickDAOImpl("jdbc:h2:~/.h2-databases/elibrary/elibrary")

/** this class is for production mode */
class SlickFileDAO
  extends SlickDAOImpl("jdbc:h2:~/.h2-databases/elibrary/elibrary")

/** and this class is gonna be exported */
//object SlickDAO extends SlickFilledMemoryDAO

/** this class is for visual testing */
class SlickFilledMemoryDAO extends SlickMemoryDAO {
  def init() = {
    insertPerson(new Person(None, "Иван", "Иванов", "Иванович"))
    insertPerson(new Person(None, "Сидор", "Сидоров", "Сидорович"))
    insertPerson(new Person(None, "Петр", "Петров", "Петрович"))
    
    insertBook(new Book(None, "bookName1", "author1", "theme1", "description1", "barcode1", "bookType1", None))
    insertBook(new Book(None, "bookName2", "author2", "theme2", "description2", "barcode2", "bookType2", None))
    insertBook(new Book(None, "bookName3", "author3", "theme3", "description3", "barcode3", "bookType3", None))
  }
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    init()
  }
}

sealed case class SlickDAOImpl(dbURL : String) extends IDAO {
  val db : H2Driver.backend.DatabaseDef = Database.forURL(dbURL)

  val persons = TableQuery[Persons]
  val books = TableQuery[Books]

  // if it can not create ddl it is already exist
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    db.withSession { implicit session =>
      persons.ddl.create
      books.ddl.create
    }
  }

  override def getAllPersons(): List[Person] = {
    db.withSession { implicit session =>
      persons.list
    }
  }

  override def insertPerson(person: Person): Unit = {
    db.withSession { implicit session => {
        persons += person
      }
    }
  }

  override def getPersonByFullName(fullName: String): Option[Person] = {
    db.withSession { implicit session => {
        val res = for {
          p <- persons if p.fullName === fullName
        } yield p
        val list: List[Person] = res.list
        if (list.size > 1) {
          throw new IllegalArgumentException("smth went wrong: more than one person with fullname = " + fullName)
        }
        list.headOption
      }
    }
  }

  override def getPersonById(personId: Int): Option[Person] = {
    db.withSession { implicit session => {
      val query: lifted.Query[Persons, Person, Seq] = for { b <- persons if b.id === personId } yield b
      query.list.headOption
    }
    }
  }

  override def insertBook(book: Book): Unit = {
    db.withSession { implicit session => {
        books += book
    }}
  }

  override def getBookById(bookId: Int): Option[Book] = {
    db.withSession { implicit session => {
      val query: lifted.Query[Books, Book, Seq] = for { b <- books if b.id === bookId } yield b
      query.list.headOption 
    }}
  }

  override def addBookToPerson(person: Person, book: Book): Unit = {
    db.withSession { implicit session => {
      books.filter(_.id === book.id)
        .map(b => b.personId)
        .update(person.id)
    }}
  }
}

//-----------------------------------------------
