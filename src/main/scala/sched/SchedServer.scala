package sched

import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.github.scribejava.core.model.{OAuth2AccessToken, Verb}
import io.javalin.{Context, Handler, Javalin}
import io.javalin.core.util.Header
import io.javalin.json.JavalinJackson
import sched.models.{SeasonReport, Team}
import sched.services.YahooOauthService
import sched.util.YahooRoutes

/**
  * Created by cleclair on 2018-08-23.
  */
case class PostFilterHandler() extends Handler {

  override def handle(ctx: Context): Unit = {
    ctx.header(Header.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")
  }

}


object SchedServer {

  implicit def string2Oauth2Token(auth_token: String): OAuth2AccessToken = {
    new OAuth2AccessToken(auth_token)
  }

  def main(args: Array[String]): Unit = {
    import com.fasterxml.jackson.databind.ObjectMapper
    val objectMapper = new ObjectMapper
    objectMapper.registerModule(DefaultScalaModule)
    JavalinJackson.configure(objectMapper)
    val javalinApp: Javalin = Javalin.create().enableCorsForOrigin("*").start(7000)

    javalinApp.after("*", PostFilterHandler())

    javalinApp.get("/", (ctx) => {
      val yahooService = YahooOauthService.initService()
      ctx.redirect(yahooService.url())
    })
    javalinApp.get("/callback", (ctx) => {
      val yahooService = YahooOauthService.initService()
      val token: OAuth2AccessToken = yahooService.service.getAccessToken(ctx.queryParam("oauth_code"))
      ctx.cookie("auth_token", token.getAccessToken)
      ctx.redirect("http://localhost:4200")
    })
    javalinApp.before((ctx) => {
      val auth_token = ctx.cookie("auth_token")
      ctx.attribute("auth_token", auth_token)
    })
    javalinApp.get("/teams", (ctx) => {
      ctx.result(YahooOauthService.getTeams(new OAuth2AccessToken(ctx.attribute("auth_token"))).toString)
    })
    javalinApp.get("/leagues/:id/players", (ctx) => {
      ctx.json(YahooOauthService.getAvailablePlayersPerLeague(
        ctx.pathParam("id"),
        new OAuth2AccessToken(ctx.attribute("auth_token"))).toString()
      )
    })
    javalinApp.get("/teams/:id/players", (ctx) => {
      ctx.result(YahooOauthService.getPlayersOnTeam(
        ctx.pathParam("id"),
        new OAuth2AccessToken(ctx.attribute("auth_token"))).toString()
      )
    })
    javalinApp.get("/stats/", (ctx) => {
      ctx.json(YahooOauthService.getNHLStatsCategories(
        new OAuth2AccessToken(ctx.attribute("auth_token")))
      )
    })
    javalinApp.get("/leagues/:id/stats", (ctx) => {
      ctx.json(YahooOauthService.getStatsPerLeague(
        ctx.pathParam("id"),
        new OAuth2AccessToken(ctx.attribute("auth_token")))
      )
    })

    javalinApp.get("/leagues/:id/allplayers", (ctx) => {
      val allPlayers = YahooOauthService.getTop300PlayersInLeague(
        ctx.pathParam("id"),
        new OAuth2AccessToken(ctx.attribute("auth_token"))
      )
      ctx.json(allPlayers)
    })

    javalinApp.get("/leagues/:id/allplayersmap", (ctx) => {
      ctx.json(YahooOauthService.getTop300PlayersInLeague(
        ctx.pathParam("id"),
        new OAuth2AccessToken(ctx.attribute("auth_token"))).map((player) => (player.id, player)).toMap
      )
    })

    javalinApp.get("/leagues/:id/teams", (ctx) => {
      val teams: IndexedSeq[Team] = YahooOauthService.getTeamsInLeague(
        ctx.pathParam("id"),
        new OAuth2AccessToken(ctx.attribute("auth_token"))
      )
      ctx.json(teams)
    })

    javalinApp.get("/leagues/:id/getreport", (ctx) => {
      val report: SeasonReport = YahooOauthService.getSeasonReport(
        ctx.pathParam(":id"),
        new OAuth2AccessToken(ctx.attribute("auth_token"))
      )
      ctx.json(report)
    })

    javalinApp.get("/leagues/:id/getdraft", (ctx) => {
      ctx.json(YahooOauthService.getDraftResults(
        ctx.pathParam(":id"),
        new OAuth2AccessToken(ctx.attribute("auth_token"))
      ))
    })

    javalinApp.get("/kkupfl", (ctx) => {
      val test = YahooOauthService.getKKUPFLStandings(new OAuth2AccessToken(ctx.attribute("auth_token")))
      ctx.json(test)
    })


    javalinApp.get("/test/:id", (ctx) => {
      //      ctx.json(YahooOauthService.getStatsPerLeague(
      //        ctx.pathParam("id"),
      //        new OAuth2AccessToken(ctx.attribute("auth_token")))
      //      )
      ctx.result(new YahooOauthService().makeRequest(Verb.GET, YahooRoutes.Team.STAT.replace(":id", ctx.pathParam("id")), new OAuth2AccessToken(ctx.attribute("auth_token"))).getBody)
    })

  }
}