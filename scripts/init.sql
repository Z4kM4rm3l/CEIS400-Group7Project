CREATE DATABASE IF NOT EXISTS toolvault_identity;
CREATE DATABASE IF NOT EXISTS toolvault_depot;
CREATE DATABASE IF NOT EXISTS toolvault_warehouse;

GRANT ALL PRIVILEGES ON toolvault_identity.* TO 'toolvault'@'%';
GRANT ALL PRIVILEGES ON toolvault_depot.* TO 'toolvault'@'%';
GRANT ALL PRIVILEGES ON toolvault_warehouse.* TO 'toolvault'@'%';

FLUSH PRIVILEGES;