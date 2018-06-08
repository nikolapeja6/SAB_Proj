

---------------------------------------------------
-- Stored Procedures.
---------------------------------------------------

USE [pn140041];
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

	print @id

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