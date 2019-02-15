package tier_1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IRemoteAdministrator extends Remote
{
   public boolean createAccountAdministrator(String name, int number, double amount) throws RemoteException, SQLException;
}