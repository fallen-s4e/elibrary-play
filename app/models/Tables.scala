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
                description : String, barCode : String, bookType : String,
                personId : Option[Int]
                 )

class Books(tag : Tag)
  extends Table[Book](tag, "BOOKS") {
  // This is the primary key column:
  def id: Column[Int]      = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def bookName:  Column[String]     = column[String]("BOOK_NAME",   O.NotNull)
  def author:   Column[String]      = column[String]("AUTHOR",      O.NotNull)
  def description: Column[String]   = column[String]("DESCRIPTION", O.NotNull)
  def barCode: Column[String]       = column[String]("BAR_CODE",    O.NotNull)
  def bookType: Column[String]      = column[String]("BOOK_TYPE",   O.NotNull)

  def personId: Column[Option[Int]] = column[Int]("PERSON_ID", O.Nullable)

  // Every table needs a * projection with the same type as the table's type parameter
  def * : ProvenShape[Book] =
    (id.?, bookName, author, description, barCode, bookType, personId) <> (Book.tupled, Book.unapply)

  def book: ForeignKeyQuery[Persons, Person] =
    foreignKey("PERSONS_FK", personId, TableQuery[Persons])(_.id)
}

//-----------------------------------------------------------------------------------------------------------
case class Theme(id           : Option[Int],
                 themeName    : String)

class Themes(tag : Tag)
  extends Table[Theme](tag, "THEMES") {

  def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def themeName    : Column[String] = column[String]("THEME_NAME",  O.NotNull)

  def * : ProvenShape[Theme] = (id?, themeName) <> (Theme.tupled, Theme.unapply)
}
//-----------------------------------------------------------------------------------------------------------

case class ThemeToBook(id        : Option[Int],
                       themeId   : Int,
                       bookId    : Int)

class ThemesToBooks(tag : Tag)
  extends Table[ThemeToBook](tag, "THEMES_TO_BOOKS") {

  def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def themeId: Column[Int] = column[Int]("THEME_ID", O.NotNull)

  def bookId = column[Int]("BOOK_ID", O.NotNull)

  def * : ProvenShape[ThemeToBook] = (id?, themeId, bookId) <> (ThemeToBook.tupled, ThemeToBook.unapply)

  def book: ForeignKeyQuery[Books, Book] =
    foreignKey("THEMES_TO_BOOKS__BOOKS_FK", bookId, TableQuery[Books])(_.id)

  def theme: ForeignKeyQuery[Themes, Theme] =
    foreignKey("THEMES_TO_BOOKS__THEMES_FK", themeId, TableQuery[Themes])(_.id)
}

//-----------------------------------------------------------------------------------------------------------

case class ThemeGroup(id             : Option[Int],
                      themeGroupName : String)

class ThemeGroups(tag : Tag)
  extends Table[ThemeGroup](tag, "THEME_GROUPS") {

  def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def themeGroupName : Column[String] = column[String]("THEME_GROUP_NAME",  O.NotNull)

  def * : ProvenShape[ThemeGroup] = (id?, themeGroupName) <> (ThemeGroup.tupled, ThemeGroup.unapply)
}
//-----------------------------------------------------------------------------------------------------------

case class ThemeToThemeGroup(id              : Option[Int],
                             themeId         : Int,
                             themeGroupId    : Int)

class ThemesToThemeGroups(tag : Tag)
  extends Table[ThemeToThemeGroup](tag, "THEMES_TO_THEMEGROUPS") {

  def id: Column[Int] = column[Int]("ID", O.PrimaryKey, O.AutoInc)

  def themeId      : Column[Int] = column[Int]("THEME_ID", O.NotNull)

  def themeGroupId : Column[Int] = column[Int]("THEME_GROUP_ID", O.NotNull)

  def * : ProvenShape[ThemeToThemeGroup] = (id?, themeId, themeGroupId) <> (ThemeToThemeGroup.tupled, ThemeToThemeGroup.unapply)

  def rowIndex = index("THEMES_TO_THEMEGROUPS_INDEX", (themeId, themeGroupId), unique = true)

  def themes: ForeignKeyQuery[Themes, Theme] =
    foreignKey("THEMES_TO_THEMEGROUPS__THEMES_FK", themeId, TableQuery[Themes])(_.id)

  def themeGroups: ForeignKeyQuery[ThemeGroups, ThemeGroup] =
    foreignKey("THEMES_TO_THEMEGROUPS__THEME_GROUPS_FK", themeGroupId, TableQuery[ThemeGroups])(_.id)
}
