package org.jsp.fetchuserApp;
import java.sql.*;
import java.util.Scanner;

public class fetchuser_placeholder 
{
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Username : ");
		String nm = sc.next();
		System.out.println("Enter Password : ");
		String pw = sc.next();
		sc.close();
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String qry ="select userid, name, phno from btm.user where username=? and password=?";
		
		try 
		{
			//(Step 1)Load and Register Driver class
			Class.forName("com.mysql.jdbc.Driver");       
			
			//(Step 2)Establish a connection with Database server
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306?user=root&password=admin");  
			
			//(Step3)Create a Statement or Platform ------Compilation
			pstmt = con.prepareStatement(qry);            
			
			//Set the Data for Placeholder
			pstmt.setString(1, nm);
			pstmt.setString(2, pw);
			
			
			//(Step 4)Execute the SQL Query or Statement--------Execute
			rs = pstmt.executeQuery();     
			
			//(Step 5)process the resultant data
			if(rs.next())           
			{
				String name = rs.getString(2);
				System.out.println("Name of the user : " + name);
				int userid = rs.getInt(1);
				System.out.println("Name of the userid : " + userid);
				int phno = rs.getInt(3);
				System.out.println("Name of the phno : " + phno);
			}
			else
			{
				System.err.println("Invalid Username or Password");
			}
		} 
		
		catch (ClassNotFoundException | SQLException e) 
		{
			e.printStackTrace();
			System.out.println("Either the Driver jar file is not present in 'lib' or path is not buid");
		}
		
		//(Step 6)Close all costly resources
		finally
		{
			if(rs!=null)
			{
				try 
				{
					rs.close();
				}
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
			
			if(pstmt!=null)
			{
				try 
				{
					pstmt.close();
				}
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
			
			if(con!=null)
			{
				try 
				{
					con.close();
				}
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
			}
		}
		
	}
}
/* JDBC---------------------------------------------------------------------------
 * Java Database connectivity is a specification(rules) that is given in the form of Abstraction API to achieve loose coupling between Java Application 
 * and Database Server.
 * 
 * Standard Steps of JDBC ----
 * 1- Load and Register Driver class
 * 2- Establish a connection between the Java application and the Database server
 * 3- Create a Statement or Platform
 * 4- Execute the SQL Query or Statement
 * 5- Process the resultant data (only mandatory for DQL statement)
 * 6- Close all costly resources
 * 
 * 
 ****Step-1***** Load and Register Driver class--------------------------------
 * 
 * In this step we need to load and register the Driver classes which are a part of the JDBC driver and are provided by the respective database server or vendor.
 * 
 * There are two different ways through which we can load and register Driver classes____
 * (1) Manually ======== 
 *         -In this case, we need to create an object of the Driver class and then we should register the Driver class with 'DriverManager' by using a 
 *          static method called registerDriver().
 * 
 *               Example/Syntax - Driver d= new Driver();
 *                                DriverManager.registerDriver(d);
 * 
 *         -The manual way of loading and registering the Driver class is not a good practice. Since it causes tight coupling between java applications and 
 *          database server
 *          
 * (2) By using a Static method called forName() ========
 *         -forName() method is declared inside a class called 'Class' itself, and it is present inside Java.lang package.
 *         -whenever we try to use this method it throws an exception called 'ClassNotFoundException'.
 *              
 *               Example/Syntax - public class Test{
 *                                   public static void main(String[] args){
 *                                      try{
 *                                          class.forName("com.mysql.jdbc.Driver");
 *                                          System.out.println("Driver class is loaded and registered");
 *                                		}
 *                                		catch( ClassNotFoundException e){
 *                                    		e.printStackTrace();
 *                                		}
 *                                   }
 *                                }  
 *                                
 * 
 ****Step-2***** Establish a connection between the Java application and the Database server---------------------------------------
 *
 * In this step we need to establish a connection between the Java application and the Database server by using Connection Interface, which is provided by JDBC API.
 * 
 *     getConnection()---
 *          - It is a static Factory/Helper method that is used to create and return the implementation object of the Connection interface based on "URL".
 *          - The return type of getConnection() method is Connection interface.
 *          
 *               Example/Syntax - Connection con = DriverManager.getConnection("URL");
 *                                URL = jdbc:mysql://localhost:3306?user=root&password=admin
 *                                
 *          - There are 3 Overloaded variants of 'getConnection()' method_____
 *               (1) getConnection(String URL)
 *               (2) getConnection(String URL, properties info)
 *               (3) getConnection(String URL, String User, String password) 
 *          
 *          - Whenever we use either of these overloaded methods it throws a checked exception called 'SQLException'.
 *          - DriverManager is a helper class of JDBC Api which contains two static methods - getConnection()
 *                                                                                            registerDriver()
 * 
 ****Step-3***** Create a Statement or Platform -------------------------------------------
 * 
 * We need to create a statement or platform to execute the SQL query or SQL statement.
 * 
 * There are 3 different ways in which we can create a Statement or a Platform____ 
 * (1) Statement=======
 *           - java.sql.Statement is an interface that is part of JDBC API and the implementation is provided by the respective database 
 *             server or vendor as a part of JDBC Driver.
 *           
 *             Example/Syntax - Statement stmt= con.createStatement();
 *                              stmt.executeUpdate(qry);   //Compile + Execute
 *             
 *      createStatement()---
 *           - It is a non-static Factory/Helper method that is used to create and return an implementation object of the Statement interface.
 *           - Hence the return type of createStatment() method is Statement interface.
 *           
 * (2) PreparedStatement======
 *           - PreparedStatement is an interface that is a part of JDBC API and the implementation is provided by the respective Database server/vendor.
 *           - preparedStatement is the sub-interface of the Statement interface.
 *           - preparedStatement supports the concept of 'placeholder' which means it can accept input from the user at runtime.
 *           - preparedStatement interface supports the concept of compiling once and executing many times(execution plan).
 *           - placeholder is supported by PreparedStatement and CollableStatement only, but not by Statement.
 *           
 *             Example/Syntax - PreparedStatement pstmt= con.preparedStatement(qry);
 *                              pstmt.executeUpdate();    //compile only
 *                              
 * (3) CollableStatement
 * 
 * 
 ****Step-4***** Execute the SQL Query or Statement ---------------------------------------
 *
 * To Execute different types of SQL queries/ SQL Statement there are 3 different types of methods are available, which are as follows___
 * (1) execute()=======
 *           - execute() method is a generic method that is used to execute any type of SQL query or SQL Statement (DDL, DML, DQL). 
 *           
 *             Example/Syntax -  + boolean execute("Generic SQL Query - DDL/DML/DQL")
 *                                        for DQL = true
 *                                        for DDL/DML = false
 *                      
 *           - The return type for execute() method is Boolean.
 *           - Execute method returns boolean true in case of DQL and boolean false in case of DDL/DML.
 *           
 * (2) executeUpdate()   //for DML
 *           - executeUpdate() method is a specialized method which is used to execute only DML Queries
 *           
 *             Example/Syntax -  + int executeUpdate("only DML")
 *             
 *           - The return type of executeUpdate() method is integer because the outcome of DML is 0 to n integer value 
 *             which gives the total number of records affected in the Database Server.
 *           - Whenever we try to execute DDL/DQL queries through executeUpdate() method then throws an exception called 'SQLException'
 *             which is a Checked Exception.
 *             
 * (3) executeQuery()    //for DQL
 *           - This method is a specialized method that is used to execute only DQL queries/statements
 *           - The outcome of DQL queries is the processed/Resultant data which is stored in the cursor or Buffer memory, which is stored
 *             in the Cursor/Buffer memory, which can be fetched by using the result set interface. Hence, the return type of executeQuery()
 *             method is ResultSet interface.
 *             
 *             Example/Syntax - + ResultSet executeQuery("only DQL")
 *           
 *           - Whenever we try to execute DML/DDL queries through executeUpdate() method then throws an exception called 'SQLException'
 *             which is a Checked Exception.
 * 
 * All 3 methods are declared in the Statement interface. 
 *
 * 
 ****Step-5***** Process the resultant data -----------------------------------------------
 * 
 * We can process the resultant data using the ResultSet interface
 *   
 *         java.sql.ResultSet - It is an interface that is a part of JDBC API and the implementation is provided by the respective Database server/vendor
 *                              as a part of JDBC Driver in the form of jar file as Driver.jar.
 *                            - By default result set interface doesn't point to any record in the cursor/buffer memory. 
 *                            
 *                            - Set of methods of resultSet interface which are used to fetch the processed/Resultant data from the Cursor/Buffer memory____
 *                            
 *                              (1) getXXX() - There are two Overloaded variant of getXXX() method ---
 *                                             (a) + XXX getXXX(int column number);      //Column index
 *                                             (b) + XXX getXXX(String column name);     //Column label
 *                                             
 *                                             * if Datatype is integer--------
 *                                               + int getInt(1);
 *                                               + int getInt("id");
 *                                               
 *                                             * if Datatype is String--------
 *                                               + String getString(2);
 *                                               + String getString("name");
 *                                               
 *                              (2) next() - This method is used to check whether the record is present in the cursor/buffer memory or not.
 *                                         - The return type of the next() method is boolean 'true' if the record is present else it will return false,
 *                                           but Not the Record.
 *                                         - The next() method can be used whenever there is a minimum number of records present in cursor/buffer memory.
 *                                         
 *                                                    Syntax - + Boolean next()
 *                                         
 *                              (3) absolute() - This method is used to check whether a particular record is present in the cursor/buffer memory or not,
 *                                               based on a parameter called 'integer row number' and it returns a Boolean value 'true' or 'false'
 *                                               but not record.
 *                                             - It directly starts from the given number by the user but, not from the zeroth record area.
 *                                          
 *                                                     Syntax - + Boolean absolute(int rownumber)
 *                                         
 *                                             - absolute() method can be used whenever there is n number of records present in cursor/buffer memory.
 *  
 *  
 ****Step-6***** CLose all costly resources -------------------------------------------
 * 
 *  The resources that make use of system properties in the form of Stream is known as Costly Resources.
 *  
 *  We have to close all costly resources inside finally block using the if condition to avoid 'NullPointerException'.
 *                 Example/Syntax - finally {
 *                                       if(rs!=null) {   //rs is the reference variable of costly resource
 *                                           try { 
 *                                              rs.close(); 
 *                                            } 
 *                                           catch (SQLException e) {
 *                                              e.printStackTrace(); 
 *                                            } 
 *                                   }      
 * */
