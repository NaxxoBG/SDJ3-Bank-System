package tier_1;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class ClientMenu<T>
{
   private T client;

   public ClientMenu(T client) {
      this.client = client;
   }

   public void printMenu() throws RemoteException, SQLException {
      if (client instanceof Clerk) {
         this.printMenuClerk();
      } else if (client instanceof Administrator) {
         this.printMenuAdmin();
      } else if (client instanceof Customer) {
         this.printMenuCustomer();
      }
   }

   private void printMenuClerk() throws RemoteException, SQLException {
      double amount = 0d;
      int accountNo = 0;
      int input = 0;
      Clerk client = (Clerk) this.client;
      print("Welcome, " + client.getName());
      do {
         print();
         print("1) Insert money");
         print("2) Withdraw money");
         print("3) Quit");
         print();
         input = Integer.parseInt(client.getInput().nextLine());
         switch (input) {
            case 1:
               print("Enter the amount to insert: ");
               amount = Double.parseDouble(client.getInput().nextLine());
               print("Enter the account number: ");
               accountNo = Integer.parseInt(client.getInput().nextLine());
               client.insertMoney(amount, accountNo);
               break;
            case 2:
               print("Enter the amount to withdraw: ");
               amount = Double.parseDouble(client.getInput().nextLine());
               print("Enter the account number: ");
               accountNo = Integer.parseInt(client.getInput().nextLine());
               client.withdrawMoney(amount, accountNo);
               break;
            case 3:
               print("Client shutdown");
               client.shutdown();
               break;
            default:
               break;
         }
      } while (input != 3);
   }

   private void printMenuAdmin() throws RemoteException, SQLException {
      double amount = 0;
      int accountNo = 0;
      int input = 0;
      String name = "";
      Administrator client = (Administrator) this.client;
      print("Welcome, " + client.getName());
      do {
         print();
         print("1) Create an account");
         print("2) Quit");
         print();
         input = Integer.parseInt(client.getInput().nextLine());
         switch (input) {
            case 1:
               print("Enter the name: ");
               name = client.getInput().nextLine();
               print("Enter the account number");
               accountNo = Integer.parseInt(client.getInput().nextLine());
               print("Enter the amount of money");
               amount = Double.parseDouble(client.getInput().nextLine());
               client.createAccount(name, accountNo, amount);
               break;
            case 2:
               print("Client shutdown");
               client.shutdown();
               break;
            default:
               break;
         }
      } while (input != 2);
   }

   private void printMenuCustomer() throws RemoteException, SQLException {
      double amount = 0d;
      int accNo = 0;
      int input = 0;
      Customer client = (Customer) this.client;

      while (!client.confirmAccount(client.getName(), client.getAccNo())) {
         print("This account does not exist, please enter your valid account number");
         accNo = Integer.parseInt(client.getInput().nextLine());
         client.setAccNo(accNo);
      }

      print("Welcome, " + client.getName());
      do {
         print();
         print("1) Withdraw money");
         print("2) Check balance");
         print("3) Quit");
         print();
         input = Integer.parseInt(client.getInput().nextLine());
         switch (input) {
            case 1:
               print("Enter the amount to withdraw");
               amount = Double.parseDouble(client.getInput().nextLine());
               client.withdrawMoney(amount);
               break;
            case 2:
               print("Your current balance is: " + client.checkBalance());
               break;
            case 3:
               print("Client shutdown");
               client.shutdown();
               break;
            default:
               break;
         }
      } while (input != 3);
   }

   private void print(String... strings) {
      for (String string : strings) {
         System.out.println(string);
      }
   }
}