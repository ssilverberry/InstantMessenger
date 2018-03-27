CREATE TABLE users (
  user_id INT PRIMARY KEY NOT NULL,
  user_login VARCHAR2(100) NOT NULL,
  user_password VARCHAR2(50) NOT NULL,
  user_email VARCHAR2(100) NOT NULL,
  user_status INT
);

CREATE UNIQUE INDEX users_user_login_uindex ON users (user_login);

CREATE SEQUENCE dept_seq START WITH 1;

CREATE OR REPLACE TRIGGER dept_bir
BEFORE INSERT ON users
FOR EACH ROW
  BEGIN
    SELECT dept_seq.NEXTVAL
    INTO   :new.user_id
    FROM   dual;
  END;