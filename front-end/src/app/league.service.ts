import { Injectable } from '@angular/core';
import {Observable} from "rxjs/index";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class LeagueService {

  constructor(private http: HttpClient) { }

  public getTeams(leagueId: String): Observable<Object[]> {
      return this.http.get<Object[]>(`http://localhost:7000/leagues/${leagueId}/teams`, {withCredentials: true})
  }

  public getPlayers(leagueId: String): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:7000/leagues/${leagueId}/allplayers`, {withCredentials: true})
  }

  public getPlayersMap(leagueId: String): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:7000/leagues/${leagueId}/allplayersmap`, {withCredentials: true})
  }

  public getStats(leagueId: String): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:7000/leagues/${leagueId}/stats`, {withCredentials: true})
  }

  public getDrafts(leagueId: String): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:7000/leagues/${leagueId}/getdraft`, {withCredentials: true})
  }

  public getReport(leagueId: String): Observable<Object[]> {
    return this.http.get<Object[]>(`http://localhost:7000/leagues/${leagueId}/getreport`, {withCredentials: true})
  }
}
