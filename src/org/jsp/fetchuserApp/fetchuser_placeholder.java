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
