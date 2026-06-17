import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { feedModeratorGuardGuard } from './feed-moderator-guard-guard';

describe('feedModeratorGuardGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => feedModeratorGuardGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
