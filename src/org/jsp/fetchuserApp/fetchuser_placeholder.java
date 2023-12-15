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
	
/* JDBC---------------------------------------------------------------------------
 * Java Database connectivity is a specification(rules) which is given in the form of Abstraction API to achieve loose coupling between Java Application 
 * and Database Server.
 * 
 * Standard Steps of JDBC ----
 * 1- Load and Register Driver class
 * 2- Establish a connection between Java application and Database server
 * 3- Create a Statement or Platform
 * 4- Execute the SQL Query or Statement
 * 5- Process the resultant data (only mandatory for DQL statement)
 * 6- CLose all costly resources
 * 
 * 
 ****Step-1***** Load and Register Driver class--------------------------------
 * 
 * In this step we need to load and register the Driver classes which is a part of JDBC driver, which are provided by the respective database server or vendor.
 * 
 * There are Two different ways through which we can load and register Driver classes____
 * (1) Manually ======== 
 *         -In this case we need to create an object of Driver class and then we should be register Driver class with 'DriverManager' by using a 
 *          static method called registerDriver().
 * 
 *               Example/Syntax - Driver d= new Driver();
 *                                DriverManager.registerDriver(d);
 * 
 *         -The manual way of loading and registering the Driver class is not a good practice. Since it causes tight-coupling between java application and 
 *          database server
 *          
 * (2) By using a Static method called forName() ========
 *         -forName() method is declared inside a class called 'Class' itself, and it is present inside Java.lang package.
 *         -when ever we try to use this method it throw an exception called 'ClassNotFoundException'.
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
 ****Step-2***** Establish a connection between Java application and Database server---------------------------------------
 *
 * In this step we need to establish a connection between Java application and Database server by using Connection Interface, which is provided by JDBC API.
 * 
 *     getConnection()---
 *          - It is a static Factory/Helper method which is used to create and return implementation object of Connection interface based on "URL".
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
 *           - java.sql.Statement is an interface which is the part of JDBC API and the implementation is provided the respective database 
 *             server or vender as a part of JDBC Driver.
 *           
 *             Example/Syntax - Statement stmt= con.createStatement();
 *                              stmt.executeUpdate(qry);   //Compile + Execute
 *             
 *      createStatement()---
 *           - It is a non-static Factory/Helper method which is used to create and return an implementation object of Statement interface.
 *           - Hence the return type of createStatment() method is Statement interface.
 *           
 * (2) PreparedStatement======
 *           - PreparedStatement is an interface which is a part of JDBC API and the implementation is provided by the respective Database server/vendor.
 *           - preparedStatement is the sub-interface of Statement interface.
 *           - preparedStatement support the concept of 'placeholder' which means it can accept input from the user at runtime.
 *           - preparedStatement interface support the concept of Compile once and execute many times(execution plan).
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
 * To Execute different type of SQL queries/ SQL Statement there are 3 different types of method are available, which are as following___
 * (1) execute()=======
 *           - execute() method is a generic method which is used to execute any type of SQL Queries or SQL Statements(DDL,DML,DQL). 
 *           
 *             Example/Syntax -  + boolean execute("Generic SQL Query - DDL/DML/DQL")
 *                                        for DQL = true
 *                                        for DDL/DML = false
 *                      
 *           - The return type for execute() method is Boolean.
 *           - Execute method return boolean true in case of DQL and boolean false in case of DDL/DML.
 *           
 * (2) executeUpdate()   //for DML
 *           - executeUpdate() method is a specialized method which is used to execute only DML Queries
 *           
 *             Example/Syntax -  + int executeUpdate("only DML")
 *             
 *           - The return type of executeUpdate() method is integer because the outcome of DML is 0 to n integer value 
 *             which gives the total number of record affected in the Database Server.
 *           - Whenever we try to execute DDL/DQL queries through executeUpdate() method then throws an exception called 'SQLException'
 *             which is a Checked Exception.
 *             
 * (3) executeQuery()    //for DQL
 *           - This method is a specialized method which is used to execute only DQL queries/statements
 *           - The outcome of DQL queries is the processed/Resultant data which is stored in the cursor or Buffer memory, which is stored
 *             in the Cursor/Buffer memory, which can be fetched by using result set interface. Hence, the return type of executeQuery()
 *             method is ResultSet interface.
 *             
 *             Example/Syntax - + ResultSet executeQuery("only DQL")
 *           
 *           - Whenever we try to execute DML/DDL queries through executeUpdate() method then throws an exception called 'SQLException'
 *             which is a Checked Exception.
 * 
 * All 3 methods are basically declared in Statement interface. 
 *
 * 
 ****Step-5***** Process the resultant data -----------------------------------------------
 * 
 * We can process the resultant data using ResultSet interface
 *   
 *         java.sql.ResultSet - It is a interface which is a part of JDBC API and the implementation is provided by the respective Database server/vendor
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
 *                                         - The return type of next() method is boolean 'true' if the record is present else it will return false,
 *                                           but Not the Record.
 *                                         - next() method can be used when ever there are minimum number of records present in cursor/buffer memory.
 *                                         
 *                                                    Syntax - + Boolean next()
 *                                         
 *                              (3) absolute() - This method is used to check weather a particular record is present in the cursor/buffer memory or not,
 *                                               based on a parameter called 'integer row number' and it return a Boolean value 'true' or 'false'
 *                                               but not record.
 *                                             - It directly start from the given number by the user but, not from zeroth record area.
 *                                          
 *                                                     Syntax - + Boolean absolute(int rownumber)
 *                                         
 *                                             - absolute() method can be used when ever there are n number of records present in cursor/buffer memory.
 *  
 *  
 ****Step-6***** CLose all costly resources -------------------------------------------
 * 
 *  The resources which makes use of system properties in the fprm of Stream is known as Costly Resources.
 *  
 *  We have to close all costly resources inside finally block using if condition to avoid 'NullPointerException'.
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

}
