package models

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.ProvenShape

/**
 * @author magzhan.karasayev
 * @since 8/24/14 12:01 AM
 */

case class Person(id : Option[Int], firstName : String, lastName : String, patronymic : String) {
  def toFullName() = {
    firstName + " " + lastName + " " + patronymic
  }
}

class Persons(tag : Tag)
  extends Table[Person](tag, "PERSONS") {
  // This is the primary key column:
  def id: Column[Int]      = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def firstName:  Column[String] = column[String]("FIRST_NAME", O.NotNull)
  def lastName:   Column[String] = column[String]("LAST_NAME",  O.NotNull)
  def patronymic: Column[String] = column[String]("PATRONYMIC", O.NotNull)

  def bIdx1 = index("b_idx1", (firstName, lastName, patronymic), unique = true)

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[Person] =
    (id.?, firstName, lastName, patronymic) <> (Person.tupled, Person.unapply)

  def fullName = firstName ++ " "  ++ lastName ++ " "  ++ patronymic
}

//----------------------------------------------------------------------------------------------------------------
case class Book(id : Option[Int], bookName : String, author : String,
                theme : String, description : String, barCode : String, bookType : String
                 )

class Books(tag : Tag)
  extends Table[Book](tag, "BOOKS") {
  // This is the primary key column:
  def id: Column[Int]      = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def bookName:  Column[String]     = column[String]("BOOK_NAME",   O.NotNull)
  def author:   Column[String]      = column[String]("AUTHOR",      O.NotNull)
  def theme: Column[String]         = column[String]("THEME",       O.NotNull)
  def description: Column[String]   = column[String]("DESCRIPTION", O.NotNull)
  def barCode: Column[String]       = column[String]("BAR_CODE",    O.NotNull)
  def bookType: Column[String]      = column[String]("BOOK_TYPE",   O.NotNull)

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[Book] =
    (id.?, bookName, author, theme, description, barCode, bookType) <> (Book.tupled, Book.unapply)
}


