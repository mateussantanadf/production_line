INSERT INTO resource (name, qtd_stock)
VALUES
('Ovo', 12),
('Farinha', 20),
('Povilho', 20),
('Leite', 30),
('Tela LED', 1),
('Placa Android', 5),
('Placa IOS', 5);

INSERT INTO product (name, price)
VALUES
('Bolo', 25.00),
('Biscoito', 12.00),
('IPhone 16', 5000.00),
('S25', 2500.00);

INSERT INTO composition (qtd, product_code, resource_code)
VALUES
(
    6,
    (SELECT code FROM product WHERE name = 'Bolo'),
    (SELECT code FROM resource WHERE name = 'Ovo')
),
(
    2,
    (SELECT code FROM product WHERE name = 'Bolo'),
    (SELECT code FROM resource WHERE name = 'Farinha')
),
(
    1,
    (SELECT code FROM product WHERE name = 'Bolo'),
    (SELECT code FROM resource WHERE name = 'Leite')
);

INSERT INTO composition (qtd, product_code, resource_code)
VALUES
(
    2,
    (SELECT code FROM product WHERE name = 'Biscoito'),
    (SELECT code FROM resource WHERE name = 'Ovo')
),
(
    3,
    (SELECT code FROM product WHERE name = 'Biscoito'),
    (SELECT code FROM resource WHERE name = 'Povilho')
);

INSERT INTO composition (qtd, product_code, resource_code)
VALUES
(
    1,
    (SELECT code FROM product WHERE name = 'IPhone 16'),
    (SELECT code FROM resource WHERE name = 'Tela LED')
),
(
    1,
    (SELECT code FROM product WHERE name = 'IPhone 16'),
    (SELECT code FROM resource WHERE name = 'Placa IOS')
);

INSERT INTO composition (qtd, product_code, resource_code)
VALUES
(
    1,
    (SELECT code FROM product WHERE name = 'S25'),
    (SELECT code FROM resource WHERE name = 'Tela LED')
),
(
    1,
    (SELECT code FROM product WHERE name = 'S25'),
    (SELECT code FROM resource WHERE name = 'Placa Android')
);