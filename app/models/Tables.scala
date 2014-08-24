package models

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.ProvenShape

/**
 * @author magzhan.karasayev
 * @since 8/24/14 12:01 AM
 */

case class Person(id : Option[Int], firstName : String, lastName : String, patronymic : String)

class Persons(tag : Tag)
  extends Table[Person](tag, "PERSONS") {
  // This is the primary key column:
  def id: Column[Int]      = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def firstName:  Column[String] = column[String]("FIRST_NAME", O.NotNull)
  def lastName:   Column[String] = column[String]("LAST_NAME",  O.NotNull)
  def patronymic: Column[String] = column[String]("PATRONYMIC", O.NotNull)

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[Person] =
    (id.?, firstName, lastName, patronymic) <> (Person.tupled, Person.unapply)
}