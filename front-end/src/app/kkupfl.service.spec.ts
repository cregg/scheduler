import { TestBed, inject } from '@angular/core/testing';

import { KkupflService } from './kkupfl.service';

describe('KkupflService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [KkupflService]
    });
  });

  it('should be created', inject([KkupflService], (service: KkupflService) => {
    expect(service).toBeTruthy();
  }));
});
