package regio_vinco;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * This is the Regio Vinco game application. Note that it extends the
 * PointAndClickGame class and overrides all the proper methods for setting up
 * the Data, the GUI, the Event Handlers, and update and timer task, the thing
 * that actually does the update scheduled rendering.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class RegioVinco extends Application {

    // THESE CONSTANTS SETUP THE GAME DIMENSIONS. THE GAME WIDTH
    // AND HEIGHT SHOULD MIRROR THE BACKGROUND IMAGE DIMENSIONS. WE
    // WILL NOT RENDER ANYTHING OUTSIDE THOSE BOUNDS.

    public static final int GAME_WIDTH = 1200;
    public static final int GAME_HEIGHT = 700;

    // FOR THIS APP WE'RE ONLY PLAYING WITH ONE MAP, BUT
    // IN THE FUTURE OUR GAMES WILL USE LOTS OF THEM
    public static final String REGION_NAME = "Afghanistan";
    public static final String MAPS_PATH = "./data/maps/";
    public static final String AFG_MAP_FILE_PATH = MAPS_PATH + "GreyscaleAFG.png";
    
    // SOME COLORS
    public static final Color MAP_COLOR_KEY = RegioVincoDataModel.makeColor(220, 110, 0);
    public static final Color TRANSPARENT_COLOR = Color.color(0, 0, 0, 0);

    // HERE ARE THE PATHS TO THE REST OF THE IMAGES WE'LL USE
    public static final String GUI_PATH = "./data/gui/";
    public static final String BACKGROUND_FILE_PATH = GUI_PATH + "RegioVincoBackground.jpg";
    public static final String TITLE_FILE_PATH = GUI_PATH + "RegioVincoTitle.png";
    public static final String START_BUTTON_FILE_PATH = GUI_PATH + "RegioVincoStartButton.png";
    public static final String START_BUTTON_MO_FILE_PATH = GUI_PATH + "RegioVincoStartButtonMouseOver.png";
    public static final String EXIT_BUTTON_FILE_PATH = GUI_PATH + "RegioVincoExitButton.png";
    public static final String EXIT_BUTTON_MO_FILE_PATH = GUI_PATH + "RegioVincoExitButtonMouseOver.png";
    public static final String SUB_REGION_FILE_PATH = GUI_PATH + "RegioVincoSubRegion.png";
    public static final String WIN_DISPLAY_FILE_PATH = GUI_PATH + "RegioVincoWinDisplay.png";

    // HERE ARE SOME APP-LEVEL SETTINGS, LIKE THE FRAME RATE. ALSO,
    // WE WILL BE LOADING SpriteType DATA FROM A FILE, SO THAT FILE
    // LOCATION IS PROVIDED HERE AS WELL. NOTE THAT IT MIGHT BE A 
    // GOOD IDEA TO LOAD ALL OF THESE SETTINGS FROM A FILE, BUT ALAS,
    // THERE ARE ONLY SO MANY HOURS IN A DAY
    public static final int TARGET_FRAME_RATE = 30;
    public static final String APP_TITLE = "Regio Vinco";
    
    // BACKGROUND IMAGE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    public static final int BACKGROUND_X = 0;
    public static final int BACKGROUND_Y = 0;
    
    // TITLE IMAGE
    public static final String TITLE_TYPE = "TITLE_TYPE";
    public static final int TITLE_X = 900;
    public static final int TITLE_Y = 0;
    
    // START GAME BUTTON
    public static final String START_TYPE = "START_TYPE";
    public static final int START_X = 900;
    public static final int START_Y = 100;

    // EXIT GAME BUTTON
    public static final String EXIT_TYPE = "EXIT_TYPE";
    public static final int EXIT_X = 1050;
    public static final int EXIT_Y = 100;
    
    // FOR THE REGION TITLE
    public static final String REGION_TITLE = "Afghanistan Provinces";
    public static final int REGION_TITLE_X = 900;
    public static final int REGION_TITLE_Y = 150;
    public static final Color REGION_COLOR = RegioVincoDataModel.makeColor(220, 220, 80);
    public static final Font REGION_FONT = Font.font("Times New Roman", FontWeight.NORMAL, 32);
    public static final int REGION_TITLE_WIDTH = 300;
    public static final int REGION_TITLE_HEIGHT = 50;
    
    // THE GAME MAP LOCATION
    public static final String MAP_TYPE = "MAP_TYPE";
    public static final String SUB_REGION_TYPE = "SUB_REGION_TYPE";
    public static final int MAP_X = 0;
    public static final int MAP_Y = 0;

    // THE WIN DIALOG
    public static final String WIN_DISPLAY_TYPE = "WIN_DISPLAY";
    public static final int WIN_X = 350;
    public static final int WIN_Y = 150;
    
    // THIS IS THE X WHERE WE'LL DRAW ALL THE STACK NODES
    public static final int STACK_X = 900;
    public static final int STACK_INIT_Y = 600;
    public static final int STACK_INIT_Y_INC = 50;

    // STATS
    public static final Color STATS_COLOR = RegioVincoDataModel.makeColor(200, 160, 80);
    public static final Font STATS_FONT = Font.font("Times New Roman", FontWeight.NORMAL, 28);
    public static final int STATS_Y = 650;
    public static final int STATS_X =  10;
    public static final Color DIALOG_STATS_COLOR = RegioVincoDataModel.makeColor(5, 5, 120);
    public static final Font DIALOG_STATS_FONT = Font.font("Times New Roman", FontWeight.NORMAL, 24);
    public static final int DIALOG_STATS_X = 400;
    public static final int DIALOG_STATS_Y = 330;
    
    // FOR THE STACK
    public static final Color SUB_REGION_NAME_COLOR = RegioVincoDataModel.makeColor(10, 10, 100);
    public static final Font SUB_REGION_FONT = Font.font("Times New Roman", FontWeight.NORMAL, 32);
    public static final Color BOTTOM_SUB_REGION_FILL_COLOR = RegioVincoDataModel.makeColor(100, 220, 100);
    public static final Color BOTTOM_SUB_REGION_TEXT_COLOR = Color.RED;
    public static final Color WRONG_SUB_REGION_FILL_COLOR = RegioVincoDataModel.makeColor(240, 160, 160);

    public static final int SUB_STACK_VELOCITY = 5;
    public static final int FIRST_REGION_Y_IN_STACK = GAME_HEIGHT-10;

    public static final String AUDIO_DIR = "./data/audio/";
    public static final String AFGHAN_ANTHEM_FILE_NAME = AUDIO_DIR + "AfghanistanNationalAnthem.mid";
    public static final String SUCCESS_FILE_NAME = AUDIO_DIR + "Success.wav";
    public static final String FAILURE_FILE_NAME = AUDIO_DIR + "Failure.wav";
    public static final String TRACKED_FILE_NAME = AUDIO_DIR + "Tracked.wav";
    public static final String AFGHAN_ANTHEM = "AFGHAN_ANTHEM";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILURE = "FAILURE";
    public static final String TRACKED_SONG = "TRACKED_SONG";

    /**
     * This is where the RegioVinco application starts. It proceeds to make a
     * game and pass it the window, and then starts it.
     *
     * @param primaryStage The window for this JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
	RegioVincoGame game = new RegioVincoGame(primaryStage);
	game.startGame();
    }

    /**
     * The RegioVinco game application starts here. All game data and GUI
     * initialization is done through the constructor, so we will just construct
     * our game and set it visible to start it up.
     *
     * @param args command line arguments, which will not be used
     */
    public static void main(String[] args) {
	launch(args);
    }
}
