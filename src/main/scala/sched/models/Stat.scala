package sched.models

/**
  * Created by cleclair on 2018-09-17.
  */
case class Stat(id: String, name: String = "", position: String = "", value: Option[Int] = None) {

  def ++(stat: Stat): Stat = {
    if(value.isEmpty || stat.value.isEmpty) throw new Exception("Cannot Add stats with no value.")
    if(id != stat.id) throw new Exception("Can only add stats that have the same ID.")
    Stat(id, name, position, Some(value.getOrElse(0) + stat.value.getOrElse(0)))
  }

  def addName(name: String): Stat = {
    Stat(this.id, name, this.position, this.value)
  }

  def addPosition(position: String) = {
    Stat(this.id, this.name, position, this.value)
  }

  def isPlayerStat(): Boolean = {
   this.position == "P"
  }
}
