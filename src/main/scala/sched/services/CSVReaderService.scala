package sched.services

import sched.models.{Player, Stat}

import scala.io.Source

/**
  * Created by cleclair on 2018-09-21.
  */
object CSVReaderService {

  def getPlayerRankings(fileLocation: String): Map[String, Player] = {

    val bufferedSource = Source.fromFile(fileLocation, "UTF-8")
    val allLines = bufferedSource.getLines().toIndexedSeq
    val header = allLines.head
    val players = allLines.tail
    val headerMap: Map[Int, String] = header.split(",").drop(4).zipWithIndex.map((tuple) => (tuple._2, tuple._1)).toMap

    players.filter((row) => !row.split(",")(0).toLowerCase().contains("g")).zipWithIndex.map {
      case (playerRow: String, index: Int) => {
        val playerColumns = playerRow.split(",")
        val playerName = playerColumns(1)
        val teamAbbr = playerColumns(2)
        val adp = playerColumns(3)
        val stats = playerColumns.drop(5).zipWithIndex.map((valueIndexTuple) => {
          Stat(
            "",
            headerMap(valueIndexTuple._2),
            "P",
            Some(valueIndexTuple._1.toDouble.toInt)
          )
        })
        Player(
          "",
          playerName,
          "",
          teamAbbr,
          "",
          index.toString,
          adp,
          stats
          )
      }
    }.map((player) => (player.name.toLowerCase, player)).toMap
  }
}