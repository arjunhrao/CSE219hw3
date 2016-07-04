package rvme.data;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import saf.components.AppDataComponent;
import rvme.MapEditorApp;
import rvme.gui.Workspace;

/**
 *
 * @author McKillaGorilla
 */
public class DataManager implements AppDataComponent {
    MapEditorApp app;
    ArrayList<Polygon> polygonList = new ArrayList<>();
    //hw4
    ObservableList<SubRegion> subregions;
    
    public DataManager(MapEditorApp initApp) {
        app = initApp;
        List list = new ArrayList();
        subregions = FXCollections.observableList(list);
    }
    
    @Override
    public void reset() {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        polygonList.clear();
        workspace.getRenderPane().setScaleX(1.0);
        workspace.getRenderPane().setScaleY(1.0);

        //might need to add stuff in here
        double h = app.getGUI().getPrimaryScene().getHeight()/2;
        double w = app.getGUI().getPrimaryScene().getWidth()/2;
        Circle circle = workspace.getMapController().getCircle();
        if (circle.getFill().equals(Paint.valueOf("#000000"))) {
            
        } else {
        circle.setCenterX(workspace.getRenderPane().getWidth()/2);
        circle.setCenterY((workspace.getRenderPane().getHeight()-62)/2);
        while (Math.abs((h - workspace.getRenderPane().localToScene(circle.getCenterX(), circle.getCenterY()).getY())) > 5) {
                if (h < workspace.getRenderPane().localToScene(circle.getCenterX(), circle.getCenterY()).getY())
                {workspace.getRenderPane().setTranslateY(workspace.getRenderPane().getTranslateY()-5.0);}
                if (h > workspace.getRenderPane().localToScene(circle.getCenterX(), circle.getCenterY()).getY())
                {workspace.getRenderPane().setTranslateY(workspace.getRenderPane().getTranslateY()+5.0);}
        }
        while (Math.abs((w - workspace.getRenderPane().localToScene(circle.getCenterX(), circle.getCenterY()).getX())) > 5) {
            if (w < workspace.getRenderPane().localToScene(circle.getCenterX(), circle.getCenterY()).getX())
            {workspace.getRenderPane().setTranslateX(workspace.getRenderPane().getTranslateX()-5.0);}
            if (w > workspace.getRenderPane().localToScene(circle.getCenterX(), circle.getCenterY()).getX()) {
                workspace.getRenderPane().setTranslateX(workspace.getRenderPane().getTranslateX()+5.0);
            }
        }
        }
        
        workspace.getRenderPane().getChildren().clear();
        
        workspace.getWorkspace().getChildren().clear();

    }
    
    public void addPolygon(Polygon p) {
        polygonList.add(p);
    }
    
    public ArrayList<Polygon> getPolygonList() {
        return polygonList;
    }
    
    public void fillPolygons(Paint p) {
        for (Polygon poly: polygonList) {
            poly.setFill(p);
        }
    }
    
    //prob won't use this method
    public Polygon convertPolygon(Polygon p) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        p.setScaleX(1/360*workspace.getRenderPane().getWidth());
        p.setScaleY(1/180*workspace.getRenderPane().getHeight());
        return p;

    }
    
    public void scaleXYCoordinates(ArrayList<Double> xy) {
        double halfX = app.getGUI().getPrimaryScene().getWidth()/2;
        double halfY = (app.getGUI().getPrimaryScene().getHeight()-60)/2;
        
        for (int n = 0; n < xy.size(); n++) {
                if (n%2 == 0) {//it's an x
                    //scale it
                    xy.set(n, xy.get(n)/360*app.getGUI().getPrimaryScene().getWidth());
                    //place it relative to origin
                    xy.set(n, halfX+xy.get(n));
                } else {
                    //scale
                    xy.set(n, xy.get(n)/180*(app.getGUI().getPrimaryScene().getHeight()-60));
                    //place relative to origin
                    xy.set(n, halfY-xy.get(n));
                }
            }

    }
    
    
    public ObservableList<SubRegion> getSubregions() {
	return subregions;
    }
}
