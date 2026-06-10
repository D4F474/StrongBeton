import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClanMembers } from './clan-members';

describe('ClanMembers', () => {
  let component: ClanMembers;
  let fixture: ComponentFixture<ClanMembers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClanMembers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClanMembers);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
