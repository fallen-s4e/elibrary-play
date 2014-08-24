import models.{IDAO, Person, SlickDAO}
import org.specs2.mutable._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class DaoSpec extends Specification {

  val slickDAO: IDAO = SlickDAO

  "Dao" should {
    "insert values" in {
      slickDAO.insertPerson(new Person(Some(1), "a", "b", "c"))
//      slickDAO.getAllPersons().size should be > 0
      println(slickDAO.getAllPersons())
    }
  }
}