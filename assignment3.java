
// Compiled with javac assignment3.java
// Executed with java -classpath ".;sqlite-jdbc-3.27.2.1.jar" assignment3
//This program will schedule passengers for flights based on a database and text files
//Created by Nathan Gaffney

/*---------------------
*Currently unfinished, the program will import data for passengers,
* There is no functionality to schedule the passengers or to import the data
*/
import java.sql.*;
import java.util.*;
import java.io.*;
import java.util.Arrays;
import java.time.LocalDate;
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

	private LocalDate departDate;
	public LocalDate getDate(){return departDate;}

	private String[] vipSeats;
	private String[] luxSeats;
	/*seatVIP is the first part of the scheduling algorithm
	VIP passengers will be placed in first available seat in VIP or 
	overflow into luxury the luxury passenger will be passed back into the scheduler*/
	public int seatVIP(String passengerInfo)
	{
		int seated = 1;
		if(vipOnBoard < vipSeats.length)
		{
			vipSeats[vipOnBoard] = passengerInfo;
			vipOnBoard++;
		}
		else
		{
			if(luxOnBoard + (vipOnBoard-NUMVIPSEATS) < luxSeats.length)
			{
				luxSeats[luxOnBoard + (vipOnBoard-NUMVIPSEATS)] = passengerInfo;
			}
			else
			{
				if(vipOnBoard < (NUMVIPSEATS + NUMLUXSEATS))
				{
					for(int i = NUMLUXSEATS; i >= 0 ; i-- )
					{
						String seatedType = luxSeats[i].substring(7,8);
						if(seatedType.equals("L"))
						{
							String removedPass = luxSeats[i];
							luxSeats[i] = passengerInfo;
							try
							{
								assignment3.insertScheduledFlight(removedPass);
							}
							catch (Exception e) 
      {
								e.printStackTrace();
						    }
						}

					}
				}
				else
				{
					 seated = 0;
				}
			}
		}
		return seated;
	}
	public void seatLUX()
	{
		int seated = 1;
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
		con = DriverManager.getConnection("jdbc:sqlite:SQLiteTest1.db");
		state = con.createStatement();
		res = state.executeQuery("select tuid, plane_id, max_vip, max_luxury from planes_table where tuid = "+ planeType);
		NUMVIPSEATS = res.getInt("max_vip");
		NUMLUXSEATS = res.getInt("max_luxury");
		vipSeats = new String[NUMVIPSEATS];
		luxSeats = new String[NUMLUXSEATS];
		int day, month, year;
		String intDates[] = theDate.split("/");
		day = Integer.parseInt(intDates[0]);
		month = Integer.parseInt(intDates[1]);
		year = Integer.parseInt(intDates[2]);
		departDate = LocalDate.of(day,month,year);
		vipOnBoard = 0;
		luxOnBoard = 0;

	}
}

public class assignment3 
{ 
	//Create Vectors to hold full planes
	public static Vector planeRC407;
	public static Vector planeTR707;
	public static Vector planeKR381;
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
	  		else {insertScheduledFlight(cLine);}
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
  public static void insertScheduledFlight(String info) throws SQLException
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
     res = state.executeQuery("SELECT tuid, first_initial, last_name FROM passengers_table");
     return res;
  }




  public static void main(String[] args) 
  {
		
    ResultSet rs;
    Connection con;
    boolean DBExists = false;
	planeRC407 = new Vector();
	planeTR707 = new Vector();
	planeKR381 = new Vector();	
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
	    System.out.println("ID: " + rs.getInt("tuid") + " -- Passenger: " + rs.getString("first_initial") + " " + rs.getString("last_name"));
	  }
      } 
    catch (Exception e) 
      {
	e.printStackTrace();
      }
  }
}