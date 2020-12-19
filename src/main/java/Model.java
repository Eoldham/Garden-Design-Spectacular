package src.main.java;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * The Model class contains all data.
 * This data is garden width and height, plants and their data (Names, traits, descriptions, coordinates).
  *@author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 */
public class Model implements java.io.Serializable {
	
	ArrayList<Plant> plantList;
	int gardenWidth;
	int gardenHeight;
	String plantTraits = "Trees, Flowers, Bushes, Yellow, Blue, Pink";
	Garden garden;
	SavedData savedData; 
/**
 * Constructor For Model.
 * Initializes the Model.
 * @param plantList ArrayList of plants.
 */
	public Model(ArrayList<Plant> plantList) {
		this.plantList = plantList;
		this.garden = new Garden(gardenWidth, gardenHeight);

	}
	
	/**
	 * Getter for plantList.
	 * @return plantList
	 */
	public ArrayList<Plant> getPlantList() {
		return plantList;
	}
	
	/**
	 * Getter for plantTraits.
	 * @return plantTraits
	 */
	public String getPlantTraits() {
		return plantTraits;
	}
	
	/**
	 * Adds plants to the Garden List.
	 * @param getX x coordinate of plant.
	 * @param getY y coordinate of plant.
	 * @param name name of plant.
	 * @return plantReturned the plant with the given name, and coordinates.
	 */
	public Plant Add(double getX, double getY, String name) {
		Plant plantReturned = null; 
		for(Plant plant : plantList) {
			if (plant.getName().equals(name)){
				plant.setxCor(getX);
				plant.setyCor(getY);
				garden.addPlant(getX, getY, name, plant.getDescription(), plant.getTraits(), plant.getScienceName(), plant.getPlantSizeString(), plant.getPlantSizeNum());
				
				plantReturned = plant; 
				
			}
		}
		System.out.println(garden.getPlantsInGarden());
		return plantReturned; 
    }
	
	/**
	 * Return Plant given just its name as a string
	 * @param name Name of Plant
	 * @return Plant the plant class of the given plant
	 */
	public Plant getPlant(String name) {
		for(Plant plant : plantList) {
			if(plant.getName().equals(name)) {
				return plant;
			}
		}
		return null;
	}
	
	/**
	 * Removes plants for garden list.
	 * @param p Plant Object.
	 */
	public void remove(Plant p) {
		garden.getPlantsInGarden().remove(p);
	}
	
	/**
	 * Keeps track of each plant's X and Y coordinates.
	 * @param x x coordinate of the plants.
	 * @param y y coordinate of the plants.
	 * @param p Plant Object.
	 */
	public void move(double x, double y, Plant p) {
		p.setxCor(x);
		p.setyCor(y);
	}
	
	/**
	 * Searches the PlantList for plants with specified traits.
	 * @param plantTypeTrait String for plant traits.
	 * @param seasonTrait String for season of bloom.
	 * @param colorTrait String for color of plant.
	 * @return results Return plant list with the results.
	 */
	public ArrayList<String> searchPlantListByTrait(String plantTypeTrait, String seasonTrait, String colorTrait){
		if(plantTypeTrait=="All Plants" || plantTypeTrait==null) {
			plantTypeTrait = " ";
		}
		if(seasonTrait=="All Seasons" || seasonTrait==null) {
			seasonTrait = " ";
		}
		if(colorTrait=="All Colors"|| colorTrait==null) {
			colorTrait = " ";
		}
		
		ArrayList<String> results = new ArrayList<String>();
		for(Plant plant : plantList) {
			List<String> plantsTraits = Arrays.asList(plant.getTraits());
			if(plantsTraits.contains(plantTypeTrait) && plantsTraits.contains(seasonTrait) && plantsTraits.contains(colorTrait)) {
				results.add(plant.getName());
			}
		}
		return results;
	}
	
	/**
	 * Getter for plant description.
	 * @param plantName Name of plant.
	 * @return plantDescription Description of plant based on name.
	 */
	public String getPlantDescription(String plantName) {
		String plantDescription = null;
		for(Plant plant : plantList) {
			if(plant.getName().equals(plantName)) {
				plantDescription = plant.getDescription();
			}
		}
		return plantDescription;
	}
	
	/**
	 * Getter for scientific name of a plant
	 * @param name the name of the plant
	 * @return String the scientific name of the plant
	 */
	public String getPlantScienceName(String name) {
		String plantScience = null;
		for(Plant plant : plantList) {
			if(plant.getName().equals(name)) {
				plantScience = plant.getScienceName();
			}
		}
		return plantScience;
	}
	
	/**
	 * Getter for the size of the plant as a string
	 * @param plantName Name of plant.
	 * @return String string that contains the size of the plant.
	 */
	public String getPlantSizeString(String name) {
		String plantSize = null;
		for(Plant plant : plantList) {
			if(plant.getName().equals(name)) {
				plantSize = plant.getPlantSizeString();
			}
		}
		return plantSize;
	}
	
	/**
	 * Getter for the size of the plant as an int
	 * @param plantName Name of plant.
	 * @return int int that contains the size of the plant
	 */
	public double getPlantSizeNum(String name) {
		double plantSize = 0;
		for(Plant plant : plantList) {
			if(plant.getName().equals(name)) {
				plantSize = plant.getPlantSizeNum();
			}
		}
		return plantSize;
	}
	
	/**
	 * Getter for plant traits.
	 * @param plantName Name of plant.
	 * @return plantTraits Traits of plant based on name.
	 */
	public ArrayList<String> getPlantTraits(String plantName) {
		ArrayList<String> plantTraits = new ArrayList<String>();
		for(Plant plant : plantList) {
			if(plant.getName().equals(plantName)) {
				for (String trait: plant.getTraits()) {
				plantTraits.add(trait);
				}
			}
		}
		return plantTraits;
	}
	
	
	
	/**
	 * Getter for Garden Object.
	 * @return garden Garden Object.
	 */
	public Garden getGarden() {
		return garden;
	}
	
	/**
	 * Setter for Garden Object.
	 * @param garden Garden Object.
	 */
	public void setGarden(Garden garden) {
		this.garden = garden;
	}

}






