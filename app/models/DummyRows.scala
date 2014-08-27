package models

/**
 * @author magzhan.karasayev
 * @since 8/27/14 11:52 AM
 */
object DummyRows {
  val persons = List(
    new Person(Some(1), "Иван", "Иванов", "Иванович"),
    new Person(Some(2), "Сидор", "Сидоров", "Сидорович"),
    new Person(Some(3), "Петр", "Петров", "Петрович")
  )

  val books = List(
    new Book(Some(1), "bookName1", "author1", "description1", "barcode1", "bookType1", None),
    new Book(Some(2), "bookName2", "author2", "description2", "barcode2", "bookType2", None),
    new Book(Some(3), "bookName3", "author3", "description3", "barcode3", "bookType3", None)
  )

  // <indexes_separated_by_underscore_1>$<indexes_separated_by_underscore_2>
  // indexes_separated_by_underscore_1 - corresponding ids of book which it belongs to
  // indexes_separated_by_underscore_2 - corresponding ids of themeGroups which it belongs to
  val themes = List(
    "theme1$1",
    "theme2$1",
    "theme1_2$2",
    "theme3$3"
  )

  val themeGroups = List(
    "themeGroup1",
    "themeGroup2",
    "themeGroup3",
    "themeGroup4"
  )
}
