package tier_1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Scanner;

import tier_2.BankServer;

public class Administrator 
{
   private String name;
   private IRemoteAdministrator shared;
   private Scanner input;
   private ClientMenu<Administrator> menu;

   public Administrator(String name) throws MalformedURLException, RemoteException, NotBoundException {
      this.name = name;

      try {
         shared = (IRemoteAdministrator) Naming.lookup(BankServer.SERVER_NAME);
      } catch (java.rmi.ConnectException e) {
         System.out.println("Could not connect to object!\n" + e.getMessage());
      }
      input = new Scanner(System.in);
   }

   public void init() throws RemoteException, SQLException {
      menu = new ClientMenu<Administrator>(this);
      menu.printMenu();
   }

   public void shutdown() {
      shared = null;
      input = null;
      System.gc();
      System.exit(0);
   }

   public void createAccount(String nameOfAccount, int number, double amount) throws RemoteException, SQLException {
      if (shared.createAccountAdministrator(nameOfAccount, number, amount)) {
         System.out.println();
         System.out.println("Success");
         System.out.println();
      } else {
         System.out.println();
         System.out.println("An account with account number " + number + " already exists");
         System.out.println();
      }
   }

   public String getName() {
      return name;
   }

   public IRemoteAdministrator getSharedObject() {
      return shared;
   }

   public Scanner getInput() {
      return input;
   }
}