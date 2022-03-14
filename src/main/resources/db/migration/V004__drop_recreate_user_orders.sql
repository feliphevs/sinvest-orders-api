DROP TABLE user_orders;

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