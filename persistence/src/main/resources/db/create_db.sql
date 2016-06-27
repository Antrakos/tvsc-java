CREATE TABLE users
(
  id       BIGINT PRIMARY KEY NOT NULL,
  name     VARCHAR(100)       NOT NULL,
  password VARCHAR(100)       NOT NULL
);
CREATE TABLE users_episodes_mapping
(
  user_id    BIGINT NOT NULL,
  episode_id BIGINT NOT NULL,
  serial_id  BIGINT NOT NULL,
  CONSTRAINT pk_episode_mapping PRIMARY KEY (user_id, episode_id),
  CONSTRAINT fk_episodes_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);
CREATE TABLE users_series_mapping
(
  user_id   BIGINT NOT NULL,
  serial_id BIGINT NOT NULL,
  CONSTRAINT pk_series_mapping PRIMARY KEY (user_id, serial_id),
  CONSTRAINT fk_series_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);