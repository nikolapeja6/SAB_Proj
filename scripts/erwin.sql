
USE [pn140041];
GO

CREATE TABLE [Admin]
( 
	[IDAdmin]            integer  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([IDAdmin] ASC)
go

CREATE TABLE [City]
( 
	[IDCity]             integer  IDENTITY  NOT NULL ,
	[NameCity]           varchar(20)  NOT NULL ,
	[PostalCode]         integer  NOT NULL 
)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XPKCity] PRIMARY KEY  CLUSTERED ([IDCity] ASC)
go

ALTER TABLE [City]
	ADD CONSTRAINT [XAK1City] UNIQUE ([PostalCode]  ASC)
go

CREATE TABLE [Courier]
( 
	[IDCourier]          integer  NOT NULL ,
	[Vehicle]            integer  NULL ,
	[DeliveredPackagesCnt] integer  NOT NULL 
	CONSTRAINT [Default_Courier_PackageCnt_1983182672]
		 DEFAULT  0,
	[Profit]             decimal(10,3)  NOT NULL ,
	[Status]             bit  NOT NULL 
)
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [XPKCourier] PRIMARY KEY  CLUSTERED ([IDCourier] ASC)
go

CREATE TABLE [CourierRequest]
( 
	[IDCourierRequest]   integer  IDENTITY  NOT NULL ,
	[IDUser]             integer  NOT NULL ,
	[IDVehicle]          integer  NOT NULL 
)
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [XPKCourierRequest] PRIMARY KEY  CLUSTERED ([IDCourierRequest] ASC)
go

CREATE TABLE [District]
( 
	[IDDistrict]         integer  IDENTITY  NOT NULL ,
	[NameDistrict]       varchar(20)  NOT NULL ,
	[x]                  decimal(10,3)  NOT NULL ,
	[y]                  decimal(10,3)  NOT NULL ,
	[IDCity]             integer  NOT NULL 
)
go

ALTER TABLE [District]
	ADD CONSTRAINT [XPKDistrict] PRIMARY KEY  CLUSTERED ([IDDistrict] ASC)
go

CREATE TABLE [Package]
( 
	[IDPackage]          integer  IDENTITY  NOT NULL ,
	[IDSender]           integer  NULL ,
	[Departure]          integer  NOT NULL ,
	[Arrival]            integer  NOT NULL ,
	[PackageType]        tinyint  NOT NULL ,
	[Weight]             decimal(10,3)  NOT NULL ,
	[IDCourier]          integer  NULL ,
	[Status]             bit  NOT NULL 
	CONSTRAINT [Default_Package_Status_966352575]
		 DEFAULT  0,
	[Price]              integer  NOT NULL ,
	[AcceptanceTime]     datetime  NOT NULL 
)
go

ALTER TABLE [Package]
	ADD CONSTRAINT [XPKPackage] PRIMARY KEY  CLUSTERED ([IDPackage] ASC)
go

CREATE TABLE [TransportOffer]
( 
	[IDTransportOffer]   integer  IDENTITY  NOT NULL ,
	[IDCourier]          integer  NOT NULL ,
	[Percentage]         decimal(10,3)  NOT NULL ,
	[IDRequest]          integer  NOT NULL 
)
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [XPKTransportOffer] PRIMARY KEY  CLUSTERED ([IDTransportOffer] ASC)
go

CREATE TABLE [User]
( 
	[IDUser]             integer  IDENTITY  NOT NULL ,
	[FirstName]          varchar(20)  NOT NULL ,
	[LastName]           varchar(20)  NOT NULL ,
	[username]           varchar(20)  NOT NULL ,
	[password]           varchar(20)  NOT NULL ,
	[SentPackageCnt]     integer  NOT NULL 
	CONSTRAINT [Default_User_PackageCnt_297401158]
		 DEFAULT  0
)
go

ALTER TABLE [User]
	ADD CONSTRAINT [XPKUser] PRIMARY KEY  CLUSTERED ([IDUser] ASC)
go

ALTER TABLE [User]
	ADD CONSTRAINT [XAK1User] UNIQUE ([username]  ASC)
go

CREATE TABLE [Vehicle]
( 
	[IDVehicle]          integer  IDENTITY  NOT NULL ,
	[LicencePlate]       varchar(40)  NOT NULL ,
	[FuelType]           tinyint  NOT NULL ,
	[FuelConsumption]    decimal(10,3)  NOT NULL 
)
go

ALTER TABLE [Vehicle]
	ADD CONSTRAINT [XPKVehicle] PRIMARY KEY  CLUSTERED ([IDVehicle] ASC)
go

ALTER TABLE [Vehicle]
	ADD CONSTRAINT [XAK1Vehicle] UNIQUE ([LicencePlate]  ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([IDAdmin]) REFERENCES [User]([IDUser])
		ON DELETE CASCADE
		ON UPDATE NO ACTION
go


ALTER TABLE [Courier]
	ADD CONSTRAINT [R_28] FOREIGN KEY ([IDCourier]) REFERENCES [User]([IDUser])
		ON DELETE CASCADE
		ON UPDATE NO ACTION
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [R_29] FOREIGN KEY ([Vehicle]) REFERENCES [Vehicle]([IDVehicle])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([IDUser]) REFERENCES [User]([IDUser])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([IDVehicle]) REFERENCES [Vehicle]([IDVehicle])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [District]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IDCity]) REFERENCES [City]([IDCity])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Package]
	ADD CONSTRAINT [R_30] FOREIGN KEY ([IDSender]) REFERENCES [User]([IDUser])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_31] FOREIGN KEY ([Departure]) REFERENCES [District]([IDDistrict])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_32] FOREIGN KEY ([Arrival]) REFERENCES [District]([IDDistrict])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Package]
	ADD CONSTRAINT [R_36] FOREIGN KEY ([IDCourier]) REFERENCES [Courier]([IDCourier])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_34] FOREIGN KEY ([IDCourier]) REFERENCES [Courier]([IDCourier])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_35] FOREIGN KEY ([IDRequest]) REFERENCES [Package]([IDPackage])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
