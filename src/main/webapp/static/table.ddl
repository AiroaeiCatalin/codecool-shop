create table product
(
    id serial not null
        constraint product_pk
            primary key,
    name varchar,
    description varchar,
    price double precision,
    supplier_id integer not null,
    product_category_id integer not null,
    currency varchar
);

alter table product owner to bogdan;

create table supplier
(
    id serial not null
        constraint supplier_pk
            primary key,
    name varchar,
    description varchar
);

alter table supplier owner to bogdan;

create table product_category
(
    id serial not null
        constraint productcategory_pk
            primary key
        constraint productcategory_product_id_fk
            references product,
    name varchar,
    department varchar
);

alter table product_category owner to bogdan;

create unique index productcategory_id_uindex
    on product_category (id);

create table "user"
(
    name varchar,
    email varchar,
    password varchar,
    id serial not null
        constraint user_pk
            primary key
);

alter table "user" owner to bogdan;

create unique index user_id_uindex
    on "user" (id);

create table "order"
(
    id serial not null
        constraint order_pk
            primary key,
    price double precision,
    name varchar,
    email varchar,
    billing_address varchar,
    status varchar,
    card_number varchar,
    card_type varchar,
    user_id integer
);

alter table "order" owner to bogdan;

create unique index order_id_uindex
    on "order" (id);

create table shipping_info
(
    user_id integer
        constraint billinginfo_user_id_fk
            references "user",
    name varchar,
    email varchar,
    phonenumber varchar,
    billing_address varchar,
    shipping_address varchar
);

alter table shipping_info owner to bogdan;

create table cart
(
    id serial not null
        constraint cart_pk
            primary key,
    user_id integer
        constraint cart_user_id_fk
            references "user"
);

alter table cart owner to bogdan;

create table line_item
(
    id serial not null
        constraint line_item_pk
            primary key,
    cart_id integer
        constraint line_item_cart_id_fk
            references cart,
    product_id integer
        constraint line_item_product_id_fk
            references product,
    quantity integer
);

alter table line_item owner to bogdan;

create table billing_info
(
    id serial not null
        constraint billing_info_pk
            primary key,
    card_owner text,
    card_number text,
    expiration_month text,
    expiration_year text,
    user_id integer
        constraint billing_info_user_id_fk
            references "user"
);

alter table billing_info owner to bogdan;

