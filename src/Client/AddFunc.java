package Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AddFunc extends Remote {
	
	PlayerInfo returnTable() throws RemoteException;
	
	void updateTable(PlayerInfo player_table) throws RemoteException;

	MazeStruct[][] returnMaze() throws RemoteException;

	void initialMaze() throws RemoteException;
	
	void updateMaze(MazeStruct[][] maze) throws RemoteException; 

	TRStruct[] returnTR() throws RemoteException;
	
	void updateTreasure(TRStruct[] tr) throws RemoteException;

	void createTreasure(int id) throws RemoteException;

	PLStruct[] returnPL() throws RemoteException;
	
	void updatePlayer(PLStruct[] pl) throws RemoteException;

	void initialPlayer(int num, String username) throws RemoteException;
	
	void getAction(int index, int action) throws RemoteException;

	int checkConnection() throws  RemoteException;

}