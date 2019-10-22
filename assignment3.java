// Need SQLLite Driver from
// https://bitbucket.org/xerial/sqlite-jdbc/downloads
// Put in same folder as this source code file

// Compiled with javac DBTest.java
// Executed with java -classpath sqlite-jdbc-3.27.2.1.jar DBTest

import java.sql.*;
class testingClass {
}
public class assignment3 
{
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
        //--------------------END PLANES

        //--------------------BEGIN FEES
        //--------------------END FEES

        //--------------------BEGIN FLIGHTS
        //--------------------END FLIGHTS
     }
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
     res = state.executeQuery("SELECT ID, Firstname, Lastname FROM User");
     return res;
  }




  public static void main(String[] args) 
  {
		
    ResultSet rs;
    Connection con;
    boolean DBExists = false;
		
    try 
      {
        getConnection();
	buildDatabase(DBExists);

	// Bring back the set of user from the database
	rs = displayUsers();
			  
	// Iterate over the resultset, print out each record's details
        while (rs.next()) 
          {
	    System.out.println("ID: " + rs.getInt("ID") + " -- User: " + rs.getString("FirstName") + " " + rs.getString("LastName"));
	  }
      } 
    catch (Exception e) 
      {
	e.printStackTrace();
      }
  }
}