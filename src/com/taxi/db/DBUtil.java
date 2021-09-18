package com.taxi.db;

import java.sql.*;

public class DBUtil
{
	private static Connection conn = null;
	public static Connection getDBConnection() throws Exception
	{
		if(conn == null){
			try{
				String url="jdbc:mysql://localhost:3306/sample";
				conn = DriverManager.getConnection(url,"root","harik777");
			}
			catch(Exception ex){
				System.out.println("Exception while getting DB Connection!");
				throw new Exception(ex);
			}
		}
		return conn;
	}
	/*
	CREATE TABLE taxibooking (
		bookingid int NOT NULL AUTO_INCREMENT,
		taxi_id int not null,
		name varchar(255),
		start_time int,
		pick_time int,
		drop_time int,
		cost int,
		profit int,
		pick_loc int,
		drop_loc int,
		PRIMARY KEY (bookingid)
	);
	*/
}