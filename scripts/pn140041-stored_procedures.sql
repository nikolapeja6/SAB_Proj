

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
	IF EXISTS (SELECT * FROM [User] WHERE username = @username)
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