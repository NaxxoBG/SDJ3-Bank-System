// *****************************************************************************
// File name  : Database.java
// Start date : 
// Programmer : Ole Hougaard
// Java       : Java 1.6.0 
// Description: 
//
// Revision history:
//  date     init  comment
//  26/9/13  HSO   Some updates and adjustments
// *****************************************************************************

package tier_3;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

/**
 * Database is an connection adapter to use for access to the database.
 * 
 * @author Ole Hougaard
 * 
 */
public class Database
{
   //MySQL constants:   
   public static final String HOST = "localhost";
   public static final String PORT = "5432";  
   public static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/";

   // Default values:
   private String driver = "org.postgresql.Driver";
   private String url = URL;
   private String user = "postgres";
   private String pw = "goobyplease";
   private Connection connection = null;

   /**
    * Create a new MyDatabase object using the given driver, url, user, and
    * password.
    * 
    * @param driver
    *            the class name of the JDBC driver.
    * @param url
    *            the JDBC url of the database.
    * @param user
    *            the username of the datbase user.
    * @param pw
    *            the password of the database user.
    * @throws ClassNotFoundException
    *             if the driver is not on the classpath.
    */
   public Database(String driver, String url, String user, String pw) throws ClassNotFoundException {
      this.driver = driver;
      this.url = url;
      this.user = user;
      this.pw = pw;
      // loading the driver
      Class.forName(driver);
   }

   /**
    * Create a new MyDatabase object for connecting to the given MySQL database
    * on localhost.
    * 
    * @param databaseName
    *            the name of the MySQL database.
    * @param user
    *            username of the database user.
    * @param pw
    *            password of the database user.
    * @throws ClassNotFoundException
    *             if the MySQL driver is not on the classpath.
    */
   public Database(String databaseName, String user, String pw) throws ClassNotFoundException {
      this.url = url + databaseName;
      this.user = user;
      this.pw = pw;
      // loading the driver
      Class.forName(driver);
   }

   /**
    * Create a new MyDatabase object for connecting to the given MySQL database
    * on localhost using the default user and password.
    * 
    * @param databaseName
    *            the name of the MySQL database.
    * @throws ClassNotFoundException
    *             if the driver is not on the classpath.
    */
   public Database(String databaseName) throws ClassNotFoundException {    
      this.url = url + databaseName;
      System.out.println (this.url);
      // loading the driver
      Class.forName(driver);
      System.out.println (driver + " loaded");
   }

   private void openDatabase() throws SQLException {
      connection = DriverManager.getConnection(url, user, pw);
   }

   private void closeDatabase() throws SQLException {
      connection.close();
   }

   /**
    * Gets the greatest primary key used in the table in the DB.<BR>
    * This method is called in insertDB so that the next inserted account can get
    * maxId + 1 as primary key.
    * 
    * This method is unnecessary, if the primary key is created to be 
    * auto incremented.
    * 
    * @return the greatest primary key used in the table
    * @throws SQLException
    */
   public int getMaxId (String table) throws SQLException {
      String sql = "Select Max(id) As maxId From " + table;
      openDatabase();
      Statement stmt = connection.createStatement();
      ResultSet rs = stmt.executeQuery(sql);
      rs.next();
      int max = rs.getInt("maxId");
      if (stmt != null) {
         stmt.close();
      }
      if (rs != null) {
         rs.close();
      }
      closeDatabase();
      return max;
   }

   /**
    * Run a query against the database and return the result as an
    * {@link ArrayList} of rows. Each row is an Object[] of values.
    * 
    * @param sql
    *            the SQL query. May contain '?'.
    * @param args
    *            arguments to fill in the '?' in the query. Needs as many
    *            arguments as '?' in the query.
    * @return the result as an ArrayList of rows.
    * @throws SQLException
    *             if the database operation fails.
    */
   public ArrayList<Object[]> query(String sql, Object... args) throws SQLException {
      openDatabase();
      PreparedStatement statement = null;
      ResultSet resultSet = null;
      try {
         statement = connection.prepareStatement(sql);
         for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
         }
         resultSet = statement.executeQuery();
         ArrayList<Object[]> list = new ArrayList<Object[]>();
         while (resultSet.next()) {
            Object[] elements = new Object[resultSet.getMetaData().getColumnCount()];
            for (int i = 0; i < elements.length; i++) {
               elements[i] = resultSet.getObject(i + 1);
            }
            list.add(elements);
         }
         return list;
      } catch (PSQLException ex) {
         ex.printStackTrace();
         return null;
      } finally {
         if (resultSet != null)
            resultSet.close();
         if (statement != null)
            statement.close();
         closeDatabase();
      }
   }

   /**
    * Run an update statement against the database. 
    * Update statements are UPDATE, DELETE, and INSERT.
    * 
    * @param sql
    *            the SQL statement. May contain '?'.
    * @param args
    *            arguments to fill in the '?' in the query. Needs as many
    *            arguments as '?' in the query.
    * @return the number of rows affected.
    * @throws SQLException
    *             if the database operation fails.
    */
   public int update(String sql, Object... args) throws SQLException {
      openDatabase();
      int rowCount = 0;
      PreparedStatement statement = null;
      try {
         statement = connection.prepareStatement(sql);
         for (int i = 0; i < args.length; i++) {
            statement.setObject(i + 1, args[i]);
         }
         rowCount = statement.executeUpdate();
         return rowCount;
      } catch (PSQLException ex) {
         return 0;
      } finally {
         if (statement != null)
            statement.close();
         closeDatabase();
      }
   }
}