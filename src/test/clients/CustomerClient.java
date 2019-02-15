package test.clients;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import tier_1.Customer;

public class CustomerClient
{
   public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
      Customer customer = new Customer("Jim", 1);
      customer.init();
   }
}