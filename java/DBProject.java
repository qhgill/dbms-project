/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science  &  Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of DBProject
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public DBProject (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end DBProject

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            DBProject.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      DBProject esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the DBProject object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new DBProject (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking"); 
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range"); 
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

            switch (readChoice()){
				   case 1: addCustomer(esql); break;
				   case 2: addRoom(esql); break;
				   case 3: addMaintenanceCompany(esql); break;
				   case 4: addRepair(esql); break;
				   case 5: bookRoom(esql); break;
				   case 6: assignHouseCleaningToRoom(esql); break;
				   case 7: repairRequest(esql); break;
				   case 8: numberOfAvailableRooms(esql); break;
				   case 9: numberOfBookedRooms(esql); break;
				   case 10: listHotelRoomBookingsForAWeek(esql); break;
				   case 11: topKHighestRoomPriceForADateRange(esql); break;
				   case 12: topKHighestPriceBookingsForACustomer(esql); break;
				   case 13: totalCostForCustomer(esql); break;
				   case 14: listRepairsMade(esql); break;
				   case 15: topKMaintenanceCompany(esql); break;
				   case 16: numberOfRepairsForEachRoomPerYear(esql); break;
				   case 17: keepon = false; break;
				   default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
   
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   
   public static void addCustomer(DBProject esql){
    // Given customer details add the customer in the DB
    try{
        String query = "INSERT INTO Customer (fname, lname, Address, phNo, DOB, gender) VALUES (";
        System.out.print("\tEnter customer first name: ");
        String input = in.readLine();
        query += "\'" + input + "\', ";
        System.out.print("\tEnter customer last name: ");
        input = in.readLine();
        query += "\'" + input + "\', ";
        System.out.print("\tEnter customer address: ");
        input = in.readLine();
        query += "\'" + input + "\', ";
        System.out.print("\tEnter customer phone number: ");
        input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter customer date of birth (YYYY-MM-DD Format): ");
        input = in.readLine();
        query += "\'" + input + "\', ";
        System.out.print("\tEnter customer gender (Male, Female, Other): ");
        input = in.readLine();
query += "\'" + input + "\')";
        esql.executeQuery(query);
    }catch(Exception e){
        System.err.println (e.getMessage());
    }
   }//end addCustomer

   public static void addRoom(DBProject esql){
    // Given room details add the room in the DB
    try{
        String query = "INSERT INTO Room VALUES (";
        System.out.print("\tEnter hotel ID: ");
        String input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter room number: ");
        input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter room type: ");
        input = in.readLine();
        query += "\'" + input + "\');";

        esql.executeQuery(query);
    }catch(Exception e){
        System.err.println (e.getMessage());
    }
   }//end addRoom

   public static void addMaintenanceCompany(DBProject esql){
    // Given maintenance Company details add the maintenance company in the DB
      try{
         String query = "INSERT INTO MaintenanceCompany (name, address, isCertified) VALUES (";
         System.out.print("\tEnter company name: ");
         String input = in.readLine();
         query += "\'" + input + "\', ";
         System.out.print("\tEnter company address: ");
         input = in.readLine();
         query += "\'" + input + "\', ";
         System.out.print("\tEnter if company is certified (Y or N): ");
         input = in.readLine();
         query += "\'" + input + "\');";

         int rowCount = esql.executeQuery(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){
	// Given repair details add repair in the DB
    try{
        String query = "INSERT INTO Repair (hotelID, roomNo, mCompany, repairDate, description, repairType) VALUES (";
        System.out.print("\tEnter hotel ID: ");
        String input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter room number: ");
        input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter company ID: ");
        input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter repair date (YYYY-MM-DD Format): ");
        input = in.readLine();
        query += "\'" + input + "\', ";
        System.out.print("\tEnter repair description: ");
        input = in.readLine();
        query += "\'" + input + "\', ";
        System.out.print("\tEnter repair type: ");
        input = in.readLine();
        query += "\'" + input + "\');";

        int rowCount = esql.executeQuery(query);
    }catch(Exception e){
        System.err.println (e.getMessage());
    }
   }//end addRepair

   public static void bookRoom(DBProject esql){
	// Given hotelID, roomNo and customer Name create a booking in the DB 
    try{
        String query = "INSERT INTO Booking (hotelID, roomNO, customer, bookingDate, price) VALUES (";
        System.out.print("\tEnter hotel ID: ");
        String input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter room number: ");
        input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter customer ID: ");
        input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter booking date: (YYYY-MM-DD Format): ");
        input = in.readLine();
        query += "\'" + input + "\', ";
        System.out.print("\tEnter the price: $: ");
        input = in.readLine();
        query += input + ");";

        int rowCount = esql.executeQuery(query);
    }catch(Exception e){
        System.err.println (e.getMessage());
    }
   }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){
    // Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      try{
         String query = "INSERT INTO Assigned (staffID, hotelID, roomNo) VALUES (";
         System.out.print("\tEnter staff ID: ");
         String input = in.readLine();
         query += input + ", ";
         System.out.print("\tEnter hotel ID: ");
         input = in.readLine();
         query += input + ", ";
         System.out.print("\tEnter room number: ");
         input = in.readLine();
         query += input + ");";

         int rowCount = esql.executeQuery(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){
    // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
    try{
        String query = "INSERT INTO Request (managerID, repairID, requestDate, description) VALUES (";
        System.out.print("\tEnter manager ID: ");
        String input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter repair ID: ");
        input = in.readLine();
        query += input + ", ";
        System.out.print("\tEnter request date (YYYY-MM-DD Format): ");
        input = in.readLine();
        query += "\'" + input + "\',";
        System.out.print("\tEnter description: ");
        input = in.readLine();
        query += "\'" + input + "\'); ";

        int rowCount = esql.executeQuery(query);
    }catch(Exception e){
        System.err.println (e.getMessage());
    }
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms available 
      try{
        String query = "SELECT COUNT(*) AS Available_Rooms FROM Room WHERE hotelID = ";
         System.out.print("\tEnter hotelID: ");
         String input = in.readLine();
         query += input + ";";

         int rowCount = esql.executeQuery(query);
      }catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end numberOfAvailableRooms
   
   // assuming all time booked
   public static void numberOfBookedRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms booked
     try{
        String query = "SELECT COUNT(DISTINCT roomNo) AS booked_rooms FROM Booking WHERE hotelID = ";
        System.out.print("\tEnter hotelID: ");
        String input = in.readLine();
        query += input + ";";
        int rowCount = esql.executeQuery(query);
     }catch(Exception e){
         System.err.println (e.getMessage());
     }
        
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){ //ASSUMING WE ARE GETTING TODAY'S AND NEXT 6 DAYS
	  // Given a hotelID, date - list all the rooms booked for a week(including the input date) 
     try{
        String query = "SELECT roomNo AS rooms_for_week FROM Booking WHERE hotelID = ";
        System.out.print("\tEnter hotelID: ");
        String input = in.readLine();
        query += input + " AND "; 
        query += " bookingDate BETWEEN TO_DATE(\'";
        System.out.print("\tEnter date (MM/DD/YYYY Format): ");
        input = in.readLine();
        query += input + "\', \'MM/DD/YYYY\') AND TO_DATE(\'" + input + "\', \'MM/DD/YYYY\') + INTERVAL '6 days';";
        int rowCount = esql.executeQuery(query);
        if(rowCount == 0){
         System.out.println("no rows");
        }
       }catch(Exception e){
        System.err.println (e.getMessage());
       }
   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
	  // List Top K Rooms with the highest price for a given date range
      try{
         String query = "SELECT B.hotelID, B.roomNo, B.price FROM Room R, Booking B WHERE B.bookingDate BETWEEN TO_DATE(\'";
         System.out.print("\tEnter start date (MM/DD/YYYY Format): ");
         String input = in.readLine();
         query += input + "\', \'MM/DD/YYYY\') AND TO_DATE(\'";
         System.out.print("\tEnter end date (MM/DD/YYYY Format): ");
         input = in.readLine();
         query += input + "\', \'MM/DD/YYYY\') AND R.roomNo = B.roomNo AND R.hotelID = B.hotelID ORDER BY B.price DESC LIMIT ";
         System.out.print("\tEnter number of rooms: ");
         input = in.readLine();
         query += input + ";";
         int rowCount = esql.executeQuery(query);
         if(rowCount == 0){
         System.out.println("no rows");
        }
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer Name, List Top K highest booking price for a customer 
      try{
         String query = "SELECT B.price FROM Booking B, Customer C WHERE C.fname = \'";
         System.out.print("\tEnter first name: ");
         String input = in.readLine();
         query += input + "\' AND C.lname = \'";
         System.out.print("\tEnter last name: ");
         input = in.readLine();
         query += input + "\' AND C.customerID = B.customer ORDER BY B.price DESC LIMIT ";
         System.out.print("\tEnter number of bookings: ");
         input = in.readLine();
         query += input + ";";
         int rowCount = esql.executeQuery(query);
         if(rowCount == 0){
         System.out.println("no rows");
        }
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
      try{
         String query = "SELECT SUM(B.price) AS total FROM Booking B, Customer C  WHERE C.fname = \'";
         System.out.print("\tEnter first name: ");
         String input = in.readLine();
         query += input + "\' AND C.lname = \'";
         System.out.print("\tEnter last name: ");
         input = in.readLine();
         query += input + "\' AND bookingDate BETWEEN TO_DATE(\'";
         System.out.print("\tEnter start date: ");
         input = in.readLine();
         query += input + "\', \'MM/DD/YYYY\') AND TO_DATE(\'";
         System.out.print("\tEnter end date: ");
         input = in.readLine();
         query += input + "\', \'MM/DD/YYYY\') AND C.customerID = B.customer AND B.hotelID = ";
         System.out.print("\tEnter hotelID: ");
         input = in.readLine();
         query += input + ";";
         int totalCost = esql.executeQuery(query);
         if(totalCost == 0){
         System.out.println("no rows");
        }
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){ 
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      try{
         String query = "SELECT R.repairType, R.hotelID, R.roomNo FROM Repair R, MaintenanceCompany M WHERE M.name = \'";
         System.out.print("\tEnter maintenance company name: ");
         String input = in.readLine();
         query += input + "\' AND M.cmpID = R.mCompany;";
         int rowCount = esql.executeQuery(query);
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      try{
         String query = "SELECT MC.name, COUNT(*)FROM MaintenanceCompany MC, Repair R WHERE MC.cmpID = R.mCompany GROUP BY MC.name ORDER BY COUNT(*) DESC LIMIT ";
         System.out.print("\tEnter number of companies: ");
         String input = in.readLine();
         query += input + ";";
         int rowCount = esql.executeQuery(query);
         if(rowCount == 0){
         System.out.println("no rows");
        }
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      try{
         String query = "SELECT COALESCE(AVG(count), 0) AS Repairs_per_year from (SELECT EXTRACT(YEAR FROM repairDate) AS repairYear, COUNT(*) FROM Repair WHERE hotelID = ";
         System.out.print("\tEnter hotelID: ");
         String input = in.readLine();
         query += input + " AND roomNo = ";
         System.out.print("\tEnter room number: ");
         input = in.readLine();
         query += input + " GROUP BY EXTRACT(YEAR FROM repairDate)";
         query += ") as countPerYear;";
         int avgRepairs = esql.executeQuery(query);
      }
      catch(Exception e){
         System.err.println (e.getMessage());
      }
   }//end listRepairsMade

}//end DBProject
