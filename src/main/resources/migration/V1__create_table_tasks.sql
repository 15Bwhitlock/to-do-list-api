CREATE TABLE tasks
(
    id             CHAR(36)         NOT NULL PRIMARY KEY,
    title          VARCHAR(255)     NOT NULL,
    description    TEXT             NULL,
    priority_level TINYINT UNSIGNED NOT NULL,
    due_date       DATE             NULL,
    is_completed   BOOLEAN          NOT NULL DEFAULT FALSE,
    created_at     TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);