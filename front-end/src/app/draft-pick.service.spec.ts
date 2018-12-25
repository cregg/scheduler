import { TestBed, inject } from '@angular/core/testing';

import { DraftPickService } from './draft-pick.service';

describe('DraftPickService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DraftPickService]
    });
  });

  it('should be created', inject([DraftPickService], (service: DraftPickService) => {
    expect(service).toBeTruthy();
  }));
});
