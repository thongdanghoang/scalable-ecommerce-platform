CREATE SCHEMA IF NOT EXISTS n3tk_products;
CREATE SCHEMA IF NOT EXISTS n3tk_transactions;
CREATE SCHEMA IF NOT EXISTS n3tk_users;
CREATE SCHEMA IF NOT EXISTS n3tk_orders;

CREATE USER IF NOT EXISTS 'n3tk_products_appuser' IDENTIFIED BY 'n3tk_PASS';
CREATE USER IF NOT EXISTS  'n3tk_transactions_appuser' IDENTIFIED BY 'n3tk_PASS';
CREATE USER IF NOT EXISTS  'n3tk_users_appuser' IDENTIFIED BY 'n3tk_PASS';
CREATE USER IF NOT EXISTS  'n3tk_orders_appuser' IDENTIFIED BY 'n3tk_PASS';

GRANT ALL PRIVILEGES ON n3tk_products.* TO 'n3tk_products_appuser'@'%';
GRANT ALL PRIVILEGES ON n3tk_transactions.* TO 'n3tk_transactions_appuser'@'%';
GRANT ALL PRIVILEGES ON n3tk_users.* TO 'n3tk_users_appuser'@'%';
GRANT ALL PRIVILEGES ON n3tk_orders.* TO 'n3tk_orders_appuser'@'%';
FLUSH PRIVILEGES;
