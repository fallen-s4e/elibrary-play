package models

import scala.slick.driver.H2Driver.simple._
import scala.slick.lifted.{ForeignKeyQuery, ProvenShape}

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
                theme : String, description : String, barCode : String, bookType : String,
                personId : Option[Int]
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

  def personId: Column[Option[Int]] = column[Int]("PERSON_ID", O.Nullable)

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[Book] =
    (id.?, bookName, author, theme, description, barCode, bookType, personId) <> (Book.tupled, Book.unapply)

  def book: ForeignKeyQuery[Persons, Person] =
    foreignKey("PERSONS_FK", personId, TableQuery[Persons])(_.id)
}

//-----------------------------------------------------------------------------------------------------------

case class ThemeToBook(id        : Option[Int],
                       themeName : String,
                       bookId    : Int)

class ThemesToBooks(tag : Tag)
  extends Table[ThemeToBook](tag, "THEMES_TO_BOOKS") {

  def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def themeName: Column[String] = column[String]("THEME_NAME", O.NotNull)

  def bookId = column[Int]("BOOK_ID", O.NotNull)

  def * : ProvenShape[ThemeToBook] = (id?, themeName, bookId) <> (ThemeToBook.tupled, ThemeToBook.unapply)

  def book: ForeignKeyQuery[Books, Book] =
    foreignKey("THEMES_TO_BOOKS_FK", bookId, TableQuery[Books])(_.id)
}

//-----------------------------------------------------------------------------------------------------------

case class ThemeToThemeGroup(id                : Option[Int],
                             themeName         : String,
                             themeGroupName    : String)

class ThemesToThemeGroups(tag : Tag)
  extends Table[ThemeToThemeGroup](tag, "THEMES_TO_THEMEGROUPS") {

  def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def themeName      : Column[String] = column[String]("THEME_NAME", O.NotNull)

  def themeGroupName : Column[String] = column[String]("THEME_NAME", O.NotNull)

  def * : ProvenShape[ThemeToThemeGroup] = (id?, themeName, themeGroupName) <> (ThemeToThemeGroup.tupled, ThemeToThemeGroup.unapply)

  def rowIndex = index("THEMES_TO_THEMEGROUPS_INDEX", (themeName, themeGroupName), unique = true)
}
