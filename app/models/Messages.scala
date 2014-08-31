package models

/**
 * @author magzhan.karasayev
 * @since 8/23/14 7:41 PM
 */
object Messages {
  val ru = Map(
     // simple strings
      "libraryTitle"              -> "Электронная библятека"
    , "selectAction"              -> "Выберите действие"
    , "next"                      -> "Далее"
    , "prev"                      -> "Назад"
    , "personFIO"                 -> "Имя пользователя"
    , "enterPersonFIO"            -> "Введите имя пользователя"
    , "bookID"                    -> "Номер книги"
    , "enterBookID"               -> "Введите номер книги"
    , "bookIsBound"               -> "Книга зарегистрирована на вас"

    // actions
    , "actions.take"               -> "Взять книгу"
    , "actions.putback"            -> "Вернуть книгу на место"
    , "actions.library"            -> "Библиотека"
    , "actions.administration"     -> "Администрирование"
    , "actions.take"               -> "Взять книгу"

    // errors
    , "errors.personMustExist"     -> "Нет такого пользователя"
    , "errors.bookMustExist"       -> "Нет такой книги"
  )
  def tr(key : String) = ru.get(key).getOrElse(key)
}
