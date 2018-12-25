import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import { HttpClientModule }    from '@angular/common/http';
import {AppComponent} from "./app.component";
import {PlayerTableComponent} from "./player-table/player-table.component";
import {LeagueService} from "./league.service";
import {Ng2OrderModule} from "ng2-order-pipe";
import { KkupflStandingsComponent } from './kkupfl-standings/kkupfl-standings.component';

@NgModule({
  declarations: [
    AppComponent,
    PlayerTableComponent,
    KkupflStandingsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    Ng2OrderModule
  ],
  providers: [ LeagueService ],
  bootstrap: [AppComponent]
})
export class AppModule { }
