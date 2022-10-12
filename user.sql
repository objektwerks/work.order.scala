DROP USER IF EXISTS 'workorder'@'localhost';
CREATE USER 'workorder'@'localhost' IDENTIFIED BY 'workorder';
SHOW CREATE USER 'workorder'@'localhost';
GRANT ALL PRIVILEGES ON work_order_db.* TO 'workorder'@'localhost' WITH GRANT OPTION;
ALTER USER 'workorder'@'localhost' IDENTIFIED BY 'workorder';
FLUSH PRIVILEGES;