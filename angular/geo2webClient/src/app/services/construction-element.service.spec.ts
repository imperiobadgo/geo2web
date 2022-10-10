import { TestBed } from '@angular/core/testing';

import { ConstructionElementService } from './construction-element.service';

describe('ConstructionElementService', () => {
  let service: ConstructionElementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConstructionElementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
