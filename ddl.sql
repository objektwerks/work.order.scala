drop database if exists `work_order_db`;

create database `work_order_db`;

use `work_order_db`;

create table `user` (
  `id` int not null auto_increment,
  `role` varchar(24) not null,
  `name` varchar(64) not null,
  `emailAddress` varchar(128) not null unique,
  `streetAddress` varchar(128) not null unique,
  `registered` varchar(27) not null,
  `pin` varchar(7) not null unique,
  `license` char(36) not null default (uuid()),
  primary key (`id`)
);
create index user_email_address_idx ON `user`(`emailAddress`);
create index user_pin_idx ON `user`(`pin`);

create table `work_order` (
  `number` int not null auto_increment,
  `homeownerId` int not null,
  `serviceProviderId` int not null,
  `title` varchar(64) not null,
  `issue` varchar(255) not null,
  `streetAddress` varchar(128) not null,
  `imageUrl` varchar(255) not null default "",
  `resolution` varchar(255) not null default "",
  `opened` varchar(27) not null,
  `closed` varchar(27) not null default "",
  primary key (`number`),
  constraint homeowner_id_fk foreign key (`homeownerId`) REFERENCES `user`(`id`),
  constraint service_provider_id_fk foreign key (`serviceProviderId`) REFERENCES `user`(`id`)
);