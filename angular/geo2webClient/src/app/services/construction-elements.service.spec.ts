import { TestBed } from '@angular/core/testing';

import { ConstructionElementsService } from './construction-elements.service';

describe('ConstructionElementsService', () => {
  let service: ConstructionElementsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConstructionElementsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
