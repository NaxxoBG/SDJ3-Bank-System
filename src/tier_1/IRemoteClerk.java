package tier_1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

public interface IRemoteClerk extends Remote
{
   public boolean insertMoneyClerk(double amount, int accNo) throws RemoteException, PSQLException, SQLException;
   public boolean withdrawMoneyClerk(double amount, int accNo) throws RemoteException, PSQLException, SQLException;
}