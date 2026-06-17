-- Exercise image URLs for Angular assets.
-- Put the image files in: frontend/frontend/src/assets/exercises
-- Example browser URL after build/dev serve: assets/exercises/barbell-bench-press.png

SET @schema_name = DATABASE();
SET @column_exists = (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = @schema_name
    AND TABLE_NAME = 'exercise'
    AND COLUMN_NAME = 'image_url'
);

SET @add_image_url_sql = IF(
  @column_exists = 0,
  'ALTER TABLE exercise ADD COLUMN image_url VARCHAR(500) NULL DEFAULT NULL AFTER created_at',
  'SELECT ''exercise.image_url already exists'''
);

PREPARE add_image_url_stmt FROM @add_image_url_sql;
EXECUTE add_image_url_stmt;
DEALLOCATE PREPARE add_image_url_stmt;

UPDATE exercise SET image_url = 'assets/exercises/barbell-bench-press.png' WHERE id = 75;
UPDATE exercise SET image_url = 'assets/exercises/incline-barbell-bench-press.png' WHERE id = 76;
UPDATE exercise SET image_url = 'assets/exercises/incline-dumbbell-press.png' WHERE id = 77;
UPDATE exercise SET image_url = 'assets/exercises/dumbbell-bench-press.png' WHERE id = 78;
UPDATE exercise SET image_url = 'assets/exercises/machine-chest-press.png' WHERE id = 79;
UPDATE exercise SET image_url = 'assets/exercises/dips.png' WHERE id = 80;
UPDATE exercise SET image_url = 'assets/exercises/pec-deck.png' WHERE id = 81;
UPDATE exercise SET image_url = 'assets/exercises/cable-crossover.png' WHERE id = 82;
UPDATE exercise SET image_url = 'assets/exercises/deadlift.png' WHERE id = 83;
UPDATE exercise SET image_url = 'assets/exercises/barbell-row.png' WHERE id = 84;
UPDATE exercise SET image_url = 'assets/exercises/t-bar-row.png' WHERE id = 85;
UPDATE exercise SET image_url = 'assets/exercises/pull-up.png' WHERE id = 86;
UPDATE exercise SET image_url = 'assets/exercises/lat-pulldown.png' WHERE id = 87;
UPDATE exercise SET image_url = 'assets/exercises/seated-cable-row.png' WHERE id = 88;
UPDATE exercise SET image_url = 'assets/exercises/straight-arm-pulldown.png' WHERE id = 89;
UPDATE exercise SET image_url = 'assets/exercises/barbell-curl.png' WHERE id = 90;
UPDATE exercise SET image_url = 'assets/exercises/dumbbell-curl.png' WHERE id = 91;
UPDATE exercise SET image_url = 'assets/exercises/preacher-curl.png' WHERE id = 92;
UPDATE exercise SET image_url = 'assets/exercises/hammer-curl.png' WHERE id = 93;
UPDATE exercise SET image_url = 'assets/exercises/triceps-pushdown.png' WHERE id = 94;
UPDATE exercise SET image_url = 'assets/exercises/skull-crusher.png' WHERE id = 95;
UPDATE exercise SET image_url = 'assets/exercises/close-grip-bench-press.png' WHERE id = 96;
UPDATE exercise SET image_url = 'assets/exercises/triceps-dip.png' WHERE id = 97;
UPDATE exercise SET image_url = 'assets/exercises/hip-thrust.png' WHERE id = 98;
UPDATE exercise SET image_url = 'assets/exercises/glute-bridge.png' WHERE id = 99;
UPDATE exercise SET image_url = 'assets/exercises/cable-kickback.png' WHERE id = 100;
UPDATE exercise SET image_url = 'assets/exercises/back-squat.png' WHERE id = 101;
UPDATE exercise SET image_url = 'assets/exercises/front-squat.png' WHERE id = 102;
UPDATE exercise SET image_url = 'assets/exercises/leg-press.png' WHERE id = 103;
UPDATE exercise SET image_url = 'assets/exercises/hack-squat.png' WHERE id = 104;
UPDATE exercise SET image_url = 'assets/exercises/bulgarian-split-squat.png' WHERE id = 105;
UPDATE exercise SET image_url = 'assets/exercises/leg-extension.png' WHERE id = 106;
UPDATE exercise SET image_url = 'assets/exercises/leg-curl.png' WHERE id = 107;
UPDATE exercise SET image_url = 'assets/exercises/romanian-deadlift.png' WHERE id = 108;
UPDATE exercise SET image_url = 'assets/exercises/standing-calf-raise.png' WHERE id = 109;
UPDATE exercise SET image_url = 'assets/exercises/overhead-press.png' WHERE id = 110;
UPDATE exercise SET image_url = 'assets/exercises/dumbbell-shoulder-press.png' WHERE id = 111;
UPDATE exercise SET image_url = 'assets/exercises/machine-shoulder-press.png' WHERE id = 112;
UPDATE exercise SET image_url = 'assets/exercises/lateral-raise.png' WHERE id = 113;
UPDATE exercise SET image_url = 'assets/exercises/rear-delt-machine-fly.png' WHERE id = 114;
UPDATE exercise SET image_url = 'assets/exercises/face-pull.png' WHERE id = 115;
UPDATE exercise SET image_url = 'assets/exercises/plank.png' WHERE id = 116;
UPDATE exercise SET image_url = 'assets/exercises/crunch.png' WHERE id = 117;
UPDATE exercise SET image_url = 'assets/exercises/leg-raise.png' WHERE id = 118;
UPDATE exercise SET image_url = 'assets/exercises/cable-crunch.png' WHERE id = 119;
