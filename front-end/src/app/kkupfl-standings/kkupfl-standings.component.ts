import { Component, OnInit } from '@angular/core';
import {KkupflService} from "../kkupfl.service";

@Component({
  selector: 'app-kkupfl-standings',
  templateUrl: './kkupfl-standings.component.html',
  styleUrls: ['./kkupfl-standings.component.css']
})
export class KkupflStandingsComponent implements OnInit {

  public kkupflStandings: any[];

  constructor(private kkupflService: KkupflService) {
    kkupflService.retrieveKKUPFLStandings().subscribe(standings => this.kkupflStandings = standings);
  }

  ngOnInit() {
  }

}
