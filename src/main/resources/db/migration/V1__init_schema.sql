CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       email VARCHAR(255) UNIQUE NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE labels(

    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) on DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE tasks(
    id BIGSERIAL PRIMARY KEY ,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(255) NOT NULL ,
    description TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'TODO',
    deadline TIMESTAMP,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL  DEFAULT NOW()
);

CREATE TABLE task_labels(
    task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE ,
    label_id BIGINT NOT NULL REFERENCES labels(id) on DELETE CASCADE ,
    PRIMARY KEY (task_id,label_id)
);

CREATE INDEX idx_tasks_user_id ON tasks(user_id);
CREATE INDEX idx_tasks_labels_user_id ON labels(user_id);
CREATE INDEX idx_tasks_status ON tasks(status);

