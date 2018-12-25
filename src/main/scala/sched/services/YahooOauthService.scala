package sched.services

import com.github.scribejava.core.builder.ServiceBuilder
import com.github.scribejava.core.model._
import com.github.scribejava.core.oauth.OAuth20Service
import sched.models._
import sched.util.YahooRoutes

import scala.collection.immutable
import scala.xml.XML

/**
  * Created by cleclair on 2018-08-24.
  */
class YahooOauthService(val service: OAuth20Service) {

  def this() = this(YahooOauthService.oAuthService)

  def url() = service.getAuthorizationUrl

  def makeRequest(verb: Verb, url: String, auth2AccessToken: OAuth2AccessToken): Response = {
    val request = new OAuthRequest(Verb.GET, url, service)
    signRequest(auth2AccessToken, request)
    request.send()
  }

  private def signRequest(accessToken: OAuth2AccessToken, request: AbstractRequest): Unit = {
    request.addHeader("Authorization", "Bearer " + accessToken.getAccessToken)
  }
}

object YahooOauthService {

  val key = System.getenv("YAHOO_KEY")
  val secret = System.getenv("YAHOO_SECRET")
  val callbackURL = "oob"
  val RESPONSE_TYPE = "code"

  val oAuthService: OAuth20Service = new ServiceBuilder().callback(callbackURL)
      .apiKey(key)
      .apiSecret(secret)
      .responseType(RESPONSE_TYPE)
      .build(YahooApi20.instance())

  def initService(): YahooOauthService = {
    val oAuthService: OAuth20Service  = new ServiceBuilder().callback(callbackURL)
        .apiKey(key)
        .apiSecret(secret)
        .responseType(RESPONSE_TYPE)
        .build(YahooApi20.instance())
    new YahooOauthService(oAuthService)
  }

  def getTeams(auth2AccessToken: OAuth2AccessToken): IndexedSeq[Team] = {
    val yahooRequest = new YahooOauthService().makeRequest(Verb.GET, YahooRoutes.usersTeamsResource, auth2AccessToken)
    //todo handle error graciously
    val responseFromAPI = XML.loadString(yahooRequest.getBody)
    val usersTeams = (responseFromAPI \\ "team").map((node) => {
      Team(
        (node \ "team_key").text,
        (node \ "name").text,
        (node \\ "team_logo" \ "url").text,
        (node \ "team_key").text.substring(0, 10)
      )
    })
    usersTeams.toIndexedSeq
  }

  def getAvailablePlayersPerLeague(leagueID: String, auth2AccessToken: OAuth2AccessToken): IndexedSeq[Player] = {
    val request = new YahooOauthService().makeRequest(Verb.GET, YahooRoutes.League.PLAYERS.replace(":id", leagueID), auth2AccessToken)
    val availablePlayers = (XML.loadString(request.getBody) \\ "player").map((node) => {
      Player(
        (node \ "player_key").text,
        (node \ "name" \ "full").text,
        (node \ "editorial_team_full_name").text,
        (node \ "editorial_team_full_name_abbr").text,
        (node \ "eligible_positions").map(_.text).mkString(",")
      )
    })
    availablePlayers.toIndexedSeq
  }

  def getPlayersOnTeam(teamID: String, auth2AccessToken: OAuth2AccessToken): IndexedSeq[Player] = {
    val request = new YahooOauthService().makeRequest(Verb.GET, YahooRoutes.Team.PLAYERS.replace(":id", teamID), auth2AccessToken)
    val availablePlayers = (XML.loadString(request.getBody) \\ "player").map((node) => {
      Player(
        (node \ "player_key").text,
        (node \ "name" \ "full").text,
        (node \ "editorial_team_full_name").text,
        (node \ "editorial_team_full_name_abbr").text,
        (node \ "eligible_positions").map(_.text).mkString(",")
      )
    })
    availablePlayers.toIndexedSeq
  }

  def getNHLStatsCategories(auth2AccessToken: OAuth2AccessToken): IndexedSeq[Stat] = {
    val request = new YahooOauthService().makeRequest(Verb.GET, YahooRoutes.Game.STATS, auth2AccessToken)
    val availableStats: IndexedSeq[Stat] = (XML.loadString(request.getBody) \\ "stat").map((node) => {
      Stat(
        (node \ "stat_id").text,
        (node \ "name").text
      )
    }).toIndexedSeq
    availableStats ++ IndexedSeq()
  }

  def getStatsPerLeague(leagueId: String, auth2AccessToken: OAuth2AccessToken): IndexedSeq[Stat] = {
    val request = new YahooOauthService().makeRequest(Verb.GET, YahooRoutes.League.SETTINGS.replace(":id", leagueId), auth2AccessToken)
    val leagueStats: IndexedSeq[Stat] = (XML.loadString(request.getBody) \\ "stat").map((node) => {
      Stat(
        (node \ "stat_id").text,
        (node \ "name").text,
        (node \ "position_type").text
      )
    }).toIndexedSeq.filter(_.position == "P")
    leagueStats
  }

  def getTop300PlayersInLeague(leagueId: String, auth2AccessToken: OAuth2AccessToken): IndexedSeq[Player] = {
    val playersFromLeague: IndexedSeq[Player] = IndexedSeq("0", "25", "50", "75", "100", "125", "150", "175", "200", "225", "250", "275", "300").flatMap((start_number) => {
      //    IndexedSeq("0", "25").flatMap((start_number) => {
      val request = new YahooOauthService().makeRequest(
        Verb.GET,
        YahooRoutes.League.PLAYERS_PAGINATED.replace(":id", leagueId).replace(":start", start_number),
        auth2AccessToken
      )
      val players: IndexedSeq[Player] = (XML.loadString(request.getBody) \\ "player").map(
        (node) => {
          Player(
            (node \ "player_key").text,
            (node \ "name" \ "full").text,
            (node \ "editorial_team_full_name").text,
            (node \ "editorial_team_full_name_abbr").text,
            (node \ "eligible_positions").map(_.text.trim()).mkString(",")
          )
        }
      ).toIndexedSeq.filter(!_.position.toLowerCase.contains("g"))
      players
    })
    val nameToRankedPlayerMap: Map[String, Player] = CSVReaderService.getPlayerRankings("/Users/cleclair/workspaces/scheduler/2018PlayerRatings.csv")
    val playersPlusPredictions = playersFromLeague.map((player: Player) => {
      val rankedPlayerObject: Player = nameToRankedPlayerMap.getOrElse(player.name.toLowerCase, player)
      val fullPlayer = player.addStats(rankedPlayerObject.stats).addAdp(rankedPlayerObject.adp).addRank(rankedPlayerObject.rank)
      fullPlayer
    })
    playersPlusPredictions.sortBy((player) => if(player.rank=="") 600 else player.rank.toInt)
  }

  def getTeamsInLeague(leagueId: String, auth2AccessToken: OAuth2AccessToken): IndexedSeq[Team] = {
    val request = new YahooOauthService().makeRequest(
      Verb.GET,
      YahooRoutes.League.TEAMS.replace(":id", leagueId),
      auth2AccessToken
    )
    val teams: IndexedSeq[Team] = (XML.loadString(request.getBody) \\ "team").map((node) => {
      Team(
        (node \ "team_key").text,
        (node \ "name").text,
        (node \\ "team_logo" \ "url").text,
        (node \ "team_key").text.substring(0, 10)
      )
    }).toIndexedSeq
    teams
  }

  def getTeamStatsInASeason(teamId: String, auth2AccessToken: OAuth2AccessToken): IndexedSeq[Stat] = {
    val request = new YahooOauthService().makeRequest(
      Verb.GET,
      YahooRoutes.Team.STAT.replace(":id", teamId),
      auth2AccessToken
    )
    val seasonStats: IndexedSeq[Stat] = (XML.loadString(request.getBody) \\ "stat")
        .map((node) => {
      Stat(
        (node \ "stat_id").text,
        "",
        "",
        toInt((node \ "value").text)
      )
    }).toIndexedSeq.filter(_.value.nonEmpty)
    seasonStats
  }

  def getSeasonReport(leagueId: String, auth2AccessToken: OAuth2AccessToken): SeasonReport = {
    val teams = getTeamsInLeague(leagueId, auth2AccessToken)
    val leagueStats: IndexedSeq[Stat] = getStatsPerLeague(leagueId, auth2AccessToken)
    val totalStats = teams.map((team) => {
      getTeamStatsInASeason(team.id, auth2AccessToken)
    })
    println("League Stats" + leagueStats.toString())
    println("Total Stats" + totalStats.toString())
    SeasonReport.generateSeasonReport(leagueStats, totalStats)
  }

  def getDraftResults(leagueId: String, auth2AccessToken: OAuth2AccessToken): IndexedSeq[DraftPick] = {
    val request = new YahooOauthService().makeRequest(
      Verb.GET,
      YahooRoutes.League.DRAFT.replace(":id", leagueId),
      auth2AccessToken
    )
    val draftPicks: IndexedSeq[DraftPick] = (XML.loadString(request.getBody) \\ "draft_result").map((node) => {
      DraftPick(
        ((node) \ "pick").text.toInt,
        ((node) \ "round").text.toInt,
        ((node) \ "team_key").text,
        ((node) \ "player_key").text
      )
    }).toIndexedSeq
    draftPicks
  }

  case class TeamScore(team_key: String, name: String, score: Double, league: String) {
    def addScore(score: Double): TeamScore = {
      TeamScore(team_key, name, this.score + score, league)
    }

    def formatScore(): TeamScore = {
      val formattedScore = (score * 100).toInt / 100.0
      TeamScore(team_key, name, formattedScore, league)
    }
  }

  def getKKUPFLStandings(auth2AccessToken: OAuth2AccessToken): Seq[TeamScore] = {
    YahooRoutes.Constants.KKUPFLLEAGUEIDS.flatMap((leagueId: String) => {
    val standingsRequestBody = new YahooOauthService().makeRequest(
        Verb.GET,
        YahooRoutes.League.KKUPFL_STANDINGS.replace("{id}", leagueId),
        auth2AccessToken
      ).getBody
      val currentWeekRequestBody = new YahooOauthService().makeRequest(
        Verb.GET,
        YahooRoutes.League.KKUPFL_CURRENT_WEEK.replace("{id}", leagueId),
        auth2AccessToken
      ).getBody
      val leagueName = (XML.loadString(standingsRequestBody) \\ "league" \ "name").text
      val teams = (XML.loadString(standingsRequestBody) \\ "team").map((node) => {
        TeamScore(
          (node \ "team_key").text,
          (node \ "name").text,
          (node \ "team_points" \ "total").text.toFloat,
          leagueName
        )
      })
      val currentWeekScoreMap: Map[String, Double] = (XML.loadString(currentWeekRequestBody) \\ "matchup").flatMap((node) => {
        (node \\ "team").map((innerNode) => {
          val key: String = (innerNode \ "team_key").text
          val currentWeekTotal: Double = (innerNode \ "team_points" \ "total").text.toFloat
          key -> currentWeekTotal
        }).toMap
      }).toMap
      teams.map((team) => team.addScore(currentWeekScoreMap(team.team_key)).formatScore())
    }).sortBy(_.score * -1)
  }

  def toInt(s: String): Option[Int] = {
    try {
      Some(s.toInt)
    } catch {
      case e: Exception => None
    }
  }
}