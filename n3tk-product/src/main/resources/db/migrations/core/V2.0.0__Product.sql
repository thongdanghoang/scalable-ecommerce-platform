# Product - auditable
CREATE TABLE `product`
(
    `id`                   BIGINT       NOT NULL,
    `version`              INT          NOT NULL,
    `createdBy`            TINYTEXT,
    `lastModifiedBy`       TINYTEXT,
    `creationDate`         DATETIME     NOT NULL,
    `lastModificationDate` DATETIME     NULL     DEFAULT NULL,
    `title`                VARCHAR(75)  NOT NULL,
    `metaTitle`            VARCHAR(100) NULL,
    `slug`                 VARCHAR(100) NOT NULL,
    `summary`              TINYTEXT     NULL,
    `type`                 SMALLINT     NOT NULL DEFAULT 0,
    `sku`                  VARCHAR(100) NOT NULL,
    `price`                FLOAT        NOT NULL DEFAULT 0,
    `discount`             FLOAT        NOT NULL DEFAULT 0,
    `quantity`             SMALLINT     NOT NULL DEFAULT 0,
    `shop`                 BIT          NOT NULL DEFAULT 0,
    `publishedAt`          DATETIME     NULL     DEFAULT NULL,
    `startsAt`             DATETIME     NULL     DEFAULT NULL,
    `endsAt`               DATETIME     NULL     DEFAULT NULL,
    `content`              TEXT         NULL     DEFAULT NULL
);
ALTER TABLE `product`
    MODIFY `id` BIGINT NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`),
    ADD UNIQUE INDEX `uq_slug` (`slug` ASC);

# Product Meta: store additional information about products including the product banner URL etc.
CREATE TABLE `product_meta`
(
    `id`        BIGINT      NOT NULL,
    `version`   INT         NOT NULL,
    `productId` BIGINT      NOT NULL,
    `key`       VARCHAR(50) NOT NULL,
    `content`   TEXT        NULL DEFAULT NULL
);
ALTER TABLE `product_meta`
    MODIFY `id` BIGINT NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`),
    ADD INDEX `idx_meta_product` (`productId` ASC),
    ADD UNIQUE INDEX `uq_product_meta` (`productId` ASC, `key` ASC),
    ADD CONSTRAINT `fk_meta_product`
        FOREIGN KEY (`productId`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

# Product Review Table: store the product reviews - auditable
CREATE TABLE `product_review`
(
    `id`                   BIGINT       NOT NULL,
    `version`              INT          NOT NULL,
    `createdBy`            TINYTEXT,
    `lastModifiedBy`       TINYTEXT,
    `creationDate`         DATETIME     NOT NULL,
    `lastModificationDate` DATETIME     NULL     DEFAULT NULL,
    `productId`            BIGINT       NOT NULL,
    `parentId`             BIGINT       NULL     DEFAULT NULL,
    `title`                VARCHAR(100) NOT NULL,
    `rating`               SMALLINT     NOT NULL DEFAULT 0,
    `published`            BIT          NOT NULL DEFAULT 0,
    `publishedAt`          DATETIME     NULL     DEFAULT NULL,
    `content`              TEXT         NULL     DEFAULT NULL
);
ALTER TABLE `product_review`
    MODIFY `id` BIGINT NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`),
    ADD INDEX `idx_review_product` (`productId` ASC),
    ADD CONSTRAINT `fk_review_product`
        FOREIGN KEY (`productId`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;
ALTER TABLE `product_review`
    ADD INDEX `idx_review_parent` (`parentId` ASC);
ALTER TABLE `product_review`
    ADD CONSTRAINT `fk_review_parent`
        FOREIGN KEY (`parentId`)
            REFERENCES `product_review` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

# Category Table and Product Category Table: store the product categories and their mappings
CREATE TABLE `category`
(
    `id`        BIGINT       NOT NULL,
    `version`   INT          NOT NULL,
    `parentId`  BIGINT       NULL DEFAULT NULL,
    `title`     VARCHAR(75)  NOT NULL,
    `metaTitle` VARCHAR(100) NULL DEFAULT NULL,
    `slug`      VARCHAR(100) NOT NULL,
    `content`   TEXT         NULL DEFAULT NULL
);
ALTER TABLE `category`
    MODIFY `id` BIGINT NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);
ALTER TABLE `category`
    ADD INDEX `idx_category_parent` (`parentId` ASC);
ALTER TABLE `category`
    ADD CONSTRAINT `fk_category_parent`
        FOREIGN KEY (`parentId`)
            REFERENCES `category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

CREATE TABLE `product_category`
(
    `productId`  BIGINT NOT NULL,
    `categoryId` BIGINT NOT NULL
);
ALTER TABLE `product_category`
    ADD PRIMARY KEY (`productId`, `categoryId`),
    ADD INDEX `idx_pc_category` (`categoryId` ASC),
    ADD INDEX `idx_pc_product` (`productId` ASC),
    ADD CONSTRAINT `fk_pc_product`
        FOREIGN KEY (`productId`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    ADD CONSTRAINT `fk_pc_category`
        FOREIGN KEY (`categoryId`)
            REFERENCES `category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

# Similar to the category and product category tables, we can design the Tag Table and Product Tag Table.
# The major differences between the Category and Tag are listed below.
# The parentId column is not required in the Tag Table.
# The count of categories remains low since these can be used to form the Main Menu for navigational purposes.
# The tags can be more as compared to categories.
# Both categories and tags can be used to relate the products.
# One should assign only a few categories to a product whereas tags count can be more.
CREATE TABLE `tag`
(
    `id`        BIGINT       NOT NULL,
    `version`   INT          NOT NULL,
    `title`     VARCHAR(75)  NOT NULL,
    `metaTitle` VARCHAR(100) NULL DEFAULT NULL,
    `slug`      VARCHAR(100) NOT NULL,
    `content`   TEXT         NULL DEFAULT NULL
);
ALTER TABLE `tag`
    MODIFY `id` BIGINT NOT NULL AUTO_INCREMENT,
    ADD PRIMARY KEY (`id`);

CREATE TABLE `product_tag`
(
    `productId` BIGINT NOT NULL,
    `tagId`     BIGINT NOT NULL
);
ALTER TABLE `product_tag`
    ADD PRIMARY KEY (`productId`, `tagId`),
    ADD INDEX `idx_pt_tag` (`tagId` ASC),
    ADD INDEX `idx_pt_product` (`productId` ASC),
    ADD CONSTRAINT `fk_pt_product`
        FOREIGN KEY (`productId`)
            REFERENCES `product` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    ADD CONSTRAINT `fk_pt_tag`
        FOREIGN KEY (`tagId`)
            REFERENCES `tag` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;