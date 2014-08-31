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
    , "enterBookId"               -> "Введите номер книги"
    , "bookID"                    -> "Номер книги"
    , "enterBookID"               -> "Введите номер книги"
    , "bookIsBound"               -> "Книга зарегистрирована на вас"
    , "bookInfo"                  -> "Информация о книге"
    , "bookReturned:"             -> "Книга была возвращена, ее номер: "

    // actions
    , "actions.take"               -> "Взять книгу"
    , "actions.putback"            -> "Вернуть книгу на место"
    , "actions.library"            -> "Библиотека"
    , "actions.administration"     -> "Администрирование"
    , "actions.take"               -> "Взять книгу"
    , "actions.home"               -> "Домой"

    // errors
    , "errors.personMustExist"     -> "Нет такого пользователя"
    , "errors.bookMustExist"       -> "Нет такой книги"

    // themes
    , "themes.resultOriented"                      -> "Ориентация на результат"
    , "themes.leadership"                          -> "Лидерство"
    , "themes.communicationAndInfluence"           -> "Коммуникация и влияние"
    , "themes.coworking"                           -> "Сотрудничество"
    , "themes.selfEducation"                       -> "Саморазвитие"
    , "themes.planningAndOrganization"             -> "Планирование и организация"
    , "themes.developmentOfSubordinates"           -> "Развитие подчиненных"
    , "themes.problemAnalysisAndSolutionFinding"   -> "Анализ проблем и выработка решений"
    , "themes.innovativeness"                      -> "Инновативность"

    , "themes.HRDepartment"                        -> "Отдел по работе с персоналом"
    , "themes.ITDepartment"                        -> "ИТ отдел"
    , "themes.logicticsAndPurchasingDepartment"    -> "Отдел логистики и закупок"
    , "themes.financialDepartment"                 -> "Финансовый отдел"
    , "themes.legalDepartment"                     -> "Юридический отдел"
    , "themes.stampsDepartment"                    -> "Отдел МарКом"
    , "themes.productionDepartment"                -> "Производственный отдел"
    , "themes.salesDepartment"                     -> "Отдел продаж"

    , "themes.englishLanguage"                     -> "Английский язык"
    , "themes.finnishLanguage"                     -> "Финский язык"

    , "themes.skill1"                              -> "Навык 1"
    , "themes.skill2"                              -> "Навык 2"
    , "themes.skill3"                              -> "Навык 3"

    // theme groups
    , "themegrps.competences"                      -> "Компетенции"
    , "themegrps.professionalKnowledge"            -> "Профессиональные знания"
    , "themegrps.foreignLanguages"                 -> "Иностранные языки"
    , "themegrps.computerSkills"                   -> "Компьютерные навыки"

  )
  def tr(key : String) = ru.get(key).getOrElse("%%%" + key + "%%%")
}
