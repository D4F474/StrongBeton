import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClanLeaderboard } from './clan-leaderboard';

describe('ClanLeaderboard', () => {
  let component: ClanLeaderboard;
  let fixture: ComponentFixture<ClanLeaderboard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClanLeaderboard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClanLeaderboard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
