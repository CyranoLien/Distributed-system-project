package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AddClient extends Remote {

	PlayerInfo getPlayerInfo() throws RemoteException;

	int setIndex(String username) throws RemoteException;

	void storePlayerInfo(int num, AddFunc stub) throws RemoteException;

	void updateTable(PlayerInfo playertable) throws RemoteException;
	
	void trakerPrint() throws RemoteException;
	
	void testP(int i)throws RemoteException;
	
}