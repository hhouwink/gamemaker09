package MallVisualiser.Editor;

import java.util.ArrayList;

public class Portefeuille {
	public ArrayList<PickUp> PickUps;

	public Portefeuille() {
		this.PickUps = new ArrayList<PickUp>();
	}

	public void AddPickUp(PickUp pickup) {
		this.PickUps.add(pickup);

	}

	public void RemovePickUp(int index) {
		this.PickUps.remove(index);
	}

	// public int getPickUpNumber(int index) {
	// return this.PickUps.get(index).getPickUpNumber();
	// }

	public PickUp getPickUp(int index) {
		return this.PickUps.get(index);
	}

	public int getLenght() {
		return this.PickUps.size();
	}

	public String toString() {
		String res = "";
		for (int i = 0; i < this.PickUps.size(); i++) {
			res += (this.PickUps.get(i).toString() + "\n");
		}

		return res;
	}

}
