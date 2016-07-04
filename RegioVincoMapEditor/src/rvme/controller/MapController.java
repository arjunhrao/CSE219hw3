/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.controller;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Scale;
import static javafx.scene.transform.Transform.scale;
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
    
    
}
