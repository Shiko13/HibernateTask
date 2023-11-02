INSERT INTO users (first_name, last_name, user_name, password, is_active)
VALUES
    ('John', 'Doe', 'john.doe', 'password1', true),
    ('Jane', 'Smith', 'jane.smith', 'password2', true),
    ('Alice', 'Johnson', 'alice.johnson', 'password3', true),
    ('Bob', 'Brown', 'bob.brown', 'password4', true),
    ('Eva', 'Davis', 'eva.davis', 'password5', true),
    ('Michael', 'Wilson', 'michael.wilson', 'password6', true),
    ('Olivia', 'Lee', 'olivia.lee', 'password7', true),
    ('Daniel', 'Taylor', 'daniel.taylor', 'password8', true),
    ('Sophia', 'Moore', 'sophia.moore', 'password9', true),
    ('James', 'Johnson', 'james.johnson', 'password10', true),
    ('Emily', 'White', 'emily.white', 'password11', true),
    ('William', 'Anderson', 'william.anderson', 'password12', true),
    ('Charlotte', 'Martin', 'charlotte.martin', 'password13', true),
    ('Benjamin', 'Thompson', 'benjamin.thompson', 'password14', true),
    ('Amelia', 'Harris', 'amelia.harris', 'password15', true),
    ('Henry', 'Nelson', 'henry.nelson', 'password16', true),
    ('Sofia', 'King', 'sofia.king', 'password17', true),
    ('Alexander', 'Hall', 'alexander.hall', 'password18', true),
    ('Mia', 'Wright', 'mia.wright', 'password19', true),
    ('Joseph', 'Adams', 'joseph.adams', 'password20', true);
INSERT INTO trainees (date_of_birth, address, user_id) VALUES
                                                           ('1995-03-15', '123 Main St', 1),
                                                           ('1992-08-20', '456 Elm St', 2),
                                                           ('1990-04-10', '789 Oak St', 3),
                                                           ('1998-12-05', '555 Pine St', 4),
                                                           ('1994-07-28', '777 Birch St', 5),
                                                           ('1997-09-14', '888 Cedar St', 6),
                                                           ('1999-06-09', '999 Redwood St', 7),
                                                           ('1993-02-25', '111 Maple St', 8),
                                                           ('1996-11-03', '222 Willow St', 9),
                                                           ('1991-01-01', '333 Birch St', 10);

INSERT INTO trainers (user_id) VALUES (11), (12), (13), (14), (15), (16), (17), (18), (19), (20);
INSERT INTO training_types (name, trainer_id) VALUES
    ('Strength Training', 1),
    ('Cardiovascular Exercise', 2),
    ('Yoga', 3),
    ('CrossFit', 4),
    ('Pilates', 5),
    ('Martial Arts', 6),
    ('Zumba', 7),
    ('Spinning', 8),
    ('Swimming', 9),
    ('Plyometrics', 10);
INSERT INTO trainings (trainee_id, trainer_id, name, type_id, date, duration)
VALUES
    (1, 1, 'Training 1', 1, '2023-11-01', 60),
    (2, 2, 'Training 2', 2, '2023-11-02', 45),
    (3, 3, 'Training 3', 3, '2023-11-03', 90),
    (4, 4, 'Training 4', 4, '2023-11-04', 75),
    (5, 5, 'Training 5', 5, '2023-11-05', 60),
    (6, 6, 'Training 6', 6, '2023-11-06', 45),
    (7, 7, 'Training 7', 7, '2023-11-07', 90),
    (8, 8, 'Training 8', 8, '2023-11-08', 75),
    (9, 9, 'Training 9', 9, '2023-11-09', 60),
    (10, 10, 'Training 10', 10, '2023-11-10', 45);