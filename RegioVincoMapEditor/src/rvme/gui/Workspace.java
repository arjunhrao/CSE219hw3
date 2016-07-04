/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import saf.components.AppWorkspaceComponent;
import rvme.MapEditorApp;
import rvme.controller.MapController;
import rvme.data.DataManager;

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
    
    public Workspace(MapEditorApp initApp) {
        app = initApp;
        workspace = new Pane();
        
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
    }
    
    public void initHW4Layout() {
        FlowPane fp = (FlowPane)app.getGUI().getAppPane().getTop();
        
        //give the edit toolbar the appropriate controls
        newButton = initChildButton(fileToolbarPane,	NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        app.getGUI().initChildButton(workspace, CLASS_FILE_BUTTON, CLASS_FILE_BUTTON, true)
        
        //now add the toolbar to the flowpane
        fp.getChildren().addAll(editToolbar);
    }
    
    
    public MapController getMapController() {return mapController;}
    
    public void processEvents() {

        
        renderPane.setOnMouseClicked(e -> {
            double x = e.getSceneX();
            double y = e.getSceneY();
            if (e.getButton() == MouseButton.PRIMARY) {
                mapController.processZoomIn(x, y, renderPane, xOrigin, yOrigin);
            }
            
            if (e.getButton() == MouseButton.SECONDARY) {
                mapController.processZoomOut(x, y, renderPane, xOrigin, yOrigin);
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
    public void reloadWorkspace() {
        
        DataManager dataManager = (DataManager)app.getDataComponent();
        //clears the workspace
        workspace.getChildren().clear();
        
        //set the background to be lightblue
        workspace.setStyle("-fx-background-color: lightblue;");
        //app.getWorkspaceComponent().getWorkspace().
        //.setBackground(new Background("#F0F8FF"));
           
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
        renderPane.setStyle("-fx-background-color: lightblue;");
        
        //adds the lines if hasLines = true; else, removes the lines.
        addLines(hasLines);
        
        
        workspace.getChildren().addAll(renderPane);
        
        //clip it to avoid overflow
        Rectangle clip = new Rectangle();
        clip.setHeight(app.getGUI().getPrimaryScene().getHeight()-60);
        clip.setWidth(app.getGUI().getPrimaryScene().getWidth());
        clip.setLayoutX(0);
        clip.setLayoutY(0);
        workspace.setClip(clip);
        
        
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
