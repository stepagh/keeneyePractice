create sequence  IF NOT EXISTS students_seq START with 1 INCREMENT BY 50;

create TABLE students (
  id BIGINT NOT NULL,
   fio VARCHAR(255),
   "group" VARCHAR(255),
   phone_number VARCHAR(255),
   CONSTRAINT pk_students PRIMARY KEY (id)
);