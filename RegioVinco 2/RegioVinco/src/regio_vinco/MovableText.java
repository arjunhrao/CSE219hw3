package regio_vinco;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * This class represents a game text node that can be moved and
 * rendered and can respond to mouse interactions.
 */
public class MovableText {
    // A JAVAFX TEXT NODE IN THE SCENE GRAPH
    protected Text text;
    protected Rectangle rectangle;
    
    // USED FOR MANAGING NODE MOVEMENT
    protected double[] velocity = new double[2];
    protected double[] acceleration = new double[2];

    /**
     * Constructor for initializing a GameNode, note that the provided
     * text argument should not be null.
     * 
     * @param initText The text managed by this object.
     */
    public MovableText(Pane pane) {
	text = new Text();
	rectangle = new Rectangle();
	text.setX(0);
	text.setY(40);
	rectangle.setX(0);
	rectangle.setY(0);
	pane.getChildren().add(rectangle);
	pane.getChildren().add(text);
	rectangle.setWidth(300);
	rectangle.setHeight(50);
	rectangle.setStroke(Color.color(0, 0, 0, 1));
    }
    
    public void setX(double x) {
	rectangle.setX(x);
	text.setX(x);
    }
    
    public void setY(double y) {
	rectangle.setY(y);
	text.setY(y + 40);
    }
    
    // ACCESSOR AND MUTATOR METHODS
    
    public Text getText() {
	return text;
    }
    
    public void setText(Text initText) {
	text = initText;
    }
    
    public Rectangle getRectangle() {
	return rectangle;
    }
    
    public double getVelocityX() {
	return velocity[0];
    }
    
    public double getVelocityY() {
	return velocity[1];
    }
    
    public void setVelocityX(double initVelocityX) {
	velocity[0] = initVelocityX;
    }
    
    public void setVelocityY(double initVelocityY) {
	velocity[1] = initVelocityY;
    }

    /**
     * Called each frame, this function moves the node according
     * to its current velocity and updates the velocity according to
     * its current acceleration, applying percentage as a weighting for how
     * much to scale the velocity and acceleration this frame.
     * 
     * @param percentage The percentage of a frame this the time step
     * that called this method represents.
     */
    public void update(double percentage) {
	// UPDATE POSITION
	double vX = velocity[0] * percentage;
	double vY = velocity[1] * percentage;
	
	text.setX(text.getX() + vX);
	rectangle.setX(rectangle.getX() + vX);
	
	text.setY(text.getY() + vY);
	rectangle.setY(rectangle.getY() + vY);
	
	// UPDATE VELOCITY
	velocity[0] += (acceleration[0] * percentage);
	velocity[1] += (acceleration[1] * percentage);
    }
}