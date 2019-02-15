package tier_1;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IRemoteCustomer extends Remote
{
   public boolean withdrawMoneyCustomer(int accNo, double amount) throws RemoteException, SQLException;
   public double checkBalanceCustomer(int accNo) throws RemoteException, SQLException;
   public boolean confirmAccountCustomer(String name, int accNo) throws RemoteException, SQLException;
}