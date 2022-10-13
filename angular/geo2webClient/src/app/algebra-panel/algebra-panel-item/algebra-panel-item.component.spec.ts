import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlgebraPanelItemComponent } from './algebra-panel-item.component';

describe('AlgebraPanelItemComponent', () => {
  let component: AlgebraPanelItemComponent;
  let fixture: ComponentFixture<AlgebraPanelItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AlgebraPanelItemComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlgebraPanelItemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
