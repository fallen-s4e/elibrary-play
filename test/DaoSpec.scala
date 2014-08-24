import models.{IDAO, Person, SlickMemoryDAO}
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
  }
}