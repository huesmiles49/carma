Create table Users (
  ID int NOT NULL auto_increment,
  FName varchar(255),
  LName varchar(255),
  Email varchar(255) unique,
  Pass varchar(255),
  Carma int,
  primary key (ID));
  
  
create table Users_Cars (
  ID int not null auto_increment,
  User_ID int,
  Make varchar(255),
  Model varchar(255),
  Color varchar(255),
  License_Plate varchar(255),
  Plate_State varchar(255),
  primary key(ID),
  foreign key (User_ID) references Users(ID));