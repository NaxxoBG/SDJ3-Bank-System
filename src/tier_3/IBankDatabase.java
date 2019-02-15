package tier_3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IBankDatabase extends Remote
{
   public static final String SERVER_NAME = "//localhost/DbServer";
   public Account[] queryDB(int accNo) throws RemoteException, SQLException;
   public boolean insertDB(int accNo, double amount, String name) throws RemoteException, SQLException;
   public void updateDB(Account acc) throws RemoteException, SQLException;
   public boolean updateBalanceWithdrawalDB(int accNo, double amount) throws RemoteException, SQLException;
   public boolean updateBalanceInsertionDB(int accNo, double insertedAmount) throws RemoteException, SQLException;
}