create sequence hibernate_sequence start 1 increment 1;

create table products (
    id uuid not null,
    name varchar not null,
    description varchar(20),
    price float4 check (price>=0),
    quantity int4,
    img_path varchar(2048),
    primary key (id)
);