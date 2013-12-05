package MallVisualiser.Editor;

public class PickUp {
	private int PositieX;
	private int PositieY;
	private int PickUpNumber;

	public PickUp(int PositieX, int PositieY, int PickUpNumber) {
		this.PositieX = PositieX;
		this.PositieY = PositieY;
		this.PickUpNumber = PickUpNumber;
	}

	public int getPositieX() {
		return PositieX;
	}

	public int getPositieY() {
		return PositieY;
	}

	public int getPickUpNumber() {
		return PickUpNumber;
	}

	public void setPositieX(int x) {
		PositieX = x;
	}

	public void setPositieY(int y) {
		PositieX = y;
	}

	public void setPickUpNumber(int number) {
		PickUpNumber = number;
	}

	public void voegPickToe(int PositieX, int PositieY, int PickUpNumber) {
		this.PositieX = PositieX;
		this.PositieY = PositieY;
		this.PickUpNumber = PickUpNumber;
	}

	public String toString() {
		String res = ("PickUp " + PickUpNumber + " ligt op Positie(x,y) : ("
				+ PositieX + "," + PositieY + ")");
		return res;
	}

}