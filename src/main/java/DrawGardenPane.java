package src.main.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
/**
 * The DrawGardenPane class contains all JavaFx View elements for the Garden Area of the View.
 * @author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 */
public class DrawGardenPane {

	// Season Styles
	final private Color springStyle = Color.rgb(129, 238, 164);
	final private Color summerStyle = Color.rgb(255, 244, 179);
	final private Color fallStyle = Color.rgb(232, 198, 150);
	final private Color winterStyle = Color.rgb(230, 255, 255);
	final private Color allSeasonStyle = Color.rgb(191,  245,  208);

	ObservableList<String> seasonOptions = FXCollections.observableArrayList("All Seasons", "Spring", "Summer",
			"Autumn", "Winter");
	final ComboBox<String> selectSeason = new ComboBox<String>(seasonOptions);

	// DrawGardenPane ToolBar
	private ToolBar drawGardenToolBar;
	private ToggleButton eraseButton = new ToggleButton("Erase");
	private ToggleButton drawButton = new ToggleButton("Draw");
	private ToggleButton paintPlantsButton = new ToggleButton("Paint");
		
	// DrawGardenPane MainPane
	private BorderPane holder;
	private BorderPane drawGardenBorder;
	private Canvas drawGardenCanvas;

	private Label gardenDimLabel;
	
	// DrawGardenPane Dimensions
	final private double DRAW_GARDENPANE_WIDTH = 910;
	final private double DRAW_GARDENPANE_HEIGHT = 690;
	
	public int gardenX = 0;
	public int gardenY = 0;
	public double largerDim;
	
	public Rectangle gardenRect = new Rectangle();
	
	// Increment
	private double GRID_INCREMENT;
/**
 * Constructor for DrawGarden Pane.
 * Initializes UI elements for DrawGardenPane.
 * @param mainView View.
 * @param width Width of Garden set by User.
 * @param height Height of Gardens set by User.
 */
	public DrawGardenPane(View mainView, int width, int height) {

		drawGardenCanvas = new Canvas(DRAW_GARDENPANE_WIDTH, DRAW_GARDENPANE_HEIGHT);
		drawGardenCanvas.minWidth(DRAW_GARDENPANE_WIDTH);
		drawGardenCanvas.minHeight(DRAW_GARDENPANE_HEIGHT);

		holder = new BorderPane();
		

		holder.getChildren().add(drawGardenCanvas);

		mainView.control.setHandlerForDrawButton(drawButton);
		mainView.control.setHandlerForEraseButton(eraseButton);
		mainView.control.setHandlerForSeasonComboBox(selectSeason);
		mainView.control.setHandlerForPaintPlantButton(paintPlantsButton);

		drawGardenToolBar = new ToolBar();
		drawGardenToolBar.setStyle("-fx-background-color: #dbd0ab;-fx-border-color: #a8a083;-fx-border-width:1;-fx-border-radius:3;");
		drawButton.setStyle("-fx-background-color:#D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		eraseButton.setStyle("-fx-background-color:#D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		paintPlantsButton.setStyle("-fx-background-color:#D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		selectSeason.setStyle("-fx-background-color:#D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		HBox hb3 = new HBox();
		HBox hb2 = new HBox();
		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);
		selectSeason.setMaxWidth(400);
		hb2.setPadding(new Insets(5, 5, 5, 5));
		hb2.setSpacing(4);
		hb2.getChildren().addAll(drawButton, eraseButton, paintPlantsButton);
		hb3.setPadding(new Insets(5, 5, 5, 0));
		hb3.getChildren().addAll(selectSeason);
		
		gardenDimLabel = new Label();
		gardenDimLabel.setLayoutX(0);
		gardenDimLabel.setLayoutY(0);
		gardenDimLabel.setFont(new Font(14));
		gardenDimLabel.setPadding(new Insets(0, 0, 0, 426));

		drawGardenToolBar.getItems().addAll(hb3, hb2, gardenDimLabel);
				
		drawGardenBorder = new BorderPane();
		drawGardenBorder.setMinWidth(DRAW_GARDENPANE_WIDTH);
		drawGardenBorder.setMinHeight(DRAW_GARDENPANE_HEIGHT);
		drawGardenBorder.setCenter(holder);
		drawGardenBorder.setTop(drawGardenToolBar);
	}
/**
 * Setter for holder(BorderPane) background based on season.
 * @param season Enum for season.
 */
	public void setSeason(Season season) {
		switch (season) {
		case SPRING:
			gardenRect.setFill(springStyle);
			break;
		case SUMMER:
			gardenRect.setFill(summerStyle);
			break;
		case AUTUMN:
			gardenRect.setFill(fallStyle);
			break;
		case WINTER:
			gardenRect.setFill(winterStyle);
			break;
		default:
			gardenRect.setFill(allSeasonStyle);
			break;
		}
	}
/**
 * Setter for ComboBox based on season.
 * @param season Enum for season.
 */
	public void setSeasonComboBox(Season season) {
		switch (season) {
		case SPRING:
			selectSeason.getSelectionModel().select("Spring");
			break;
		case SUMMER:
			selectSeason.getSelectionModel().select("Summer");
			break;
		case AUTUMN:
			selectSeason.getSelectionModel().select("Autumn");
			break;
		case WINTER:
			selectSeason.getSelectionModel().select("Winter");
			break;
		default:
			selectSeason.getSelectionModel().select("All Seasons");
			break;
		}
	}
/**
 * Sets the scale of the grid based on width set by user.
 * @param width Width set by user.
 */
	
	public void setLines(int width, int height) {
		int tileCounter = 0;
		System.out.println("Grid Increment: "+GRID_INCREMENT);
		for (int i = 0; i < largerDim; i += GRID_INCREMENT) {
			if(tileCounter<=width) {
				Line vertical = new Line(i, 0, i, gardenY);
				vertical.setStroke(Color.DARKGRAY);
				vertical.setStrokeWidth(0.8);
				holder.getChildren().add(vertical);
			}
			if(tileCounter<=height) {	
				Line horizontal = new Line(0, i, gardenX, i);
				horizontal.setStroke(Color.DARKGRAY);
				horizontal.setStrokeWidth(0.8);
				holder.getChildren().add(horizontal);
			}
			tileCounter++;
		}
		
	}
	
	
	/**
	 * Set the dimensions of each grid pane according to the users inputed garden width and height.
	 * The grid increment is set as the given pixel length of the larger dimensions(either width or height)
	 * Divided by the user inputed larger dimension. Then it loops through the largerDim to determine the
	 * number of grid_increments the x and y direction will get.
	 * 
	 * 
	 * @param int the user set width of the garden
	 * @param int the user set height of the garden
	 */
	public void setGardenDim(int width, int height) {
		int tileCounter = 0;
		if (width > height) {
			GRID_INCREMENT = DRAW_GARDENPANE_WIDTH / (double)width;
			largerDim = DRAW_GARDENPANE_WIDTH;
		}
		else {
			GRID_INCREMENT = DRAW_GARDENPANE_HEIGHT / (double)height;
			largerDim = DRAW_GARDENPANE_HEIGHT;
		}
		for (int i = 0; i < largerDim; i += GRID_INCREMENT) {
			if(tileCounter<=width) {
				gardenX = i;
			}
			if(tileCounter<=height) {	
				gardenY = i;
			}
			tileCounter++;
		}
		
		gardenDimLabel.setText("Dimensions: " + width + "ft x " + height + "ft");
	}
	
	
	/**
	 * Create the rectangle that represents the garden size. Create the gardencanvas that stores all
	 * the imageviews in the garden.
	 * 
	 */
	public void setRectangle() {
		double height = DRAW_GARDENPANE_HEIGHT - (DRAW_GARDENPANE_HEIGHT - gardenY);
		double width = DRAW_GARDENPANE_WIDTH - (DRAW_GARDENPANE_WIDTH - gardenX);
		
		gardenRect.setHeight(height);
		gardenRect.setWidth(width);
		gardenRect.setX(0);
		gardenRect.setY(0);
		holder.getChildren().clear();
		holder.getChildren().add(gardenRect);
		
		drawGardenCanvas.setHeight(height);
		drawGardenCanvas.setWidth(width);
		drawGardenCanvas.maxHeight(height);
		drawGardenCanvas.maxWidth(width);
		holder.getChildren().add(drawGardenCanvas);
		
		
		
		
	}
	

/**
 * Getter for drawGardenBorder.
 * @return drawGardenBorder
 */
	public BorderPane getDrawGardenBorder() {
		return drawGardenBorder;
	}
/**
 * Getter for drawGardenCanvas.
 * @return drawGardenCanvas
 */
	public Canvas getDrawGardenCanvas() {
		return drawGardenCanvas;
	}
/**
 * Getter for eraseButton.
 * @return eraseButton
 */
	public ToggleButton getEraseButton() {
		return eraseButton;
	}
/**
 * Getter for drawButton.
 * @return drawButton
 */
	public ToggleButton getDrawButton() {
		return drawButton;
	}

/**
 * Getter for the holder BorderPane.
 * @return holder
 */
	public BorderPane getHolder() {
		return holder;
	}
	
	/**
	 * Getter for the width of the garden pane.
	 * @return double DRAW_GARDENPANE_WIDTH
	 */
	public double getGardenPaneWidth() {
		return DRAW_GARDENPANE_WIDTH;
	}
	
	/**
	 * Getter for the height of the garden pane.
	 * @return double DRAW_GARDENPANE_HEIGHT
	 */
	public double getGardenPaneHeight() {
		return DRAW_GARDENPANE_HEIGHT;
	}
}
