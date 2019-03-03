INSERT INTO user (id, username, password, name, email) VALUES (1, 'admin', '123456', 'laowang', '2550208030@qq.com');
INSERT INTO user (id, username, password, name, email)  VALUES (2, 'wfy', '123456', 'wangfengyuan', '3074237904@qq.com');

INSERT INTO authority (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO authority (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);

create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null);
