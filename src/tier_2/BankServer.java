package tier_2;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

import tier_3.IBankDatabase;

public class BankServer implements IRemoteMarker
{
   private IBankDatabase database;

   public BankServer() throws MalformedURLException, RemoteException, NotBoundException {
      database = (IBankDatabase) Naming.lookup(IBankDatabase.SERVER_NAME);
   }

   @Override
   public boolean insertMoneyClerk(double amount, int accNo) throws RemoteException, SQLException {
      return database.updateBalanceInsertionDB(accNo, amount);
   }

   @Override
   public boolean withdrawMoneyClerk(double amount, int accNo) throws RemoteException, SQLException {
      return database.updateBalanceWithdrawalDB(accNo, amount);
   }

   @Override
   public boolean withdrawMoneyCustomer(int accNo, double amount) throws RemoteException, SQLException {
      return database.updateBalanceWithdrawalDB(accNo, amount);
   }

   @Override
   public double checkBalanceCustomer(int accNo) throws RemoteException, SQLException {
      return database.queryDB(accNo)[0].amount;
   }

   @Override
   public boolean confirmAccountCustomer(String name, int accNo) throws RemoteException, SQLException {
      return database.queryDB(accNo)[0].name.equals(name);
   }

   @Override
   public boolean createAccountAdministrator(String name, int number, double amount) throws RemoteException, SQLException {
      return database.insertDB(number, amount, name);
   }

   public static void main(String[] args) throws RemoteException, MalformedURLException, AlreadyBoundException, NotBoundException {
      try {
         BankServer server = new BankServer();
         IRemoteMarker shared = (IRemoteMarker) UnicastRemoteObject.exportObject(server, 0);
         LocateRegistry.createRegistry(1100);
         Naming.bind(SERVER_NAME, shared);
         System.out.println("Remote object is bound");
      }
      catch (Exception e) {
         System.out.println("Failed to bind remote object!\n" + e.getLocalizedMessage());
      }
   }
}