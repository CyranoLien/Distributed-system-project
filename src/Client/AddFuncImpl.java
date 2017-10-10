package Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class AddFuncImpl extends UnicastRemoteObject implements AddFunc {
	private static final long serialVersionUID = 1L;

	private PlayerInfo player_table;
	private int size_x = 15;
	private int size_y = 15;
	private int num_t = 10;
	private int num_p = 100;

	private MazeStruct[][] maze;
	private PLStruct[] pl;
	private TRStruct[] tr;

	public AddFuncImpl(String name) throws RemoteException {
		super();
		//player_table = new PlayerInfo();
		//player_table.stubInitial();
		// TODO Auto-generated constructor stub
	}

	public PlayerInfo returnTable() {
		return player_table;
	}

	public void updateTable(PlayerInfo player_table) {
		this.player_table = player_table;
	}

	public MazeStruct[][] returnMaze() throws RemoteException {
		return maze;
	}

	public void initialMaze() throws RemoteException {
		maze = new MazeStruct[size_x][size_y];
		tr = new TRStruct[num_t];
		pl = new PLStruct[num_p];

		MazeGenerator xxx = new MazeGenerator();
		maze = xxx.mazeGenerator(size_x, size_y);
		TRGenerator yyy = new TRGenerator();
		tr = yyy.treasureGenerator(maze, num_t, size_x, size_y);
	}
	
	public void updateMaze(MazeStruct[][] maze) throws RemoteException{
		this.maze = maze;
	}

	public TRStruct[] returnTR() throws RemoteException {
		return tr;
	}

	public void updateTreasure(TRStruct[] tr) {
		this.tr = tr;
	}

	public void createTreasure(int id) {
		Random random1 = new Random();
		Random random2 = new Random();
		int treasure_x, treasure_y;
		boolean successful = false;
		while (!successful) {
			treasure_x = random1.nextInt(size_x);
			treasure_y = random2.nextInt(size_y);
			if (maze[treasure_x][treasure_y].getST() != 0) {
				successful = false;
			} else {
				tr[id].setX(treasure_x);
				tr[id].setY(treasure_y);
				maze[treasure_x][treasure_y].setST(1);
				maze[treasure_x][treasure_y].setCompID(id);
				successful = true;
			}
		}
	}

	public PLStruct[] returnPL() throws RemoteException {
		return pl;
	}

	public void updatePlayer(PLStruct[] pl) {
		this.pl = pl;
	}

	public void initialPlayer(int index, String username) throws RemoteException {
		int m = 0, n = 0;
		boolean successful = false;
		while (!successful) {
			Random random1 = new Random();
			Random random2 = new Random();
			m = random1.nextInt(size_x);
			n = random2.nextInt(size_y);
			if (maze[m][n].getST() != 0) {
				successful = false;
			} else {
				maze[m][n].setST(2);
				//maze[m][n].setCompID(index);
				successful = true;
			}
		}
		pl[index] = new PLStruct();
		pl[index].setX(m);
		pl[index].setY(n);
		pl[index].setUsername(username);
		pl[index].setScore(0);
	}

	public void getAction(int index, int action) {
		int m = pl[index].getX();
		int n = pl[index].getY();
		int score = pl[index].getScore();
		int compID;

		switch (action) {
		case (0):
			break;
		case (1): // turn left
			if (maze[m][n].getWY() == 0) {
				m = m - 1;
				pl[index].setX(m);
				if (maze[m][n].getST() == 1) {
					pl[index].setScore(score + 1);
					compID = maze[m][n].getCompID();
					maze[m][n].setCompID(-1);
					createTreasure(compID);
				}
				//maze[m][n].setCompID(index);
				maze[m][n].setST(2);
			}
			break;
		case (2): // turn down
			if (n + 1 < 15) {
				if (maze[m][n + 1].getWX() == 0) {
					n = n + 1;
					pl[index].setY(n);
					if (maze[m][n].getST() == 1) {
						pl[index].setScore(score + 1);
						compID = maze[m][n].getCompID();
						maze[m][n].setCompID(-1);
						createTreasure(compID);
					}
					//maze[m][n].setCompID(index);
					maze[m][n].setST(2);
				}
			}
			break;
		case (3): // turn right
			if (m + 1 < 15) {
				if (maze[m + 1][n].getWY() == 0) {
					m = m + 1;
					pl[index].setX(m);
					if (maze[m][n].getST() == 1) {
						pl[index].setScore(score + 1);
						compID = maze[m][n].getCompID();
						maze[m][n].setCompID(-1);
						createTreasure(compID);
					}
					//maze[m][n].setCompID(index);
					maze[m][n].setST(2);
				}
			}
			break;
		case (4): // turn up
			if (maze[m][n].getWX() == 0) {
				n = n - 1;
				pl[index].setY(n);
				if (maze[m][n].getST() == 1) {
					pl[index].setScore(score + 1);
					compID = maze[m][n].getCompID();
					maze[m][n].setCompID(-1);
					createTreasure(compID);
				}
				//maze[m][n].setCompID(index);
				maze[m][n].setST(2);
			}
			break;
		}
	}

	public int checkConnection() throws RemoteException  {
		return 1;
	}

}