package test.clients;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import tier_1.Clerk;

public class ClerkClient
{
   public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
      Clerk clerk = new Clerk("Bill");
      clerk.init();
   }
}