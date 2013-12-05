package MallVisualiser.Editor;

import java.awt.event.MouseEvent;
import java.util.List;

import MallVisualiser.Assets;
import MallVisualiser.Editor.EditorButton.ButtonType;
import framework.Button;
import framework.Camera;
import framework.Game;
import framework.Graphics;
import framework.Input.TouchEvent;
import framework.Screen;
import framework.ToggleButton;

public class EditorScreen extends Screen {
	private ToggleButton[] toggleButtons;
	private Button[] buttons;
	private World world;
	private WorldIO worldIO;

	private int mouseX;
	private int mouseY;
	private boolean[] buttonDown = new boolean[4];

	private Camera cam;

	public EditorScreen(Game game) {
		super(game);
		this.cam = game.getCamera();
		world = World.newWorld(100, 100);
		worldIO = new WorldIO(world, game.getFileIO());

		toggleButtons = new ToggleButton[5];
		{
			toggleButtons[0] = new EditorToggleButton(
					EditorToggleButton.ButtonType.Buildmode, game,
					Assets.editorBuildIcon, Assets.editorBuildIcon_toggled,
					824 + 25, // x
					50 // y
			);
			toggleButtons[0].w = 50;
			toggleButtons[0].h = 50;
		}
		{
			toggleButtons[1] = new EditorToggleButton(
					EditorToggleButton.ButtonType.Destroymode, game,
					Assets.editorDestroyIcon, Assets.editorDestroyIcon_toggled,
					824 + 25 + 50 + 25, // x
					50 // y
			);
			toggleButtons[1].w = 50;
			toggleButtons[1].h = 50;
		}
		{
			toggleButtons[2] = new EditorToggleButton(
					EditorToggleButton.ButtonType.Startmode, game,
					Assets.editorFlagIcon, Assets.editorFlagIcon_toggled,
					824 + 25, // x
					250 // y
			);
			toggleButtons[2].w = 50;
			toggleButtons[2].h = 50;
		}
		{
			toggleButtons[3] = new EditorToggleButton(
					EditorToggleButton.ButtonType.Runmode, game,
					Assets.editorRunIcon, Assets.editorRunIcon_toggled,
					824 + 25 + 50 + 25, // x
					150 // y
			);
			toggleButtons[3].w = 50;
			toggleButtons[3].h = 50;
		}
		{
			toggleButtons[4] = new EditorToggleButton(
					EditorToggleButton.ButtonType.PickUpmode, game,
					Assets.editorPickUp1Icon, Assets.editorPickUp1Icon_toggled,
					824 + 25, // x
					150 // y
			);
			toggleButtons[4].w = 50;
			toggleButtons[4].h = 50;
		}

		// Register shared-toggles:
		ToggleButton[] sharedToggles = new ToggleButton[5];
		sharedToggles[0] = toggleButtons[0];
		sharedToggles[1] = toggleButtons[1];
		sharedToggles[2] = toggleButtons[2];
		sharedToggles[3] = toggleButtons[3];
		sharedToggles[4] = toggleButtons[4];
		toggleButtons[0].registerSharedToggles(sharedToggles);
		toggleButtons[1].registerSharedToggles(sharedToggles);
		toggleButtons[2].registerSharedToggles(sharedToggles);
		toggleButtons[3].registerSharedToggles(sharedToggles);
		toggleButtons[4].registerSharedToggles(sharedToggles);
		sharedToggles = null;

		buttons = new Button[5];
		{
			buttons[0] = new EditorButton(ButtonType.NewMap, game,
					Assets.editorNewMapIcon, 800, 300, worldIO);
			buttons[0].w = 125;
			buttons[0].h = 75;
		}
		{
			buttons[4] = new EditorButton(ButtonType.ResizeMap, game,
					Assets.editorResizeMapIcon, 800, 375, worldIO);
			buttons[4].w = 125;
			buttons[4].h = 75;
		}
		{
			buttons[1] = new EditorButton(ButtonType.LoadMap, game,
					Assets.editorLoadMapIcon, 800, 450, worldIO);
			buttons[1].w = 125;
			buttons[1].h = 75;
		}
		{
			buttons[2] = new EditorButton(ButtonType.SaveMap, game,
					Assets.editorSaveMapIcon, 800, 525, worldIO);
			buttons[2].w = 125;
			buttons[2].h = 75;
		}
		{
			buttons[3] = new EditorButton(ButtonType.toMenu, game,
					Assets.editorToMenuIcon, 800, 600, worldIO);
			buttons[3].w = 125;
			buttons[3].h = 75;
		}
	}

	public void update(float deltaTime) {
		try {
			Graphics g = game.getGraphics();
			int bufferWidth = g.getWidth();
			int bufferHeight = g.getHeight();
			List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
			game.getInput().getKeyEvents();

			int len = touchEvents.size();
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);

				// Zooming/Panning:
				if (event.type == TouchEvent.TOUCH_DOWN) {
					mouseX = event.x;
					mouseY = event.y;
					buttonDown[event.button] = true;
				}
				if (event.type == TouchEvent.TOUCH_UP) {
					mouseX = event.x;
					mouseY = event.y;
					buttonDown[event.button] = false;
					world.deselectTile();
				}

				if (event.type == TouchEvent.TOUCH_DRAGGED) {
					if (buttonDown[MouseEvent.BUTTON3]) {
						cam.moveCameraWith(mouseX - event.x, mouseY - event.y);
						mouseX = event.x;
						mouseY = event.y;
					}
					if (buttonDown[MouseEvent.BUTTON2]) {
						cam.changeCameraHeightWith(event.y - mouseY);
						mouseX = event.x;
						mouseY = event.y;
					}

				}

				// Press buttons:
				if (event.type == TouchEvent.TOUCH_UP
						&& event.button == MouseEvent.BUTTON1) {
					for (int j = 0; j < buttons.length; j++) {
						buttons[j].pressButton(event.x, event.y);
					}
					for (int j = 0; j < toggleButtons.length; j++) {
						toggleButtons[j].pressButton(event.x, event.y);
					}
				}

				// Building:
				if (event.type == TouchEvent.TOUCH_DRAGGED
						|| event.type == TouchEvent.TOUCH_DOWN) {
					if (buttonDown[MouseEvent.BUTTON1] && event.x < 824) { // World
																			// is
																			// only
																			// shown
																			// in
																			// x
																			// \in
																			// {0,824}
						if (toggleButtons[0].isToggled) {
							// Build-mode
							world.build((int) (event.x * cam.viewW
									/ bufferWidth + cam.viewX), (int) (event.y
									* cam.viewH / bufferHeight + cam.viewY));
						} else if (toggleButtons[1].isToggled) {
							// Destroy-mode
							world.destroy((int) (event.x * cam.viewW
									/ bufferWidth + cam.viewX), (int) (event.y
									* cam.viewH / bufferHeight + cam.viewY));
						} else if (toggleButtons[2].isToggled) {
							// Start-mode
							world.start((int) (event.x * cam.viewW
									/ bufferWidth + cam.viewX), (int) (event.y
									* cam.viewH / bufferHeight + cam.viewY));
						} else if (toggleButtons[3].isToggled) {
							// Run-Mode
							world.run(
									(int) (event.x * cam.viewW / bufferWidth + cam.viewX),
									(int) (event.y * cam.viewH / bufferHeight + cam.viewY));
						} else if (toggleButtons[4].isToggled) {
							// PickUp1-mode
							world.pickup1((int) (event.x * cam.viewW
									/ bufferWidth + cam.viewX), (int) (event.y
									* cam.viewH / bufferHeight + cam.viewY));
						}
						world.selectTile((int) (event.x * cam.viewW
								/ bufferWidth + cam.viewX), (int) (event.y
								* cam.viewH / bufferHeight + cam.viewY));
					}
					if (buttonDown[MouseEvent.BUTTON2]) {
						cam.changeCameraHeightWith(event.y - mouseY);
						mouseX = event.x;
						mouseY = event.y;
					}

				}

			}
		} catch (NullPointerException e) {
			// Prevents multi-thread problems with dispose.
		}
	}

	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		try {
			g.clear(0xff000000);

			// World:
			g.setClipBounds(0, 0, 824, 800);
			world.paint(g, cam);
			world.paintSelectedIndex(g, cam);
			g.resetClipBounds();

			// UI:
			g.drawPixmap(Assets.editorBackgroundBoef, 780, 0);
			for (int i = 0; i < toggleButtons.length; i++) {
				toggleButtons[i].paint(g);
			}
			for (int i = 0; i < buttons.length; i++) {
				buttons[i].paint(g);
			}
		} catch (NullPointerException e) {
			g.resetClipBounds();
			// Prevents multi-thread problems with dispose.
		}
	}

	public void pause() {
		// Settings.save(game.getFileIO());
	}

	public void resume() {

	}

	public void dispose() {
		toggleButtons = null;
		buttons = null;
		world.dispose();
		world = null;
		worldIO = null;
		buttonDown = null;
		cam = null;
	}
}
