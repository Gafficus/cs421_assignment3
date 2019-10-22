// Need SQLLite Driver from
// https://bitbucket.org/xerial/sqlite-jdbc/downloads
// Put in same folder as this source code file

// Compiled with javac DBTest.java
// Executed with java -classpath sqlite-jdbc-3.27.2.1.jar DBTest

import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.Arrays;
class basePlane{
	private int NUMVIPSEATS;
	private int NUMLUXSEATS;
	private int vipOnBoard;
	private int luxOnBoard;
	private int planeNumber;
	public int getPlaneNumber(){return planeNumber;}

	private int value; 
	public int getValue(){return value;}

	private String planeID;
	public String getPlaneID(){return planeID;}

	private Date departDate;
	public Date getDate(){return departDate;}

	private ResultSet[] vipSeats;
	private ResultSet[] luxSeats;
	public void seatVIP(ResultSet passengerInfo)
	{
		if(vipOnBoard < vipSeats.length())
		{
			vipSeats[vipOnBoard] = passengerInfo;
			vipOnBoard++;
		}
		else
		{
			if(luxOnBoard + (vipOnBoard-NUMVIPSEATS) < luxSeats)
			{
				luxSeats[luxOnBoard + (vipOnBoard-NUMVIPSEATS)] = passengerInfo;
			}
			else
			{

			}
		}
	}
	public void seatLUX()
	{

	}
	/*Constructor to build a plane object.
	 *planeType is 1,2,or 3 determined by the preference of the passenger
	 *theDate is the desired date the passenger will be leaving
	 */
	public basePlane(int planeType, String theDate) throws ClassNotFoundException, SQLException
	{
		Connection con;
		ResultSet res;
		Statement state;
		state = con.createStatement();
		res = state.executeQuery("select tuid, plane_id, max_vip, max_luxury from planes_table where tuid = "+ planeType);
		NUMVIPSEATS = rs.getInt(max_vip);
		NUMLUXSEATS = rs.getInt(max_luxury);
		vipSeats = new ResultSet[max_vip];
		luxSeats = new ResultSet[max_luxury];
		departDate = new Date(theDate);
		vipOnBoard = 0;
		luxOnBoard = 0;

	}
}

public class assignment3 
{ 
	public Vector planeRC407;
	public Vector planeTR707;
	public Vector planeKR381;
  private static Connection getConnection() throws ClassNotFoundException, SQLException 
  {
    Connection con;

    // Database path -- if it's new database, it will be created in the project folder
    con = DriverManager.getConnection("jdbc:sqlite:SQLiteTest1.db");
    return con; 
  }

  private static void buildDatabase(boolean DBExists) throws ClassNotFoundException, SQLException 
  {  
    Connection con;
    Statement state, state2;
    ResultSet res;
    PreparedStatement prep;
    if (!DBExists) 
      {
        DBExists = true;
        
        con = getConnection();
     
        // Check for database table existence and if it's not there, create it and add 2 records
        
        state = con.createStatement();
        res = state.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='User'");
        if (!res.next()) 
          {
            System.out.println("Building the USER table");
            state2 = con.createStatement();
            state2.executeUpdate("CREATE TABLE User(" +
                                       "ID INTEGER," +
               "FirstName VARCHAR(60)," +
               "LastName VARCHAR(60)," + 
                                       "PRIMARY KEY (ID));");

             //Add a couple of records using parameters
             System.out.println("Add record 1 to USER table");
             prep = con.prepareStatement("INSERT INTO User VALUES(?,?,?);");
             prep.setInt(1, 1001);
             prep.setString(2, "Sue");
             prep.setString(3, "Smith");
             prep.execute();
                
             System.out.println("Add record 2 to USER table");
             prep = con.prepareStatement("INSERT INTO User VALUES(?,?,?);");
             prep.setInt(1, 1002);
             prep.setString(2, "John");
             prep.setString(3, "Jones");
             prep.execute();
           }
          
        //--------------------BEGIN PASSENGERS
        state = con.createStatement();
        res = state.executeQuery("select name from sqlite_master where type ='table' and name = 'passengers_table'");
        if(!res.next())
        {
        	System.out.println("Build passengers_table");
        	state2 = con.createStatement();
        	state2.executeUpdate("create table passengers_table" +
									"( tuid integer," +
									   "first_initial varchar(1)," +
									   "middle_initial varchar(1)," +
							  	       "last_name varchar(60)," +
								       "phone_number varchar(60)," +
									   "primary key (tuid)" +
									 ");");
        	//TODO BUILD PASSENGERS FROM FILE
        }
        //--------------------END PASSENGERS

        //--------------------BEGIN PLANES
        state = con.createStatement();
        res = state.executeQuery("select name from sqlite_master where type = 'table' and name = 'planes_table'");
        if(!res.next())
        {
        	System.out.println("Build planes_table");
        	state2 = con.createStatement();
        	state2.executeUpdate("create table planes_table"+
									"(tuid integer,"+
									"plane_id varchar(5),"+
								    "max_vip integer,"+
								    "max_luxury integer,"+
									"primary key (tuid)"+
									");");
        	//Plan One
        	prep = con.prepareStatement("INSERT INTO planes_table VALUES(?,?,?,?);");
            prep.setInt(1, 1);
            prep.setString(2, "RC407");
            prep.setInt(3, 4);
            prep.setInt(4, 6);
            prep.execute();

            //Plane Two
            prep = con.prepareStatement("INSERT INTO planes_table VALUES(?,?,?,?);");
            prep.setInt(1, 2);
            prep.setString(2, "TR707");
            prep.setInt(3, 3);
            prep.setInt(4, 5);
            prep.execute();

            //Plane Three
            prep = con.prepareStatement("INSERT INTO planes_table VALUES(?,?,?,?);");
            prep.setInt(1, 3);
            prep.setString(2, "KR381");
            prep.setInt(3, 6);
            prep.setInt(4, 8);
            prep.execute();
        }
        //--------------------END PLANES

        //--------------------BEGIN FEES
        state = con.createStatement();
        res = state.executeQuery("select name from sqlite_master where type = 'table' and name = 'fees_table'");
        if(!res.next())
        {
        	System.out.println("Build fees_table");
        	state2 = con.createStatement();
        	state2.executeUpdate("create table fees_table"+
									"(tuid integer,"+
									"seat_type varchar(1),"+
								    "fee integer,"+
								    "primary key (tuid)"+
									");");
        	//Plan One
        	prep = con.prepareStatement("INSERT INTO fees_table VALUES(?,?,?);");
            prep.setInt(1, 1);
            prep.setString(2, "V");
            prep.setInt(3, 4000);
            prep.execute();

            //Plane Two
            prep = con.prepareStatement("INSERT INTO fees_table VALUES(?,?,?);");
            prep.setInt(1, 2);
            prep.setString(2, "L");
            prep.setInt(3, 2500);
            prep.execute();
        }
        //--------------------END FEES

        //--------------------BEGIN FLIGHTS
        state = con.createStatement();
        res = state.executeQuery("select name from sqlite_master where type = 'table' and name = 'flights_table'");
        if(!res.next())
        {
        	System.out.println("Build flights_table");
        	state2 = con.createStatement();
        	state2.executeUpdate("create table flights_table"+
									"(tuid integer,"+
									"plane_tuid integer,"+
								    "airport_code varchar(3),"+
								    "depart_gate integer,"+
								    "depart_time varchar(60),"+
								    "primary key (tuid),"+
								    "foreign key (plane_tuid) references planes_table"+
									");");

            //Plane Two
            prep = con.prepareStatement("INSERT INTO flights_table VALUES(?,?,?,?,?);");
            prep.setInt(1, 1);
            prep.setInt(2, 1);
            prep.setString(3, "MBS");
            prep.setInt(4, 3);
            prep.setString(5, "07:00");
            prep.execute();

            prep = con.prepareStatement("INSERT INTO flights_table VALUES(?,?,?,?,?);");
            prep.setInt(1, 2);
            prep.setInt(2, 2);
            prep.setString(3, "MBS");
            prep.setInt(4, 1);
            prep.setString(5, "13:00");
            prep.execute();

            prep = con.prepareStatement("INSERT INTO flights_table VALUES(?,?,?,?,?);");
            prep.setInt(1, 3);
            prep.setInt(2, 3);
            prep.setString(3, "MBS");
            prep.setInt(4, 2);
            prep.setString(5, "21:00");
            prep.execute();
        }
        //--------------------END FLIGHTS

        //--------------------BEGIN SCHEDULES
        state = con.createStatement();
        res = state.executeQuery("select name from sqlite_master where type ='table' and name = 'schedules_table'");
        if(!res.next())
        {
        	System.out.println("Build schedules_table");
        	state2 = con.createStatement();
        	state2.executeUpdate("create table schedules_table" +
									"( tuid integer," +
									   "passenger_tuid varchar(1)," +
									   "flight_tuid integer," +
									   "flight_date varchar(60),"+
							  	       "seat_section varchar(1)," +
								       "seat_number integer," +
									   "primary key (tuid)," +
									   "foreign key (passenger_tuid) references passengers_table,"+
									   "foreign key (flight_tuid) references flights_table"+
									 ");");
        	//TODO BUILD PASSENGERS FROM FILE
        }
        //--------------------END SCHEDULES
     }
  }

  private static void readData() throws SQLException, IOException
  {
  	String file = "planes.txt";
  	BufferedReader br = new BufferedReader(new FileReader(file));
  	String cLine = br.readLine();
  	while(cLine != null){
  		String tokens[] = cLine.split( " ");
	  	System.out.println("Loading information of passenger " + tokens[1]);
	  	try{
	  		if( tokens[0].equals("P")){insertPassenger(tokens);}
	  		else {insertScheduledFlight(tokens);}
	  	}
	  	catch (Exception e) 
	      {
			e.printStackTrace();
	      }
	    cLine = br.readLine();
	 }
  	
      br.close();
  }
  private static void insertPassenger(String info[]) throws ClassNotFoundException,SQLException
  {
  	
  	Connection con;
    PreparedStatement prep;
    ResultSet res;
    Statement stat;
  	int indexValue = 0;
  	int tuid = Integer.parseInt(info[1]);
    con = getConnection();
    stat = con.createStatement();
  	res = stat.executeQuery("select tuid from passengers_table where tuid = " +info[1]);
  	if(!res.next())
  	{
  		prep = con.prepareStatement("INSERT INTO passengers_table VALUES(?,?,?,?,?);");
    prep.setInt(1, tuid);
    prep.setString(2, info[2]);
    prep.setString(3, info[3]);
    prep.setString(4, info[4]);
    prep.setString(5, info[5]);
    prep.execute();
  	}
    

  }
  private static void insertScheduledFlight(String info[]) throws SQLException
  {
  	//Connection con;
  	//PreparedStatement pre;
  	//con = getConnection();
  	//prep = con.prepareStatement("insert into schedules_table values (?,?,?,?,?,?);");
  	//prep.setInt
  }
  public static ResultSet displayUsers() throws SQLException, ClassNotFoundException 
  {
   Statement state;
   ResultSet res;    
   Connection con = null;

    if (con == null) 
      {
	// Get Database Connection
        con = getConnection();
      }
     state = con.createStatement();
     int index = 2;
     res = state.executeQuery("SELECT tuid, first_initial, last_name FROM passengers_table where tuid = "+ index);
     return res;
  }




  public static void main(String[] args) 
  {
		
    ResultSet rs;
    Connection con;
    boolean DBExists = false;
		
    try 
      {
      	readData();
        getConnection();
	buildDatabase(DBExists);

	// Bring back the set of user from the database
	rs = displayUsers();
			  
	// Iterate over the resultset, print out each record's details
        while (rs.next()) 
          {
	    System.out.println("ID: " + rs.getInt("tuid") + " -- User: " + rs.getString("first_initial") + " " + rs.getString("last_name"));
	  }
      } 
    catch (Exception e) 
      {
	e.printStackTrace();
      }
  }
}