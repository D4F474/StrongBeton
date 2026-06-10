import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClanEmptyState } from './clan-empty-state';

describe('ClanEmptyState', () => {
  let component: ClanEmptyState;
  let fixture: ComponentFixture<ClanEmptyState>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClanEmptyState]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClanEmptyState);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
