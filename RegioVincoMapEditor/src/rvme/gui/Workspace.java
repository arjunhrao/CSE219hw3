/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import properties_manager.PropertiesManager;
import saf.components.AppWorkspaceComponent;
import rvme.MapEditorApp;
import rvme.PropertyType;
import rvme.controller.MapController;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import static saf.settings.AppPropertyType.*;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;

/**
 *
 * @author McKillaGorilla
 */
public class Workspace extends AppWorkspaceComponent {
    MapEditorApp app;
    Pane renderPane = new Pane();
    MapController mapController;
    double xOrigin;
    double yOrigin;
    Boolean hasLines;
    int counter = 0;
    
    //HW4
    SplitPane splitPane = new SplitPane();
    FlowPane editToolbar = new FlowPane();
    Pane mapPane = new Pane();
    TableView<SubRegion> subregionsTable;
    TableColumn subregionNameColumn;
    TableColumn capitalNameColumn;
    TableColumn leaderNameColumn;
    Button renameMapButton;
    Button addImageButton;
    Button removeImageButton;
    Button changeBackgroundColorButton;
    Button changeBorderColorButton;
    Button randomizeMapColorsButton;
    Button changeMapDimensionsButton;
    Button playAnthemButton;
    Button pauseAnthemButton;
    
    public Workspace(MapEditorApp initApp) {
        app = initApp;
        workspace = new Pane();
        app.getGUI().getAppPane().setCenter(splitPane);
        
        initHW4Layout();
        
        xOrigin = app.getGUI().getPrimaryScene().getWidth()/2;
        yOrigin = (app.getGUI().getPrimaryScene().getHeight()-60)/2;
        
        //dont want this here since HW4
        //removeButtons();
        hasLines = false;
        
        //create controller
        mapController = new MapController(app);
        
        //initialize processing of eventhandlers - create new method
        processEvents();
        processHW4Events();
    }
    
    public void initHW4Layout() {
        workspace.getStyleClass().add(CLASS_BORDERED_PANE);
        
        FlowPane fp = (FlowPane)app.getGUI().getAppPane().getTop();
        
        //give the edit toolbar the right style
        editToolbar.getStyleClass().add(CLASS_BORDERED_PANE);
        //give the edit toolbar the appropriate controls
        //newButton = initChildButton(fileToolbarPane,	NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        //ADDING BUTTONS
        renameMapButton = app.getGUI().initChildButton(editToolbar, RENAME_MAP.toString(), RENAME_MAP_TT.toString(), false);
        addImageButton = app.getGUI().initChildButton(editToolbar, ADD_IMAGE.toString(), ADD_IMAGE_TT.toString(), false);
        removeImageButton = app.getGUI().initChildButton(editToolbar, REMOVE.toString(), REMOVE_TT.toString(), false);
        //changeBackgroundColorButton = app.getGUI().initChildButton(editToolbar, CHANGE_BACKGROUND_COLOR.toString(), CHANGE_BACKGROUND_COLOR_TT.toString(), false);
        //changeBorderColorButton = app.getGUI().initChildButton(editToolbar, CHANGE_BORDER_COLOR.toString(), CHANGE_BORDER_COLOR_TT.toString(), false);
        //Turns out the above are supposed to be color pickers.
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ColorPicker changeBackgroundColorPicker = new ColorPicker();
        ColorPicker changeBorderColorPicker = new ColorPicker();
        Label bgColorLabel = new Label(props.getProperty(CHANGE_BACKGROUND_COLOR_TT) + ": ");
        Label borderColorLabel = new Label(props.getProperty(CHANGE_BORDER_COLOR_TT) + ": ");
        HBox colorPickingToolbar = new HBox();
        colorPickingToolbar.getChildren().addAll(bgColorLabel, changeBackgroundColorPicker, borderColorLabel, changeBorderColorPicker);
        
        randomizeMapColorsButton = app.getGUI().initChildButton(editToolbar, RANDOMIZE_MAP_COLORS.toString(), RANDOMIZE_MAP_COLORS_TT.toString(), false);
        changeMapDimensionsButton = app.getGUI().initChildButton(editToolbar, CHANGE_MAP_DIMENSIONS.toString(), CHANGE_MAP_DIMENSIONS_TT.toString(), false);
        playAnthemButton = app.getGUI().initChildButton(editToolbar, PLAY_ANTHEM.toString(), PLAY_ANTHEM_TT.toString(), false);
        pauseAnthemButton = app.getGUI().initChildButton(editToolbar, PAUSE_ANTHEM.toString(), PAUSE_ANTHEM_TT.toString(), false);
        //add the colorpicking stuff after all of the buttons but before the sliders
        editToolbar.getChildren().add(colorPickingToolbar);
        editToolbar.setHgap(1.0);
        editToolbar.setVgap(5.0);

        //ADDING SLIDERS and their labels
        Slider borderThickness = new Slider();
        Slider zoom = new Slider();
        Label borderThicknessLabel = new Label("Border Thickness:");
        Label zoomLabel = new Label("Zoom:");
        HBox sliders = new HBox();
        sliders.getChildren().addAll(borderThicknessLabel, borderThickness, zoomLabel, zoom);
        editToolbar.getChildren().addAll(sliders);
        //now add the toolbar to the flowpane. SUCCESS! Although we might want to consider spacing and button size...
        
        fp.getChildren().addAll(editToolbar);
        editToolbar.setMinWidth(800.0);
        
        //set up the split pane
        initTableAndImage(); //also adds the table to the split pane
        
        //add the split pane to the workspace
        //splitPane.setStyle("-fx-background-color: blue;");
        //workspace.getChildren().addAll(splitPane);
        //workspace.setStyle("-fx-background-color: lightblue;");
        
    }
    
    public void initTableAndImage() {
        //be able to get the properties
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        
        
        // NOW SETUP THE TABLE COLUMNS
        //this is how it should be
        //gonna use strings, not xml, for now
        //subregionNameColumn = new TableColumn(props.getProperty(PropertyType.CATEGORY_COLUMN_HEADING));
        subregionNameColumn = new TableColumn(props.getProperty(PropertyType.SUBREGIONNAME_COLUMN_HEADING));
        capitalNameColumn = new TableColumn(props.getProperty(PropertyType.CAPITAL_COLUMN_HEADING));
        leaderNameColumn = new TableColumn(props.getProperty(PropertyType.LEADER_COLUMN_HEADING));
        
        
        subregionsTable = new TableView();
        // AND LINK THE COLUMNS TO THE DATA - figure out how to do this later with the PropertyValueFactory
        subregionNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("subregionName"));
        capitalNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("capitalName"));
        leaderNameColumn.setCellValueFactory(new PropertyValueFactory<LocalDate, String>("leaderName"));
        subregionsTable.getColumns().add(subregionNameColumn);
        subregionsTable.getColumns().add(capitalNameColumn);
        subregionsTable.getColumns().add(leaderNameColumn);
        //PropertyValueFactory<String, String> xd = new PropertyValueFactory<String, String>("subregionName");
        
        DataManager dataManager = (DataManager)app.getDataComponent();
        SubRegion x = new SubRegion("New York", "Albany", "Andrew Cuomo");
        //ObservableList<SubRegion> subregions = new ObservableList<SubRegion>();
        //note that the above comment was a result of forgetting to instantiate the subregions list in the datamanager
        dataManager.getSubregions().add(x);
        SubRegion nj = new SubRegion("New Jersey", "Trenton", "Chris Christie");
        dataManager.getSubregions().add(nj);
        
        System.out.println(x.getCapitalName());
        subregionsTable.setItems(dataManager.getSubregions());
        
        //splitPane.getItems().addAll(subregionsTable);
        //splitPane.setDividerPosition(10, 300);
        
        //set up the fake image and add it to the mappane so that it shows up on the left and roughly centered
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + "FakeMapImage.png";
        Image fakeMapImage = new Image(imagePath);
        ImageView mapIm = new ImageView(fakeMapImage);
        mapIm.relocate(15,100);
        mapPane.getChildren().add(mapIm);
        
        //add to splitpane
        splitPane.getItems().add(mapPane);
        splitPane.getItems().add(subregionsTable);
        
    }
    
    
    public MapController getMapController() {return mapController;}
    
    public void processHW4Events() {
        changeMapDimensionsButton.setOnAction(e -> {
            //pop up a dimensions dialog
            mapController.processMapDimensions();
        });
        subregionsTable.setOnMouseClicked(e -> {
           
                
            if (e.getClickCount() == 2) {
                SubRegion it = subregionsTable.getSelectionModel().getSelectedItem();
                mapController.processEditSubregion(it);
            }
        });
        //app.getGUI().getNewButton().setOnAction(e -> {
            //mapController.processNewRequest();
        //});
        
    }
    
    public void processEvents() {

        
        renderPane.setOnMouseClicked(e -> {
            double x = e.getSceneX();
            double y = e.getSceneY();
            if (e.getButton() == MouseButton.PRIMARY) {
                mapController.processZoomIn(x, y, renderPane, xOrigin, yOrigin);
            }
            
            if (e.getButton() == MouseButton.SECONDARY) {
                mapController.processMapDimensions();
            }
        });
        //checks if g has been pressed and then toggles the lines by switching the value of hasLines
        app.getGUI().getPrimaryScene().setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.G) {
                if (hasLines)
                    hasLines = false;
                else
                    hasLines = true;
                reloadWorkspace();
            }
            
            if (e.getCode() == KeyCode.LEFT) {
                mapController.processKeyLeft(renderPane);
                reloadWorkspace();
            }
            if (e.getCode() == KeyCode.DOWN) {
                mapController.processKeyDown(renderPane);
                reloadWorkspace();
            }
            if (e.getCode() == KeyCode.RIGHT) {mapController.processKeyRight(renderPane);reloadWorkspace();}
            if (e.getCode() == KeyCode.UP) {mapController.processKeyUp(renderPane);reloadWorkspace();}
        });
        
        
    }
    
    @Override
    public void newDialog() {
        mapController.processNewMapDialog();
        
    }
    
    @Override
    public void reloadWorkspace() {
        
        DataManager dataManager = (DataManager)app.getDataComponent();
        //clears the workspace
        //workspace.getChildren().clear();
        
        //set the background to be lightblue
        //workspace.setStyle("-fx-background-color: lightblue;");
           
        //fill polygons with green
        dataManager.fillPolygons(Paint.valueOf("#556B2F"));
        
        
        //clears the pane so you can load something else
        renderPane.getChildren().clear();
        
        //add the polygons
        for (Polygon poly: dataManager.getPolygonList()) {
          renderPane.getChildren().addAll(poly);
        }
        
        //renderPane.setScaleX(4);
        //renderPane.setScaleY(4);
        //renderPane.setStyle("-fx-background-color: lightblue;");
        
        //adds the lines if hasLines = true; else, removes the lines.
        addLines(hasLines);
        
        
        //workspace.getChildren().addAll(renderPane);
        
        //clip it to avoid overflow
        Rectangle clip = new Rectangle();
        clip.setHeight(app.getGUI().getPrimaryScene().getHeight()-60);
        clip.setWidth(app.getGUI().getPrimaryScene().getWidth());
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        //workspace.setClip(clip);
        
        //hw4
        //workspace.getChildren().clear();
        //splitPane.getItems().clear();
        //editToolbar.getChildren().clear();
        //initHW4Layout();
        app.getGUI().getAppPane().setCenter(splitPane);
        
        
    }
    public void newRenderPane() {
        renderPane = new Pane();
    }

    @Override
    public void initStyle() {
    }
    
    //adds lines to the renderPane
    public void addLines(Boolean bool) {
        //NOTE: the renderPane does not have a width or height (default 0.0) until it is rendered.
        //I started the map default with gridlines off because it renders it once and then the toggling
        //works fine. Hopefully this is fine (I don't see why it wouldn't be).
        
        //renderPane.addLines
        Group group = new Group();
        double foo;
        //add longitudes
        for (int j = 0; j < 13; j++) {
            foo = 0.0;
            Line line = new Line();
            //if not the prime meridian, make it dashed
            if (j != 6 && j != 0 && j != 12) {
                line.getStrokeDashArray().addAll(2d,20d);
                line.setStroke(Paint.valueOf("#999999"));
            } else {
                line.setStroke(Paint.valueOf("#FAEBD7"));
            }
            //if first or last, move it a little inwards to avoid expanding the renderPane
            if (j == 0)
                foo = 0.01;
            if (j==12)
                foo = -0.01;

            line.setStartX((double)((double)((double)j+foo)/12.0*renderPane.getWidth()));
            line.setStartY(0.02f);
            line.setEndX((double)((double)((double)j+foo)/12.0*renderPane.getWidth()));
            line.setEndY(renderPane.getHeight()-.02);
            group.getChildren().add(line);
            
        }
        
        //add latitudes
        for (int j = 0; j < 7; j++) {
            Line line = new Line();
            foo = 0.0;
            //if not the prime meridian, make it dashed
            
            if (j != 3) {
                line.getStrokeDashArray().addAll(2d,20d);
                line.setStroke(Paint.valueOf("#999999"));
            } else {
                line.setStroke(Paint.valueOf("#FAEBD7"));
            }
            if (j == 0)
                foo = 0.01;
            if (j == 6)
                foo = -0.01;
            
            line.setStartY((double)((double)((double)j+foo)/6.0*(renderPane.getHeight())));
            line.setStartX(0.02f);
            line.setEndY((double)((double)((double)j+foo)/6.0*(renderPane.getHeight())));
            line.setEndX(renderPane.getWidth()-.02);
            group.getChildren().add(line);
            
        }
        renderPane.getChildren().add(group);
        group.setVisible(bool);
    }
    
    public void removeButtons() {
        FlowPane fp = (FlowPane)app.getGUI().getAppPane().getTop();
        fp.getChildren().remove(2);
        fp.getChildren().remove(0);
    }
    
    public Pane getRenderPane() {
        return renderPane;
    }
}
