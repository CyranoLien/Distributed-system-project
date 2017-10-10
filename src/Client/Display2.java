package Client;

import java.rmi.RemoteException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Display2 extends JFrame {
	private static final long serialVersionUID = 1L;

	private static PlayerInfo player_table;
	private static int size_x;
	private static int size_y;
	private int index;
	private String selfname;
	private int num_t;
	private int num_p;

	private AddFunc stub;
	private AddFunc func;
	int m, n;
	int score = 0;
	int input = 0;
	MazeStruct[][] maze;
	JLabel[] labelt = new JLabel[10];// treasure
	JLabel[] labelp = new JLabel[100];// player
	JLabel[] labeln = new JLabel[100];// player name
	JLabel[] labels = new JLabel[100];// player score
	JLabel[][] labelx = new JLabel[16][16];
	JLabel[][] labely = new JLabel[16][16];

	ImageIcon bg1 = new ImageIcon("img/bg1.png");
	ImageIcon bg2 = new ImageIcon("img/bg2.jpg");
	ImageIcon wx = new ImageIcon("img/wallx.png");
	ImageIcon wy = new ImageIcon("img/wally.png");
	ImageIcon tt = new ImageIcon("img/treax.jpg");
	ImageIcon pp = new ImageIcon("img/playx.png");

	JLabel labelBG0 = new JLabel(bg1);
	JLabel labelBG1 = new JLabel(bg2);
	JPanel panel0 = new JPanel();
	JPanel panel1 = new JPanel();
	JFrame frame1 = new JFrame();

	// 重写构造函数
	public Display2(int index, String username, MazeStruct[][] maze, int x, int y, int num_t, int num_p) {
		this.index = index;
		this.selfname = username;
		this.maze = maze;
		this.num_t = num_t;
		this.num_p = num_p;
		size_x = x;
		size_y = y;

		labelBG0.setBounds(200, 0, 400, 400);
		labelBG1.setBounds(0, 0, 200, 400);
		frame1.getLayeredPane().add(labelBG0, new Integer(Integer.MIN_VALUE));
		panel0 = (JPanel) frame1.getContentPane();
		panel0.setLayout(null);
		panel0.setOpaque(false); // set component transparent
		// panel0.setFocusable(true);
		// panel0.addKeyListener(new KeyBoardListener());

		frame1.getLayeredPane().add(labelBG1, new Integer(Integer.MIN_VALUE));
		panel1 = (JPanel) frame1.getContentPane();
		panel1.setLayout(null);
		panel1.setOpaque(false); // set component transparent

		build(); // build walls for maze
		frame1.setTitle("Frame " + index + "  " + selfname);
		frame1.setBounds(500, 200, 600, 422);
		frame1.setVisible(true);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// 初始化每个方格上边墙
	private void wallx(int i, int j, int x, int y) {
		labelx[i][j] = new JLabel(wx);
		labelx[i][j].setBounds(x, y, 19, 1);
		panel0.add(labelx[i][j]);
	}

	// 初始化每个方格左边墙
	private void wally(int i, int j, int x, int y) {
		labely[i][j] = new JLabel(wy);
		labely[i][j].setBounds(x, y, 1, 19);
		panel0.add(labely[i][j]);
	}

	// 初始化构造墙、宝藏和玩家
	private void build() {
		for (int i = 0; i < size_x; i++) {
			for (int j = 0; j < size_y; j++) {
				if (maze[i][j].getWX() == 1)
					wallx(i, j, 251 + 20 * i, 50 + 20 * j); // build every upper wall
				if (maze[i][j].getWY() == 1)
					wally(i, j, 250 + 20 * i, 51 + 20 * j); // build every left wall
			}
			wallx(i, size_x, 251 + 20 * i, 50 + 20 * size_x); // build every rest lower wall
			wally(size_y, i, 250 + 20 * size_y, 51 + 20 * i); // build every rest right wall
		}
		for (int i = 0; i < num_t; i++) {
			labelt[i] = new JLabel(tt);
			panel0.add(labelt[i]);
		}
		for (int i = 0; i < num_p; i++) {
			labelp[i] = new JLabel();
			labeln[i] = new JLabel();
			labels[i] = new JLabel();
			panel0.add(labelp[i]);
			panel0.add(labeln[i]);
			panel0.add(labels[i]);
		}
		// treasure();
		// player(now_p);
	}

	// 刷新显示宝藏
	private void treasure(TRStruct[] tr) {
		for (int i = 0; i < num_t; i++) {
			labelt[i].setBounds(250 + 20 * tr[i].getX(), 51 + 20 * tr[i].getY(), 18, 18);
		}
		panel0.repaint();
	}

	// 刷新显示玩家
	public void player(int now_p, PLStruct[] pl) throws RemoteException {
		int j = 0;
		for (int i = 0; i < now_p; i++) {
			if (stub.returnTable().getState(i) != -1) {
				labelp[i].setText(pl[i].getUsername());
				labelp[i].setBounds(250 + 20 * pl[i].getX(), 51 + 20 * pl[i].getY(), 20, 20);

				labeln[i].setText(pl[i].getUsername() + " :");
				labeln[i].setBounds(20, 20 + j * 40, 60, 30);
				labels[i].setText(pl[i].getScore() + "");
				labels[i].setBounds(60, 20 + j * 40, 60, 30);
				j++;
			} else {
				panel0.remove(labelp[i]);
				panel1.remove(labeln[i]);
				panel1.remove(labels[i]);
				// labeln[i].setText(pl[i].getUsername());
				// labeln[i].setBounds(20, 20 + i * 30, 180, 30);
				// labels[i].setText("Disconnected");
				// labels[i].setBounds(20, 50 + i * 30, 180, 30);
			}
		}
		panel1.repaint();
		panel0.repaint();
	}

	// // 键盘响应事件 0表示刷新 1表示向西 2表示向南 3表示向东 4表示向北
	// class KeyBoardListener extends KeyAdapter {
	// public void keyPressed(KeyEvent e) {
	// int action = 0;
	// TRStruct[] tr;
	// PLStruct[] pl;
	//
	// if (e.getKeyCode() == KeyEvent.VK_0) {
	// action = 0;
	// } else if ((e.getKeyCode() == KeyEvent.VK_1) || (e.getKeyCode() ==
	// KeyEvent.VK_A)) {
	// action = 1;
	// } else if ((e.getKeyCode() == KeyEvent.VK_2) || (e.getKeyCode() ==
	// KeyEvent.VK_S)) {
	// action = 2;
	// } else if ((e.getKeyCode() == KeyEvent.VK_3) || (e.getKeyCode() ==
	// KeyEvent.VK_D)) {
	// action = 3;
	// } else if ((e.getKeyCode() == KeyEvent.VK_4) || (e.getKeyCode() ==
	// KeyEvent.VK_W)) {
	// action = 4;
	// } else if (e.getKeyCode() == KeyEvent.VK_9) {
	// System.exit(0);
	// }
	// try {
	// int temp = func.returnTable().getState(func.returnTable().getServerID());
	// stub = func.returnTable().getStub(func.returnTable().getServerID());
	// if (temp == 1) {
	// stub.getAction(index, action);
	// tr = stub.returnTR();
	// pl = stub.returnPL();
	//
	// rebuild(func, tr, pl);
	// }
	// } catch (RemoteException e1) {
	// // TODO Auto-generated catch block
	// e1.printStackTrace();
	// }
	// }
	// }
	// 响应事件 0表示刷新 1表示向西 2表示向南 3表示向东 4表示向北 9表示退出游戏
	public void inputListener(String input) throws InterruptedException {
		int action = 0;
		// int input = Integer.parseInt(Input);
		if (input.equals("0")) {
			action = 0;
		} else if (input.equals("1")) {
			action = 1;
		} else if (input.equals("2")) {
			action = 2;
		} else if (input.equals("3")) {
			action = 3;
		} else if (input.equals("4")) {
			action = 4;
		} else if (input.equals("9")) {
			System.exit(0);
		}

		try {
			int temp = func.returnTable().getState(func.returnTable().getServerID());
			stub = func.returnTable().getStub(func.returnTable().getServerID());
			if (temp == 1) {
				stub.getAction(index, action);

				rebuild(func);
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// 更新游戏界面
	public void rebuild(AddFunc func) {
		this.func = func;
		TRStruct[] tr;
		PLStruct[] pl;
		int now_p;

		try {
			int temp = func.returnTable().getState(func.returnTable().getServerID());
			stub = func.returnTable().getStub(func.returnTable().getServerID());
			if (temp == 1) {
				player_table = stub.returnTable();
				now_p = player_table.getTotalplayer();
				tr = stub.returnTR();
				pl = stub.returnPL();

				treasure(tr);
				player(now_p, pl);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
