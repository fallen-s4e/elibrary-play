import models.{IDAO, Person, SlickMemoryDAO}
import org.specs2.mutable._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class DaoSpec extends Specification {

  val slickDAO: IDAO = SlickMemoryDAO

  "Dao" should {
    "insert values and get" in {
      slickDAO.insertPerson(new Person(None, "Иван",  "Иванов",   "Иванович"))
      slickDAO.insertPerson(new Person(None, "Сидор", "Сидоров",  "Сидорович"))
      slickDAO.insertPerson(new Person(None, "Петр",  "Петров",   "Петрович"))
      println(slickDAO.getAllPersons())
      slickDAO.getAllPersons().size === 3
      println(slickDAO.getAllPersons())
    }
  }
}