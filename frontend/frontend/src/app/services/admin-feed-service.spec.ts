import { TestBed } from '@angular/core/testing';

import { AdminFeedService } from './admin-feed-service';

describe('AdminFeedService', () => {
  let service: AdminFeedService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AdminFeedService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
