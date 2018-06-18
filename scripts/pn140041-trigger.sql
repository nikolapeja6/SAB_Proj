

-----------------------------------------------
-- Triggers.
-----------------------------------------------

USE [pn140041];
GO

-- trigger that deletes all other offers when an offer is accepted
CREATE TRIGGER TR_TransportOffer_Trigger
ON [Package]
FOR Update
AS
BEGIN

	DECLARE @oldStatus int
	DECLARE @newStatus int
	DECLARE @packageId int

	DECLARE @created int
	DECLARE @accepted int
	SET @created = 0
	SET @accepted = 1


	SELECT @oldStatus = [Status]
	FROM deleted

	SELECT @packageId = IDPackage, @newStatus = [Status]
	FROM inserted

	IF @oldStatus = @created AND @newStatus = @accepted
	BEGIN
		DELETE FROM TransportOffer WHERE IDRequest = @packageId
	END

END
GO