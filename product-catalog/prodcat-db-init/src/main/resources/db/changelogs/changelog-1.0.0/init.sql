create table product_category_entity
(
    created_date       timestamp(6) with time zone,
    last_modified_date timestamp(6) with time zone,
    version            bigint,
    id                 uuid not null,
    parent_id          uuid,
    created_by         varchar(255),
    description        varchar(255),
    last_modified_by   varchar(255),
    name               varchar(255),
    primary key (id)
);
create table product_entity
(
    discount           numeric(38, 2),
    is_active          boolean,
    is_shippable       boolean,
    price              numeric(38, 2),
    created_date       timestamp(6) with time zone,
    last_modified_date timestamp(6) with time zone,
    version            bigint,
    id                 uuid         not null,
    variant_id         uuid,
    created_by         varchar(255),
    description        varchar(255),
    last_modified_by   varchar(255),
    name               varchar(255),
    sku                varchar(255) not null unique,
    primary key (id)
);
create table product_image_entity
(
    is_primary         boolean,
    sort_order         integer,
    created_date       timestamp(6) with time zone,
    last_modified_date timestamp(6) with time zone,
    version            bigint,
    id                 uuid not null,
    product_id         uuid,
    variant_id         uuid,
    alt                varchar(255),
    created_by         varchar(255),
    last_modified_by   varchar(255),
    url                varchar(255),
    primary key (id)
);
create table products_categories
(
    category_id uuid not null,
    product_id  uuid not null,
    primary key (category_id, product_id)
);
create table product_variant_entity
(
    additional_price   numeric(38, 2),
    is_active          boolean,
    created_date       timestamp(6) with time zone,
    last_modified_date timestamp(6) with time zone,
    version            bigint,
    id                 uuid         not null,
    attributes         varchar(255),
    created_by         varchar(255),
    last_modified_by   varchar(255),
    sku                varchar(255) not null unique,
    primary key (id)
);
alter table if exists product_category_entity add constraint FK9l1vipne4gu1jni4d6i2h9rir foreign key (parent_id) references product_category_entity

alter table if exists product_entity add constraint FKfiowibrdg3cog00yse4go9ifk foreign key (variant_id) references product_variant_entity

alter table if exists product_image_entity add constraint FKevrsaa2jw2toqth1cm3coi1dq foreign key (variant_id) references product_variant_entity

alter table if exists product_image_entity add constraint FK2dmu6alah61c15yu1em3jld86 foreign key (product_id) references product_entity

alter table if exists products_categories add constraint FKkvtej0g824ugm313jwt3bob9k foreign key (category_id) references product_category_entity

alter table if exists products_categories add constraint FKjs2lvu420bc3lrcwpuyp34jp5 foreign key (product_id) references product_entity
