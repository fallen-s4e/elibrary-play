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
}
