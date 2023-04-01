create database ecommercedb;
create user 'ecommerceuser' identified by 'ecommercepassword';
GRANT ALL PRIVILEGES ON ecommercedb.* TO 'ecommerceuser'@'%';
FLUSH PRIVILEGES;
# DROP USER 'ecommerceuser'@'%';