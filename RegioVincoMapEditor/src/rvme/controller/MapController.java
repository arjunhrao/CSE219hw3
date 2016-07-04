/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.controller;

import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import static javafx.scene.transform.Transform.scale;
import properties_manager.PropertiesManager;
import saf.AppTemplate;
import rvme.data.DataManager;
import rvme.gui.Workspace;

/**
 *
 * @author ravirao
 */
public class MapController {
    AppTemplate app;
    DataManager myManager;
    double counter = 1.0;
    double temp1 = 0.0;
    double temp2 = 0.0;
    double temp3 = 0.0;
    double temp4 = 0.0;
    double x2 = 0.0;
    double y2 = 0.0;
    int foo = 0;
    Circle circle = new Circle(5.0, Paint.valueOf("#000000"));

    
    public MapController(AppTemplate initApp) {
	app = initApp;
        //myManager=(DataManager)app.getDataComponent();
        
    }
    
    public Circle getCircle() {return circle;}
    
    public DataManager getDataManager() { return myManager;}
    public void setDataManager(DataManager x) { myManager = x;}
    
    public void processInitialize() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        myManager=(DataManager)app.getDataComponent();
        workspace.reloadWorkspace();
    }
    
    public void processZoomIn(double x, double y, Pane renderPane, double xOrigin, double yOrigin) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        myManager=(DataManager)app.getDataComponent();
        
        //double newOriginX = x;
        //double newOriginY = y-10;
        
        //renderPane.setLayoutX(x);
        //renderPane.setLayoutY(y);
        //renderPane.setTranslateX(xOrigin-x);
        //renderPane.setTranslateY(yOrigin-(y-10));
        
        //root.getLayoutX add root.getWidth/2 - x
        if (counter == 1) {
            double layoutX = renderPane.getLayoutX();
            double layoutY = renderPane.getLayoutY();
            temp3 = renderPane.getWidth()/2;
            temp4 = renderPane.getHeight()/2;
        }
        if (counter != 1) {
            x2 = x2+(x-app.getGUI().getPrimaryScene().getWidth()/2)/counter;
            y2 = y2+(y-app.getGUI().getPrimaryScene().getHeight()/2)/counter;
        }
        if (counter == 1) {
        x2 = x;
        y2 = y-62;
        }
        
        
        renderPane.setLayoutX(renderPane.getLayoutX() + (temp3) - x);
        renderPane.setLayoutY(renderPane.getLayoutY() + (temp4) - (y-62));
        //same for y
        
       
        
        
        //System.out.println(renderPane.getScaleX());
        //ZOOM
        //renderPane.setScaleX(1.2*renderPane.getScaleX());
        //renderPane.setScaleY(1.2*renderPane.getScaleY());
        
        //renderPane.setLayoutX(layoutX+ (.2*paneWidth) - x);
        //renderPane.setLayoutY(layoutY + (.2*paneHeight) - y);
        
        //can center this at the point we clicked at for clarity
        circle = new Circle(5.0, Paint.valueOf("#999999"));
        circle.setVisible(false);
        circle.setCenterX(x2);
        circle.setCenterY(y2);
        renderPane.getChildren().add(circle);
        System.out.println(x);
        System.out.println(y-62);
        
        //System.out.println(x);
        //System.out.println(y-62);
        double newX = renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX();
        double newY = renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY();
        //System.out.println(newX);
        //x - newX = some constant, and we want the same constant for x - newX after the zoom.
        double temp1 = newX;
        double temp2 = newY;
        
        //System.out.println(renderPane.getLayoutX());
        
        renderPane.setScaleX(renderPane.getScaleX()*2.0);
        renderPane.setScaleY(renderPane.getScaleY()*2.0);
        

        newX = renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX();
        newY = renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY();
        //System.out.println(newX);
        //while (Math.abs(x - newX - temp1) >= 2) {
        //System.out.println(renderPane.getLayoutX());
        //the following code will never be reached, but it was a way that worked for me for first time clicking.
        if (counter == 0.0) {
            System.out.println(renderPane.getLayoutX());
            renderPane.setLayoutX(renderPane.getLayoutX() - (newX - temp1)/(counter));
            System.out.println(renderPane.getLayoutX());

            renderPane.setLayoutY(renderPane.getLayoutY() - (newY - temp2)/(counter));
        }
        
        double h = app.getGUI().getPrimaryScene().getHeight()/2;
        double w = app.getGUI().getPrimaryScene().getWidth()/2;
        if (counter != 0.0) {
            while (Math.abs((h - renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY())) > 5) {
                if (h < renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY())
                {renderPane.setTranslateY(renderPane.getTranslateY()-5.0);}
                if (h > renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY())
                {renderPane.setTranslateY(renderPane.getTranslateY()+5.0);}
                
            }
            while (Math.abs((w - renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX())) > 5) {
                if (w < renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX())
                {renderPane.setTranslateX(renderPane.getTranslateX()-5.0);}
                if (w > renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX()) {
                    renderPane.setTranslateX(renderPane.getTranslateX()+5.0);
                }

            }
        }
        System.out.println("*****");
        counter = counter*2.0;
        
        renderPane.getChildren().remove(circle);
        workspace.reloadWorkspace();
        
    }
    
    public void processZoomOut(double x, double y, Pane renderPane, double xOrigin, double yOrigin) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        myManager=(DataManager)app.getDataComponent();
        
        if (x2 == 0 && y2 == 0) {
            x2 = app.getGUI().getPrimaryScene().getWidth()/2;
            y2 = app.getGUI().getPrimaryScene().getHeight()/2;
        }
        
        circle = new Circle(5.0, Paint.valueOf("#999999"));
        circle.setVisible(false);
        circle.setCenterX(x2);
        circle.setCenterY(y2);
        renderPane.getChildren().add(circle);
        
        renderPane.setScaleX(renderPane.getScaleX()*0.5);
        renderPane.setScaleY(renderPane.getScaleY()*0.5);
        
        
        
        double h = app.getGUI().getPrimaryScene().getHeight()/2;
        double w = app.getGUI().getPrimaryScene().getWidth()/2;
        if (counter != 0.0) {
            while (Math.abs((h - renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY())) > 5) {
                if (h < renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY())
                {renderPane.setTranslateY(renderPane.getTranslateY()-5.0);}
                if (h > renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getY())
                {renderPane.setTranslateY(renderPane.getTranslateY()+5.0);}
                
            }
            while (Math.abs((w - renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX())) > 5) {
                if (w < renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX())
                {renderPane.setTranslateX(renderPane.getTranslateX()-5.0);}
                if (w > renderPane.localToScene(circle.getCenterX(), circle.getCenterY()).getX()) {
                    renderPane.setTranslateX(renderPane.getTranslateX()+5.0);
                }

            }
        }
        
        
        counter = counter/2.0;
        
        workspace.reloadWorkspace();
        

        
    }
    
    public void processKeyLeft(Pane renderPane) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        myManager=(DataManager)app.getDataComponent();
        renderPane.setLayoutX(renderPane.getLayoutX() + 50/counter);
        x2 = x2 - 50/counter;
        //x2 = x2+((x2-50)-app.getGUI().getPrimaryScene().getWidth()/2)/counter;
    }
    public void processKeyDown(Pane renderPane) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        myManager=(DataManager)app.getDataComponent();
        renderPane.setLayoutY(renderPane.getLayoutY() - 50/counter);
        x2 += 50/counter;
        //y2 = y2+((y2+50)-app.getGUI().getPrimaryScene().getHeight()/2)/counter;

    }
    public void processKeyUp(Pane renderPane) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        myManager=(DataManager)app.getDataComponent();
        renderPane.setLayoutY(renderPane.getLayoutY() + 50/counter);
        x2 -= 50/counter;
        //y2 = y2+((y2-50)-app.getGUI().getPrimaryScene().getHeight()/2)/counter;

    }
    public void processKeyRight(Pane renderPane) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        myManager=(DataManager)app.getDataComponent();
        renderPane.setLayoutX(renderPane.getLayoutX() - 50/counter);
        x2 = x2 + 50/counter;
        //x2 = x2+((x2-50)-app.getGUI().getPrimaryScene().getWidth()/2)/counter;

    }

    //hw4
    public void processMapDimensions() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        
        myManager=(DataManager)app.getDataComponent();
        
        
        int x = myManager.getItems().indexOf(it);
        
        //Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        
        
        //need a popup dialogue box with multiple input fields for all of the todoitem's data
        
        //the following line is included so that we can use things from the xml files
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        
        //creates a stage for the dialog
        //Stage myStage = new Stage();
        
        //creates the dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        //dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.setTitle(props.getProperty(EDIT_ITEM_HEADING));
        
        //adds ok and cancel buttons
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        
        //start creating the boxes/panes for the gui
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));
        
        TextField category = new TextField();
        //IMPORTANT: this actually puts the relevant information inside
        if (it.getCategory() != null) {
            category.setText(it.getCategory());
        } else {
            category.setText("");
        }
        //Note: the next two commented lines of code set the prompty text, which is nice but unnecessary
        //category.setPromptText(props.getProperty(CATEGORY_PROMPT));
        TextField description = new TextField();
        //IMPORTANT: this actually puts the relevant information inside
        if (it.getDescription() != null) {
            description.setText(it.getDescription());
        } else {
            category.setText("");
        }
        //description.setPromptText("Description");

        //HBox HBox1 = new HBox();

        ToDoItem myItem = new ToDoItem();
        Label catprompt = new Label();
        //catprompt.setFont(Font.font("Cambria", 32));
        //catprompt.setTextFill(Color.web("#0076a3"));
        //this line clearly works... because setting the text of the label to this .path() works and compiles
        this.getClass().getClassLoader().getResource("tdlm/css/tdlm_style.css");
        
        
        //after like 5 hours I still can't figure out why the style won't change... :(
        
        //catprompt.getStyleClass().add("category_prompt_label");
        //String stylesheet = props.getProperty(APP_PATH_CSS);
	//stylesheet += props.getProperty(APP_CSS);
	//URL stylesheetURL = app.getClass().getResource(stylesheet);
	//String stylesheetPath = stylesheetURL.toExternalForm();
	//app.getGUI().getPrimaryScene().getStylesheets().add(stylesheetPath);
        
        //the next line is how the stylesheet is actually added. This was shown to me by Kevin after
        //the deadline - I should've asked about it but I thought I had time to figure it out.
        //I couldn't though, so I didn't get credit for this :/
        // This would have to be done for each label.
        catprompt.getStylesheets().addAll(app.getGUI().getPrimaryScene().getStylesheets());
        catprompt.getStyleClass().add("category_prompt_label");
        
        catprompt.setText(props.getProperty(CATEGORY_PROMPT));
        //catprompt.getStyleClass().add("prompt_label");
        //catprompt.setText(this.getClass().getClassLoader().getResource("tdlm/css/tdlm_style.css").getPath());
        
        //gridPane.getStyleClass().add("prompt_label");
        
        gridPane.add(catprompt, 0, 0);
        gridPane.add(category, 1, 0);
        gridPane.add(new Label(props.getProperty(DESCRIPTION_PROMPT)), 0, 1);
        gridPane.add(description, 1, 1);

        DatePicker startDate = new DatePicker();
        DatePicker endDate = new DatePicker();
        startDate.setValue(myItem.getStartDate());
        endDate.setValue(myItem.getEndDate());
        //IMPORTANT: this actually puts the relevant information inside
        startDate.setValue(it.getStartDate());
        //IMPORTANT: this actually puts the relevant information inside
        endDate.setValue(it.getEndDate());
        
        gridPane.add(new Label(props.getProperty(STARTDATE_PROMPT)), 0, 2);
        gridPane.add(startDate, 1, 2);

        gridPane.add(new Label(props.getProperty(ENDDATE_PROMPT)), 0, 3);
        gridPane.add(endDate, 1, 3);

        CheckBox completed = new CheckBox();
        //IMPORTANT: this actually puts the relevant information inside
        completed.setSelected(it.getCompleted());
        //do i need to make sure that it can't be in the indeterminate state?
        
        gridPane.add(new Label(props.getProperty(COMPLETED_PROMPT)), 0, 4);
        gridPane.add(completed, 1, 4);
       
        //catprompt.setStyle("category_prompt_label");
        //gridPane.setStyle("category_prompt_label");
        
        dialog.getDialogPane().setContent(gridPane);
        Optional<ButtonType> result = dialog.showAndWait();
        
        

        if (result.isPresent() && result.get() == okButtonType) {
            //set and save the data to myItem and add it to the arraylist in the datamanager obj myManager
            myManager.getItems().get(x).setCategory(category.getText());
            myManager.getItems().get(x).setDescription(description.getText());
            myManager.getItems().get(x).setStartDate(startDate.getValue());
            myManager.getItems().get(x).setEndDate(endDate.getValue());
            myManager.getItems().get(x).setCompleted(completed.isSelected());
            
            
            //enable the save button
            app.getGUI().getSaveButton().setDisable(false);
            
            //update the workspace / table / savestate
            changesMade();
            workspace.reloadWorkspace();
            //useless line of code: app.getWorkspaceComponent().getWorkspace().getChildren().clear();
        }
    }
        
        public void changesMade() {
        //changes the value of saved in the AppFileController to show that the list
        //has been edited and has not been saved so that exiting will make sure to ask before just exiting.
        app.getGUI().getFileController().setSaved(false);
        }
        
    }
    
    
}
