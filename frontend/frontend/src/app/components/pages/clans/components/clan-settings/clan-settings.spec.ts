import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClanSettings } from './clan-settings';

describe('ClanSettings', () => {
  let component: ClanSettings;
  let fixture: ComponentFixture<ClanSettings>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClanSettings]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ClanSettings);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
