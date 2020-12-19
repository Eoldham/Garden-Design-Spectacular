package src.main.java;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * The View classes contains all pane elements and is the UI of the application
 * Contains all panes.
 * @author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 */
public class View {
	
	public Controller control;
	private GraphicsContext gc;
	private DrawGardenPane drawGardenPane;
	private PlantSearchPane plantSearchPane;
	private ToolBarPane ratingToolBar;
	private BorderPane border;
	private EncyclopediaPane encyclopediaPane; 
	private Stage theStage;
	private int numChildrenInBorder;
	private StartPageView startPage;
	private Scene theScene;

	final private int ROOT_WIDTH = 1275;
	final private int ROOT_HEIGHT = 775;

	// garden dimensions
	int gardenWidth, gardenHeight;
	

	private ArrayList<String> allPlantNames;
	public Map<String, ImageView> plantList = new HashMap<String, ImageView>();

	/**
	 * Constructor for View.
	 * Initializes the View.
	 * @param theStage The JavaFX Stage class is the top level JavaFX container.
	 * @param control Instance of Controller class.
	 * @param width Width of garden.
	 * @param height Height of garden.
	 */
	public View(Stage theStage, Controller control, int width, int height) {
		this.control = control;
		this.theStage = theStage;
		
		allPlantNames = control.getPlantNames();
		createHashMap();
		
		theStage.setTitle("Garden Builder");
		startPage = new StartPageView(this);
		
		
		//Garden Builder View
		Group root = new Group();
		theScene = new Scene(root, getROOT_WIDTH(), getROOT_HEIGHT());
		theScene.setCursor(Cursor.HAND);
		root.minWidth(ROOT_WIDTH);
		root.maxWidth(ROOT_WIDTH);
		
		//ToolBar
		ratingToolBar = new ToolBarPane(this);

		// DragnDropPane
		plantSearchPane = new PlantSearchPane(this);
		
		
		//DrawGardenPane
		drawGardenPane = new DrawGardenPane(this, width, height);
		
		//EncyclopediaPane
		encyclopediaPane = new EncyclopediaPane(this);
		
		
		border = new BorderPane();
		border.setStyle("-fx-background-color: #add8e6;");
		root.getChildren().add(border);
		border.setLeft(plantSearchPane.getMainPane());
		border.setCenter(drawGardenPane.getDrawGardenBorder());
		border.setTop(ratingToolBar.getRatingToolBar());
		
		gc = drawGardenPane.getDrawGardenCanvas().getGraphicsContext2D();
		gc.setStroke(Color.BLACK);
		gc.setLineWidth(5);

		numChildrenInBorder = border.getChildren().size();
		theStage.show();
		
		if (control.getLoadGarden()) {
			Garden newGarden = control.getLoadedGarden();
			loadNewGarden(newGarden.getPlantsInGarden(), newGarden.getSeasonRatings(), newGarden.getSeason(), newGarden.getGardenWidth(), newGarden.getGardenHeight());
		}
	}
	
	/**
	 * Creates a HashMap of the plant ImageViews.
	 */
	public void createHashMap() {
		for(String name : allPlantNames) {
			Image plantImage;
			try {
				plantImage = new Image(new FileInputStream("src/resources/images/"+name+".png"));
				ImageView plantImageView = new ImageView();
				plantImageView.setId(name);
				plantImageView.setImage(plantImage);
				plantImageView.setPreserveRatio(true);
		    	plantImageView.setFitHeight(70);
				control.setHandlerForDragAndDrop(plantImageView);
				control.setHandlerForToggledImageViews(plantImageView);
				plantList.put(name, plantImageView);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Setter for MainScene.
	 */
	public void setMainScene() {
		theStage.setScene(theScene);
	}
	
	/**
	 * Getter for theScene.
	 * @return theScene Scene.
	 */
	public Scene getScene() {
		return theScene; 
	}
	
	/**
	 * Getter for plantSearchPane.
	 * @return plantSearchPane
	 */
	public PlantSearchPane getPlantSearchPane() {
		return plantSearchPane;
	}
	
	/**
	 * Getter for drawGardenPane.
	 * @return drawGardenPane
	 */
	public DrawGardenPane getDrawGardenPane() {
		return drawGardenPane;
	}
	
	/**
	 * Getter for ratingToolBar.
	 * @return ratingToolBar
	 */
	public ToolBarPane getToolBarPane() {
		return ratingToolBar;
	}
	
	/**
	 * Method to show the Plant Encyclopedia.
	 */
	public void showEncyclopedia() {
		border.setCenter(encyclopediaPane.getPane());
	}
	
	/**
	 * Method to show the drawGardenPane.
	 * @param plants ArrayList of Plants.
	 */
	public void showGarden(ArrayList<Plant> plants) {
		border.setCenter(drawGardenPane.getDrawGardenBorder());
		for (Plant p: plants) {
			addPlants(p);
		}
	}
	
	/**
	 * Scales and adds plant image view to the garden.
	 * @param plant Plant Object.
	 */
	public void addPlants(Plant plant) {
		ImageView plantIV = plantList.get(plant.getName());
		
		double boxSize = 0;
		if (control.getGarden().getGardenWidth() > control.getGarden().getGardenHeight()) {
			boxSize = drawGardenPane.getGardenPaneWidth()/control.getGarden().getGardenWidth();
		}
		else {
			boxSize = drawGardenPane.getGardenPaneWidth()/control.getGarden().getGardenHeight();
		}
		
		ImageView imgView = new ImageView();
		imgView.setImage(plantIV.getImage());
    	imgView.setPreserveRatio(true);
    	imgView.setFitHeight(boxSize*plant.getPlantSizeNum());
    	imgView.setFitWidth(boxSize*plant.getPlantSizeNum());
    	imgView.setX(plant.getxCor() - imgView.getFitWidth()/2);
    	imgView.setY(plant.getyCor() - imgView.getFitHeight()/2);
    	imgView.setId(plant.getName() + plant.getxCor() + plant.getyCor());
    	control.setHandlerForRemoveClick(imgView);
    	control.setHandlerForPlantDragged(imgView);
 
    	border.getChildren().add(imgView);
	}
	
	/**
	 * Moves plant ImageView.
	 * Takes the plant ID and moves the coordinates of the plant.
	 * @param imgView ImageView of plant.
	 * @param p Plant Object.
	 */
	public void movePlant(ImageView imgView, Plant p) {
		imgView.setId(p.getName() + p.getxCor() + p.getyCor());
		imgView.setX(p.getxCor() - imgView.getFitWidth()/2);
		imgView.setY(p.getyCor() - imgView.getFitHeight()/2);
		
	}
	
	/**
	 * Shows plants in the drawGardenPane based on if they bloom during that season.
	 * @param plantsInSeason Updated ArrayList of Plants that bloom in that season.
	 */
	public void showPlantsInSeason(ArrayList<Plant> plantsInSeason) {
		System.out.println(this.numChildrenInBorder);
		//Remove plants
		for (int i = border.getChildren().size() - 1; i >=3; i--){
			border.getChildren().remove(i);
		}
		//Add Plants
		for (Plant p: plantsInSeason) {
			addPlants(p);
		}
	}
	
	/**
	 * Removes plants from view.
	 * @param plant Plant Object.
	 */
	public void removePlant(Plant plant) {
				border.getChildren().remove(control.garden.getPlantsInGarden().indexOf(plant) + numChildrenInBorder);
			
	}
	
	/**
	 * Updates the Garden.
	 * Updates the ArrayList of plants, seasonsRatings, season state, and width and height of garden.
	 * @param plants Plant Object.
	 * @param seasonRatings ArrayList of seasonRatings.
	 * @param season Enum for season.
	 * @param width width of the garden.
	 * @param height height of the garden.
	 */
	public void loadNewGarden(ArrayList<Plant> plants, ArrayList<Integer> seasonRatings, Season season, int width, int height ){
		
		theStage.setScene(theScene);
		//Remove plants
		for (int i = border.getChildren().size() - 1; i >=3; i--){
			border.getChildren().remove(i);
		}
		
		
		gc.clearRect(0, 0, drawGardenPane.getDrawGardenCanvas().getWidth(), drawGardenPane.getDrawGardenCanvas().getHeight());
		
		
		//Add Plants
				for (Plant p: plants) {
					addPlants(p);
				}
		
		//Update Rating
		plantSearchPane.update(allPlantNames);
		ratingToolBar.updateRating(seasonRatings);
		drawGardenPane.setSeason(season);
		drawGardenPane.setSeasonComboBox(season);
		drawGardenPane.setGardenDim(width, height);
		drawGardenPane.setRectangle();
		drawGardenPane.setLines(width, height);
	}
	
	/**
	 * Getter for gc (GraphicsContext).
	 * @return gc GraphicsContext
	 */
	public GraphicsContext getgc() {
		return gc;
	}
	
	/**
	 * Getter for theStage.
	 * @return theStage Stage.
	 */
	public Stage getStage() {
		return theStage;
	}
	
	/**
	 * Getter for allPlantNames.
	 * @return allPlantNames ArrayList of plant names.
	 */
	public ArrayList<String> getAllPlantNames() {
		return allPlantNames;
	}
	
	/**
	 * Getter for plantList HashMap.
	 * @return plantList HashMap.
	 */
	public Map<String, ImageView> getPlantList() {
		return plantList;
	}

	/**
	 * Getter for ROOT_HEIGHT.
	 * @return ROOT_HEIGHT
	 */
	public int getROOT_HEIGHT() {
		return ROOT_HEIGHT;
	}
	
	/**
	 * Getter for ROOT_WIDTH.
	 * @return ROOT_WIDTH
	 */
	public int getROOT_WIDTH() {
		return ROOT_WIDTH;
	}
	
	/**
	 * Removes plants from Garden ArrayList.
	 * @param plants Plant Object.
	 */
	
	public void removePlantsInGarden(ArrayList<Plant> plants) {
		//Remove plants
				for (int i = border.getChildren().size() - 1; i >=3; i--){
					border.getChildren().remove(i);
				}
	}

	/**
	 * Sets the Encyclopedia page based on the plant chosen
	 * @param name
	 */
	public void setPlantPage(String name) {
		border.setCenter(encyclopediaPane.buildPlantPage(name));
	}


}

