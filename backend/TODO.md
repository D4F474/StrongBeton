# StrongBeton TODO

## Critical before online

- [ ] Move database password, JWT secret, and Cloudinary credentials from `application.properties` to environment variables.
- [ ] Rotate the currently exposed JWT and Cloudinary secrets.
- [ ] Configure CORS for the real Angular domain.
- [ ] Replace user/requester ids from request params with `@AuthenticationPrincipal` where the action belongs to the current user.
- [ ] Add ownership checks for workout list/details/create/update/delete.
- [ ] Add ownership checks for sets create/update/delete.
- [ ] Add ownership check when updating feed posts.
- [ ] Remove or protect `/users/` because it returns `List<User>`.
- [ ] Remove `System.out.println` and `printStackTrace` from controllers/services.
- [ ] Replace generic exception messages returned to clients with safe API errors.

## Diploma cleanup

- [ ] Finish empty clan methods: `deleteClan`, `addPointsToClan`, `searchClans`, `getTopClans`, `getClanMembers`, `getMemberContributions`.
- [ ] Finish clan points and league calculation.
- [ ] Fix API naming style:
  - `/AccUpToCoach`
  - `/GetUser`
  - `/ListAllUsernames`
  - `/Injuries`
  - `/newExercise`
  - `/deleteSet`
  - `/deleteWorkout`
- [ ] Return DTOs instead of entities from public API endpoints.
- [ ] Add request validation for signup, login, workouts, sets, posts, comments, images, clans.
- [ ] Add basic README:
  - project idea
  - technologies
  - architecture
  - database overview
  - how to run backend
  - main API flows
- [ ] Add screenshots/diagrams for the diploma documentation.

## Logic fixes

- [ ] Prevent friend request to yourself.
- [ ] Prevent duplicate friend requests.
- [ ] Handle reverse friend request case.
- [ ] Return clear errors when friend username does not exist.
- [ ] Decide whether `/seeAllFriends/{username}` is public or should use only the current user.
- [ ] Add pagination to feed posts.
- [ ] Add like/comment counts to feed DTO mapping.
- [ ] Make `getPhoto` return a plain DTO or URL, not `Optional`.
- [ ] Validate image type: `jpg`, `png`, `webp`.
- [ ] Validate image max size.
- [ ] Store Cloudinary `public_id` if old images should be deleted/replaced properly.
- [ ] Fix `FeedPostCommentRepository.deleteByFeedPost`, because it currently deletes `FeedPostLike`.
- [ ] Fix `ExercisesServiceImpl.findById`, because it maps an `Optional` directly.
- [ ] Replace `return null` flows with explicit errors or empty DTO responses.

## Database cleanup

- [ ] Add unique constraint on `user.email`.
- [ ] Add unique constraint on `user.username`.
- [ ] Add unique constraint on `friendship(user_id, friend_id)`.
- [ ] Add unique constraint on `feed_post_like(user_id, post_id)`.
- [ ] Add unique constraint on `clan_members(clan_id, user_id)`.
- [ ] Check `clan.logo_url`; it looks like it should be string URL or photo relation, not `INT`.
- [ ] Change clan XP/points fields to suitable numeric types.
- [ ] Use consistent timestamp column naming: `created_at`, not mixed `create_at`.
- [ ] Add useful indexes for feed, workouts, friends, clans.

## Java/Spring polish

- [ ] Use `@Enumerated(EnumType.STRING)` for enum fields that are stored as readable values.
- [ ] Use custom exceptions for common cases:
  - `ResourceNotFoundException`
  - `ForbiddenActionException`
  - `DuplicateResourceException`
  - `InvalidRequestException`
- [ ] Centralize exception handling in `GlobalExceptionHandler`.
- [ ] Replace raw `IllegalStateException` and `RuntimeException` where possible.
- [ ] Prefer explicit mapper methods for complex DTOs instead of relying only on `ModelMapper`.
- [ ] Make service dependencies `private final`.
- [ ] Remove unused imports and old commented debug code.
- [ ] Add transactions consistently around modifying service methods.

## Tests

- [ ] Auth tests:
  - signup
  - login
  - wrong credentials
  - duplicate email/username
- [ ] Friend tests:
  - send request
  - accept request
  - decline request
  - remove friend
  - duplicate/self request prevention
- [ ] Feed tests:
  - create post
  - update only own post
  - delete only own post
  - like/unlike
  - comment
- [ ] Workout tests:
  - user can access own workouts
  - user cannot access another user's workouts
  - active coach can access client workouts
  - inactive coach cannot access client workouts
- [ ] Image tests:
  - upload valid image
  - reject missing file
  - reject invalid type
- [ ] Clan tests:
  - create clan
  - join clan
  - invite member
  - accept/decline invite
  - promote/demote
  - transfer leadership
  - kick permissions

## Deployment

- [ ] Choose backend hosting.
- [ ] Set production MySQL database.
- [ ] Configure environment variables on hosting.
- [ ] Configure HTTPS.
- [ ] Connect Angular frontend domain.
- [ ] Add database backup strategy.
- [ ] Add basic logging/monitoring.
- [ ] Add a production profile: `application-prod.properties`.
- [ ] Add a dev profile: `application-dev.properties`.
