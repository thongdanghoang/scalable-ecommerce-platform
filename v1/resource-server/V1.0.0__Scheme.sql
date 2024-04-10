create table authority
(
    id      bigint auto_increment
        primary key,
    version int         not null,
    name    varchar(50) not null,
    user_id bigint      null
);

create table cart
(
    id                  bigint auto_increment
        primary key,
    version             int    not null,
    user_auth_entity_id bigint not null
);

alter table cart
    add constraint UK_oxlxitl2e6gwj2gvm0pll3nj0
        unique (user_auth_entity_id);

create table cart_item
(
    id          bigint auto_increment
        primary key,
    version     int    not null,
    amount      int    null,
    quantity_id bigint null,
    cart_id     bigint null
);

alter table cart_item
    add constraint FK1uobyhgl1wvgt1jpccia8xxs3
        foreign key (cart_id) references cart (id);

create table category
(
    id                 bigint auto_increment
        primary key,
    name               varchar(255) not null,
    parent_category_id bigint       null
);

alter table category
    add constraint FKs2ride9gvilxy2tcuv7witnxc
        foreign key (parent_category_id) references category (id);

create table color
(
    id         bigint auto_increment
        primary key,
    color_name varchar(255) not null
);

create table email_otp
(
    id           bigint auto_increment
        primary key,
    version      int          not null,
    email        varchar(255) not null,
    expired_in   datetime(6)  not null,
    otp          varchar(6)   not null,
    wrong_submit int          not null
);

alter table email_otp
    add constraint UK_agpgp27lulkh46wjbkbpc7cfs
        unique (email);

create table image
(
    id         bigint auto_increment
        primary key,
    image_url  varchar(255) not null,
    color_id   bigint       null,
    product_id bigint       null
);

alter table image
    add constraint FKpqyba4pglabqgdmwincaw6oxk
        foreign key (color_id) references color (id);

create table order_address
(
    id             bigint auto_increment
        primary key,
    address_detail varchar(255) null,
    district       varchar(255) null,
    full_name      varchar(255) null,
    phone          varchar(255) null,
    province       varchar(255) null,
    type           varchar(255) null,
    ward           varchar(255) null
);

create table order_item
(
    id              bigint auto_increment
        primary key,
    amount          int    not null,
    quantity_id     bigint not null,
    order_entity_id bigint null
);

create table otp_verification
(
    id                  bigint auto_increment
        primary key,
    token               varchar(255) not null,
    user_auth_entity_id bigint       null
);

create table payment_detail
(
    id              bigint auto_increment
        primary key,
    created_date    datetime(6)  not null,
    grand_total     bigint       not null,
    payment_method  varchar(255) null,
    payment_status  varchar(255) not null,
    update_date     datetime(6)  not null,
    order_entity_id bigint       null
);


-- Product
create table product
(
    id             bigint auto_increment
        primary key,
    version        int          not null,
    created_date   datetime(6)  null,
    description    longtext     null,
    discount       float        not null,
    name           varchar(255) not null,
    number_of_sold int          null,
    price          int          not null,
    rated          float        null,
    sku            varchar(255) not null,
    category_id    bigint       null
);

alter table image
    add constraint FKgpextbyee3uk9u6o2381m7ft1
        foreign key (product_id) references product (id);

alter table product
    add constraint UK_q1mafxn973ldq80m1irp3mpvq
        unique (sku);

alter table product
    add constraint FK1mtsbur82frn64de7balymq9s
        foreign key (category_id) references category (id);

create table purchase_order
(
    id               bigint auto_increment
        primary key,
    created_date     datetime(6)  not null,
    delivery_method  varchar(255) null,
    discount         bigint       not null,
    grand_total      bigint       not null,
    shipping_fee     bigint       not null,
    status           varchar(255) not null,
    total            bigint       not null,
    update_date      datetime(6)  not null,
    order_address_id bigint       null,
    user_id          bigint       not null
);

alter table order_item
    add constraint FKo90xdji9hhjq4ofckvh6v6gs9
        foreign key (order_entity_id) references purchase_order (id);

alter table payment_detail
    add constraint FKe3aswy8bubn33myesxpak2ob4
        foreign key (order_entity_id) references purchase_order (id);

alter table purchase_order
    add constraint UK_peau3nbmqrt51ppksv4pci7ci
        unique (order_address_id);

alter table purchase_order
    add constraint FKirm0thg3hl4yo0rwigxsny7go
        foreign key (order_address_id) references order_address (id);

create table quantity
(
    id                bigint auto_increment
        primary key,
    quantity_in_stock int    null,
    color_id          bigint null,
    product_id        bigint null,
    size_id           bigint null
);

alter table cart_item
    add constraint FKcric0kjpiu3qk7qnhjkn4rdac
        foreign key (quantity_id) references quantity (id);

alter table order_item
    add constraint FKeq3knrjbfwcmudfwaxre66ngv
        foreign key (quantity_id) references quantity (id);

alter table quantity
    add constraint FK9jgf68gj8wx4hvk8nsn1b946f
        foreign key (color_id) references color (id);

alter table quantity
    add constraint FKodjufegjdwhdwqajibonj8wtf
        foreign key (product_id) references product (id);

create table size
(
    id        bigint auto_increment
        primary key,
    size_name varchar(255) not null
);

alter table quantity
    add constraint FKqnxa0y0rdqx0it9yutbys49it
        foreign key (size_id) references size (id);

create table user
(
    id                              bigint auto_increment
        primary key,
    version                         int          not null,
    created_date                    datetime(6)  null,
    disabled_until                  datetime(6)  null,
    enabled                         bit          not null,
    number_of_failed_login_attempts int          not null,
    password                        varchar(255) not null,
    updated_date                    datetime(6)  null,
    username                        varchar(255) not null
);

alter table authority
    add constraint FKr1wgeo077ok1nr1shx0t70tg8
        foreign key (user_id) references user (id);

alter table cart
    add constraint FK6gy7t8i7uskgbw1yu3lj07d8
        foreign key (user_auth_entity_id) references user (id);

alter table otp_verification
    add constraint FKiviw250opam4pio0od8uis5g1
        foreign key (user_auth_entity_id) references user (id);

alter table purchase_order
    add constraint FK5j4jnq1w6e2ged6evsvmgmx8b
        foreign key (user_id) references user (id);

alter table user
    add constraint UK_sb8bbouer5wak8vyiiy4pf2bx
        unique (username);

create table user_address
(
    id             bigint auto_increment
        primary key,
    version        int          not null,
    address_detail varchar(255) null,
    district       varchar(255) null,
    full_name      varchar(255) null,
    is_default     bit          null,
    phone          varchar(255) null,
    province       varchar(255) null,
    type           varchar(255) null,
    ward           varchar(255) null,
    user_id        bigint       null
);

alter table user_address
    add constraint FKk2ox3w9jm7yd6v1m5f68xibry
        foreign key (user_id) references user (id);

create table user_profile
(
    id                bigint auto_increment
        primary key,
    version           int          not null,
    avatar_url        varchar(255) null,
    birthday          date         null,
    email             varchar(255) not null,
    full_name         varchar(255) null,
    gender            varchar(255) null,
    height            int          null,
    is_email_verified bit          not null,
    is_phone_verified bit          not null,
    phone             varchar(255) null,
    weight            int          null,
    user_id           bigint       null
);

alter table user_profile
    add constraint UK_1un6sdkbtaspkwmsiferm1dhm
        unique (phone);

alter table user_profile
    add constraint UK_tcks72p02h4dp13cbhxne17ad
        unique (email);

alter table user_profile
    add constraint FK6kwj5lk78pnhwor4pgosvb51r
        foreign key (user_id) references user (id);

