package tier_1;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Scanner;

import tier_2.BankServer;

public class Customer
{
   private String name;
   private int accNo = 1;
   private IRemoteCustomer shared;
   private Scanner input;
   private ClientMenu<Customer> menu;

   public Customer(String name, int accNo) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
      this.name = name;
      this.accNo = accNo;
      try {
         shared = (IRemoteCustomer) Naming.lookup(BankServer.SERVER_NAME);
      } catch (java.rmi.ConnectException e) {
         System.out.println("Could not connect to object!\n" + e.getMessage());
      }
      input = new Scanner(System.in);
   }

   public void init() throws RemoteException, SQLException {
      menu = new ClientMenu<Customer>(this);
      this.menu.printMenu();
   }

   public void shutdown() {
      shared = null;
      input = null;
      System.gc();
      System.exit(0);
   }

   public void withdrawMoney(double amount) throws RemoteException, SQLException {
      if (shared.withdrawMoneyCustomer(accNo, amount)) {
         System.out.println("Success!");
      } else {
         System.out.println("Insufficient funds");
      }
   }

   public double checkBalance() throws RemoteException, SQLException {
      return shared.checkBalanceCustomer(accNo);
   }

   public boolean confirmAccount(String name, int accNo) throws RemoteException, SQLException {
      return shared.confirmAccountCustomer(name, accNo);
   }

   public String getName() {
      return name;
   }

   public IRemoteCustomer getShared() {
      return shared;
   }

   public int getAccNo() {
      return accNo;
   }

   public void setAccNo(int accNo) {
      this.accNo = accNo;
   }

   public Scanner getInput() {
      return input;
   }
}