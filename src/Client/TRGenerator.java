package Client;

import java.util.Random;

public class TRGenerator {
	private int size_x, size_y;
	private int num;
	private TRStruct tr[];

	private void initialStatus() {
		for (int i = 0; i < num; i++) {
			tr[i] = new TRStruct();
			tr[i].setX(0);
			tr[i].setY(0);
		}
	}

	private void setLocation(MazeStruct[][] maze) {
		Random random1 = new Random();
		Random random2 = new Random();
		int location_x, location_y;

		for (int i = 0; i < num; i++) {
			boolean successful = false;
			while (!successful) {
				location_x = random1.nextInt(size_x);
				location_y = random2.nextInt(size_y);
				if (maze[location_x][location_y].getST() != 0) {
					successful = false;
				} else {
					tr[i].setX(location_x);
					tr[i].setY(location_y);
					maze[location_x][location_y].setST(1);
					maze[location_x][location_y].setCompID(i);
					successful = true;
				}
			}
		}
	}

	public TRStruct[] treasureGenerator(MazeStruct[][] maze, int num, int x, int y) {
		size_x = x;
		size_y = y;
		this.num = num;
		tr = new TRStruct[num];

		initialStatus();
		setLocation(maze);
		
		return tr;
	}
}
