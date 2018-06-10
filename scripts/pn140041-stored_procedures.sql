

---------------------------------------------------
-- Stored Procedures.
---------------------------------------------------

USE [pn140041];
GO


------------------------------
-- Utils.
------------------------------

-- convert int to varchar 
CREATE FUNCTION fIntToVarchar(@value int)
RETURNS varchar(max)
AS
BEGIN
	RETURN CAST(@value AS varchar(max))
END
GO
------------------------------
-- City operations.
------------------------------

-- insert city
CREATE PROC spInsertCity
@cityName varchar(100),
@postalCode varchar(100),
@id int  output,
@message varchar(max) output
AS
BEGIN
	
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @cityName = '' OR @cityName IS NULL
	BEGIN
		SET @message = '@cityName is null or empty.'
		GOTO error_handling
	END

	IF @postalCode = '' OR @postalCode IS NULL
	BEGIN
		SET @message = '@postalCode is null or empty.'
		GOTO error_handling
	END

	-- check if city with same postal code already exists
	IF EXISTS (SELECT * FROM [City] WHERE PostalCode = @postalCode)
	BEGIN
		SET @message = 'City with postalcode '+ @postalCode+' already exists.'
		GOTO error_handling
	END

	-- check if city with same name already exists
	IF EXISTS (SELECT * FROM [City] WHERE NameCity = @cityName)
	BEGIN
		SET @message = 'City with name '+ @cityName +' already exists.'
		GOTO error_handling
	END

	-- insert
	INSERT INTO [City] VALUES (@cityName, @postalCode)

	-- get id
	SELECT @id = IDCity FROM [City] WHERE NameCity = @cityName AND PostalCode = @postalCode

	RETURN

	-- set id to invalid value, print message and return
	error_handling:

		SET @id = -1
		print @message
		RETURN
END
GO


-- delete city with name
CREATE PROC spDeleteCityName
@name varchar(max),
@ret int output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @name = '' OR @name IS NULL
	BEGIN
		SET @message = '@name is null or empty.'
		GOTO error_handling
	END

	-- check if city with name exists
	IF NOT EXISTS (SELECT * FROM [City] WHERE NameCity = @name)
	BEGIN
		SET @message = 'City with name = ''' + @name + ''' does not exists.'
		GOTO error_handling
	END

	-- delete city
	DELETE FROM [City] WHERE NameCity = @name

	-- reuturn
	SELECT @ret = IDCity FROM [City] WHERE NameCity = @name
	RETURN


	-- set id to invalid value, print message and return
	error_handling:

		SET @ret = -1
		print @message
		RETURN


END
GO

-- delete city with id
CREATE PROC spDeleteCityId
@id int,
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @id = 0 OR @id = -1
	BEGIN
		SET @message = '@id is invalid.'
		GOTO error_handling
	END

	-- check if city with id exists
	IF NOT EXISTS (SELECT * FROM [City] WHERE IDCity = @id)
	BEGIN
		SET @message = 'City with id = ' + CAST(@id AS varchar(max)) + ' does not exists.'
		GOTO error_handling
	END

	-- delete city
	DELETE FROM [City] WHERE IDCity = @id

	-- reuturn
	SET @ret = 1
	RETURN


	-- set id to invalid value, print message and return
	error_handling:

		SET @ret = 0
		print @message
		RETURN

END
GO

-- get all city ids
CREATE PROC spGetAllCities
@ret varchar(max) output
AS
BEGIN
	IF EXISTS (SELECT * FROM [City])
		SELECT @ret = COALESCE(@ret + ',','') + CAST(IDCity AS varchar(max)) FROM [City]
	ELSE
		SET @ret = ''
	RETURN 
END
GO

-------------------------------
-- District operations.
-------------------------------

-- insert district
CREATE PROC spInsertDistrict
@districtName varchar(100),
@idCity int,
@x int,
@y int,
@id int output,
@message varchar(max) output
AS
BEGIN
	
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @districtName = '' OR @districtName IS NULL
	BEGIN
		SET @message = '@districtName is null or empty.'
		GOTO error_handling
	END

	IF @idCity IS NULL OR @idCity = -1
	BEGIN
		SET @message = '@idCity is null or -1.'
		GOTO error_handling
	END


	-- check if city id is valid
	IF NOT EXISTS (SELECT * FROM [City] WHERE IDCity = @idCity)
	BEGIN
		SET @message = 'City with ID  '+ CAST(@idCity AS varchar(max)) +' does not exist.'
		GOTO error_handling
	END

	-- check if district with same name exists in same city
	IF EXISTS (SELECT * FROM [District] WHERE IDCity = @idCity AND NameDistrict = @districtName)
	BEGIN
		SET @message = 'District with name  '+ @districtName +' already exists in city with id = ' + CAST(@idCity AS varchar(max)) + '.'
		GOTO error_handling
	END


	-- insert
	INSERT INTO [District](NameDistrict, IDCity, x, y) VALUES (@districtName, @idCity, @x, @y)

	-- get id
	SELECT @id = IDDistrict FROM [District] WHERE NameDistrict = @districtName AND IDCity = @idCity

	RETURN

	-- set id to invalid value, print message and return
	error_handling:

		SET @id = -1
		print @message
		RETURN
END
GO

-- get all districts from city
CREATE PROC spGetAllDistrictFromCity
@idCity int,
@ret varchar(max) output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF  @idCity IS NULL OR @idCity = -1
	BEGIN
		SET @message = '@idCity is null or -1.'
		GOTO error_handling
	END

	-- check if city with id exists 
	IF NOT EXISTS (SELECT * FROM [City] WHERE IDCity = @idCity)
	BEGIN
		SET @message = 'City with id = ' + CAST(@idCity AS varchar(max)) + ' does not exists.'
		GOTO error_handling
	END

	IF EXISTS (SELECT * FROM [District] WHERE IDCity = @idCity)
		SELECT @ret = COALESCE(@ret + ',','') + CAST(IDDistrict AS varchar(max)) FROM [District] WHERE IDCity = @idCity
	ELSE
		SET @ret = ''
	

	RETURN

	-- print message and return
	error_handling:

		print @message
		RETURN
END
GO

-- get all districts from city
CREATE PROC spGetAllDistricts
@ret varchar(max) output
AS
BEGIN

	IF EXISTS (SELECT * FROM [District])
		SELECT @ret = COALESCE(@ret + ',','') + dbo.fIntToVarchar(IDDistrict) FROM [District]
	ELSE
		SET @ret = ''

	RETURN

END
GO

-- delete district by id
CREATE PROC spDeleteDistrictId
@IdDistrict int,
@ret bit output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF  @IdDistrict IS NULL OR @IdDistrict = -1
	BEGIN
		SET @message = '@IdDistrict is null or -1.'
		GOTO error_handling
	END

	-- check if district with id exists 
	IF NOT EXISTS (SELECT * FROM [District] WHERE IDDistrict = @IdDistrict)
	BEGIN
		SET @message = 'District with id = ' + dbo.fIntToVarchar(@IdDistrict) + 'does not exist.'
		GOTO error_handling
	END

	DELETE FROM [District] WHERE IDDistrict = @IdDistrict

	SET @ret = 1

	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- delete district by name
CREATE PROC spDeleteDistrictName
@districtName varchar(max),
@ret bit output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF  @districtName IS NULL OR @districtName = ''
	BEGIN
		SET @message = '@districtName is null or empty.'
		GOTO error_handling
	END

	-- check if district with id exists 
	IF NOT EXISTS (SELECT * FROM [District] WHERE NameDistrict = @districtName)
	BEGIN
		SET @message = 'District with name = ' + @districtName + 'does not exist.'
		GOTO error_handling
	END

	DELETE FROM [District] WHERE NameDistrict = @districtName

	SET @ret = 1

	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- delete all districts by city id
CREATE PROC spDeleteAllDistrictsFromCityId
@cityId int,
@ret int output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF  @cityId IS NULL OR @cityId = -1
	BEGIN
		SET @message = '@cityId is null or -1.'
		GOTO error_handling
	END

	-- check if city with id exists 
	IF NOT EXISTS (SELECT * FROM [City] WHERE IDCity = @cityId)
	BEGIN
		SET @message = 'City with id = ' + dbo.fIntToVarchar(@cityId)+ 'does not exist.'
		GOTO error_handling
	END

	-- set cnt
	SELECT @ret = COUNT(*) FROM [District] WHERE IDCity = @cityId

	DELETE FROM [District] WHERE IDCity = @cityId

	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- delete all districts by city name
CREATE PROC spDeleteAllDistrictsFromCityName
@cityName varchar(100),
@ret int output,
@message varchar(max) output
AS
BEGIN
	
	SET @ret = 0

	-- check if @cityName is null or empty
	IF @cityName IS NULL OR @cityName = ''
	BEGIN
		SET @message = '@cityName is null or empty.'
		RETURN
	END

	-- check if city with name exists
	IF NOT EXISTS (SELECT * FROM [City] WHERE NameCity = @cityName)
	BEGIN
		SET @message = 'City with name = ' + @cityName + ' does not exist.'
		RETURN
	END

	DECLARE @cityId int
	SELECT @cityId = IDCity FROM [City] WHERE NameCity = @cityName

	EXEC spDeleteAllDistrictsFromCityId @cityId, @ret output , @message output 

	RETURN

END
GO


-------------------------------
-- User operations.
-------------------------------

-- insert user.
CREATE PROC spInsertUser
@username varchar(100),
@firstName varchar(100),
@lastName varchar(100),
@password varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END
	
	IF @firstName IS NULL OR @firstName = ''
	BEGIN
		SET @message = '@firstName is null or empty.'
		GOTO error_handling
	END

	IF @lastName IS NULL OR @lastName = ''
	BEGIN
		SET @message = '@lastName is null or empty.'
		GOTO error_handling
	END

	IF @password IS NULL OR @password = ''
	BEGIN
		SET @message = '@password is null or empty.'
		GOTO error_handling
	END

	-- check if user with username exists
	IF EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' already exists.'
		GOTO error_handling
	END

	INSERT INTO [User](username, firstName, lastName, [password])
	VALUES(@username, @firstName, @lastName, @password)

	SET @ret = 1
	
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- get all users
CREATE PROC spGetAllUsers
@ret varchar(max) output
AS
BEGIN

	IF EXISTS (SELECT * FROM [User])
		SELECT @ret = COALESCE(@ret + ',','') + username FROM [User]
	ELSE
		SET @ret = ''

	RETURN	

END
GO

-- delete user
CREATE PROC spDeleteUser
@username varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN
	
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END
	

	-- check if user with username exists
	IF NOT EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' does not exist.'
		GOTO error_handling
	END

	DELETE FROM [User] WHERE username = @username

	SET @ret = 1

	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- declare admin
CREATE PROC spDeclareAdmin
@username varchar(100),
@ret int output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	-- check if user with username exists
	IF NOT EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' does not exist.'
		SET @ret = 2
		GOTO error_handling
	END

	IF EXISTS (SELECT * FROM [Admin] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' is already admin.'
		SET @ret = 1
		GOTO error_handling
	END

	-- insert into admin
	INSERT INTO [Admin] VALUES(@username)

	SET @ret = 0

	RETURN

	-- print message and return
	error_handling:
		
		print @message
		RETURN

END
GO

-- get sent packages
CREATE PROC spGetSentPackages
@username varchar(100),
@sentPackages int output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	-- check if user with username exists
	IF NOT EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' does not exist.'
		GOTO error_handling
	END

	-- get sent packages
	SELECT @sentPackages = SentPackageCnt FROM [User] WHERE [username] = @username

	RETURN
	
	-- print message and return
	error_handling:
		
		SET @sentPackages = -1
		print @message
		RETURN
END
GO
 
-------------------------------
-- Vehicle operations.
-------------------------------

-- insert vehicle
CREATE PROC spInsertVehicle
@licence varchar(100),
@fuelType int,
@fuelConsumption decimal(38,8),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @licence IS NULL OR @licence = ''
	BEGIN
		SET @message = '@licence is null or empty.'
		GOTO error_handling
	END

	IF @fuelType IS NULL OR @fuelType < 0
	BEGIN
		SET @message = '@fuelType is null or invalid.'
		GOTO error_handling
	END

	IF @fuelConsumption IS NULL OR @fuelConsumption <= 0
	BEGIN
		SET @message = '@fuelConsumption is null or invalid.'
		GOTO error_handling
	END

	-- check if fuel type is valid
	DECLARE @propane int
	DECLARE @diesel int
	DECLARE @naturalGas int

	SET @propane	= 0
	SET @diesel		= 1
	SET @naturalGas = 2

	IF @fuelType <> @propane AND @fuelType <> @diesel AND @fuelType <> @naturalGas
	BEGIN
		EXEC xp_sprintf @message OUTPUT, 'FuelType = %d is neighter propane(%d), diesel(%d) nor naturl_gas(%d).', @fuelType, @propane, @diesel, @naturalGas
		GOTO error_handling
	END

	-- check if licence plate already exists
	IF EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licence)
	BEGIN
		SET @message = 'Vehicle with licence plate = '+@licence + ' already exists.'
		GOTO error_handling
	END

	-- insert
	INSERT INTO [Vehicle](LicencePlate, FuelType, FuelConsumption) VALUES(@licence, @fuelType, @fuelConsumption)

	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- delete vehicle
CREATE PROC spDeleteVehicle
@licence varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @licence IS NULL OR @licence = ''
	BEGIN
		SET @message = '@licence is null or empty.'
		GOTO error_handling
	END

	-- check if vehicle with licence plate exists
	IF NOT EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licence)
	BEGIN
		SET @message = 'Vehicle with licence_plate = ' + @licence + ' does not exist.'
		GOTO error_handling
	END

	-- delete
	DELETE FROM [Vehicle] WHERE LicencePlate = @licence

	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- get all vehicles
CREATE PROC spGetAllVehicles
@ret varchar(max) output
AS
BEGIN
	IF EXISTS (SELECT * FROM [Vehicle])
		SELECT @ret = COALESCE(@ret + ',','') + LicencePlate FROM [Vehicle]
	ELSE
		SET @ret = ''

	RETURN	
END
GO

-- change fuel type
CREATE PROC spChangeFuelType
@licence varchar(100),
@fuelType int,
@ret bit output,
@message varchar(max) output
AS
BEGIN
	
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @licence IS NULL OR @licence = ''
	BEGIN
		SET @message = '@licence is null or empty.'
		GOTO error_handling
	END

	-- check if licence plate exists
	IF NOT EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licence)
	BEGIN
		SET @message = 'Vehicle with licence plate = '+@licence + ' does not exists.'
		GOTO error_handling
	END

	-- check if fuel type is valid
	DECLARE @propane int
	DECLARE @diesel int
	DECLARE @naturalGas int

	SET @propane	= 0
	SET @diesel		= 1
	SET @naturalGas = 2

	IF @fuelType <> @propane AND @fuelType <> @diesel AND @fuelType <> @naturalGas
	BEGIN
		EXEC xp_sprintf @message OUTPUT, 'FuelType = %d is neighter propane(%d), diesel(%d) nor naturl_gas(%d).', @fuelType, @propane, @diesel, @naturalGas
		GOTO error_handling
	END

	-- update
	UPDATE [Vehicle]
	SET FuelType = @fuelType
	WHERE LicencePlate = @licence

	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- change fuel consumption
CREATE PROC spChangeFuelConsumption
@licence varchar(100),
@fuelConsumption decimal(38,8),
@ret bit output,
@message varchar(max) output
AS
BEGIN
	
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @licence IS NULL OR @licence = ''
	BEGIN
		SET @message = '@licence is null or empty.'
		GOTO error_handling
	END

	IF @fuelConsumption IS NULL OR @fuelConsumption  <= 0
	BEGIN
		SET @message = '@fuelConsumption is null or invalid.'
		GOTO error_handling
	END

	-- check if licence plate exists
	IF NOT EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licence)
	BEGIN
		SET @message = 'Vehicle with licence plate = '+@licence + ' does not exists.'
		GOTO error_handling
	END

	-- update
	UPDATE [Vehicle]
	SET FuelConsumption = @fuelConsumption
	WHERE LicencePlate = @licence

	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO


-------------------------------
-- Courier request operations.
-------------------------------

-- insert courier request
CREATE PROC spInsertCourierRequest
@username varchar(100),
@licencePlate varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	IF @licencePlate IS NULL OR @licencePlate = ''
	BEGIN
		SET @message = '@licencePlate is null or empty.'
		GOTO error_handling
	END

	-- check if user with username exists
	IF NOT EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' does not exist.'
		GOTO error_handling
	END

	-- check if vehicle with licence exists
	IF NOT EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licencePlate)
	BEGIN
		SET @message = 'Vehicle with LicencePlate = ' + @licencePlate + ' does not exist.'
		GOTO error_handling
	END

	-- check if request for same user already exists
	IF EXISTS (SELECT * FROM [CourierRequest] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' already submited a request.'
		GOTO error_handling
	END

	-- insert
	INSERT INTO [CourierRequest](username, LicencePlate) VALUES(@username, @licencePlate)

	-- return
	SET @ret = 1
	RETURN 

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN
END
GO

-- delete request
CREATE PROC spDeleteCourierRequest
@username varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	-- check if request exists
	IF NOT EXISTS (SELECT * FROM [CourierRequests] WHERE username = @username)
	BEGIN
		SET @message = 'Request for user with username = ' + @username + ' was not submitted.'
		GOTO error_handling
	END

	-- delete
	DELETE FROM [CourierRequest] WHERE username = @username

	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO


-- change vehicle in courier request
CREATE PROC spChangeVehicleInCourierRequest
@username varchar(100),
@licencePlate varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	IF @licencePlate IS NULL OR @licencePlate = ''
	BEGIN
		SET @message = '@licencePlate is null or empty.'
		GOTO error_handling
	END

	-- check if vehicle exists
	IF NOT EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licencePlate)
	BEGIN
		SET @message = 'Vehicle with licence plate = ' + @licencePlate + ' does not exist.'
		GOTO error_handling
	END

	-- check if request exists
	IF NOT EXISTS (SELECT * FROM [CourierRequests] WHERE username = @username)
	BEGIN
		SET @message = 'Request for user with username = ' + @username + ' was not submitted.'
		GOTO error_handling
	END

	-- update
	UPDATE [CourierRequest]
	SET LicencePlate = @licencePlate
	WHERE username = @username

	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN
END
GO


-- get all courier requests
CREATE PROC spGetAllCourierRequests
@ret varchar(max) output
AS
BEGIN

	IF EXISTS (SELECT * FROM [CourierRequest])
		SELECT @ret = COALESCE(@ret + ',','') + username FROM [CourierRequest]
	ELSE
		SET @ret = ''

	RETURN	
END
GO

-- grant request
CREATE PROC spGrantCourierRequest
@username varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	-- check if user submitted request
	IF NOT EXISTS (SELECT * FROM [CourierRequest] WHERE username = @username)
	BEGIN
		SET @message = 'Courier request for user with username =' + @username + ' was not submitted.'
		GOTO error_handling
	END

	-- check if user exists
	IF NOT EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username =' + @username + ' does not exist.'
		GOTO error_handling
	END

	-- fetch vehicle licence plate
	DECLARE @licencePlate varchar(100)
	SELECT @licencePlate = LicencePlate FROM [CourierRequest] WHERE username = @username

	-- check if vehicle exists
	IF NOT EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licencePlate)
	BEGIN
		SET @message = 'Vehicle with licence_place =' + @licencePlate + ' does not exist.'
		GOTO error_handling
	END

	-- check if courier with same licence plate exists
	IF EXISTS (SELECT * FROM [Courier] WHERE LicencePlate = @licencePlate)
	BEGIN
		SET @message = 'Courier with vehicel with licence plate =' + @licencePlate + ' already exists.'
		GOTO error_handling
	END

	-- check if courier with same username exists
	IF EXISTS (SELECT * FROM [Courier] WHERE username = @username)
	BEGIN
		SET @message = 'Courier with username =' + @username + ' already exists.'
		GOTO error_handling
	END

	-- insert into courier
	INSERT INTO Courier(username, LicencePlate) VALUES(@username, @licencePlate)

	-- delete request for user
	DELETE FROM [CourierRequest] WHERE username = @username

	-- return
	SET @ret = 1
	RETURN
	
	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN
END
GO

-------------------------------
-- Courier operations.
-------------------------------

-- delete courier
CREATE PROC spDeleteCourier
@username varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	-- see if courier with username exists
	IF NOT EXISTS (SELECT * FROM [Courier] WHERE username = @username)
	BEGIN
		SET @message = 'Courier with username = ' + @username + 'does not exist.'
		GOTO error_handling
	END

	-- delete courier
	DELETE FROM [Courier] WHERE username = @username

	-- TODO (nipej): see if update of other things is needed.

	-- return
	SET @ret = 1
	RETURN
	
	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN
END
GO

-- insert courier
CREATE PROC spInsertCourier
@username varchar(100),
@licencePlate varchar(100),
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	IF @licencePlate IS NULL OR @licencePlate = ''
	BEGIN
		SET @message = '@licencePlate is null or empty.'
		GOTO error_handling
	END

	-- check if user with username exits
	IF NOT EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' does not exist.'
		GOTO error_handling
	END

	-- check if vehicle with licence place exists
	IF NOT EXISTS (SELECT * FROM [Vehicle] WHERE LicencePlate = @licencePlate)
	BEGIN
		SET @message = 'Vehicle with LicencePlate = ' + @licencePlate+ ' does not exist.'
		GOTO error_handling
	END

	-- see if courier with same username exists
	IF EXISTS (SELECT * FROM [Courier] WHERE username = @username)
	BEGIN
		SET @message = 'Courier with username = ' + @username + ' already exist.'
		GOTO error_handling
	END

	-- see if courier with same vehicle exists
	IF EXISTS (SELECT * FROM [Courier] WHERE LicencePlate = @licencePlate)
	BEGIN
		SET @message = 'Courier with vehicle licence plate = ' + @licencePlate + ' already exist.'
		GOTO error_handling
	END


	-- insert
	INSERT INTO [Courier](username, LicencePlate) VALUES(@username, @licencePlate)

	-- return
	SET @ret = 1
	RETURN
	
	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- get couriers with status
CREATE PROC spGetCouriersWithStatus
@status int,
@ret varchar(max) output
AS
BEGIN
	IF EXISTS (SELECT * FROM [Courier] WHERE [Status] = @status)
		SELECT @ret = COALESCE(@ret + ',','') + username FROM [Courier] WHERE [Status] = @status
	ELSE
		SET @ret = ''

	RETURN	
END
GO

-- get all couriers
CREATE PROC spGetAllCouriers
@ret varchar(max) output
AS
BEGIN
	IF EXISTS (SELECT * FROM [Courier])
		SELECT @ret = COALESCE(@ret + ',','') + username FROM [Courier]
	ELSE
		SET @ret = ''

	RETURN	
END
GO

-- get average courier profit
CREATE PROC spGetAverageCourierProfit
@numOfDeliveries int,
@res decimal(38,8) output
AS
BEGIN

	DECLARE @cnt int
	DECLARE @sum int

	SELECT @sum = SUM(Profit), @cnt = COUNT(username)
	FROM [Courier]
	WHERE DeliveredPackagesCnt >= @numOfDeliveries

	IF @cnt IS NOT NULL AND @cnt <> 0
		SET @res = CAST(@sum AS decimal(38,8)) / @cnt
	ELSE
		SET @res = 0
	RETURN 
END
GO

-------------------------------
-- Package operations.
-------------------------------

CREATE FUNCTION fCalculatePackagePrice(@packageId int)
RETURNS decimal(38,8)
AS
BEGIN
	DECLARE @initial_price int
	DECLARE @weight_factor int
	DECLARE @weight decimal(38,8)
	DECLARE @price_per_kg int

	DECLARE @id_from int
	DECLARE @id_to int
	DECLARE @packageType int

	DECLARE @x1 int
	DECLARE @y1 int
	DECLARE @x2 int
	DECLARE @y2 int
	DECLARE @euclidian_distance decimal(38,8)

	DECLARE @ret decimal(38,8)

	DECLARE @letter int
	DECLARE @standard int
	DECLARE @fragile int


	-- actual values
	SELECT @weight = [Weight], @id_from = Departure, @id_to = Arrival, @packageType = PackageType
	FROM [Package] WHERE IDPackage = @packageId

	SELECT @x1 = x, @y1 = y
	FROM [District] WHERE IDDistrict = @id_from

	SELECT @x2 = x, @y2 = y
	FROM [District] WHERE IDDistrict = @id_to

	SET @euclidian_distance = SQRT(SQUARE(@x1 - @x2) + SQUARE(@y1 - @y2))

	-- constant things

	SET @letter		= 0
	SET @standard	= 1
	SET @fragile	= 2

	SELECT @initial_price = 
	CASE 
		WHEN @packageType = @letter THEN	10
		WHEN @packageType = @standard THEN	25
		WHEN @packageType = @fragile THEN	75
	END

	SELECT @weight_factor = 
	CASE 
		WHEN @packageType = @letter THEN	0
		WHEN @packageType = @standard THEN	1
		WHEN @packageType = @fragile THEN	2
	END

	SELECT @price_per_kg = 
	CASE 
		WHEN @packageType = @letter THEN	0
		WHEN @packageType = @standard THEN	100
		WHEN @packageType = @fragile THEN	300
	END


	SET @ret = (@initial_price + (@weight_factor * @weight) * @price_per_kg) * @euclidian_distance

	RETURN @ret

END
GO

-- insert package
CREATE PROC spInsertPackage
@districtFrom int,
@districtTo int,
@username varchar(100),
@packageType int,
@weight decimal(38,8),
@id int output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or empty.'
		GOTO error_handling
	END

	IF @districtFrom IS NULL OR @districtFrom < 0
	BEGIN
		SET @message = '@districtFrom is null or invalid.'
		GOTO error_handling
	END

	IF @districtTo IS NULL OR @districtTo < 0
	BEGIN
		SET @message = '@districtTo is null or invalid.'
		GOTO error_handling
	END

	IF @packageType IS NULL OR @packageType < 0
	BEGIN
		SET @message = '@paskageType is null or invalid.'
		GOTO error_handling
	END

	IF @weight IS NULL OR @weight <= 0
	BEGIN
		SET @message = '@weight is null or invalid.'
		GOTO error_handling
	END

	-- check if @username is a valid username
	IF NOT EXISTS (SELECT * FROM [User] WHERE username = @username)
	BEGIN
		SET @message = 'User with username = ' + @username + ' does not exist.'
		GOTO error_handling
	END

	-- check if @districtFrom is a valid district id
	IF NOT EXISTS (SELECT * FROM [District] WHERE IDDistrict = @districtFrom)
	BEGIN
		SET @message = 'District with id = ' + @districtFrom + ' does not exist.'
		GOTO error_handling
	END

	-- check if @districtTo is a valid district id
	IF NOT EXISTS (SELECT * FROM [District] WHERE IDDistrict = @districtTo)
	BEGIN
		SET @message = 'District with id = ' + @districtTo + ' does not exist.'
		GOTO error_handling
	END

	-- check package type validity
	DECLARE @letter int
	DECLARE @standard int
	DECLARE @fragile int

	SET @letter		= 0
	SET @standard	= 1
	SET @fragile	= 2
	
	IF @packageType <> @letter AND @packageType <> @standard AND @packageType <> @fragile
	BEGIN
		EXEC xp_sprintf @message OUTPUT, 'PackageType = %d is neighter letter(%d), standard(%d) nor fragile(%d).', @packageType, @letter, @standard, @fragile
		GOTO error_handling
	END 


	-- calculate price
	DECLARE @price decimal(38,8)
	SET @price = 0

	-- insert new package
	INSERT INTO  [Package]([Departure], [Arrival], [PackageType], [Weight], [Price]) VALUES (@districtFrom, @districtTo, @packageType, @weight, 0)

	-- fetch id
	SELECT @id = MAX(IDPackage) FROM [Package]


	-- update price
	SET @price = dbo.fCalculatePackagePrice(@id)

	UPDATE [Package]
	SET Price = @price
	WHERE IDPackage = @id

	-- return
	RETURN 

	-- print message and return
	error_handling:
		
		SET @id = -1
		print @message
		RETURN
END
GO

-- insert transport offer
CREATE PROC spInsertTransportOffer
@courierUsername varchar(100),
@packageId int,
@percent decimal(38,8),
@id int output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @courierUsername IS NULL OR @courierUsername = ''
	BEGIN
		SET @message = '@courierUsername is null or empty.'
		GOTO error_handling
	END
	
	IF @packageId IS NULL OR @packageId < 0
	BEGIN
		SET @message = '@packageId is null or invalid.'
		GOTO error_handling
	END

	IF @percent IS NULL OR @percent < 0
	BEGIN
		SET @message = '@percent is null or invalid.'
		GOTO error_handling
	END

	-- check if courier exists
	IF NOT EXISTS (SELECT * FROM [Courier] WHERE username = @courierUsername)
	BEGIN
		SET @message = 'Couriser with username = ' + @courierUsername + ' does not exist.'
		GOTO error_handling
	END

	-- check if package exists
	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId)
	BEGIN
		SET @message = 'Package with id = ' + dbo.fIntToVarchar(@packageId) + ' does not exist.'
		GOTO error_handling
	END

	-- check if package accepts offers
	DECLARE @createdStatus int
	SET @createdStatus = 0

	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId AND [Status] = @createdStatus)
	BEGIN
		SET @message = 'Package with id = ' + dbo.fIntToVarchar(@packageId) + ' has alrady accepted an offer.'
		GOTO error_handling
	END

	-- insert new offer
	INSERT INTO [TransportOffer]([Percentage], [IDRequest], [username]) VALUES(@percent, @packageId, @courierUsername)

	-- fetch id
	SELECT @id = MAX(IDTransportOffer) FROM [TransportOffer]

	--return
	RETURN
	
	-- print message and return
	error_handling:
		
		SET @id = -1
		print @message
		RETURN
END
GO

-- accept offer 
CREATE PROC spAcceptOffer
@offerId int,
@ret bit output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @offerId IS NULL OR @offerId < 0
	BEGIN
		SET @message = '@offerId is null or invalid.'
		GOTO error_handling
	END

	-- check if offer exists
	IF NOT EXISTS (SELECT * FROM [TransportOffer] WHERE IDTransportOffer = @offerId)
	BEGIN
		SET @message = 'Offer with id = ' + dbo.fIntToVarchar(@offerId) + ' does not exist.' 
		GOTO error_handling
	END

	-- check if package isn't accepted
	DECLARE @packageId int
	DECLARE @notAcceptedStatus int
	DECLARE @username varchar(100)

	SELECT @packageId = IDRequest, @username = username FROM [TransportOffer] WHERE IDTransportOffer = @offerId
	SET @notAcceptedStatus = 0

	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId AND [Status] = @notAcceptedStatus)
	BEGIN
		SET @message = 'Package with id = ' + dbo.fIntToVarchar(@packageId) + ' referenced by offer has already accepted offer.' 
		GOTO error_handling
	END

	-- update package 
	DECLARE @current datetime
	SELECT @current = GETDATE()
	
	UPDATE [Package]
	SET username = @username, AcceptanceTime = @current, [Status] = 1
	WHERE IDPackage = @packageId

	-- delete all other offers
	DELETE FROM [TransportOffer]
	WHERE IDRequest = @packageId AND username <> @username


	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN
END
GO


-- get all offers
CREATE PROC spGetAllOffers
@ret varchar(max) output
AS
BEGIN
	IF EXISTS (SELECT * FROM [TransportOffer])
		SELECT @ret = COALESCE(@ret + ',','') + IDTransportOffer FROM [TransportOffer]
	ELSE
		SET @ret = ''

	RETURN	
END
GO


-- get all offers for package
CREATE PROC spGetAllOffersForPackage
@packageId int,
@ret varchar(max) output
AS
BEGIN
	IF EXISTS (SELECT * FROM [TransportOffer] WHERE IDRequest = @packageId)
		SELECT @ret = COALESCE(@ret + ',','') + '(' + IDTransportOffer + ',' + [Percentage] + ')' FROM [TransportOffer] WHERE IDRequest = @packageId
	ELSE
		SET @ret = ''

	RETURN	
END
GO

-- delete package
CREATE PROC spDeletePackage
@packageId int,
@ret bit output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @packageId IS NULL OR @packageId < 0
	BEGIN
		SET @message = '@packageId is null or invalid.'
		GOTO error_handling
	END


	-- check if package exists
	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId)
	BEGIN
		SET @message = 'Package with id = ' + @packageId + ' does not exist.'
		GOTO error_handling
	END

	-- delete from package
	DELETE FROM [Package] WHERE IDPackage = @packageId

	-- delete from offer
	DELETE FROM [TransportOffer] WHERE IDRequest = @packageId

	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN
END
GO

-- change package weight
CREATE PROC spChangePackageWeight
@packageId int,
@weight decimal(38,8),
@ret bit output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @packageId IS NULL OR @packageId < 0
	BEGIN
		SET @message = '@packageId is null or invalid.'
		GOTO error_handling
	END

	IF @weight IS NULL OR @weight < 0
	BEGIN
		SET @message = '@weight is null or invalid.'
		GOTO error_handling
	END

	-- check if package exists
	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId)
	BEGIN
		SET @message = 'Package with id = ' + @packageId + ' does not exist.'
		GOTO error_handling
	END

	-- update
	UPDATE [Package]
	SET [Weight] = @weight
	WHERE IDPackage = @packageId

	DECLARE @price decimal(38,8)
	SET @price = dbo.fCalculatePackagePrice(@packageId)

	UPDATE [Package]
	SET [Price] = @price
	WHERE IDPackage = @packageId


	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- change package type
CREATE PROC spChangePackageType
@packageId int,
@type int,
@ret bit output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @packageId IS NULL OR @packageId < 0
	BEGIN
		SET @message = '@packageId is null or invalid.'
		GOTO error_handling
	END

	IF @type IS NULL OR @type < 0 OR @type > 2
	BEGIN
		SET @message = '@type is null or invalid.'
		GOTO error_handling
	END

	-- check if package exists
	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId)
	BEGIN
		SET @message = 'Package with id = ' + @packageId + ' does not exist.'
		GOTO error_handling
	END

	-- update
	UPDATE [Package]
	SET [PackageType] = @type
	WHERE IDPackage = @packageId

	DECLARE @price decimal(38,8)
	SET @price = dbo.fCalculatePackagePrice(@packageId)

	UPDATE [Package]
	SET [Price] = @price
	WHERE IDPackage = @packageId


	-- return
	SET @ret = 1
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = 0
		print @message
		RETURN

END
GO

-- get delivery status
CREATE PROC spGetDeliveryStatus
@packageId int,
@status int output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @packageId IS NULL OR @packageId < 0
	BEGIN
		SET @message = '@packageId is null or invalid.'
		GOTO error_handling
	END

	-- check if package exists
	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId)
	BEGIN
		SET @message = 'Package with id = ' + dbo.fIntToVarchar(@packageId) + ' does not exist.'
		GOTO error_handling
	END

	-- get status
	SELECT @status = [Status] FROM [Package] WHERE IDPackage = @packageId


	-- return
	RETURN

	-- print message and return
	error_handling:
		
		SET @status = -1
		print @message
		RETURN

END
GO

-- get delivery price
CREATE PROC spGetDeliveryPrice
@packageId int,
@price decimal(38,8) output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @packageId IS NULL OR @packageId < 0
	BEGIN
		SET @message = '@packageId is null or invalid.'
		GOTO error_handling
	END

	-- check if package exists
	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId)
	BEGIN
		SET @message = 'Package with id = ' + @packageId + ' does not exist.'
		GOTO error_handling
	END

	-- get status
	SET @price = dbo.fCalculatePackagePrice(@packageId)

	UPDATE [Package]
	SET Price = @price
	WHERE IDPackage = @packageId

	-- return
	RETURN

	-- print message and return
	error_handling:
		
		SET @price = -1
		print @message
		RETURN

END
GO

-- get acceptance time
CREATE PROC spGetAcceptanceTime
@packageId int,
@ret datetime output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @packageId IS NULL OR @packageId < 0
	BEGIN
		SET @message = '@packageId is null or invalid.'
		GOTO error_handling
	END

	-- check if package exists
	IF NOT EXISTS (SELECT * FROM [Package] WHERE IDPackage = @packageId)
	BEGIN
		SET @message = 'Package with id = ' + @packageId + ' does not exist.'
		GOTO error_handling
	END

	-- get status
	SELECT @ret = [AcceptanceTime] FROM [Package] WHERE IDPackage = @packageId


	-- return
	RETURN

	-- print message and return
	error_handling:
		
		SET @ret = NULL
		print @message
		RETURN

END
GO


-- get all packages with type
CREATE PROC spGetAllPackagesWithType
@type int,
@ret varchar(max) output,
@message varchar(max) output
AS
BEGIN
	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @type IS NULL OR @type < 0 OR @type > 2
	BEGIN
		SET @message = '@type is null or invalid.'
		GOTO error_handling
	END


	IF EXISTS (SELECT * FROM [Package] WHERE PackageType = @type)
		SELECT @ret = COALESCE(@ret + ',','') + IDPackage FROM [Package] WHERE PackageType = @type
	ELSE
		SET @ret = ''

	RETURN	

	-- print message and return
	error_handling:
		
		SET @ret = ''
		print @message
		RETURN

END
GO

-- get all packages 
CREATE PROC spGetAllPackages
@ret varchar(max) output
AS
BEGIN
	
	IF EXISTS (SELECT * FROM [Package])
		SELECT @ret = COALESCE(@ret + ',','') + IDPackage FROM [Package] 
	ELSE
		SET @ret = ''

	RETURN
END
GO


-- get drive
CREATE PROC spGetDrive
@username varchar(100),
@ret varchar(max) output
AS
BEGIN

	SET @ret = NULL
	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
		RETURN

	IF EXISTS (SELECT * FROM [Package] WHERE username = @username AND [Status] = 2)
		SELECT @ret = COALESCE(@ret + ',','') + IDPackage FROM [Package] WHERE username = @username AND [Status] = 2
	ELSE
		SET @ret = ''

	RETURN	

END
GO

-- function that calculates distance between districts
CREATE FUNCTION fCalculateDistance(@idFrom int, @idTo int)
RETURNS decimal(38,8)
AS
BEGIN
	DECLARE @x1 int
	DECLARE @x2 int
	DECLARE @y1 int
	DECLARE @y2 int

	SELECT @x1 = x, @y1 = y
	FROM [District] WHERE IDDistrict = @idFrom

	SELECT @x2 = x, @y2 = y
	FROM [District] WHERE IDDistrict = @idTo

	RETURN SQRT(SQUARE(@x1 - @x2) + SQUARE(@y1 - @y2))

END
GO

-- drive next package
CREATE PROC spDriveNextPackage
@username varchar(100),
@id int output,
@message varchar(max) output
AS
BEGIN

	-- error message
	SET @message = ''

	-------------------------------
	-- check if input is not empty
	-------------------------------
	IF @username IS NULL OR @username = ''
	BEGIN
		SET @message = '@username is null or invalid.'
		GOTO error_handling
	END

	-- see if courier exists
	IF NOT EXISTS (SELECT * FROM [Courier] WHERE username = @username)
	BEGIN
		SET @message = 'Courier with username = ' + @username + ' does not exist.'
		GOTO error_handling
	END

	DECLARE @created int
	DECLARE @accepted int
	DECLARE @progress int
	DECLARE @delivered int

	SET @created	= 0
	SET @accepted	= 1
	SET @progress	= 2
	SET @delivered	= 3

	DECLARE @progress_cnt int
	SELECT @progress_cnt = COUNT([Package].IDPackage) 
	FROM [Package] INNER JOIN [TransportOffer] ON [Package].IDPackage = [TransportOffer].IDRequest
	WHERE [TransportOffer].username = @username AND [Package].[Status] = @progress

	IF @progress_cnt IS NULL OR @progress_cnt = 0
	BEGIN
		-- no delivery in progress
		-- check if a new delivery can be started 
		DECLARE @accepted_cnt int
		SELECT @accepted_cnt = COUNT([Package].IDPackage)
		FROM [Package] INNER JOIN [TransportOffer] ON [Package].IDPackage = [TransportOffer].IDRequest
		WHERE [TransportOffer].username = @username AND [Package].[Status] = @accepted

		IF @accepted_cnt IS NULL OR @accepted_cnt = 0
		BEGIN
			-- no packages for the courier
			-- return -1
			SET @id = -1
			RETURN
		END

		-- start a new drive

		-- update status or accepted packages to in_progress 
		UPDATE [Package]
		SET [Status] = @progress
		WHERE [Status] = @accepted AND IDPackage in (SELECT IDRequest FROM [TransportOffer] WHERE username = @username)

	END

	-- delivery already stared and there are more packages
	-- just fetch next and deliver

	SELECT TOP(1) @id = IDPackage 
	FROM [Package] 
	WHERE username = @username AND [Status] = @progress
	ORDER BY AcceptanceTime ASC

	-- update status of that package
	UPDATE [Package]
	SET [Status] = @delivered
	WHERE IDPackage = @id

	-- update packages delivered for courier
	UPDATE [Courier]
	SET [DeliveredPackagesCnt] = [DeliveredPackagesCnt] + 1
	WHERE username = @username

	-- see if there are any more left
	SELECT @progress_cnt = COUNT(IDPackage) 
	FROM [Package] INNER JOIN [TransportOffer] ON [Package].IDPackage = [TransportOffer].IDRequest
	WHERE [TransportOffer].username = @username AND [Package].[Status] = @progress

	IF @progress_cnt IS NULL OR @progress_cnt = 0
	BEGIN
		-- no more packages in this dirve
		-- calculate profit

		DECLARE @profit decimal(38,8)
		SET @profit = 0

		DECLARE @spent decimal(38,8)
		SET @spent = 0

		DECLARE @fuelPrice int
		DECLARE @fuelConsumption int
		DECLARE @fuelType int

		SELECT @fuelType = [Vehicle].FuelType, @fuelConsumption = [Vehicle].FuelConsumption
		FROM [TransportOffer] INNER JOIN [Courier] ON [TransportOffer].username = [Courier].username INNER JOIN [Vehicle] ON [Vehicle].LicencePlate = [Courier].LicencePlate
		WHERE [TransportOffer].username = @username 

		SELECT @fuelPrice = 
		CASE 
			WHEN @fuelType = 0 THEN 15
			WHEN @fuelType = 1 THEN 32
			WHEN @fuelType = 2 THEN 36
		END

		DECLARE @lastDistrictId int
		DECLARE @idTo int
		DECLARE @idFrom int
		DECLARE @pprice decimal(38,8)
		DECLARE @perc decimal(38,8)

		SET @lastDistrictId = -1
		SET @profit = 0
		SET @spent = 0

		DECLARE @curs CURSOR
		SET @curs = CURSOR FOR 
		SELECT [TransportOffer].[Percentage] AS Perc, [Package].Price AS Pprice, [Package].Departure AS idFrom, [Package].Arrival AS IdTo
		FROM [Package] INNER JOIN [TransportOffer] ON [Package].IDPackage = [TransportOffer].IDRequest
		WHERE [TransportOffer].username = @username AND [Package].[Status] = @delivered

		OPEN @curs

		-- get element
		FETCH NEXT FROM @curs
		INTO @perc, @pprice, @idFrom, @idTo

		WHILE @@FETCH_STATUS = 0
		BEGIN
			
			SET @profit = @profit + ((CAST((100 + @perc) AS decimal(38,8)) / 100) * @pprice)

			IF @lastDistrictId <> -1
				SET @spent = @spent + dbo.fCalculateDistance(@lastDistrictId, @idFrom) * @fuelPrice * @fuelConsumption
			SET @lastDistrictId = @idFrom
			SET @spent = @spent + dbo.fCalculateDistance(@lastDistrictId, @idTo) * @fuelPrice * @fuelConsumption
			
			FETCH NEXT FROM @curs
			INTO @perc, @pprice, @idFrom, @idTo
		END

		CLOSE @curs
		DEALLOCATE @curs

		--SELECT @profit = SUM((CAST((100 + [TransportOffer].[Percentage]) AS decimal(38,8)) / 100) * [Package].Price), @spent = SUM(dbo.fCalculateDistance([Package].Departure, [Package].Arrival) * @fuelPrice * @fuelConsumption)
		--FROM [Package] INNER JOIN [TransportOffer] ON [Package].IDPackage = [TransportOffer].IDRequest
		--WHERE [TransportOffer].username = @username AND [Package].[Status] = @delivered

		SET @profit = @profit - @spent

		-- update profit for courier
		UPDATE [Courier]
		SET Profit = Profit + @profit
		WHERE username = @username


		-- delete all completed offers
		DELETE FROM [TransportOffer]
		WHERE username = @username AND EXISTS (SELECT * FROM [Package] WHERE IDPackage = IDRequest AND [Status] = 3)

	END


	RETURN	

	-- print message and return
	error_handling:
		-- error => return -2
		SET @id = -2
		print @message
		RETURN

END
GO


-------------------------------
-- General operations.
-------------------------------

-- erase all data from tables
CREATE PROC spEraseAll
AS
BEGIN

	DELETE FROM [TransportOffer]
	DELETE FROM [Package]
	DELETE FROM [District]
	DELETE FROM [City]
	DELETE FROM [Admin]
	DELETE FROM [CourierRequest]
	DELETE FROM [Courier]
	DELETE FROM [Vehicle]
	DELETE FROM [User]

END
GO