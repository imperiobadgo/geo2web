import { TestBed } from '@angular/core/testing';

import { ConstructionElementCommunicationService } from './construction-element-communication.service';

describe('ConstructionElementService', () => {
  let service: ConstructionElementCommunicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ConstructionElementCommunicationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
