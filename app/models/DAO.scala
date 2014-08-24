package models


import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.TableQuery

/**
 * @author magzhan.karasayev
 * @since 8/23/14 10:21 PM
 */
trait IDAO {
  def getAllPersons() : List[Person]
  def getPersonByFullName(fullName : String) : Option[Person]
  def insertPerson(person : Person) : Unit
}

/** this class is for DAO unit tests */
class SlickMemoryDAO
  extends SlickDAO("jdbc:h2:~/.h2-databases/elibrary/elibrary")

/** this class is for production mode */
class SlickFileDAO
  extends SlickDAO("jdbc:h2:~/.h2-databases/elibrary/elibrary")

/** and this class is gonna be exported */
object SlickDAO extends SlickFilledMemoryDAO

/** this class is for visual testing */
class SlickFilledMemoryDAO extends SlickMemoryDAO {
  def init() = {
    insertPerson(new Person(None, "Иван", "Иванов", "Иванович"))
    insertPerson(new Person(None, "Сидор", "Сидоров", "Сидорович"))
    insertPerson(new Person(None, "Петр", "Петров", "Петрович"))
  }
  init()
}

sealed case class SlickDAO(dbURL : String) extends IDAO {
  val db : H2Driver.backend.DatabaseDef = Database.forURL(dbURL)

  val persons = TableQuery[Persons]

  // if it can not create ddl it is already exist
  scala.util.control.Exception.ignoring(classOf[Exception]) {
    db.withSession { implicit session =>
      persons.ddl.create
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
}

//-----------------------------------------------
