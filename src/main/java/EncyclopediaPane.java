package src.main.java;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * The EncyclopediaPane class contains all View elements for the EncylopediaPane.
  *@author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 * 
 *
 */
public class EncyclopediaPane {
	
	// EncyclopediaPane Dimensions
	final private int EncyclopediaPane_WIDTH = 800;
	final private int EncyclopediaPane_HEIGHT = 760;
	final View mainView;
	
	private TilePane imgContainer;
	private BorderPane borderPane; 

	public EncyclopediaPane(View mainView) {
		VBox vb = new VBox(8);
		
		this.mainView = mainView; 
		
		Text label = new Text("Plant Encyclopedia");
		label.setStyle("-fx-font-weight: bold;-fx-font: 24 Papyrus");
		
		Text Info = new Text ("To use the Plant Encyclopedia, you can search using the left side search tool and click on the image");
		Info.setStyle("-fx-font: 18 garamond");
		Text Info2 = new Text (" to bring up the plant's page. To go back to your garden click done. ");
		Info2.setStyle("-fx-font: 18 garamond");
		
		Button done = new Button ("Done");
		mainView.control.setHandlerForDonePlantEncycClicked(done);
		done.setStyle("-fx-background-color:#dbd0ab;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3; -fx-font: 18 Garamond");
		
		vb.getChildren().addAll(label, Info, Info2, done);
		vb.setAlignment(Pos.CENTER);
		
		borderPane = new BorderPane();
		borderPane.setStyle("-fx-background-color:#D1EBDC;-fx-border-color: black;-fx-border-width:2;-fx-border-radius:3;");
		borderPane.setCenter(vb);
		borderPane.setMinWidth(EncyclopediaPane_WIDTH);
		borderPane.setMinHeight(EncyclopediaPane_HEIGHT);
		borderPane.setMaxHeight(EncyclopediaPane_HEIGHT);
		BorderPane.setAlignment(borderPane,Pos.TOP_CENTER);
		BorderPane.setAlignment(done,Pos.BASELINE_CENTER);
		
		
	}
	/**
	 * Getter for borderPane.
	 * @return borderPane
	 */
	public BorderPane getPane() {
		return borderPane; 
	}
	
	/**
	 * Builds the Encyclopedia page view.
	 * @param name String name of plant.
	 */
	public BorderPane buildPlantPage(String name) {
		
		borderPane = new BorderPane();
		borderPane.setMinWidth(EncyclopediaPane_WIDTH);
		borderPane.setMinHeight(EncyclopediaPane_HEIGHT);
		borderPane.setMaxHeight(EncyclopediaPane_HEIGHT);
		
		imgContainer = new TilePane();
		
		//Create title for the page
		VBox top = new VBox();
		Text title = new Text("Encyclopedia");
		title.setStyle("-fx-font-weight: bold");
		title.setStyle("-fx-font: 24 Papyrus");
		top.getChildren().add(title);
		top.setAlignment(Pos.CENTER);
		
		
		//get plant information
		ImageView img = new ImageView(mainView.getPlantList().get(name).getImage());
		imgContainer.getChildren().add(img);
		imgContainer.setAlignment(Pos.CENTER);
		imgContainer.setMaxSize(100, 100);
		
		
		
		Text commonName = new Text("Common Name: " + name.replace("_", " "));
		commonName.setStyle("-fx-font: 18 garamond");
		
		
		Text description = new Text("Description: " + mainView.control.getPlantDescription(name));
		description.setStyle("-fx-font: 18 garamond");
		
		Text sciName = new Text("Scientific Name: " + mainView.control.getPlantScienceName(name).replace("_", " "));
		sciName.setStyle("-fx-font: 18 garamond");
		
		Text size = new Text("Size: " + mainView.control.getPlantSizeString(name));
		size.setStyle("-fx-font: 18 garamond");
		
		//Create Vbox
			VBox vb = new VBox(8);
			vb.getChildren().addAll(top,imgContainer,commonName,sciName, description, size);
			vb.setAlignment(Pos.CENTER);
		
			HBox hb = new HBox();
			hb.setAlignment(Pos.CENTER);
			
			Text traitTitle = new Text("Traits: ");
			traitTitle.setStyle("-fx-font: 18 garamond");
			vb.getChildren().add(traitTitle);
			
		for (String trait: mainView.control.getPlantTraits(name) ) {
			Text traits = new Text(trait + " ");
			traits.setStyle("-fx-font: 18 garamond");
			hb.getChildren().add(traits);
		}
		
		vb.getChildren().add(hb);
		

		Button done = new Button ("Done");
		mainView.control.setHandlerForDonePlantEncycClicked(done);
		done.setStyle("-fx-background-color:#dbd0ab;-fx-border-color: black;-fx-border-width:.5;-fx-border-radius:3;-fx-font: 18 Garamond");
		vb.getChildren().add(done);
		
		
		borderPane = new BorderPane();
		borderPane.setStyle("-fx-background-color:#D1EBDC;-fx-border-color: black;-fx-border-width:2;-fx-border-radius:3;");
		borderPane.setTop(vb);
		BorderPane.setAlignment(borderPane,Pos.TOP_CENTER);
		
		return borderPane;
		
	}
}
