# SAB Proj

School project for the **SAB** ([*Softverski Alati Baza podataka*][sab], en. *Database Software Tools*) course at the [School of Electrical Engineering][etf], [University of Belgrade][uni].

The goal of the project was to build an information system for package transport. The operations that can be executed in the system are defined by the interfaces described in the [project statement][statement] and the provided javadoc. The database itself was modeled and forward-engineered using the [erwin Data Modeler][erwin_site] and the additional stored procedures and triggers were written by hand. The database management system used in the project is Microsoft's [SQL Server][mssql], which is why the scripts were written in T-SQL.

## Dependencies

The main part of the project is a Java eclipse project, so in order to run you need to have [jre][jre] or [jdk][jdk] installed, and preferably [eclipse][eclipse].

To view and edit the database model, you will need to have the [erwin Data Modeler][erwin_site] installed.

The SQL scripts are written for  Microsoft's [SQL Server][mssql], so you will need to install it, or [create a SQL Database instance on Azure][azure_database].

## Code structure

The project consists of several parts:
   - the project statement (in [Serbian][sr_statement] and [English][statement]), along with the javadocs for the API that the project needed to implement, is located in the ```docs``` folder.
   - erwin database model, located in the ```erwin``` folder.
   - T-SQL scripts for creating the database (```pre_erwin.sql```), creating the tables (generated from erwin model), creating stored procedures and triggers are all merged into a single script (```pn140041.sql```), as requested by the project statement. There is also the ```purge.sql``` script for purging the contents of the SQL Server created by the other scripts. All the scripts are located in the ```scripts``` folder.
   - the Java eclipse project with the actual app that interacts with the database is located in the ```SAB_proj``` folder.

[sab]: http://si4sab.etf.rs/
[etf]: https://www.etf.bg.ac.rs/en
[uni]: http://www.bg.ac.rs/en/
[statement]: ./docs/SAB_homework_1718.pdf
[erwin_site]: https://erwin.com/products/data-modeler/
[erwin]: ./erwin
[scripts]: ./scripts
[mssql]: https://www.microsoft.com/en-us/sql-server
[nikolapeja6]: https://github.com/nikolapeja6
[jre]: https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
[jdk]: https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[eclipse]: https://www.eclipse.org/downloads/packages/
[azure_database]: https://azure.microsoft.com/en-in/services/sql-database/
[sr_statement]: ./docs/SAB_domaci_1718.pdf
