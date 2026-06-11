import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClanOverview } from './clan-overview';

describe('ClanOverview', () => {
  let component: ClanOverview;
  let fixture: ComponentFixture<ClanOverview>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClanOverview]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClanOverview);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
