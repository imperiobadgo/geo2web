import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AlgebraInputComponent } from './algebra-input.component';

describe('AlgebraInputComponent', () => {
  let component: AlgebraInputComponent;
  let fixture: ComponentFixture<AlgebraInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AlgebraInputComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AlgebraInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
