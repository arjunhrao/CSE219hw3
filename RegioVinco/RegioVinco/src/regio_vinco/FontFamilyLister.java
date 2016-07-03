package regio_vinco;

import java.util.List;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author McKillaGorilla
 */
public class FontFamilyLister extends Application {
    
    public static void main(String[] args) {
	launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
	TextArea textArea = new TextArea();
	List<String> fontFamilyNames = Font.getFamilies();
	for (String s : fontFamilyNames) {
	    textArea.appendText(s + "\n");
	}
	Scene scene = new Scene(textArea, 500, 300);
	primaryStage.setScene(scene);
	primaryStage.show();
    }
}
