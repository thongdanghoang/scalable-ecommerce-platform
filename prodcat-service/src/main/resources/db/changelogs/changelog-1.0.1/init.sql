create table categories
(
    version            integer      not null,
    created_date       timestamp(6) not null,
    last_modified_date timestamp(6) not null,
    id                 uuid         not null,
    parent_id          uuid,
    name               varchar(100) not null unique,
    description        varchar(500),
    created_by         varchar(255) not null,
    last_modified_by   varchar(255) not null,
    primary key (id)
);
create table products
(
    price              numeric(38, 2) not null,
    version            integer        not null,
    created_date       timestamp(6)   not null,
    last_modified_date timestamp(6)   not null,
    id                 uuid           not null,
    name               varchar(200)   not null,
    description        varchar(1000),
    created_by         varchar(255)   not null,
    last_modified_by   varchar(255)   not null,
    primary key (id)
);
create table products_categories
(
    category_id uuid not null,
    product_id  uuid not null,
    primary key (category_id, product_id)
);
alter table if exists categories
    add constraint fk_Categories_Parent foreign key (parent_id) references categories;
alter table if exists products_categories
    add constraint fk_ProductsCategories_Categories foreign key (category_id) references categories;
alter table if exists products_categories
    add constraint fk_ProductsCategories_Products foreign key (product_id) references products;