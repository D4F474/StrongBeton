import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkoutSetControl } from './workout-set-control';

describe('WorkoutSetControl', () => {
  let component: WorkoutSetControl;
  let fixture: ComponentFixture<WorkoutSetControl>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkoutSetControl]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkoutSetControl);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
