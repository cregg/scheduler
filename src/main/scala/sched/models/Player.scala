package sched.models

/**
  * Created by cleclair on 2018-08-29.
  */
case class Player(
    id: String,
    name: String,
    team_name: String,
    team_abbr: String,
    position: String,
    rank: String = "",
    adp: String = "",
    stats: IndexedSeq[Stat] = IndexedSeq()
) {

  def addStats(stats: IndexedSeq[Stat]): Player ={
    Player(
      this.id,
      this.name,
      this.team_name,
      this.team_abbr,
      this.position,
      this.rank,
      this.adp,
      stats
    )
  }

  def addAdp(adp: String): Player = {
    Player(
      this.id,
      this.name,
      this.team_name,
      this.team_abbr,
      this.position,
      this.rank,
      adp,
      this.stats
    )
  }

  def addRank(rank: String): Player = {
    Player(
      this.id,
      this.name,
      this.team_name,
      this.team_abbr,
      this.position,
      rank,
      this.adp,
      this.stats
    )
  }
}