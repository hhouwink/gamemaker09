package MallVisualiser.Editor;

import MallVisualiser.Assets;
import framework.Camera;
import framework.Graphics;
import framework.Pixmap;

public class World {

	private int[][] world;

	public Pixmap wall = Assets.editorWall;
	public Pixmap floor = Assets.editorFloor;

	public float width = 17;
	public float height = 17;
	public float width_ = width * 1.98f; // anti-aliasing paint size
	public float height_ = height * 1.98f; // anti-aliasing paint size

	private int selectedTile_x = -1;
	private int selectedTile_y = -1;

	protected World(int sizeX, int sizeY) {
		world = new int[sizeX][sizeY];
	}

	public static World newWorld(int sizeX, int sizeY) {
		if (sizeX >= 0 && sizeY >= 0) {
			return new World(sizeX, sizeY);
		}

		return null;
	}

	public void resizeWorld(int sizeX, int sizeY) {
		if (world == null || world[0] == null) {
			// We don't have a world to resize.
			renewWorld(sizeX, sizeY);
			return;
		}
		if (sizeX >= 0 && sizeY >= 0) {
			// Resize command is valid
			int[][] newWorld = new int[sizeX][sizeY];
			int ixMax = Math.min(world.length, sizeX);
			int iyMax = Math.min(world[0].length, sizeY);
			for (int ix = 0; ix < ixMax; ix++) {
				for (int iy = 0; iy < iyMax; iy++) {
					newWorld[ix][iy] = world[ix][iy];
				}
			}
			world = newWorld;
		}
	}

	public void renewWorld(int sizeX, int sizeY) {
		if (sizeX >= 0 && sizeY >= 0) {
			world = new int[sizeX][sizeY];
		}
	}

	public void renewWorld(int[][] world) {
		this.world = world;
	}

	public void paint(Graphics g, Camera cam) {
		if (world != null && world.length > 0 && world[0] != null
				&& world[0].length > 0) {
			for (int i = 0; i < world.length - 1; i++) {
				for (int j = 0; j < world[i].length - 1; j++) {
					if (world[i][j] == 0) {
						g.drawPixmap(wall, (i * width), (j * height), width_,
								height_, cam);
					} else {
						g.drawPixmap(floor, (i * width), (j * height), width_,
								height_, cam);
					}
					if (i == selectedTile_x && j == selectedTile_y) {
						g.drawRect((i * width), (j * height), width, height, 0,
								0, 255, 100, cam);
					}
				}
			}

			/*
			 * Anti-aliasing painting:
			 */

			// Last row
			for (int i = 0; i < world.length - 1; i++) {
				int j = world[i].length - 1;
				if (world[i][j] == 0) {
					g.drawPixmap(wall, (i * width), (j * height), width_,
							height * 1.1, cam);
				} else {
					g.drawPixmap(floor, (i * width), (j * height), width_,
							height * 1.1, cam);
				}
				if (i == selectedTile_x && j == selectedTile_y) {
					g.drawRect((i * width), (j * height), width, height, 0, 0,
							255, 100, cam);
				}
			}
			// Last column
			int i = world.length - 1;
			for (int j = 0; j < world[i].length - 1; j++) {
				if (world[i][j] == 0) {
					g.drawPixmap(wall, (i * width), (j * height), width * 1.1,
							height_, cam);
				} else {
					g.drawPixmap(floor, (i * width), (j * height), width * 1.1,
							height_, cam);
				}
				if (i == selectedTile_x && j == selectedTile_y) {
					g.drawRect((i * width), (j * height), width, height, 0, 0,
							255, 100, cam);
				}
			}
			// Last corner
			int j = world[i].length - 1;
			if (world[i][j] == 0) {
				g.drawPixmap(wall, (i * width), (j * height), width * 1.1,
						height * 1.1, cam);
			} else {
				g.drawPixmap(floor, (i * width), (j * height), width * 1.1,
						height * 1.1, cam);
			}
			if (i == selectedTile_x && j == selectedTile_y) {
				g.drawRect((i * width), (j * height), width, height, 0, 0, 255,
						100, cam);
			}

		}// End Sanity IF
	}

	public void paintSelectedIndex(Graphics g, Camera cam) {
		if (world != null && world.length > 0 && world[0] != null
				&& world[0].length > 0) {
			// Draw index near selected tile:
			if (selectedTile_x >= 0 && selectedTile_x < world.length
					&& selectedTile_y >= 0 && selectedTile_y < world[0].length) {
				String msg = "(" + selectedTile_x + "," + selectedTile_y + ")";
				int fontSize = 20;
				g.setFontSize(fontSize);
				g.drawRect((int) ((selectedTile_x * width - cam.viewX)
						/ cam.viewW * g.getWidth()), (int) ((selectedTile_y
						* height - cam.viewY)
						/ cam.viewH * g.getHeight() - 0.9 * fontSize),
						(int) (fontSize * msg.length() * 0.48), fontSize, 255,
						0, 0, 120);
				g.drawText(msg, selectedTile_x * width,
						selectedTile_y * height, 0x00ffff, cam);
			}
		}// End Sanity IF
	}

	public void build(int x, int y) {
		if (x >= 0 && y >= 0 && x < world.length * width
				&& y < world[0].length * height) {
			world[(int) (x / width)][(int) (y / height)] = 1;
		}
	}

	public void selectTile(int x, int y) {
		selectedTile_x = (int) (x / width);
		selectedTile_y = (int) (y / height);
	}

	public void deselectTile() {
		selectedTile_x = -1;
		selectedTile_y = -1;
	}

	public void destroy(int x, int y) {
		if (x >= 0 && y >= 0 && x < world.length * width
				&& y < world[0].length * height) {
			world[(int) (x / width)][(int) (y / height)] = 0;
		}
	}

	public void start(int x, int y) {
		if (x >= 0 && y >= 0 && x < world.length * width
				&& y < world[0].length * height) {
			world[(int) (x / width)][(int) (y / height)] = 2;
			// schrijf iets op de plek van de 2 te onthouden. zodra nieuwe 2.
			// dan wordt oude 2 een 0
		}
	}

	public int getNumElementsX() {
		return world.length;
	}

	public int getNumElementsY() {
		return world[0].length;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder((world.length + 1)
				* world[0].length * 2);
		for (int j = 0; j < world[0].length; j++) {
			for (int i = 0; i < world.length; i++) {
				sb.append(world[i][j]);
				sb.append(' ');
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	public void dispose() {
		world = null;
	}

	public float indexToCoordX(int x) {
		return width * (x + 0.5f);
	}

	public float indexToCoordY(int y) {
		return height * (y + 0.5f);
	}

	public boolean isAccessible(int x, int y) {
		if (x < world.length && y < world[x].length)
			return world[x][y] == 1;
		return false;
	}

}
