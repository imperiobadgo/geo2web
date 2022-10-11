import { TestBed } from '@angular/core/testing';

import { AlgebraPanelService } from './algebra-panel.service';

describe('AlgebraPanelService', () => {
  let service: AlgebraPanelService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlgebraPanelService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
