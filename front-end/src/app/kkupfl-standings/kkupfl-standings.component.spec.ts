import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KkupflStandingsComponent } from './kkupfl-standings.component';

describe('KkupflStandingsComponent', () => {
  let component: KkupflStandingsComponent;
  let fixture: ComponentFixture<KkupflStandingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KkupflStandingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KkupflStandingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
