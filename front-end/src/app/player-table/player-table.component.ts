import { Component, OnInit } from '@angular/core';
import {LeagueService} from "../league.service";
import {timer} from "rxjs/index";
import {switchMap} from "rxjs/internal/operators";

@Component({
  selector: 'player-table',
  templateUrl: './player-table.component.html',
  styleUrls: ['./player-table.component.css']
})
export class PlayerTableComponent implements OnInit {

  public teams: any[];
  public players: any[];
  public visiblePlayers: any[];
  public stats: any[];
  public draftPicks: any[];
  public playerMap: any;
  public report: any;
  public totals: string[];
  public playerObjectFromDraft: any[];

  constructor(private leagueService: LeagueService) { }

  ngOnInit() {
    this.leagueService.getTeams("386.l.20480").subscribe((teams: any[]) => {
      this.teams = teams;
      console.log(this.teams);
    });
    this.leagueService.getPlayers("386.l.20480").subscribe((players: any[]) => {
      this.players = players;
      console.log(this.players);
    });
    this.leagueService.getPlayersMap("386.l.20480").subscribe((playerMap: any) => {
      this.playerMap = playerMap;
      console.log(this.playerMap);
    });
    this.leagueService.getStats("386.l.20480").subscribe((stats: any[]) => {
      this.stats = stats;
      console.log(this.stats);
    });
    this.leagueService.getReport("386.l.20480").subscribe((report: any) => {
      this.report = report;
      console.log(this.report);
    });
    timer(0, 5000).pipe(
      switchMap(() => this.leagueService.getDrafts("386.l.20480"))
    ).subscribe((draftPicks: any[]) => {
      this.draftPicks = draftPicks;
      console.log(draftPicks);
      if(this.players !== undefined){
        const draftedPlayerIds: any[] = draftPicks.map((draftPick: any) => draftPick.playerKey);
        const playersMinusDrafted: any[] = this.players.filter((player: any) => draftedPlayerIds.includes(player.id) === false);
        this.players = playersMinusDrafted;
        this.visiblePlayers = playersMinusDrafted.slice(0, 50);
        this.getDraftResultsPerTeam(`386.l.20480.t.13`);
        this.getTotals(`386.l.20480.t.13`);
      }
    })
  }
  search(searchTerm: string) {
    this.visiblePlayers = this.players.filter((player: any) => player.name.includes(searchTerm));
  }

  getDraftResultsPerTeam(teamId: string): any[] {
    if(this.draftPicks === undefined || this.playerMap === undefined) {
      return [];
    }
    let playerObjectFromDraft = this.draftPicks.filter((pick: any) => pick.teamKey == teamId);
    let playerObjectFromDraft2 = playerObjectFromDraft.map((pick: any) => this.playerMap[pick.playerKey]);
    let playerObjectFromDraft3 = playerObjectFromDraft2.filter((player: any) => player !== undefined);
    console.log("playersObject");
    console.log(playerObjectFromDraft);
    this.playerObjectFromDraft = playerObjectFromDraft3
  }

  getTotals(teamId: string): string[] {
    let draftedPlayers = this.playerObjectFromDraft;
    if(this.draftPicks === undefined || this.playerMap === undefined) {
      return [];
    }
    // const reducer = (accumulator, currentValue) => accumulator + currentValue;
    // const arrayOfStats: any[] = [];
    // const lengthOfStats = draftedPlayers[0].stats.length;
    const listOfStats: any[][] = draftedPlayers.map((player: any) => player.stats);
    // const test = listOfStats.reduce((a, b) => a.map((x, i) => x + b[i].value));
    var result = [];
    for(var i = 0; i < listOfStats.length; i++){
      for(var j = 0; j < listOfStats[i].length; j++){
        result[j] = (result[j] || 0) + listOfStats[i][j].value;
      }
    }

    // for(i = 0; i < lengthOfStats; i++){
    //   for(j = 0)
    //   const sum = 0;
    //   listOfStats[i]
    // }
    this.totals = result.map((int) => "" + int);
  }
}
