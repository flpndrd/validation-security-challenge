INSERT INTO tb_user (email, password) VALUES
('ana@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG'),
('bob@gmail.com', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG');

INSERT INTO tb_role (authority) VALUES
('ROLE_CLIENT'),
('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES
(1, 1),
(2, 1),
(2, 2);

INSERT INTO tb_city (name) VALUES
('São Paulo'),
('Brasília'),
('Fortaleza'),
('Salvador'),
('Manaus'),
('Curitiba'),
('Goiânia'),
('Belém'),
('Porto Alegre'),
('Rio de Janeiro'),
('Belo Horizonte');

INSERT INTO tb_event (name, date, url, city_id) VALUES
('Feira do Software', '2021-05-16', 'https://feiradosoftware.com', 1),
('CCXP', '2021-04-13', 'https://ccxp.com.br', 1),
('Congresso Linux', '2021-05-23', 'https://congressolinux.com.br', 2),
('Semana Spring React', '2021-05-03', 'https://devsuperior.com.br', 3);
