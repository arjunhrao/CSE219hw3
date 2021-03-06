package regio_vinco;

import audio_manager.AudioManager;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pacg.PointAndClickGame;
import static regio_vinco.RegioVinco.*;

/**
 * This class is a concrete PointAndClickGame, as specified in The PACG
 * Framework. Note that this one plays Regio Vinco.
 *
 * @author McKillaGorilla
 */
public class RegioVincoGame extends PointAndClickGame {

    // THIS PROVIDES GAME AND GUI EVENT RESPONSES
    RegioVincoController controller;

    // THIS PROVIDES MUSIC AND SOUND EFFECTS
    AudioManager audio;
    
    // THESE ARE THE GUI LAYERS
    Pane backgroundLayer;
    Pane gameLayer;
    Pane guiLayer;
    
    // WE'LL SET THESE WHEN WE ENTER THE GAME
    Label regionLabel;
    Label statsLabel;
    Label dialogStatsLabel;

    /**
     * Get the game setup.
     */
    public RegioVincoGame(Stage initWindow) {
	super(initWindow, APP_TITLE, TARGET_FRAME_RATE);
	initAudio();
    }
    
    public AudioManager getAudio() {
	return audio;
    }
    
    public Pane getGameLayer() {
	return gameLayer;
    }

    /**
     * Initializes audio for the game.
     */
    private void initAudio() {
	audio = new AudioManager();
	try {
	    audio.loadAudio(TRACKED_SONG, TRACKED_FILE_NAME);
	    audio.play(TRACKED_SONG, true);

	    audio.loadAudio(AFGHAN_ANTHEM, AFGHAN_ANTHEM_FILE_NAME);
	    audio.loadAudio(SUCCESS, SUCCESS_FILE_NAME);
	    audio.loadAudio(FAILURE, FAILURE_FILE_NAME);
	} catch (Exception e) {
	    
	}
    }

    // OVERRIDDEN METHODS - REGIO VINCO IMPLEMENTATIONS
    // initData
    // initGUIControls
    // initGUIHandlers
    // reset
    // updateGUI
    /**
     * Initializes the complete data model for this application, forcing the
     * setting of all game data, including all needed SpriteType objects.
     */
    @Override
    public void initData() {
	// INIT OUR DATA MANAGER
	data = new RegioVincoDataModel();
	data.setGameDimensions(GAME_WIDTH, GAME_HEIGHT);

	boundaryLeft = 0;
	boundaryRight = GAME_WIDTH;
	boundaryTop = 0;
	boundaryBottom = GAME_HEIGHT;
    }

    /**
     * For initializing all GUI controls, specifically all the buttons and
     * decor. Note that this method must construct the canvas with its custom
     * renderer.
     */
    @Override
    public void initGUIControls() {
	// LOAD THE GUI IMAGES, WHICH INCLUDES THE BUTTONS
	// THESE WILL BE ON SCREEN AT ALL TIMES
	backgroundLayer = new Pane();
	addStackPaneLayer(backgroundLayer);
	addGUIImage(backgroundLayer, BACKGROUND_TYPE, loadImage(BACKGROUND_FILE_PATH), BACKGROUND_X, BACKGROUND_Y);
	
	
	// THEN THE GAME LAYER
	gameLayer = new Pane();
	addStackPaneLayer(gameLayer);
	
	// THEN THE GUI LAYER
	guiLayer = new Pane();

	addStackPaneLayer(guiLayer);
	addGUIImage(guiLayer, TITLE_TYPE, loadImage(TITLE_FILE_PATH), TITLE_X, TITLE_Y);
	addGUIButton(guiLayer, START_TYPE, loadImage(START_BUTTON_FILE_PATH), START_X, START_Y);
	addGUIButton(guiLayer, EXIT_TYPE, loadImage(EXIT_BUTTON_FILE_PATH), EXIT_X, EXIT_Y);
	
	Button startButton = this.guiButtons.get(START_TYPE);
	startButton.setBorder(Border.EMPTY);
	startButton.setPadding(Insets.EMPTY);
	startButton.setEffect(null);
	Button exitButton = this.guiButtons.get(EXIT_TYPE);
	exitButton.setBorder(Border.EMPTY);
	exitButton.setPadding(Insets.EMPTY);
	exitButton.setEffect(null);
	
	// NOW LET'S ADD THE REGION LABEL
	regionLabel = new Label();
	guiLayer.getChildren().add(regionLabel);
	regionLabel.translateXProperty().setValue(REGION_TITLE_X);
	regionLabel.translateYProperty().setValue(REGION_TITLE_Y);
	regionLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, null)));
	regionLabel.setTextFill(REGION_COLOR);
	regionLabel.setFont(REGION_FONT);
	regionLabel.setMinWidth(REGION_TITLE_WIDTH);
	regionLabel.setMaxWidth(REGION_TITLE_WIDTH);
	regionLabel.setMinHeight(REGION_TITLE_HEIGHT);
	regionLabel.setMaxHeight(REGION_TITLE_HEIGHT);
	
	// NOTE THAT THE MAP IS ALSO AN IMAGE, BUT
	// WE'LL LOAD THAT WHEN A GAME STARTS, SINCE
	// WE'LL BE CHANGING THE PIXELS EACH TIME
	// FOR NOW WE'LL JUST LOAD THE ImageView
	// THAT WILL STORE THAT IMAGE
	ImageView mapView = new ImageView();
	mapView.setX(MAP_X);
	mapView.setY(MAP_Y);
	guiImages.put(MAP_TYPE, mapView);
	guiLayer.getChildren().add(mapView);
	
	// IN GAME STATS
	statsLabel = new Label("");
	guiLayer.getChildren().add(statsLabel);
	statsLabel.translateXProperty().setValue(STATS_X);
	statsLabel.translateYProperty().setValue(STATS_Y);
	statsLabel.setTextFill(STATS_COLOR);
	statsLabel.setFont(STATS_FONT);

	// NOW LOAD THE WIN DISPLAY, WHICH WE'LL ONLY
	// MAKE VISIBLE AND ENABLED AS NEEDED
	ImageView winView = addGUIImage(guiLayer, WIN_DISPLAY_TYPE, loadImage(WIN_DISPLAY_FILE_PATH), WIN_X, WIN_Y);
	winView.setVisible(false);
	
	// AND THE STATS IN THE DIALOG
	dialogStatsLabel = new Label();
	guiLayer.getChildren().add(dialogStatsLabel);
	dialogStatsLabel.translateXProperty().setValue(DIALOG_STATS_X);
	dialogStatsLabel.translateYProperty().setValue(DIALOG_STATS_Y);
	dialogStatsLabel.setTextFill(DIALOG_STATS_COLOR);
	dialogStatsLabel.setFont(DIALOG_STATS_FONT);
	dialogStatsLabel.setWrapText(true);
	dialogStatsLabel.setVisible(false);
    }
    
    public void setMapVisible(boolean visible) {
	ImageView mapView = guiImages.get(MAP_TYPE);
	mapView.setVisible(visible);
    }
    
    // HELPER METHOD FOR LOADING IMAGES
    private Image loadImage(String imagePath) {	
	Image img = new Image("file:" + imagePath);
	return img;
    }
    
    public void updateStatsLabel(String statsText) {
	statsLabel.setText(statsText);
    }
    
    public void updateDialogStatsLabel(String statsText) {
	dialogStatsLabel.setText(statsText);
    }

    /**
     * For initializing all the button handlers for the GUI.
     */
    @Override
    public void initGUIHandlers() {
	controller = new RegioVincoController(this);

	Button startButton = guiButtons.get(START_TYPE);
	startButton.setOnAction(e -> {
	    controller.processStartGameRequest();
	});

	Button exitButton = guiButtons.get(EXIT_TYPE);
	exitButton.setOnAction(e -> {
	    controller.processExitGameRequest();
	});

	// MAKE THE CONTROLLER THE HOOK FOR KEY PRESSES
	keyController.setHook(controller);

	// SETUP MOUSE PRESSES ON THE MAP
	ImageView mapView = guiImages.get(MAP_TYPE);
	mapView.setOnMousePressed(e -> {
	    controller.processMapClickRequest((int) e.getX(), (int) e.getY());
	});
	
	// KILL THE APP IF THE USER CLOSES THE WINDOW
	window.setOnCloseRequest(e->{
	    controller.processExitGameRequest();
	});
    }

    /**
     * Called when a game is restarted from the beginning, it resets all game
     * data and GUI controls so that the game may start anew.
     */
    @Override
    public void reset() {
	// IF THE WIN DIALOG IS VISIBLE, MAKE IT INVISIBLE
	setMapVisible(true);
	guiImages.get(WIN_DISPLAY_TYPE).setVisible(false);
	dialogStatsLabel.setVisible(false);

	// AND RESET ALL GAME DATA
	data.reset(this);
	
	// DISPLAY THE REGION TITLE
	regionLabel.setText(REGION_TITLE);
    }

    /**
     * This mutator method changes the color of the debug text.
     *
     * @param initColor Color to use for rendering debug text.
     */
    public static void setDebugTextColor(Color initColor) {
//        debugTextColor = initColor;
    }

    /**
     * Called each frame, this method updates the rendering state of all
     * relevant GUI controls, like displaying win and loss states and whether
     * certain buttons should be enabled or disabled.
     */
    int backgroundChangeCounter = 0;

    @Override
    public void updateGUI() {
	// IF THE GAME IS OVER, DISPLAY THE APPROPRIATE RESPONSE
	if (data.won()) {
	    ImageView winImage = guiImages.get(WIN_DISPLAY_TYPE);
	    winImage.setVisible(true);
	    dialogStatsLabel.setVisible(true);
	}
    }

    public void reloadMap() {
	Image tempMapImage = loadImage(AFG_MAP_FILE_PATH);
	PixelReader pixelReader = tempMapImage.getPixelReader();
	WritableImage mapImage = new WritableImage(pixelReader, (int) tempMapImage.getWidth(), (int) tempMapImage.getHeight());
	ImageView mapView = guiImages.get(MAP_TYPE);
	mapView.setImage(mapImage);
	int numSubRegions = ((RegioVincoDataModel) data).getRegionsFound() + ((RegioVincoDataModel) data).getRegionsNotFound();
	this.boundaryTop = -(numSubRegions * 50);
	
	// MAKE THE OUTER BORDER PIXELS TRANSPARENT
	PixelReader mapReader = mapImage.getPixelReader();
	PixelWriter mapWriter = mapImage.getPixelWriter();
	for (int i = 0; i < mapImage.getWidth(); i++) {
	    for (int j = 0; j < mapImage.getHeight(); j++) {
		Color testColor = mapReader.getColor(i, j);
		if (testColor.equals(MAP_COLOR_KEY)) {
		    mapWriter.setColor(i, j, TRANSPARENT_COLOR);
		}
	    }
	}

	// AND GIVE THE WRITABLE MAP TO THE DATA MODEL
	((RegioVincoDataModel) data).setMapImage(mapImage);
    }
    
    public void setDialogStatsVisible(boolean visible) {
	dialogStatsLabel.setVisible(visible);
    }
}