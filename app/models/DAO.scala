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

  def insertBook(book : Book) : Int
  def getBookById(bookId : Int) : Option[Book]
  def getBookByBarCode(barCode: String): Option[Book]
  def deleteBookByBarcode(barCode: String): Unit

  def addBookToPerson(person : Person, book : Book) : Unit
  def putBookBack(book : Book) : Unit

  def addThemeToBook(book : Book, theme : String) : Unit
  def addThemeToThemeGroup(theme : String, themeGroup : String) : Unit

  def getAllThemeGroups() : List[String]
  def getBooksByTheme(theme : String) : List[Book]
  def getThemesByThemegroup(themeGroup : String) : List[String]
}

sealed case class SlickDAOImpl(dbURL : String) extends IDAO {
  val db : H2Driver.backend.DatabaseDef = Database.forURL(dbURL)

  val logger = java.util.logging.Logger.getLogger(this.getClass.getName)

  val persons              = TableQuery[Persons]
  val books                = TableQuery[Books]
  val themesToBooks        = TableQuery[ThemesToBooks]
  val themesToThemeGroups  = TableQuery[ThemesToThemeGroups]

  // if it can not create ddl it is already exist
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    db.withSession { implicit session =>
      List(persons, books, themesToBooks, themesToThemeGroups).
        foreach(_.ddl.create)
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

  override def insertBook(book: Book): Int = {
    db.withSession { implicit session => {
      (books returning books.map(_.id)) += book
    }}
  }

  override def getBookById(bookId: Int): Option[Book] = {
    db.withSession { implicit session => {
      val query: lifted.Query[Books, Book, Seq] = for { b <- books if b.id === bookId } yield b
      query.list.headOption
    }}
  }

  override def getBookByBarCode(barCode: String): Option[Book] = {
    db.withSession { implicit session => {
      books.filter(_.barCode === barCode).list.headOption
    }}
  }

  override def addBookToPerson(person: Person, book: Book): Unit = {
    db.withSession { implicit session => {
      books.filter(_.id === book.id)
        .map(b => b.personId)
        .update(person.id)
    }}
  }

  override def addThemeToBook(book: Book, theme: String): Unit = {
    logger.info(String.format("adding theme %s to book %s", theme, book))
    db.withSession { implicit session => {
      if (themesToBooks.filter(b => b.bookId === book.id && b.themeName === theme).list.size == 0) {
        themesToBooks += ThemeToBook(None, theme, book.id.get)
      }
    }}
  }

  override def addThemeToThemeGroup(theme: String, themeGroup: String): Unit = {
    logger.info(String.format("adding theme %s to themeGroup %s", theme, themeGroup))
    db.withSession { implicit session => {
      addThemeToThemeGroup_TransMandatory(session, theme, themeGroup)
    }}
  }

  protected def addThemeToThemeGroup_TransMandatory(implicit session: H2Driver.backend.Session,
                                                    theme: String, themeGroup: String): Unit = {
    if (themesToThemeGroups.filter(r => r.themeName === theme && r.themeGroupName === themeGroup).list.size == 0) {
      themesToThemeGroups += ThemeToThemeGroup(None, theme, themeGroup)
    }
  }

  override def getBooksByTheme(theme: String): List[Book] = {
    db.withSession { implicit session => {
      val query: lifted.Query[Books, Book, Seq] = for {
        ttb <- themesToBooks
        book <- books
        if (ttb.themeName === theme && ttb.bookId === book.id)
      } yield book
      query.list
    }}
  }

  override def getThemesByThemegroup(themeGroup: String): List[String] = {
    db.withSession { implicit session => {
      val query: lifted.Query[lifted.Column[String], String, Seq] = for {
        tttg <- themesToThemeGroups
        if (tttg.themeGroupName === themeGroup)
      } yield tttg.themeName
      query.list
    }}
  }

  override def putBookBack(book: Book): Unit = {
    db.withSession { implicit session => {
      books.filter(_.id === book.id)
        .map(b => b.personId)
        .update(None)
    }}
  }
  override def getAllThemeGroups() : List[String] = {
    db.withSession { implicit session => {
      themesToThemeGroups.groupBy(_.themeGroupName).map(_._1).list
    }}
  }

  override def deleteBookByBarcode(barCode: String): Unit = {
    db.withSession { implicit session => {
      val bookId = books.filter(_.barCode === barCode).list.headOption.
        getOrElse(throw new IllegalArgumentException("no book with such barcode")).id.get
      themesToBooks.filter(_.bookId === bookId).delete
      books.filter(_.id === bookId).delete
    }}
  }
}

//-----------------------------------------------

/** this class is for DAO unit tests */
class SlickMemoryDAO
  extends SlickDAOImpl("jdbc:h2:mem:elibrary:;DB_CLOSE_DELAY=-1")

/** this class is for production mode */
class SlickFileDAO
  extends SlickDAOImpl("jdbc:h2:~/.h2-databases/elibrary/elibrary")

/** this class is for visual testing */
class SlickFilledMemoryDAO extends SlickMemoryDAO {
  def initThemes() = {
    DummyRows.themeGrpToThemes.foreach((entry) => entry match {
      case (themeGrp: String, themes: List[String]) => {
        db.withSession { implicit session => {
          themes.map((theme) => addThemeToThemeGroup_TransMandatory(session, theme, themeGrp))
        }}
      }
    })
  }

  scala.util.control.Exception.ignoring(classOf[Exception]) {
    DummyRows.persons.foreach(insertPerson(_))
  }
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    DummyRows.books.foreach(insertBook(_))
  }
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    initThemes()
  }
}