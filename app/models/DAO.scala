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

  def insertTheme(theme : Theme) : Int
  def getAllThemes() : List[Theme]
  def getThemeByName(themeName : String) : Option[Theme]
  def addThemeToBook(book : Book, theme : Theme) : Unit
  def addThemeToThemeGroup(theme : Theme, themeGroup : ThemeGroup) : Unit

  def insertThemeGroup(themeGroup : ThemeGroup) : Int
  def getAllThemeGroups() : List[ThemeGroup]
  def getBooksByTheme(theme : Theme) : List[Book]
  def getThemesByThemegroup(themeGroup : ThemeGroup) : List[Theme]
  def getThemegroupByName(themeGroupName : String) : Option[ThemeGroup]
}

sealed case class SlickDAOImpl(dbURL : String) extends IDAO {
  val db : H2Driver.backend.DatabaseDef = Database.forURL(dbURL)

  val logger = java.util.logging.Logger.getLogger(this.getClass.getName)

  val persons              = TableQuery[Persons]
  val books                = TableQuery[Books]
  val themesToBooks        = TableQuery[ThemesToBooks]
  val themes               = TableQuery[Themes]
  val themeGroups          = TableQuery[ThemeGroups]
  val themesToThemeGroups  = TableQuery[ThemesToThemeGroups]

  // if it can not create ddl it is already exist
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    db.withSession { implicit session =>
      List(persons, books, themes, themeGroups, themesToBooks, themesToThemeGroups).
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

  override def insertTheme(theme : Theme): Int = {
    db.withSession { implicit session => {
      (themes returning themes.map(_.id)) += theme
    }}
  }

  override def insertThemeGroup(themeGroup : ThemeGroup): Int = {
    db.withSession { implicit session => {
      (themeGroups returning themeGroups.map(_.id)) += themeGroup
    }}
  }

  override def getThemeByName(themeName : String) : Option[Theme] = {
    db.withSession { implicit session => {
      themes.filter(_.themeName === themeName).list.headOption
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

  override def addThemeToBook(book: Book, theme: Theme): Unit = {
    logger.info(String.format("adding theme %s to book %s", theme, book))
    db.withSession { implicit session => {
      if (themesToBooks.filter(b => b.bookId === book.id && b.themeId === theme.id).list.size == 0) {
        logger.info(String.format("adding row :  %s", ThemeToBook(None, theme.id.get, book.id.get)))
        themesToBooks += ThemeToBook(None, theme.id.get, book.id.get)
      }
    }}
  }

  override def addThemeToThemeGroup(theme: Theme, themeGroup: ThemeGroup): Unit = {
    logger.info(String.format("adding theme %s to themeGroup %s", theme, themeGroup))
    db.withSession { implicit session => {
      addThemeToThemeGroup_TransMandatory(session, theme, themeGroup)
    }}
  }

  protected def addThemeToThemeGroup_TransMandatory(implicit session: H2Driver.backend.Session,
                                                    theme: Theme, themeGroup: ThemeGroup): Unit = {
    if (themesToThemeGroups.filter(r => r.themeId === theme.id && r.themeGroupId === themeGroup.id).list.size == 0) {
      themesToThemeGroups += ThemeToThemeGroup(None, theme.id.get, themeGroup.id.get)
    }
  }

  override def getBooksByTheme(theme: Theme): List[Book] = {
    db.withSession { implicit session => {
      val query: lifted.Query[Books, Book, Seq] = for {
        ttb <- themesToBooks
        book <- books
        if (ttb.themeId === theme.id.get && ttb.bookId === book.id)
      } yield book
      query.list
    }}
  }

  override def getThemesByThemegroup(themeGroup: ThemeGroup): List[Theme] = {
    db.withSession { implicit session => {
      val query: lifted.Query[Themes, Theme, Seq] = for {
        tttg <- themesToThemeGroups
        theme <- themes
        if (tttg.themeGroupId === themeGroup.id.get && theme.id === tttg.themeId)
      } yield theme
      query.list
    }}
  }

  def getThemegroupByName(themeGroupName : String) : Option[ThemeGroup] = {
    db.withSession { implicit session => {
      themeGroups.filter(_.themeGroupName === themeGroupName).list.headOption
    }}
  }

  override def putBookBack(book: Book): Unit = {
    db.withSession { implicit session => {
      books.filter(_.id === book.id)
        .map(b => b.personId)
        .update(None)
    }}
  }
  override def getAllThemeGroups() : List[ThemeGroup] = {
    db.withSession { implicit session => {
      themeGroups.list
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

  override def getAllThemes() : List[Theme] = {
    db.withSession { implicit session => {
      themes.list
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
    DummyRows.themeGrpToThemes.zipWithIndex.foreach(entry => entry match {
      case ((themeGrp: String, themeList: List[String]), themeGrpId) => {
        db.withSession { implicit session => {
          val themeGroup: ThemeGroup = ThemeGroup(Some(themeGrpId + 1), themeGrp)
          themeGroups += themeGroup
          themeList.zipWithIndex.foreach(_ match {
            case (themeName, themeId) => {
              val theme = Theme(Some(themeId + 1), themeName)
              themes += theme
              addThemeToThemeGroup_TransMandatory(session, theme, themeGroup)
            }
          })
        }}
      }
    })
  }

  scala.util.control.Exception.ignoring(classOf[Exception]) {
    DummyRows.persons.foreach(insertPerson(_))
  }
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    DummyRows.books.foreach(book => {
      insertBook(book)
      addThemeToBook(book, DummyRows.themes.head)
    })
  }
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    initThemes()
  }
}