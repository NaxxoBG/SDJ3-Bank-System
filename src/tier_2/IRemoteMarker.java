package tier_2;

import java.rmi.Remote;

import tier_1.IRemoteAdministrator;
import tier_1.IRemoteClerk;
import tier_1.IRemoteCustomer;

public interface IRemoteMarker extends Remote, IRemoteClerk, IRemoteCustomer, IRemoteAdministrator
{
   public static final String SERVER_NAME = "//localhost/Server";
}