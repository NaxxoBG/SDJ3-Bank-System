package test.clients;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.SQLException;

import tier_1.Administrator;

public class AdminClient
{
   public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, SQLException {
      Administrator admin = new Administrator("Francis");
      admin.init();
   }
}