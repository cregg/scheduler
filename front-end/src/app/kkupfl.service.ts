import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class KkupflService {

  constructor(private http: HttpClient) { }

  public retrieveKKUPFLStandings(): Observable<any[]> {
    return this.http.get<any[]>("http://localhost:7000/kkupfl", {withCredentials: true})
  }
}
