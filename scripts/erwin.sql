
USE [pn140041];
GO

CREATE TABLE [Admin]
( 
	[username]           varchar(100)  NOT NULL 
)
go

ALTER TABLE [Admin]
	ADD CONSTRAINT [XPKAdmin] PRIMARY KEY  CLUSTERED ([username] ASC)
go

CREATE TABLE [City]
( 
	[IDCity]             integer  IDENTITY  NOT NULL ,
	[NameCity]           varchar(100)  NOT NULL ,
	[PostalCode]         varchar(100)  NOT NULL 
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
	[DeliveredPackagesCnt] integer  NOT NULL 
	CONSTRAINT [Default_Courier_PackageCnt_1983182672]
		 DEFAULT  0,
	[Profit]             decimal(10,3)  NOT NULL 
	CONSTRAINT [Default_Courier_Profit_1586259163]
		 DEFAULT  0,
	[Status]             bit  NOT NULL 
	CONSTRAINT [Default_Courier_Status_1604019690]
		 DEFAULT  0,
	[username]           varchar(100)  NOT NULL ,
	[LicencePlate]       varchar(40)  NULL 
)
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [XPKCourier] PRIMARY KEY  CLUSTERED ([username] ASC)
go

CREATE TABLE [CourierRequest]
( 
	[username]           varchar(100)  NOT NULL ,
	[LicencePlate]       varchar(40)  NOT NULL 
)
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [XPKCourierRequest] PRIMARY KEY  CLUSTERED ([username] ASC)
go

CREATE TABLE [District]
( 
	[IDDistrict]         integer  IDENTITY  NOT NULL ,
	[NameDistrict]       varchar(100)  NOT NULL ,
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
	[Departure]          integer  NOT NULL ,
	[Arrival]            integer  NOT NULL ,
	[PackageType]        tinyint  NOT NULL ,
	[Weight]             decimal(10,3)  NOT NULL ,
	[Status]             smallint  NOT NULL 
	CONSTRAINT [Default_Package_Status_966352575]
		 DEFAULT  0,
	[Price]              integer  NULL ,
	[AcceptanceTime]     datetime  NULL ,
	[username]           varchar(100)  NULL 
)
go

ALTER TABLE [Package]
	ADD CONSTRAINT [XPKPackage] PRIMARY KEY  CLUSTERED ([IDPackage] ASC)
go

CREATE TABLE [TransportOffer]
( 
	[IDTransportOffer]   integer  IDENTITY  NOT NULL ,
	[Percentage]         decimal(10,3)  NOT NULL ,
	[IDRequest]          integer  NOT NULL ,
	[username]           varchar(100)  NOT NULL 
)
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [XPKTransportOffer] PRIMARY KEY  CLUSTERED ([IDTransportOffer] ASC)
go

CREATE TABLE [User]
( 
	[FirstName]          varchar(100)  NOT NULL ,
	[LastName]           varchar(100)  NOT NULL ,
	[username]           varchar(100)  NOT NULL ,
	[password]           varchar(100)  NOT NULL ,
	[SentPackageCnt]     integer  NOT NULL 
	CONSTRAINT [Default_User_PackageCnt_297401158]
		 DEFAULT  0
)
go

ALTER TABLE [User]
	ADD CONSTRAINT [XPKUser] PRIMARY KEY  CLUSTERED ([username] ASC)
go

ALTER TABLE [User]
	ADD CONSTRAINT [XAK1User] UNIQUE ([username]  ASC)
go

CREATE TABLE [Vehicle]
( 
	[LicencePlate]       varchar(40)  NOT NULL ,
	[FuelType]           tinyint  NOT NULL ,
	[FuelConsumption]    decimal(10,3)  NOT NULL 
)
go

ALTER TABLE [Vehicle]
	ADD CONSTRAINT [XPKVehicle] PRIMARY KEY  CLUSTERED ([LicencePlate] ASC)
go

ALTER TABLE [Vehicle]
	ADD CONSTRAINT [XAK1Vehicle] UNIQUE ([LicencePlate]  ASC)
go


ALTER TABLE [Admin]
	ADD CONSTRAINT [R_11] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE CASCADE
		ON UPDATE NO ACTION
go


ALTER TABLE [Courier]
	ADD CONSTRAINT [R_28] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE CASCADE
		ON UPDATE NO ACTION
go

ALTER TABLE [Courier]
	ADD CONSTRAINT [R_29] FOREIGN KEY ([LicencePlate]) REFERENCES [Vehicle]([LicencePlate])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([username]) REFERENCES [User]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [CourierRequest]
	ADD CONSTRAINT [R_15] FOREIGN KEY ([LicencePlate]) REFERENCES [Vehicle]([LicencePlate])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [District]
	ADD CONSTRAINT [R_10] FOREIGN KEY ([IDCity]) REFERENCES [City]([IDCity])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Package]
	ADD CONSTRAINT [R_30] FOREIGN KEY ([username]) REFERENCES [User]([username])
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
	ADD CONSTRAINT [R_36] FOREIGN KEY ([username]) REFERENCES [Courier]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_34] FOREIGN KEY ([username]) REFERENCES [Courier]([username])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [TransportOffer]
	ADD CONSTRAINT [R_35] FOREIGN KEY ([IDRequest]) REFERENCES [Package]([IDPackage])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
