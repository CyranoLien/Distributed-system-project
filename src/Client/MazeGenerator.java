package Client;

import java.util.Arrays;
import java.util.Collections;

public class MazeGenerator {
	private int size_x, size_y;
	private int[][] cell;
	private MazeStruct[][] maze;

	private void generateMaze(int cx, int cy) {
		// 枚举类有一个values方法，以数组的方式返回一个枚举类型的所有常量
		// dirs就是一个数组，每个数组分量就代表一个枚举里的方向常量，即一个方向
		DIR[] dirs = DIR.values();

		// 将一个数组转化为一个List对象,这个方法会返回一个ArrayList类型的对象,这个ArrayList类并非java.util.ArrayList类,而是Arrays类的静态内部类
		// shuffle是Collections 中的静态方法,它用于将一个list集合中的元素顺序进行打乱,类似于洗牌的过程
		Collections.shuffle(Arrays.asList(dirs));

		// 遍历dirs中的每一个数
		for (DIR dir : dirs) {
			int nx = cx + dir.dx;
			int ny = cy + dir.dy;

			// between()判断当前是否在迷宫外
			if (between(nx, size_x) && between(ny, size_y) && (cell[nx][ny] == 0)) {
				cell[cx][cy] |= dir.bit;
				cell[nx][ny] |= dir.opposite.bit;
				generateMaze(nx, ny);
			}
		}
	};

	private enum DIR {

		N(1, 0, -1), S(2, 0, 1), E(4, 1, 0), W(8, -1, 0);
		private final int bit;
		private final int dx;
		private final int dy;
		private DIR opposite;

		// use the static initializer to resolve forward references
		static {
			N.opposite = S;
			S.opposite = N;
			E.opposite = W;
			W.opposite = E;
		}

		private DIR(int bit, int dx, int dy) {
			this.bit = bit;
			this.dx = dx;
			this.dy = dy;
		}
	}

	private boolean between(int v, int upper) {
		return (v >= 0) && (v < upper);
	}

	public MazeStruct[][] mazeGenerator(int x, int y) {
		int m = 0, n = 0;
		size_x = x;
		size_y = y;
		cell = new int[size_x][size_y];
		maze = new MazeStruct[size_x][size_y];
		generateMaze(0, 0);

		for (int j = 0; j < size_y; j++) {
			for (int i = 0; i < size_x; i++) {
				maze[i][j] = new MazeStruct();
				m = ((cell[i][j] & 1) == 0) ? 1 : 0;
				n = ((cell[i][j] & 8) == 0) ? 1 : 0;
				maze[i][j].setWX(m);
				maze[i][j].setWY(n);
				maze[i][j].setCompID(-1);
			}
		}
		return maze;
	}
}
