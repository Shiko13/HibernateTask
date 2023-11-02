DROP TABLE users, trainees, trainers, training_types, trainings;
CREATE TABLE users (id SERIAL PRIMARY KEY, first_name VARCHAR(255) NOT NULL, last_name VARCHAR(255) NOT NULL, user_name VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, is_active BOOLEAN NOT NULL);
CREATE TABLE trainees (id SERIAL PRIMARY KEY, date_of_birth DATE, address VARCHAR(255), user_id BIGINT);
CREATE TABLE trainers (id SERIAL PRIMARY KEY, user_id BIGINT NOT NULL);
CREATE TABLE training_types (id SERIAL PRIMARY KEY, name VARCHAR(255) NOT NULL, trainer_id BIGINT, FOREIGN KEY (trainer_id) REFERENCES trainers (id) ON DELETE CASCADE);
CREATE TABLE trainings (id SERIAL PRIMARY KEY, trainee_id BIGINT NOT NULL, trainer_id BIGINT NOT NULL, name VARCHAR(255) NOT NULL, type_id BIGINT NOT NULL, date DATE NOT NULL, duration BIGINT NOT NULL, FOREIGN KEY (trainee_id) REFERENCES trainees(id), FOREIGN KEY (trainer_id) REFERENCES trainers(id), FOREIGN KEY (type_id) REFERENCES training_types(id));
