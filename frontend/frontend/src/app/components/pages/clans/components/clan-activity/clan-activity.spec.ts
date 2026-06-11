import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClanActivity } from './clan-activity';

describe('ClanActivity', () => {
  let component: ClanActivity;
  let fixture: ComponentFixture<ClanActivity>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClanActivity]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClanActivity);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
