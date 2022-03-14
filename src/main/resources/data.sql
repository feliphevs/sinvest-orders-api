DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_orders;
DROP TABLE IF EXISTS user_stock_balances;
create table users(
    id bigint primary key generated always as identity,
    username text not null,
    dollar_balance numeric,
    enabled boolean not null default true,
    created_on timestamp not null default current_timestamp,
    updated_on timestamp not null default current_timestamp
    version bigint not null
);
create table user_orders(
    id bigint primary key generated always as identity,
    id_user bigint references users(id),
    id_stock bigint not null,
    stock_symbol text not null,
    stock_name text not null,
    volume bigint not null default 0,
    volume_remaining bigint not null default 0,
    price numeric not null default 0.0,
    type int not null,
    status int not null,
    created_on timestamp not null default current_timestamp,
    updated_on timestamp not null default current_timestamp
);
create table user_stock_balances(
    id_user bigint references users(id),
    id_stock bigint not null,
    stock_symbol text not null,
    stock_name text not null,
    volume bigint not null default 0,
    created_on timestamp not null default current_timestamp,
    updated_on timestamp not null default current_timestamp,
    primary key (id_user, id_stock)
);
INSERT INTO users (id, username, dollar_balance, enabled, created_on, updated_on) VALUES(1, 'userone', 1000, true, current_timestamp, current_timestamp);
INSERT INTO users (id, username, dollar_balance, enabled, created_on, updated_on) VALUES(2, 'usertwo', 1000, true, current_timestamp, current_timestamp);

INSERT INTO user_orders (id, id_user, id_stock, stock_symbol, stock_name, volume, volume_remaining, price, type, status, created_on, updated_on) VALUES(1, 1, 1, 'A', 'ALFA', 20, 20, 10, 0, 0, current_timestamp, current_timestamp);
INSERT INTO user_orders (id, id_user, id_stock, stock_symbol, stock_name, volume, volume_remaining, price, type, status, created_on, updated_on) VALUES(2, 1, 2, 'B', 'BETA', 20, 20, 10, 0, 0, current_timestamp, current_timestamp);

INSERT INTO user_stock_balances (id_user, id_stock, stock_symbol, stock_name, volume, created_on, updated_on) VALUES(1, 1, 'A', 'ALFA', 20, current_timestamp, current_timestamp);
INSERT INTO user_stock_balances (id_user, id_stock, stock_symbol, stock_name, volume, created_on, updated_on) VALUES(1, 2, 'B', 'BETA', 20, current_timestamp, current_timestamp);
