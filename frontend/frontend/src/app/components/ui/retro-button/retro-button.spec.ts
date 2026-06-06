import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RetroButton } from './retro-button';

describe('RetroButton', () => {
  let component: RetroButton;
  let fixture: ComponentFixture<RetroButton>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RetroButton]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RetroButton);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
