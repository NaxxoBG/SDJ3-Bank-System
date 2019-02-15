package tier_3;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.postgresql.util.PSQLException;

import tier_2.BankServer;
import tier_2.IRemoteMarker;

@SuppressWarnings("unused")
public class BankDatabase implements IBankDatabase
{
   private Database db;
   private String table;
   private IRemoteMarker shared;

   public BankDatabase(String dbName, String table) throws RemoteException {
      this.table = table;
      try {
         this.db = new Database(dbName);
      } catch (ClassNotFoundException e) {
         System.out.println("MySQL driver not found. Exiting.");
         System.exit(1);
      }
   }

   public Account getAccount(int accNumber) throws RemoteException, SQLException {
      try {
         return queryDB(accNumber)[0];
      } catch (ArrayIndexOutOfBoundsException e) {
         return null;
      }
   }

   public synchronized boolean insertDB(int accNo, double amount, String name) throws SQLException {
      int res = 0;
      String sql = "INSERT INTO \"Bank_System\".bank_acc (accno, amount, name) VALUES (?, ?, ?)";
      try {
         res = db.update(sql, accNo, amount, name);
         if (res == 0) {
            return false;
         } else {
            return true;
         }
      } catch (SQLException e) {
         e.printStackTrace();
         return false;
      }
   }

   public synchronized void updateDB(Account acc) throws SQLException {
      String sql = "UPDATE \"Bank_System\".bank_acc SET amount = ? where accno = ?";
      try {
         db.update(sql, acc.amount, acc.accNo);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public synchronized void deleteDB(Account acc) throws SQLException {
      System.out.println("BankDatabase.deleteDB; no test of amount == 0.00");

      String sql = "DELETE FROM \"Bank_System\".bank_acc WHERE accno = ?";
      try {
         db.update(sql, acc.accNo);
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public synchronized Account[] queryDB(int accNumber) throws SQLException {
      String sql = "SELECT accno, amount, name FROM \"Bank_System\".bank_acc WHERE accno = ?";

      ArrayList<Object[]> list;
      Account[] accountList;
      try {
         //if you change the query, change the varargs of the query() below
         list = db.query(sql, accNumber);
         accountList = new Account[list.size()];

         for (int i = 0; i < accountList.length; i++) {
            accountList[i] = toAccount(list.get(i));
         }
         return accountList;
      } catch (SQLException e) {
         return null;
      }
   }

   private Account toAccount(Object[] arr) {    
      Account acc = new Account(((Number) arr[0]).intValue(), ((Double) arr[1]).doubleValue(), (String) arr[2].toString());
      return acc;
   }

   @Override
   public boolean updateBalanceWithdrawalDB(int accNo, double withdrawnAmount) throws RemoteException, SQLException {
      Account acc = getAccount(accNo);
      if (acc == null) {
         System.out.println("Account not found");
         return false;
      } else {
         if (acc.amount < withdrawnAmount) {
            System.out.println("Insufficient funds");
            return false;
         } else {
            acc.removeAmount(withdrawnAmount);
            this.updateDB(acc);
            return true;
         }
      }
   }

   @Override
   public boolean updateBalanceInsertionDB(int accNo, double insertedAmount) throws RemoteException, SQLException {
      Account acc = getAccount(accNo);
      if (acc == null) {
         return false;
      } else {
         acc.addAmount(insertedAmount);
         this.updateDB(acc);
         return true;
      }
   }

   public static void main(String[] args) throws Exception {
      System.out.println("DbBank start ...");

      BankDatabase db = new BankDatabase("postgres", "bank_acc");

      /*db.insertDB(1, 300.00, "Jim");
      db.insertDB(2, 500.00, "Tim");
      db.insertDB(3, 600.00, "Ben");

      Map<Integer, Account[]> map = new HashMap<Integer, Account[]>();
      Account[] accArr = db.queryDB(21);
      Account[] accArr2 = db.queryDB(1);
      Account[] accArr3 = db.queryDB(14);

      map.put(1, accArr);
      map.put(2, accArr2);
      map.put(3, accArr3);

      for (Entry<Integer, Account[]> entry : map.entrySet()) {
         Account[] result = entry.getValue();
         for (Account account : result) {
            System.out.println("\naccNo: " + account.accNo + "; amount: " + account.amount);
         }
      }
      System.out.println("\nDbBank: arr size: " + map.size());*/
      try {
         IBankDatabase shared = (IBankDatabase) UnicastRemoteObject.exportObject(db, 0);

         LocateRegistry.createRegistry(1099);
         Naming.bind(IBankDatabase.SERVER_NAME, shared);

         System.out.println("Remote object is bound");
      } catch (Exception e) {
         System.out.println("Failed to bind remote object!\n" + e.getLocalizedMessage());
         System.exit(0);
      }
   }
}