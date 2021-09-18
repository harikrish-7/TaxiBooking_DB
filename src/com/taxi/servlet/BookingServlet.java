package com.taxi.servlet;

import java.util.*;
import java.sql.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taxi.db.DBUtil;
import com.taxi.GeneralData;


public class BookingServlet extends HttpServlet
{
	GeneralData generalDataObj = null;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			if(generalDataObj == null){
				generalDataObj = new GeneralData();
			}
			response.setContentType("text/html");
			int taxi_count=generalDataObj.taxi_count;
			Statement stmnt=DBUtil.getDBConnection().createStatement();
			PrintWriter out = response.getWriter();

			String pick_up_time = request.getParameter("pick_up_time");
			int colonIndex = pick_up_time.indexOf(':');
			int hour=Integer.parseInt(pick_up_time.substring(0,colonIndex));
			int min=Integer.parseInt(pick_up_time.substring(colonIndex+1));
			int pick=Integer.parseInt(request.getParameter("pickloc"));
			int drop=Integer.parseInt(request.getParameter("droploc"));

			if((pick-drop)!=0)
			{
				String name=request.getParameter("name");
				int pick_up_time_int=min+hour*60;
				int travel_time=Math.abs(pick-drop)*15;

				List<Integer> available=new ArrayList<Integer>();
				int present_loc[]=new int[taxi_count];
				/*for(int i=0;i<taxi_count;i++){
					present_loc[i]=1;
				}*/
				for(int i=0;i<taxi_count;i++)
				{
					int presentloc=1,max=-1;
					ResultSet res=stmnt.executeQuery("select drop_time, drop_loc from taxibooking where taxi_id = "+(i+1));
					while(res.next())
					{
						int droptime=res.getInt("drop_time");
						int droploc =res.getInt("drop_loc");

						if(pick_up_time_int>(droptime))
						{
							if(droptime>max)
							{													//Getting location of taxis at time of current booking
								max=droptime;
								presentloc=droploc;
							}
						}
					}
					present_loc[i]=presentloc;
					System.out.println("Present location of "+i+" is "+present_loc[i]);
				}
				int mini_dist=15*(generalDataObj.locat_idx_end-generalDataObj.locat_idx_start+1);
				for(int i=0;i<taxi_count;i++)									//Checking for free taxis
				{
					int lapse = Math.abs(pick-present_loc[i])*15;
					ResultSet free=stmnt.executeQuery("select drop_time, start_time from taxibooking where taxi_id = "+(i+1));
					int fre=1;
					while(free.next())
					{
						fre=0;
						int droptime =free.getInt("drop_time");
						int starttime=free.getInt("start_time");
						int curnt_start=pick_up_time_int-lapse;
						int curnt_end=pick_up_time_int+travel_time;
						if((pick_up_time_int<=droptime)&&(pick_up_time_int>=starttime))
							break;
						else if((curnt_start<=droptime)&&(curnt_start>=starttime))
							break;
						else if((curnt_end  <=droptime)&&(curnt_end  >=starttime))
							break;
						else if((droptime  <=curnt_end)&&(droptime   >=curnt_start))
							break;
						else if((starttime<=curnt_end )&&(starttime  >=curnt_start))
							break;
						fre=1;
					}
					if(fre==1){
						available.add(i);

						if(lapse<mini_dist){
							mini_dist=lapse;
						}
					}
					
				}
				int jour_spend=mini_dist*generalDataObj.penalty;
				int jour_cost=travel_time*generalDataObj.charge;
				if(available.size()==0)
				{
					out.print("Oops! \nTaxi unavailable at specified time and place \n");
				}
				else
				{
					System.out.println("Free are "+available);
					int alloted_taxi;
					if(available.size()==1){
						alloted_taxi=available.get(0);
					}
					else
					{
						for(int i=0;i<available.size();i++)
						{
							int dist=(present_loc[available.get(i)]-pick)*15;
							if(dist<0){
								dist*=-1;										//Retaining mini dist only
							}
							if(dist>mini_dist)
							{
								available.remove(i);
								i--;
							}
						}
						System.out.println("Min dist is "+mini_dist+" and min_dist taxis are "+available);
						if(available.size()==1){
							alloted_taxi=available.get(0);
						}
						else
						{
							int min_earn=999999;
							int ans=0;
							for(int i : available)
							{
								ResultSet ear=stmnt.executeQuery("select sum(profit) earning from taxibooking where taxi_id = "+(i+1));
								int earning=0;
								if(ear.next()){
									earning=ear.getInt("earning");
								}
								if(earning<min_earn)
								{
									min_earn=earning;
									ans=i;
								}
								System.out.println(i+" earned "+earning+" while mini earn is "+min_earn);
							}
							alloted_taxi=ans;
						}
					}
					out.print("<br><h3>Alloted Taxi is Taxi number "+(alloted_taxi+1)+"</h3>");
					out.print("<h3>The price of the journey is "+jour_cost+"</h3>");

					stmnt.executeUpdate("insert into taxibooking(taxi_id, name,pick_loc,drop_loc,start_time,drop_time,cost,profit,pick_time) values ("+(alloted_taxi+1)+",'"+name+"',"+pick+","+drop+","+(pick_up_time_int-mini_dist)+","+(pick_up_time_int+travel_time)+","+jour_cost+","+(jour_cost-jour_spend)+","+(pick_up_time_int)+")");
				}
			}
			else{
				out.print("Oops!<br>Invalid Entry!");
			}

			RequestDispatcher rd=request.getRequestDispatcher("bookorhistory.html");
			rd.include(request, response);
		}
		catch(Exception ex)
	    {
            ex.printStackTrace();
	    }
	}
}