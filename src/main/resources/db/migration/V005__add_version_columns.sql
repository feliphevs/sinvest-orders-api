alter table user_orders add column version bigint;
alter table users add column version bigint;
alter table user_stock_balances add column version bigint;