import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowWorkoutDialogComponent } from './show-workout-dialog.component';

describe('ShowWorkoutDialogComponent', () => {
  let component: ShowWorkoutDialogComponent;
  let fixture: ComponentFixture<ShowWorkoutDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ShowWorkoutDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowWorkoutDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
