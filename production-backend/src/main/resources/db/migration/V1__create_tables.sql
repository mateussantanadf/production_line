CREATE TABLE resource (
    code BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    qtd_stock INT NOT NULL
);

CREATE TABLE product (
    code BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

CREATE TABLE composition (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    qtd INT NOT NULL,

    product_code BIGINT NOT NULL,
    resource_code BIGINT NOT NULL,

    CONSTRAINT fk_composition_product
        FOREIGN KEY (product_code) REFERENCES product(code),

    CONSTRAINT fk_composition_resource
        FOREIGN KEY (resource_code) REFERENCES resource(code)
);