package src.main.java;

import java.io.FileInputStream; 
import java.io.FileNotFoundException; 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * The PlantSearchPane contains the view for the PlantSearchPane portion of the view.
 * This includes the images of the draggable plants, searchbar, and filters.
 * @author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 *
 */
public class PlantSearchPane  {
	
	ObservableList<String> plantOptions = 
			FXCollections.observableArrayList(
					"All Plants",
					"Perennials",
					"Trees",
					"Shrubs"
					);
	final ComboBox<String> typesOfPlants = new ComboBox<String>(plantOptions);
	ObservableList<String> seasonOptions = 
			FXCollections.observableArrayList(
					"All Seasons",
					"Spring",
					"Summer",
					"Autumn",
					"Winter"
					);
	final ComboBox<String>  typesOfSeasons = new ComboBox<String>(seasonOptions);
	ObservableList<String> colorOptions = 
			FXCollections.observableArrayList(
					"All Colors",
					"Yellow",
					"Green",
					"Red",
					"Purple",
					"Blue",
					"Orange",
					"White",
					"Pink"
					);
	final ComboBox<String>  typesOfColors = new ComboBox<String>(colorOptions);
	
	private TextField searcher;
	private View mainView;
	private VBox mainPane;
	private TilePane imageContainerPane;
	private ScrollPane scrollPane;
	
	private ArrayList<VBox> plantContainers;
	private VBox currVBox = null;
	
	final private int SCROLL_PANE_MAX_HEIGHT = 625;
	
	/**
	 * Constructor for PlantSearchPane.
	 * Initializes the PlantSearchPane.
	 * @param mainView View instance of the view.
	 */
	public PlantSearchPane(View mainView)  {
		this.mainView = mainView;
		
		mainPane = new VBox(0);
		mainPane.setStyle("-fx-background-color: #dbd0ab;-fx-border-color: #a8a083;-fx-border-width:1;-fx-border-radius:3;");

		//Hbox contains the search Bar
		Label searchlabel = new Label("Search:");
		searcher = new TextField();
		searcher.setStyle("-fx-focus-color: #D1EBDC");
		Button searchButton = new Button("Go");
		searchButton.setStyle("-fx-background-color: #D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		mainView.control.setHandlerForSearchBar(searchButton);
		
		
		HBox hb = new HBox();
		hb.getChildren().addAll(searchlabel, searcher,searchButton);
		hb.setSpacing(8);
		hb.setPadding(new Insets(5, 10, 5, 10));
		hb.setMaxHeight(50);
		hb.setAlignment(Pos.CENTER);
		
		//HBox contains the drop down selection box
		typesOfPlants.setPromptText("Select Plant...");
		typesOfPlants.setStyle("-fx-background-color:#D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		typesOfSeasons.setPromptText("Select Season...");
		typesOfSeasons.setStyle("-fx-background-color: #D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		typesOfColors.setPromptText("Select Color...");
		typesOfColors.setStyle("-fx-background-color: #D1EBDC;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;");
		HBox hb1 = new HBox();
		mainView.control.setHandlerForSearchTab(typesOfPlants);
		mainView.control.setHandlerForSearchTab(typesOfSeasons);
		mainView.control.setHandlerForSearchTab(typesOfColors);
		hb1.getChildren().addAll(typesOfPlants, typesOfSeasons, typesOfColors);
		hb1.setPadding(new Insets(5, 5, 30, 5));
		hb1.setMaxHeight(50);
		hb1.setSpacing(5);
		hb1.setAlignment(Pos.BOTTOM_CENTER);
		
		//TilePane contains the image and labels
		imageContainerPane = new TilePane();
		imageContainerPane.setPrefColumns(1);
		imageContainerPane.setVgap(35);
		imageContainerPane.setHgap(35);
		imageContainerPane.setAlignment(Pos.CENTER);
		
		scrollPane = new ScrollPane();
		scrollPane.setPrefHeight(SCROLL_PANE_MAX_HEIGHT);
		scrollPane.setFitToWidth(true);
		scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent; "); 
		
		plantContainers = new ArrayList<VBox>();
		update(mainView.getAllPlantNames());
		
		scrollPane.setContent(imageContainerPane);
		mainPane.getChildren().addAll(hb, hb1, scrollPane);
		
	}
	
	/**
	 * Update method that updates the pane with the appropriate plant images based on the filters.
	 * @param names ArrayList of names of plants.
	 */
	public void update(ArrayList<String> names) {
		imageContainerPane.getChildren().clear();
		
		for(String inputName : names) {
			for(String generalName : mainView.getAllPlantNames()) {
				if(generalName.toLowerCase().contains(inputName.toLowerCase())) {
					
					Text plantNameLabel = new Text(generalName.replace("_", " "));
					
					if(mainView.control.checkPlantSize(generalName)) {
						plantNameLabel.setFill(Color.BLACK);
					}
					else plantNameLabel.setFill(Color.BROWN);
				
					String plantDescription = mainView.control.getPlantDescription(generalName);
					
					VBox vb = new VBox();
					vb.setAlignment(Pos.CENTER);
					
					vb.getChildren().add(mainView.getPlantList().get(generalName));
					vb.getChildren().add(plantNameLabel);
					
					Tooltip toolTip = new Tooltip(plantDescription);
					Tooltip.install(vb, toolTip);
					
					plantContainers.add(vb);
					
					imageContainerPane.getChildren().add(vb);
				}
			}
		}
		
	}
	
	/**
	 * Takes a imageview in the plant search pane and sets it as the selected image, puts a border 
	 * around the image
	 * 
	 * @param ImageView plantImage, the selected plant image
	 */
	public void setSelectedImage(ImageView plantImage) {
		if(currVBox != null) {
			currVBox.setStyle("-fx-border-color: transparent;");
		}
		if(plantImage == null) {
			currVBox = null;
		}
		else {
			for(VBox vbox : plantContainers) {
				if(vbox.getChildren().contains(plantImage)) {
					vbox.setStyle("-fx-border-color: #D1EBDC; -fx-border-width: 5; -fx-border-radius: 5;");
					currVBox = vbox;
				}
			}
		}
	}
	/**
	 * Getter for the currently selected plant
	 * @return VBox the vbox that stores the label and imageview of the selected plant
	 */
	public VBox getToggledPlant() {
		if(currVBox==null) {
			return null;
		}
		else return currVBox;
	}
	
	/**
	 * Getter for mainPane VBox.
	 * @return mainPane
	 */
	public VBox getMainPane() {
		return mainPane;
	}
	
	/**
	 * Getter for searcher TextField.
	 * @return searcher
	 */
	public TextField getTextField() {
		return searcher;
	}
	
}
