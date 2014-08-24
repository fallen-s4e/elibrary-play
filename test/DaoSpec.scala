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

  def insertPersons(slickDAO: IDAO) {
    slickDAO.insertPerson(new Person(None, "Иван", "Иванов", "Иванович"))
    slickDAO.insertPerson(new Person(None, "Сидор", "Сидоров", "Сидорович"))
    slickDAO.insertPerson(new Person(None, "Петр", "Петров", "Петрович"))
  }

  def cleanup() = {
    Database.forURL(new SlickMemoryDAO().dbURL, driver = "org.h2.Driver") withSession { implicit session =>
      Q.updateNA("drop all objects").execute
    }
  }

  "Dao" should {
    "insert values and get" in {
      cleanup()
      val slickDAO: IDAO = new SlickMemoryDAO()
      insertPersons(slickDAO)
      slickDAO.getAllPersons().size === 3
    }
    "restrict same values" in {
      cleanup()
      val slickDAO: IDAO = new SlickMemoryDAO()
      insertPersons(slickDAO)
      scala.util.control.Exception.ignoring(classOf[Throwable]) {
        insertPersons(slickDAO)
      }
      slickDAO.getAllPersons().size === 3
    }
  }
}