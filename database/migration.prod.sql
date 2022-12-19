USE citypatrol;

CREATE TABLE city(
    id INT NOT NULL AUTO_INCREMENT,
    city_name VARCHAR(30) NOT NULL,
    population_number INT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE hood(
    id INT NOT NULL AUTO_INCREMENT,
    hood_name VARCHAR(30) NOT NULL,
    region VARCHAR(30) NOT NULL,
    population_number INT NOT NULL,
    city_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (city_id) REFERENCES city(id)
);

CREATE TABLE crime(
    id INT NOT NULL AUTO_INCREMENT,
    crime_type VARCHAR(30) NOT NULL,
    crime_description VARCHAR(255) NOT NULL,
    hood_id INT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (hood_id) REFERENCES hood(id)
);