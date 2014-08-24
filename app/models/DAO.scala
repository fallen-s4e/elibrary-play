package models


import scala.slick.driver.H2Driver
import scala.slick.lifted.TableQuery

import scala.slick.driver.H2Driver.simple._

/**
 * @author magzhan.karasayev
 * @since 8/23/14 10:21 PM
 */
trait IDAO {
  def getAllPersons() : List[Person]
  def insertPerson(person : Person) : Unit
}

private class MockDAO extends IDAO {
  private var persons : List[Person] = List(
    Person(Some(1), "Иван",  "Иванов",   "Иванович"),
    Person(Some(2), "Сидор", "Сидоров",  "Сидорович"),
    Person(Some(3), "Петр",  "Петров",   "Петрович")
  )

  override def getAllPersons(): List[Person] = persons

  override def insertPerson(person: Person): Unit = { persons :+ person }
}

object SlickMemoryDAO
  extends SlickDAO(Database.forURL("jdbc:h2:mem:elibrary:;DB_CLOSE_DELAY=-1", driver="org.h2.Driver"))

object SlickFileDAO
  extends SlickDAO(Database.forURL("jdbc:h2:~/.h2-databases/elibrary/elibrary", driver="org.h2.Driver"))

case class SlickDAO(db : H2Driver.backend.DatabaseDef) extends IDAO {
  val persons = TableQuery[Persons]

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
}

//object DAO extends SlickDAO

//-----------------------------------------------
