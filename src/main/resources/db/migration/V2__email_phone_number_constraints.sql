ALTER TABLE professors
    ADD CONSTRAINT check_professor_phone
        CHECK (phone_number ~ '^\+7\d{10}$');

ALTER TABLE students
    ADD CONSTRAINT check_student_phone
        CHECK (phone_number ~ '^\+7\d{10}$');

ALTER TABLE users
    ADD CONSTRAINT check_user_email
        CHECK (email ~* '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$');