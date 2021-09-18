package com.taxi.servlet;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taxi.db.DBUtil;

public class HistoryServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Statement stmnt=DBUtil.getDBConnection().createStatement();
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			int history_taxi=Integer.parseInt(request.getParameter("taxino"));
			out.print("<h1>History</h1><br>");
			out.print("<h3>History of Taxi number "+history_taxi+"</h3><br>");

			history_taxi-=1;
			ResultSet ern =stmnt.executeQuery("select * from taxibooking where taxi_id = "+(history_taxi+1)+"");
			int i=0;
			int earning = 0;
			while(ern.next())
			{
				int trip_earning = ern.getInt("profit");
				int pick_time = ern.getInt("pick_time");
				int drop_time = ern.getInt("drop_time");
				out.print((i+1)+") Picked "+ern.getString("name")+" from "+ern.getInt("pick_loc")+" to "+ern.getInt("drop_loc")+" and billed "+ern.getInt("cost")+" and earned "+trip_earning);
				out.print(". Picked at "+pick_time/60+" hr "+pick_time%60+" min and Dropped at "+drop_time/60+" hr "+drop_time%60+" min<br>");
				i++;
				earning = earning + trip_earning;
			}
			out.print("<br><b>Net Amount earned = "+earning+"</b><br>");

			if(i==0){
				out.print("<b>No trips thus far!</b>");
			}
			RequestDispatcher re=request.getRequestDispatcher("bookorhistory.html");
			re.include(request, response);
		}
		catch(Exception ex)
	    {
            ex.printStackTrace();
	    }
	}

}