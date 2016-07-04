package rvme.data;

import java.time.LocalDate;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author McKillaGorilla
 */
public class SubRegion {
    public static final String DEFAULT_CATEGORY = "?";
    public static final String DEFAULT_DESCRIPTION = "?";
    public static final LocalDate DEFAULT_DATE = LocalDate.now();
    public static final boolean DEFAULT_COMPLETED = false;
    
    //final StringProperty category;
    //final StringProperty description;
    //final ObjectProperty<LocalDate> startDate;
    //final ObjectProperty<LocalDate> endDate;
    //final BooleanProperty completed;
    
    //hw4
    String capitalName = "";
    String leaderName = "";
    String subregionName = "";
    
       
    public SubRegion() {
        //category = new SimpleStringProperty("asdf");
        //description = new SimpleStringProperty("hey");
        //startDate = new SimpleObjectProperty("ho");
        //endDate = new SimpleObjectProperty("lets");
        //completed = new SimpleBooleanProperty(false);
        

        //category = new SimpleStringProperty(DEFAULT_CATEGORY);
        //description = new SimpleStringProperty(DEFAULT_DESCRIPTION);
        //startDate = new SimpleObjectProperty(DEFAULT_DATE);
        //endDate = new SimpleObjectProperty(DEFAULT_DATE);
        //completed = new SimpleBooleanProperty(DEFAULT_COMPLETED);
    }

    public SubRegion(String initSubregionName, String initCapital, String initLeader) {
        this();
        subregionName = initSubregionName;
        capitalName = initCapital;
        leaderName = initLeader;
    }

    public String getSubregionName() {
        return subregionName;
    }

    public void setSubregionName(String value) {
        subregionName = value;
    }
    
    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String value) {
        leaderName = value;
    }
    
    public String getCapitalName() {
        return capitalName;
    }

    public void setCapitalName(String value) {
        capitalName = value;
    }
    
    public void reset() {
        setCapitalName(DEFAULT_CATEGORY);
        setLeaderName(DEFAULT_DESCRIPTION);
        setSubregionName(DEFAULT_CATEGORY);
    }
}
