package sched.util

object YahooRoutes {

  val playersFromTeam = "https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasysports.games%20where%20use_login%3D1%20and%20game_key%20in%20('nhl')&format=json"
  val playersFromLeague = "https://fantasysports.yahooapis.com/fantasy/v2/league/363.l.63462/players?sort=AR&format=json&count=25&start={start}"
  val playersFromLeague10 = "https://fantasysports.yahooapis.com/fantasy/v2/league/363.l.63462/players?sort=AR&format=json&count=25"
  val playersFromLeagueSubId = "https://fantasysports.yahooapis.com/fantasy/v2/league/{id}/players?sort=AR&format=json&count=25&start={start}"
  val usersTeams = "https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasysports.teams%20where%20game_key%3D%22352%22%20and%20use_login%3D1&format=json&diagnostics=true&callback="
  val gamesFromUser = "https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasysports.games%20where%20use_login%3D1%20and%20game_key%20in%20('nhl')&format=json"
  val usersRoster = "https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasysports.teams.roster%20where%20use_login%3D1%20and%20game_key%3D363&format=json&diagnostics=true"
  val draftResults = "https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasysports.draftresults%20where%20league_key%3D'363.l.63462'&format=json&diagnostics=false"
  val playersFromTeamReplaceId = "https://fantasysports.yahooapis.com/fantasy/v2/team/:id/players?format=json"
  val usersGamesResourceYQL = "https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasysports.games%20where%20game_key%20in%20('nhl')&format=json&diagnostics=true&callback="
  val usersLeaguesResourceYQL = "https://query.yahooapis.com/v1/yql?q=select%20*%20from%20fantasysports.teams%20where%20use_login%3D1%20and%20game_key%20in%20({game_ids})&format=json"

  //  val usersLeaguesResource = "https://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games;game_keys=nhl/teams&format=json"
  val usersGamesResource = "https://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/games;is_available=1?format=json"
  val usersTeamsResource = "https://fantasysports.yahooapis.com/fantasy/v2/users;use_login=1/teams"
  val leaguesResource = "https://fantasysports.yahooapis.com/fantasy/v2/leagues;league_keys={keys}?format=json"
  val draftsFromLeague = "https://fantasysports.yahooapis.com/fantasy/v2/league/{key}/draftresults?format=json"

  val teamStatsURL = "https://fantasysports.yahooapis.com/fantasy/v2/team/363.l.11176.t.12/stats"

  object League {
    val PLAYERS = "https://fantasysports.yahooapis.com/fantasy/v2/league/:id/players?sort=AR"
    val PLAYERS_PAGINATED = "https://fantasysports.yahooapis.com/fantasy/v2/league/:id/players?sort=AR&count=25&start=:start"
    val SETTINGS = "https://fantasysports.yahooapis.com/fantasy/v2/league/:id/settings"
    val TEAMS = "https://fantasysports.yahooapis.com/fantasy/v2/league/:id/teams"
    val DRAFT = "https://fantasysports.yahooapis.com/fantasy/v2/league/:id/draftresults"
    val KKUPFL_STANDINGS = "https://fantasysports.yahooapis.com/fantasy/v2/leagues;league_keys=386.l.{id}/standings"
    val KKUPFL_CURRENT_WEEK = "https://fantasysports.yahooapis.com/fantasy/v2/leagues;league_keys=386.l.{id}/scoreboard"
  }

  object Game {
    val STATS = "https://fantasysports.yahooapis.com/fantasy/v2/game/nhl/stat_categories"
  }

  object Team {
    val PLAYERS = "https://fantasysports.yahooapis.com/fantasy/v2/team/:id/roster"
    val STAT = "https://fantasysports.yahooapis.com/fantasy/v2/team/:id/stats"
  }

  object Constants {
    val KKUPFLLEAGUEIDS = IndexedSeq(
      "2831", "30797", "30814",
      "30823", "30828", "30826",
      "30844", "31472", "31477",
      "31479", "31486", "31490",
      "31496", "31499", "31500",
      "82453"
    )
  }

}
