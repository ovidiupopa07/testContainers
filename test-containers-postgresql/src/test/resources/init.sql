create table car (
    id INT NOT NULL PRIMARY KEY ,
    brand varchar (30) not null ,
    model varchar (50) not null ,
    horse_power int not null ,
    fuel varchar (30) not null ,
    torque varchar (30) not null
);


insert into car (id, brand, model, horse_power, fuel, torque) values (1, 'VW', 'Tiguan', 200, 'Petrol', '400Nm');