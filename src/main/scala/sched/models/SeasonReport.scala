package sched.models

/**
  * Created by cleclair on 2018-09-18.
  */
object SeasonReport {

  def generateSeasonReport(categories: IndexedSeq[Stat], categoriesWithValue: IndexedSeq[IndexedSeq[Stat]]): SeasonReport = {

    val idToNameMap = categories.map((stat) => (stat.id, stat)).toMap
    val statsWithMap: IndexedSeq[IndexedSeq[Stat]] = categoriesWithValue.map((setsOfStats: IndexedSeq[Stat]) => setsOfStats.filter((stat) => idToNameMap.get(stat.id).nonEmpty))
    val categoriesWithNameAndValue = statsWithMap.map((setOfStats) => {
      setOfStats.map((stat) => stat.addName(idToNameMap(stat.id).name).addPosition(idToNameMap(stat.id).position))
    })

    val groupedCategories: Map[String, IndexedSeq[Stat]] = categoriesWithNameAndValue.flatten.groupBy(_.id)
    val maxStats: IndexedSeq[Stat] = groupedCategories.values.map((stats) => stats.maxBy(_.value.get)).toIndexedSeq
    val minStats: IndexedSeq[Stat] = groupedCategories.values.map((stats) => stats.minBy(_.value.get)).toIndexedSeq
    val averageStats: IndexedSeq[Stat] = groupedCategories.values.map((stats: IndexedSeq[Stat]) => {
      val average = stats.foldLeft(0)((x: Int, y: Stat) => x + y.value.get) / stats.length
      val firstItem = stats.head
      Stat(firstItem.id, firstItem.name, firstItem.position, Some(average))
    }).toIndexedSeq
    SeasonReport(
      maxStats.filter(_.isPlayerStat()).sortBy(_.id.toInt),
      minStats.filter(_.isPlayerStat()).sortBy(_.id.toInt),
      averageStats.filter(_.isPlayerStat()).sortBy(_.id.toInt)
    )
  }
}

case class SeasonReport(maxStats: IndexedSeq[Stat], minStats: IndexedSeq[Stat], avgStats: IndexedSeq[Stat])
