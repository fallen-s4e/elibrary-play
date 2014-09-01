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

  private val compentencesThemes = List(
      "themes.resultOriented"
    , "themes.leadership"
    , "themes.communicationAndInfluence"
    , "themes.coworking"
    , "themes.selfEducation"
    , "themes.planningAndOrganization"
    , "themes.developmentOfSubordinates"
    , "themes.problemAnalysisAndSolutionFinding"
    , "themes.innovativeness"
  )

  private val professionalKnowledgeThemes = List(
      "themes.HRDepartment"
    , "themes.ITDepartment"
    , "themes.logicticsAndPurchasingDepartment"
    , "themes.financialDepartment"
    , "themes.legalDepartment"
    , "themes.stampsDepartment"
    , "themes.productionDepartment"
    , "themes.salesDepartment"
  )

  private val foreignLanguages = List(
      "themes.englishLanguage"
    , "themes.finnishLanguage"
  )

  private val computerSkills = List(
      "themes.skill1"
    , "themes.skill2"
    , "themes.skill3"
  )

  val themeGrpToThemes = Map(
      "themegrps.competences"              -> compentencesThemes
    , "themegrps.professionalKnowledge"    -> professionalKnowledgeThemes
    , "themegrps.foreignLanguages"         -> foreignLanguages
    , "themegrps.computerSkills"           -> computerSkills
  )

  val themes : List[Theme] = themeGrpToThemes.values
    .flatMap(x => x).zipWithIndex
    .map(x => Theme(Some(x._2 + 1), x._1))
    .toList
  val themeGroups          = themeGrpToThemes.keys
}
