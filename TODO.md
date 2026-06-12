# StrongBeton Beta Polish TODO

## P0 - Before Wider Beta

- [ ] Add all required production database views to the Railway/init migration flow:
  - `workout_with_tonnage`
  - `show_friend_list_view`
  - any stats/leaderboard views still used by repositories
- [ ] Add ownership checks for workout mutations:
  - add workout detail
  - add/update/delete set
  - delete workout detail
  - delete workout
- [ ] Add validation to feed endpoints:
  - reject empty content
  - reject invalid post type
  - return `400` instead of `500`
- [ ] Decide feed visibility:
  - friends-only feed
  - public beta feed
  - hybrid: friends feed plus discover/public tab
- [ ] Prevent editing/deleting another user's feed post.

## P1 - Product Polish

- [ ] Auto-create a `WORKOUT` feed post when a workout is finished.
- [ ] Store `workout_id` on workout feed posts so the post can link to the workout summary.
- [ ] Add a clean workout summary card in feed:
  - workout name
  - total tonnage
  - exercises count
  - score
  - date
- [ ] Show better empty states:
  - no friends
  - no posts
  - no active workout
  - no stats yet
- [ ] Replace broken mojibake text in Bulgarian UI strings.
- [ ] Add user-facing success/error messages for workout finish, set save, post create, and profile image upload.

## P2 - Reliability

- [ ] Add a backend test profile with H2 or test MySQL settings.
- [ ] Make `mvnw test` pass without relying on local `.env`.
- [ ] Fix Angular component tests by mocking services instead of making real HTTP calls.
- [ ] Add focused tests for:
  - finish workout
  - set scoring
  - feed load
  - feed create/delete
  - friend-only visibility
- [ ] Replace `System.out.println` debug logs with proper logger calls.
- [ ] Add basic backend health endpoint for production checks.

## P3 - Deploy And Maintenance

- [ ] Move frontend API URL out of hardcoded interceptor value and into environment/config.
- [ ] Update README with the current beta setup, not the old project setup.
- [ ] Document Railway DB migration steps.
- [ ] Keep `.env` out of git and rotate any secret that was exposed during development.
- [ ] Decide whether `frontend-old` should stay in the repo or be archived separately.
- [ ] Add a small beta checklist:
  - register
  - login
  - create workout
  - add exercise
  - log set
  - finish workout
  - see stats
  - create feed post
  - see friend feed
  - upload profile image

## Nice To Have

- [ ] Add pagination or infinite scroll to feed.
- [ ] Add delete confirmation modal instead of browser confirm.
- [ ] Add loading skeletons consistently across pages.
- [ ] Add public changelog for beta testers.
- [ ] Add feedback link or simple bug report form.
