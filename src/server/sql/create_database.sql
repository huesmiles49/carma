
drop table if exists Reviews;
drop table if exists MatchGPS;
drop table if exists Matches;
drop table if exists Reservations;
drop table if exists Spots;
drop table if exists Users_Cars;
drop table if exists Users;

Create table Users (
  ID int NOT NULL auto_increment,
  FName varchar(255),
  LName varchar(255),
  Email varchar(255) unique,
  Pass varchar(255),
  Carma int,
  primary key (ID)
);
  
  
create table Users_Cars (
  ID int not null auto_increment,
  User_ID int,
  Make varchar(255),
  Model varchar(255),
  Color varchar(255),
  License_Plate varchar(255),
  Plate_State varchar(255),
  primary key(ID),
  foreign key (User_ID) references Users(ID)
);

create table Spots (
  ID int not null auto_increment,
  Lister_ID int,
  Lister_Car int,
  Location varchar(255),
  GPS_Lat varchar(255),
  GPS_Long varchar(255),
  Time_Listed varchar(255),
  Time_Swap varchar(255),
  Comment varchar(255),
  primary key(ID),
  foreign key (Lister_ID) references Users(ID),
  foreign key (Lister_Car) references Users_Cars(ID)
);

create table Reservations(
   ID int not null auto_increment,
   Spot_ID int,
   Reserver_ID int,
   Reserver_Car int,
   GPS_Lat varchar(255), 			
   GPS_Long varchar(255),
   primary key(ID),
   foreign key(Spot_ID) references Spots(ID),
   foreign key(Reserver_ID) references Users(ID),
   foreign key(Reserver_Car) references Users_Cars(ID),
   unique (Spot_ID,Reserver_ID)
);

create table Matches(
   ID int not null auto_increment,
   Spot_ID int,
   Reservations_ID int,
   primary key(ID),
   foreign key(Spot_ID) references Spots(ID),
   foreign key(Reservations_ID) references Reservations(ID)
);

create table MatchGPS(
   ID int not null auto_increment,
   User_ID int,
   Matches_ID int,
   GPS_Lat varchar(255),
   GPS_Long varchar(255),
   primary key(ID),
   foreign key(User_ID) references Users(ID),
   foreign key(Matches_ID) references Matches(ID),
   unique (User_ID,Matches_ID)
);

create table Reviews(
   ID int not null auto_increment,
   About_ID int,
   From_ID int,
   Swap_ID int,
   Comment varchar(255),
   Star_Rating int,
   primary key(ID),
   foreign key(About_ID) references Users(ID),
   foreign key(From_ID) references Users(ID),
   foreign key(Swap_ID) references Matches(ID)
);

insert into Users(FName,LName,Email,Pass,Carma) values ("John","Doe", "John@Doe.com", "abcd", 2000);
insert into Users(FName,LName,Email,Pass,Carma) values ("Jane","Doe", "Jane@Doe.com", "efgh", 1000);

insert into Users_Cars(User_ID,Make,Model,Color,License_Plate,Plate_State) values ((Select ID from Users where Email="John@Doe.com"),"Ford","Focus","White","7GTH876","CA");
insert into Users_Cars(User_ID,Make,Model,Color,License_Plate,Plate_State) values ((Select ID from Users where Email="Jane@Doe.com"),"Toyota","Camery","Blue","7GTH866","CA");

insert into Spots(Lister_ID,Lister_Car,Location,GPS_Lat,GPS_Long,Time_Listed,Time_Swap,Comment) values ((Select ID from Users where Email="John@Doe.com"),(Select ID from Users_Cars where User_ID=(Select ID from Users where Email="John@Doe.com")),"Parking Lot 7","34.0678924","-118.16765869999999","2017-10-14T00:59:04.652","Now","In Lot 7A");
insert into Spots(Lister_ID,Lister_Car,Location,GPS_Lat,GPS_Long,Time_Listed,Time_Swap,Comment) values ((Select ID from Users where Email="Jane@Doe.com"),(Select ID from Users_Cars where User_ID=(Select ID from Users where Email="Jane@Doe.com")),"Parking Lot 5","35.0678924","-118.16765869999999","2017-10-14T00:59:04.652","Nowish","On third floor");

insert into Reservations(Spot_ID,Reserver_ID,Reserver_Car, GPS_Lat, GPS_Long) values ((Select ID from Spots where Lister_ID=(Select ID from Users where Email="John@Doe.com")),(Select ID from Users where Email="Jane@Doe.com"),(Select ID from Users_Cars where User_ID=(Select ID from Users where Email="Jane@Doe.com")),"34.143032","-118.082479");
insert into Reservations(Spot_ID,Reserver_ID,Reserver_Car, GPS_Lat, GPS_Long) values ((Select ID from Spots where Lister_ID=(Select ID from Users where Email="Jane@Doe.com")),(Select ID from Users where Email="John@Doe.com"),(Select ID from Users_Cars where User_ID=(Select ID from Users where Email="John@Doe.com")),"34.071260","-118.166478");
insert into Matches(Spot_ID,Reservations_ID) values ((Select ID from Spots where Lister_ID=(Select ID from Users where Email="John@Doe.com")),(Select ID from Reservations where Reserver_ID=(Select ID from Users where Email="Jane@Doe.com")));
insert into Matches(Spot_ID,Reservations_ID) values ((Select ID from Spots where Lister_ID=(Select ID from Users where Email="Jane@Doe.com")),(Select ID from Reservations where Reserver_ID=(Select ID from Users where Email="John@Doe.com")));

