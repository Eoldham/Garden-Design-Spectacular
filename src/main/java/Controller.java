package src.main.java;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * The Controller class handles EventHandlers and contains the main and start
 * methods. The Controller class is the "main" class and intializes the Java
 * Program.
 * 
  *@author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 */

public class Controller extends Application {

	View view;
	Model model;
	transient Scanner scan;
	Garden garden;
	Canvas canvas;
	boolean loadGarden;
	Garden loadedGarden;
	boolean isEncyc;

	private final String PLANT_INFO_CSV = "src/resources/plants.csv";
	private final String HELP_TXT = "src/resources/help.txt";
	public String[] helpText = loadHelp();
	
	private final int GARDEN_HOLDER_XPOS = 370;
	private final int GARDEN_HOLDER_YPOS = 75;

	final int X_DRAW_OFFSET = 369;
	private final int Y_DRAW_OFFSET = 77;
	
	private boolean imageViewsAreToggleable = false;

	/**
	 * The main method is the start of the Java Program.
	 * 
	 * @param args Command Line Arguments.
	 */
	public static void main(String[] args) {
		// This initializes the JavaFX view
		launch(args);

	}

	/**
	 * The main entry point for all JavaFX applications. The start method is called
	 * after the init method has returned, and after the system is ready for the
	 * application to begin running.
	 */
	@Override
	public void start(Stage theStage) {
		model = new Model(loadPlantList());
		view = new View(theStage, this, model.garden.getGardenWidth(), model.garden.getGardenHeight());
		scan = new Scanner(System.in);
		canvas = view.getDrawGardenPane().getDrawGardenCanvas();
		loadGarden = false;

		this.garden = model.garden;

	}

	/**
	 * Loads plants from plants.csv file. Takes plant data from plants.csv file
	 * (plant name, description, and plant traits).
	 * 
	 * @return plantList the ArrayList of plants from the CSV file.
	 */
	public ArrayList<Plant> loadPlantList() {
		ArrayList<Plant> plantList = new ArrayList<Plant>();
		String line = "";

		// Read each line of the CSV file and pull the plant name, description, and
		// String array of plant traits
		try {
			FileReader file = new FileReader(PLANT_INFO_CSV);
			BufferedReader csvFile = new BufferedReader(file);

			while ((line = csvFile.readLine()) != null) {

				String[] plant = line.split(",");

				String[] plantTraits = plant[2].split("-");
				plantTraits = Arrays.copyOf(plantTraits, plantTraits.length + 1);
				plantTraits[plantTraits.length - 1] = " ";
				plantList.add(new Plant(plant[0], plant[1], plantTraits, plant[3], plant[4], Double.parseDouble(plant[5])/12));

			}
			csvFile.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return plantList;
	}

	/*
	 * Loads Help Page information from .txt. Loads help information from help.txt.
	 * 
	 * @return helpInfo a String containing the help information
	 */
	public String[] loadHelp() {
		int lineNumber = 0;
		String[] helpInfo = new String[16];
		String line = "";
		try {
			FileReader file = new FileReader(HELP_TXT);
			BufferedReader txtFile = new BufferedReader(file);
			while ((line = txtFile.readLine()) != null) {
				helpInfo[lineNumber] = line;
				lineNumber++;
			}
			txtFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return helpInfo;
	}

	public ArrayList<String> getPlantNames() {
		ArrayList<String> plantNames = new ArrayList<String>();

		for (Plant plant : model.getPlantList()) {
			plantNames.add(plant.getName());
		}
		return plantNames;
	}

	/**
	 * Handler for Search Bar. When the searchButton is pressed it will search for
	 * the specified plant in the search field.
	 * 
	 * @param searchButton Button labeled "Go".
	 */
	public void setHandlerForSearchBar(Button searchButton) {
		searchButton.setOnAction(event -> {
			PlantSearchPane pane = view.getPlantSearchPane();
			System.out.println(pane.getTextField().getText());
			ArrayList<String> names = new ArrayList<String>();
			names.add(pane.getTextField().getText());
			pane.update(names);
		});
	}

	/**
	 * Handler for SearchTab (DropDown Menu). Handler for SearchTabs based on Type
	 * of Plant, Season of Bloom, and Color of Plant.
	 * 
	 * @param options ComboBox with names of traits
	 */
	public void setHandlerForSearchTab(ComboBox<String> options) {
		options.setOnAction(event -> {
			view.getPlantSearchPane().getTextField().setText("");

			String plantTypeTrait = view.getPlantSearchPane().typesOfPlants.getSelectionModel().getSelectedItem();
			String seasonTrait = view.getPlantSearchPane().typesOfSeasons.getSelectionModel().getSelectedItem();
			String colorTrait = view.getPlantSearchPane().typesOfColors.getSelectionModel().getSelectedItem();
			System.out.println(plantTypeTrait + " " + seasonTrait + " " + colorTrait);
			ArrayList<String> names = new ArrayList<String>();

			if (plantTypeTrait != "All Plants" || seasonTrait != "All Seasons" || colorTrait != "All Colors") {
				names = model.searchPlantListByTrait(plantTypeTrait, seasonTrait, colorTrait);
			} else {
				names.add("");
			}
			view.getPlantSearchPane().update(names);
		});
	}

	/**
	 * Handler for Drag and Drop Event Changes cursor to closed hand when dragging
	 * Allows user to pick up image from Plant Search Pane and drag it to the
	 * DrawGardenPane.
	 * 
	 * @param imgView ImageView of plant.
	 */
	
	public void setHandlerForDragAndDrop(ImageView imgView) {	
			imgView.setOnMousePressed (event -> {
					view.getScene().setCursor(Cursor.CLOSED_HAND);
				
			});
			imgView.setOnMouseReleased(event -> {	
				if(!imageViewsAreToggleable) {
					dragAndDrop(event, imgView.getImage());
				}
			});
	}

	/**
	 * Enables drag and drop by finding the plants X and Y Coordinate in relation to
	 * the cursor. Updates plant X and Y when moved, and the garden rating system.
	 * 
	 * @param event MouseEvent picks the top most node (ImageView) and moves its
	 *              coordinates.
	 * @param img   Image the image of the plant.
	 */
	public void dragAndDrop(MouseEvent event, Image img) {
		if (!this.isEncyc) {
			view.getScene().setCursor(Cursor.HAND);
			Node n = (Node) event.getSource();
			Plant plant = model.getPlant(n.getId());
			
//			System.out.println("Plant X: "+event.getSceneX());
//			System.out.println("Plant Y: "+event.getSceneY());
//			System.out.println("Scene X: "+view.getDrawGardenPane().gardenX);
//			System.out.println("Scene Y: "+view.getDrawGardenPane().gardenY);
			if(plant.getPlantSizeNum()<model.garden.getGardenHeight() && plant.getPlantSizeNum()<model.garden.getGardenWidth()) {
				if(event.getSceneX()>GARDEN_HOLDER_XPOS && event.getSceneY()>GARDEN_HOLDER_YPOS) {
					if(event.getSceneX()<view.getDrawGardenPane().gardenX+GARDEN_HOLDER_XPOS && event.getSceneY()<view.getDrawGardenPane().gardenY+GARDEN_HOLDER_YPOS) {
						plant = model.Add(event.getSceneX(), event.getSceneY(), n.getId());
						view.addPlants(plant);
						garden.setSeasonRatings();
						view.getToolBarPane().updateRating(garden.getSeasonRatings());
						System.out.println("Rating Updated");
					}
				}
			}
		}
	}

	/**
	 * Handler for Draw Button. Changes cursor to crosshair when selected and
	 * enables drawing on Canvas.
	 * 
	 * @param drawButton ToggleButton that if toggled allows drawing on Canvas.
	 */
	public void setHandlerForDrawButton(ToggleButton drawButton) {
		drawButton.setOnAction(event -> {
			view.getScene().setCursor(Cursor.CROSSHAIR);
			view.getDrawGardenPane().getEraseButton().setSelected(false);

			if (drawButton.isSelected()) {

				view.getScene().setCursor(Cursor.CROSSHAIR);
				// Draw Start
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMousePressed(e -> {
					view.getgc().beginPath();
					view.getgc().lineTo(e.getSceneX() - X_DRAW_OFFSET, e.getSceneY() - Y_DRAW_OFFSET);
					view.getgc().stroke();
				});

				// Draw Line
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMouseDragged(e -> {
					view.getgc().lineTo(e.getSceneX() - X_DRAW_OFFSET, e.getSceneY() - Y_DRAW_OFFSET);
					view.getgc().stroke();
				});
			} else {
				view.getScene().setCursor(Cursor.HAND);
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMousePressed(e -> {
				});
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMouseDragged(e -> {
				});
			}
		});
	}

	/**
	 * Handler for Erase Button. Changes cursor to crosshair when selected and
	 * enables erasing of lines drawn.
	 * 
	 * @param eraseButton ToggleButton that if toggled allows erasing of drawings on
	 *                    Canvas.
	 */
	public void setHandlerForEraseButton(ToggleButton eraseButton) {
		eraseButton.setOnAction(event -> {
			view.getDrawGardenPane().getDrawButton().setSelected(false);

			if (eraseButton.isSelected()) {
				view.getScene().setCursor(Cursor.CROSSHAIR);
				// Erase Start
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMousePressed(e -> {
					double lineWidth = view.getgc().getLineWidth() * 4;
					view.getgc().clearRect(e.getSceneX() - X_DRAW_OFFSET - lineWidth,
							e.getSceneY() - Y_DRAW_OFFSET - lineWidth, lineWidth, lineWidth);
				});

				// Erase Line
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMouseDragged(e -> {
					double lineWidth = view.getgc().getLineWidth() * 4;
					view.getgc().clearRect(e.getSceneX() - X_DRAW_OFFSET - lineWidth,
							e.getSceneY() - Y_DRAW_OFFSET - lineWidth, lineWidth, lineWidth);
				});
			} else {
				view.getScene().setCursor(Cursor.HAND);
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMousePressed(e -> {
				});
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMouseDragged(e -> {
				});
			}
		});
	}

	/**
	 * Handler for Removing ImageView. When doubled clicked or right clicked the
	 * ImageView will be removed from the DrawGardenPane. Rating will also be
	 * updated.
	 * 
	 * @param imgView ImageView image of plant.
	 */
	public void setHandlerForRemoveClick(ImageView imgView) {
		imgView.setOnMouseClicked(event -> {
			if(!deleteImageToggle) {
				removeClick(event);
			}
		});
	}
	/**
	 * Sets right click and double click to remove ImageView from DrawGardenPane.
	 * 
	 * @param event MouseEvent for right and double click.
	 */
	public void removeClick(MouseEvent event) {
		if (!this.isEncyc) {
		Node n = (Node)event.getSource();
		Plant plantRemoved = null;
		for (Plant p : garden.getPlantsInGarden()) {
			if (n.getId().equals(p.getName() + p.getxCor() + p.getyCor())) {
				plantRemoved = p;
			}
		}
		view.removePlant(plantRemoved);
		model.remove(plantRemoved);

		garden.setSeasonRatings();
		view.getToolBarPane().updateRating(garden.getSeasonRatings());
		}
	}

	/**
	 * Handler for dragging plant node. Cursor is set to closed hand when dragging
	 * plant. Calls movePlant to move the image.
	 * 
	 * @param imgView ImageView image of plant.
	 */
	public void setHandlerForPlantDragged(ImageView imgView) {
		
		imgView.setOnMousePressed (event -> {
			view.getScene().setCursor(Cursor.CLOSED_HAND);
		});
		imgView.setOnMouseReleased(event -> movePlant(event));
		
	}

	/**
	 * Updates the X and Y coordinate for the plants.
	 * 
	 * @param event MouseEvent for dragging of mouse when plant clicked.
	 */
	public void movePlant(MouseEvent event) {
		if (!this.isEncyc) {
		view.getScene().setCursor(Cursor.HAND);
		ImageView imgView = (ImageView) event.getSource();
		Node n = (Node) event.getSource();
		Plant plantMoved = null;
		for (Plant p : garden.getPlantsInGarden()) {
			if (n.getId().equals(p.getName() + p.getxCor() + p.getyCor())) {
				plantMoved = p;
			}

		}
		model.move(event.getSceneX(), event.getSceneY(), plantMoved);
		view.movePlant(imgView, plantMoved);
		}
	}

	/**
	 * Handler for ComboBox. Updates season, and plants shown based on the ComboBox
	 * selection.
	 * 
	 * @param selectSeason ComboBox of strings for each season.
	 */
	public void setHandlerForSeasonComboBox(ComboBox<String> selectSeason) {
		selectSeason.setOnAction(event -> {
			Season season = Season.SPRING;
			switch (selectSeason.getSelectionModel().getSelectedItem()) {
			case "Spring":
				season = Season.SPRING;
				break;
			case "Summer":
				season = Season.SUMMER;
				break;
			case "Autumn":
				season = Season.AUTUMN;
				break;
			case "Winter":
				season = Season.WINTER;
				break;
			default:
				season = Season.ALL_SEASONS;
			}
			garden.setSeason(season);
			view.showPlantsInSeason(garden.getPlantsInSeason());
			view.getDrawGardenPane().setSeason(season);
		});
	}

	/**
	 * Handler for NewFile menu. Takes the MenuItem NewFile and constructs a popup
	 * for when the user creates a new garden.
	 * 
	 * @param newFile MenuItem set to ToolBar properties shown.
	 */
	public void setHandlerForNewFilePopUpClicked(MenuItem newFile) {
		newFile.setOnAction(event -> {
			ToolBarPane tbp = view.getToolBarPane();
			tbp.gardenName.setText("");
			tbp.widthValueFactory.setValue(1);
			tbp.heightValueFactory.setValue(1);
			tbp.getNewFilePopUp().show(view.getStage());
		});
	}

	/**
	 * Handler for newFile Button. When Button clicked pulls up the main view.
	 * 
	 * @param newFile Button to pull up the main view.
	 */
	public void setHandlerForNewFilePopUpClicked(Button newFile) {
		newFile.setOnAction(event -> {
			view.getToolBarPane().getNewFilePopUp().show(view.getStage());
		});
	}

	/**
	 * Handler for start Button. When button clicked it brings up the main page and
	 * hides the popup.
	 * 
	 * @param start Button to start the main view.
	 */
	public void setHandlerForNewFileClicked(Button start) {
		start.setOnAction(event -> {
			ToolBarPane tbp = view.getToolBarPane();
			createNewFile(tbp.gardenName.getText(), tbp.widthValueFactory.getValue(),
					tbp.heightValueFactory.getValue());
			view.getToolBarPane().getNewFilePopUp().hide();
			view.getToolBarPane().getHelpPopUp().show(view.getStage());
		});
	}

	/**
	 * Updates the Model with values set by the user.
	 * 
	 * @param gardenName String for name of garden.
	 * @param width      int of width set by the user.
	 * @param height     int of Height set by the user.
	 */
	public void createNewFile(String gardenName, int width, int height) {
		garden = new Garden(width, height);
		model.setGarden(garden);
		this.view.loadNewGarden(garden.getPlantsInGarden(), garden.getSeasonRatings(), garden.getSeason(),
				garden.getGardenWidth(), garden.getGardenHeight());
		save(gardenName);
	}

	/**
	 * Handler for save. Saves the state of the program.
	 * 
	 * @param save MenuItem for save.
	 */
	public void setHandlerForSaveMenuItemClicked(MenuItem save) {
		save.setOnAction(event -> {
			save(model.savedData.getFileName());
		});
	}

	/**
	 * Handler for cancel Button. When pressed it closes the save popup.
	 * 
	 * @param cancel Button that cancels the process of saving the state of the
	 *               program.
	 */
	public void setHandlerForCancelNewFileClicked(Button cancel) {
		cancel.setOnAction(event -> {
			view.getToolBarPane().getNewFilePopUp().hide();
		});
	}

	/**
	 * Handler for saveAs.
	 * 
	 * @param saveAs MenuItem.
	 */
	public void setHandlerForSaveAsPopUpClicked(MenuItem saveAs) {
		saveAs.setOnAction(event -> {
			view.getToolBarPane().getSaveAsPopUp().show(view.getStage());
		});
	}

	/**
	 * When save button is clicked open saves the garden state and its name.
	 * 
	 * @param save     Button labeled "Save".
	 * @param fileName TextField of garden name.
	 */
	public void setHandlerForSaveClicked(Button save, TextField fileName) {
		save.setOnAction(event -> {

			save(fileName.getText());
			fileName.setText("");
			view.getToolBarPane().getSaveAsPopUp().hide();
		});
	}

	/**
	 * Saves the file
	 * 
	 * @param fileName String for file name.
	 */
	public void save(String fileName) {
		model.savedData = new SavedData(fileName, model);
		try {
			FileOutputStream fileOut = new FileOutputStream("gardens/" + fileName + ".ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(model.savedData);
			out.close();
			fileOut.close();
			System.out.printf("Serialized data is saved in " + fileName + ".ser");
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Handler for Cancel SaveAs. Closes popup and cancels SaveAs process.
	 * 
	 * @param cancel Button labeled "Cancel".
	 */
	public void setHandlerForCancelSaveAsClicked(Button cancel) {
		cancel.setOnAction(event -> {
			view.getToolBarPane().getSaveAsPopUp().hide();
		});
	}

	/**
	 * Handler for Open MenuItem.
	 * @param open MenuItem.
	 */
	public void setHandlerForOpenPopUpClicked(MenuItem open) {
		open.setOnAction(event -> {
			view.getToolBarPane().updateOpenPopUp(getGardenFiles());
			view.getToolBarPane().getOpenPopUp().show(view.getStage());
		});
	}
	
	/**
	 * Handler for Open Button.
	 * @param open Button labeled "Open".
	 */
	public void setHandlerForOpenPopUpClicked(Button open) {
		open.setOnAction(event -> {
			view.getToolBarPane().updateOpenPopUp(getGardenFiles());
			view.getToolBarPane().getOpenPopUp().show(view.getStage());
		});
	}

	/**
	 * Handler for Cancel Button.
	 * @param cancel Button labeled "Cancel".
	 */
	public void setHandlerForCancelOpenPopUpClicked(Button cancel) {
		cancel.setOnAction(event -> {
			view.getToolBarPane().getOpenPopUp().hide();
		});
	}
	
	/**
	 * Handler for opening a file.
	 * @param open Button labeled "Open".
	 * @param files ListView of Strings of file names.
	 */
	public void setHandlerForOpenClicked(Button open, ListView<String> files) {
		open.setOnAction(event -> {
			String fileName = (String) files.getSelectionModel().getSelectedItem();
			open(fileName);
			view.getToolBarPane().getOpenPopUp().hide();
		});
	}
	
	/**
	 * Opens a saved file based on its file name.
	 * @param fileName String for file name.
	 */
	public void open(String fileName) {
		try {
			FileInputStream fileIn = new FileInputStream("gardens/" + fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			SavedData sd = (SavedData) in.readObject();
			this.openNewFile(sd);
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("File not found");
			c.printStackTrace();
			return;
		}
	}

	/**
	 * Opens a new file.
	 * @param sd SavedData.
	 */
	public void openNewFile(SavedData sd) {
		model.setGarden(sd.getGarden());
		model.savedData = sd;
		this.garden = model.getGarden();
		view.getPlantSearchPane().update(view.getAllPlantNames());
		this.view.loadNewGarden(garden.getPlantsInGarden(), garden.getSeasonRatings(), garden.getSeason(),
				garden.getGardenWidth(), garden.getGardenHeight());
	}

	/**
	 * Getter for GardenFiles.
	 * 
	 * @return fileNames Names of files.
	 */
	public ArrayList<String> getGardenFiles() {
		ArrayList<String> fileNames = new ArrayList<String>();

		File[] files = new File("gardens").listFiles();
		if (files != null) {
			for (File file : files) {
				fileNames.add(file.getName());
			}
		}

		return fileNames;
	}

	/**
	 * Getter for plant descriptions. Returns plants descriptions based on plant
	 * names from the model.
	 * 
	 * @param plantName String for name of plants.
	 * @return plant_description based on plant names.
	 */
	public String getPlantDescription(String plantName) {
		return model.getPlantDescription(plantName);
	}
	
	
	/**
	 * Getter for plant Size as a string. Returns plants size based on plant
	 * names from the model
	 * 
	 * @param name String for name of plant. 
	 * @return plant Size based on plant name. 
	 */
	public String getPlantSizeString(String name) {
		return model.getPlantSizeString(name);
	}
	
	/**
	 * Getter for plant Scientific Name. Returns plants Scientific name based
	 * on plant name from model
	 * 
	 * @param name String for name of Plant.
	 * @return Size based on plant name.
	 */
	public String getPlantScienceName(String name) {
		return model.getPlantScienceName(name);
	}
	

	/**
	 * Getter for ArrayList of plant traits.
	 * 
	 * @param plantName String for name of plants.
	 * @return plant_traits returns plant traits based on plant names from model.
	 */
	public ArrayList<String> getPlantTraits(String plantName) {
		return model.getPlantTraits(plantName);
	}

	/**
	 * Handler for Encyclopedia Button. When clicked opens Encyclopedia popup.
	 * 
	 * @param b Button labeled "Plant Encyclopedia".
	 */
	public void setHandlerForPlantEncyclopediaClicked(Button b) {
		b.setOnAction(event -> {
			this.isEncyc = true;
			view.removePlantsInGarden(garden.getPlantsInGarden());
			view.getPlantSearchPane().setSelectedImage(null);
			imageViewsAreToggleable = true;
			view.showEncyclopedia();
		});
	}

	/**
	 * Handler for Done Button in Plant Encyclopedia. When clicked it closes the
	 * Encyclopedia popup.
	 * 
	 * @param b Button labeled "Done".
	 */
	public void setHandlerForDonePlantEncycClicked(Button b) {
		b.setOnAction(event -> {
			this.isEncyc = false;
			imageViewsAreToggleable = false;
			view.getPlantSearchPane().setSelectedImage(null);
			view.showGarden(garden.getPlantsInGarden());
		});
	}
	
	/**
	 * Handler for Encyclopedia. Gives information based on image selected.
	 * 
	 * @param img ImageView of plant.
	 */

	public void encyclopediaChoice(MouseEvent event, ImageView imgView ) {
			if (this.isEncyc) {
				view.setPlantPage(imgView.getId());
				view.getScene().setCursor(Cursor.HAND);
			}
	}

	/**
	 * Handler for Help Button. Brings up help popup when clicked.
	 * 
	 * @param helpButton Button labeled "Help".
	 */
	public void setHandlerForHelpButton(Button helpButton) {
		helpButton.setOnAction(event -> {
			view.getToolBarPane().getHelpPopUp().show(view.getStage());
		});
	}

	/**
	 * Handler for Close Button. Hides the popup when clicked.
	 * 
	 * @param b Button labeled "Close".
	 */
	public void setHandlerForHelpButtonClose(Button b) {
		b.setOnAction(event -> {
			view.getToolBarPane().getHelpPopUp().hide();
		});
	}
	
	/**
	 * Handler for The paint with plant ToggleButton. When toggled...
	 * 
	 * @param paintPlantButton
	 */
	
	double prevX = 0;
	double prevY = 0;
	boolean deleteImageToggle = false;
	public void setHandlerForPaintPlantButton(ToggleButton paintPlantButton) {
		paintPlantButton.setOnAction(event -> {
			if(paintPlantButton.isSelected()) {
				imageViewsAreToggleable = true;
				deleteImageToggle = true;
				
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMousePressed(e -> {
					VBox selectedPlant = view.getPlantSearchPane().getToggledPlant();
					if(selectedPlant!=null) {
						ImageView plantName = (ImageView)selectedPlant.getChildren().get(0);
						
						Plant plant = model.Add(e.getSceneX(), e.getSceneY(), plantName.getId() );
						view.addPlants(plant);
						garden.setSeasonRatings();
						view.getToolBarPane().updateRating(garden.getSeasonRatings());
						prevX = 0;
						prevY = 0;
					}
				});
				
				view.getDrawGardenPane().getDrawGardenCanvas().setOnMouseDragged(e -> {
					VBox selectedPlant = view.getPlantSearchPane().getToggledPlant();
					if(selectedPlant != null) {
						ImageView plantName = (ImageView)selectedPlant.getChildren().get(0);
						Plant plant = model.getPlant(plantName.getId());
						double plantSize = plant.getPlantSizeNum() * 15;
						if(e.getSceneX() > view.getPlantSearchPane().getMainPane().getWidth() + plantSize/2 && e.getSceneY() > 100) {	
							if(e.getSceneX() < view.getPlantSearchPane().getMainPane().getWidth() + view.getDrawGardenPane().gardenRect.getWidth() - plantSize/3 && e.getSceneY() < 100 + view.getDrawGardenPane().gardenRect.getHeight() - plantSize/2) {	
								if(e.getX()>prevX+plantSize || e.getY()>prevY+plantSize || e.getX()<prevX-plantSize || e.getY()<prevY-plantSize) {
									Plant plant1 = model.Add(e.getSceneX(), e.getSceneY(), plantName.getId() );
									view.addPlants(plant1);
									garden.setSeasonRatings();
									view.getToolBarPane().updateRating(garden.getSeasonRatings());
									prevX = e.getX();
									prevY = e.getY();
								}
							}
						}
					}
				});
				
			}
			else {
				imageViewsAreToggleable = false;
				deleteImageToggle = false;
				view.getPlantSearchPane().setSelectedImage(null);
			}
		});
	}

	
	/**
	 * Setting the handler for selecting a single plant image in searchgardenpane at once. When plant image is
	 * selected, change cursor style, highlight the border of the image, and re-set selected plant image
	 * 
	 * @param ImageView the imageview of the plant that the user clicked on
	 */
	public void setHandlerForToggledImageViews(ImageView plantImage) {
		plantImage.setOnMousePressed(event -> {
			view.getScene().setCursor(Cursor.CLOSED_HAND);
			if(imageViewsAreToggleable) {
				encyclopediaChoice(event,plantImage);
				view.getPlantSearchPane().setSelectedImage(plantImage);
				
			}
		});
	}
	
	/**
	 * Set the handler for opening up the rating pop up. When the rating circles in the top right are clicked
	 * Show the rating popup
	 * 
	 * @param HBox the hbox that shows the four rating circles at all times
	 */
	public void setHandlerForRatingPopUp(HBox ratingCircles) {
		ratingCircles.setOnMousePressed(event -> {
			System.out.println("Work?");
			view.getToolBarPane().getRatingPopUp().show(view.getStage());
		});
	}
	
	
	
	/**
	 * Check to see if the plant size is larger than the garden Deminsions return True or False
	 * 
	 * @param plantName String of the plantName to search
	 * @return boolean true if plant fits within garden, false if plant is to large for garden
	 */
	public boolean checkPlantSize(String plantName) {
		Plant plant = model.getPlant(plantName);
		if(plant.getPlantSizeNum()<model.garden.getGardenHeight() && plant.getPlantSizeNum()<model.garden.getGardenWidth()) {
			return true;
		}
		else return false;
	}

	/**
	 * Getter for loadGarden boolean.
	 * 
	 * @return loadGarden boolean either true or false.
	 */
	public boolean getLoadGarden() {
		return loadGarden;
	}

	/**
	 * Getter for LoadedGarden.
	 * 
	 * @return loadedGarden Garden object.
	 */
	public Garden getLoadedGarden() {
		return loadedGarden;
	}

	/**
	 * Getter for helpText.
	 * 
	 * @return helpText String[] of help from help.txt.
	 */
	public String[] getHelpText() {
		return helpText;
	}
	
	public Garden getGarden() {
		return garden;
	}
	
}
