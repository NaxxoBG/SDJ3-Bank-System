package tier_1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Scanner;

import tier_2.BankServer;

public class Clerk
{
   private String name;
   private IRemoteClerk shared;
   private Scanner input;
   private ClientMenu<Clerk> menu;

   public Clerk(String name) throws MalformedURLException, RemoteException, NotBoundException {
      this.name = name;

      try {
         shared = (IRemoteClerk) Naming.lookup(BankServer.SERVER_NAME);
      } catch (java.rmi.ConnectException e) {
         System.out.println("Could not connect to object!\n" + e.getMessage());
      }
      input = new Scanner(System.in);
   }

   public void init() throws RemoteException, SQLException {
      menu = new ClientMenu<Clerk>(this);
      menu.printMenu();
   };

   public void shutdown() {
      shared = null;
      input = null;
      System.gc();
      System.exit(0);
   }

   public void insertMoney(double amount, int accNo) throws RemoteException, SQLException {
      if (shared.insertMoneyClerk(amount, accNo)) {
         System.out.println();
         System.out.println("Success");
         System.out.println();
      } else {
         System.out.println();
         System.out.println("Account not found");
         System.out.println();
      }
   }

   public void withdrawMoney(double amount, int accNo) throws RemoteException, SQLException {
      if (!shared.withdrawMoneyClerk(amount, accNo)) {
         System.out.println();
         System.out.println("Account not found or insufficient funds");
         System.out.println();
      } else {
         System.out.println();
         System.out.println("Success");
         System.out.println();
      }
   }

   public String getName() {
      return name;
   }

   public IRemoteClerk getRemote() {
      return this.shared;
   }

   public Scanner getInput() {
      return input;
   }
}