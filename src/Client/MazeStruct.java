package Client;

public class MazeStruct implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private int wx;
	private int wy;
	private int status;	// 0: null, 1: treasure, 2: player;
	private int compID;

	public int getWX() {
		return wx;
	}

	public void setWX(int wx) {
		this.wx = wx;
	}

	public int getWY() {
		return wy;
	}

	public void setWY(int wy) {
		this.wy = wy;
	}

	public int getST() {
		return status;
	}

	public void setST(int status) {
		this.status = status;
	}
	
	public int getCompID() {
		return compID;
	}

	public void setCompID(int compID) {
		this.compID = compID;
	}
}
