--liquibase formatted sql
--changeset daniel:202602170152
--comment: cards table create

CREATE TABLE CARDS(
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    `order` int NOT NULL,
    boards_column_id BIGINT NOT NULL,
    CONSTRAINT boards_column_cards_fk FOREIGN KEY (boards_column_id) REFERENCES BOARDS_COLUMNS(id) ON DELETE
);

--rollback DROP TABLE CARDS