package models

/**
 * @author magzhan.karasayev
 * @since 8/23/14 7:41 PM
 */
object Messages {
  def libraryTitle = "Электронная библятека"
  def selectAction = "Выберите действие:"
  def next = "Далее"

  object Actions {
    def take           = "Взять книгу"
    def putback        = "Вернуть книгу на место"
    def library        = "Библиотека"
    def administration = "Администрирование"
  }

  object Erorrs {
    def personMustExist = "Нет такого пользователя"
  }
}
