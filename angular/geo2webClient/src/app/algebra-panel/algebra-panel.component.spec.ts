import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlgebraPanelComponent } from './algebra-panel.component';

describe('AlgebraPanelComponent', () => {
  let component: AlgebraPanelComponent;
  let fixture: ComponentFixture<AlgebraPanelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AlgebraPanelComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlgebraPanelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
