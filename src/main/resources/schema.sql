CREATE TABLE binary_contents (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes BYTEA NOT NULL
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(60) NOT NULL,
    profile_id UUID
        REFERENCES binary_contents(id)
        ON DELETE SET NULL
);

CREATE TABLE user_statuses(
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL UNIQUE
        REFERENCES users(id)
        ON DELETE CASCADE,
    last_active_at TIMESTAMP NOT NULL
);


CREATE TYPE channel_type AS ENUM ('PUBLIC', 'PRIVATE');

CREATE TABLE channels (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    name VARCHAR(100),
    description VARCHAR(500),
    type channel_type NOT NULL
);

CREATE TABLE read_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    user_id UUID NOT NULL
        REFERENCES users(id)
        ON DELETE CASCADE,
    channel_id UUID NOT NULL
        REFERENCES channels(id)
        ON DELETE CASCADE,
    last_read_at TIMESTAMP NOT NULL,
    -- 복합 유니크
    CONSTRAINT uk_read_statuses_user_channel
    UNIQUE(user_id, channel_id)
);

CREATE TABLE messages (
    id UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    content TEXT,
    channel_id UUID NOT NULL
        REFERENCES channels(id)
        ON DELETE CASCADE,
    author_id UUID
        REFERENCES users(id)
        ON DELETE SET NULL
);


CREATE TABLE message_attachments (
 message_id UUID
     REFERENCES messages(id)
     ON DELETE CASCADE,
 attachment_id UUID
     REFERENCES binary_contents(id)
     ON DELETE CASCADE,

 CONSTRAINT pk_message_attachments
     PRIMARY KEY (message_id, attachment_id)
);
