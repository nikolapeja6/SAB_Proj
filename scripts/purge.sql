

-----------------------------
-- Purge.
-----------------------------

USE [master];
GO

-- set single user mode
IF db_id('pn140041') IS NOT NULL
	ALTER DATABASE pn140041 SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
GO

-- drop database
DROP DATABASE IF EXISTS pn140041;
GO
