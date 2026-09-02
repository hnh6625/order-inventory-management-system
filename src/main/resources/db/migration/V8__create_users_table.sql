    CREATE TABLE users (
        id UUID NOT NULL ,
        username VARCHAR(100) NOT NULL,
        password VARCHAR(255) NOT NULL,
        role VARCHAR(50) NOT NULL ,
        created_at TIMESTAMP NOT NULL ,
        CONSTRAINT pk_users PRIMARY KEY (id),
        CONSTRAINT uq_users_username UNIQUE (username)
    );

    -- insert default user for test
    INSERT INTO users (id, username, password, role, created_at) VALUES
    (gen_random_uuid(), 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SYSTEM_ADMIN', NOW()),
    (gen_random_uuid(), 'ops', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'OPERATIONS_STAFF', NOW()),
    (gen_random_uuid(), 'warehouse', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'WAREHOUSE_STAFF', NOW());