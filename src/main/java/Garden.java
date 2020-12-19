package src.main.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * The Garden Class contains all model data for the Garden.
 * Contains height, width, seasons, rating, and plant data.
 *  *@author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 */
class Garden implements java.io.Serializable {
	private int width;
	private int height;
	private ArrayList<Plant> plantsInGarden = new ArrayList<Plant>();
	
	private Season season;
	private ArrayList<Integer> ratings;

	public Garden(int width, int height) {
		this.width = width;
		this.height = height;
		this.season = Season.ALL_SEASONS;
		setSeasonRatings();
	}
	
	/**
	 * Adds plant to plants in garden list.
	 * @param xCord x coordinate of plant.
	 * @param yCord y coordinate of plant.
	 * @param name name of plant.
	 * @param description description of the plant.
	 * @param traits traits of the plant.
	 * @param plantScieneName, scientific name of the plant.
	 * @param plantSizeString, size of the plant as a string.
	 */
	//Updated so that it needs a description variable. 
	public void addPlant(double xCord, double yCord, String name, String description, String[] traits, String plantScienceName, String plantSizeString, double plantSizeNum) {
		Plant plant = new Plant(name, description, traits, plantScienceName, plantSizeString, plantSizeNum);
		plant.setxCor(xCord);
		plant.setyCor(yCord);
		getPlantsInGarden().add(plant);
	}
	
	/**
	 * Getter for Plants in Season.
	 * @return plantsInGarden list updated with plants matching the season.
	 */
	public ArrayList<Plant> getPlantsInSeason() {
		ArrayList<Plant> plantsInSeason = new ArrayList<Plant>();
		if(season == Season.ALL_SEASONS) {
			return plantsInGarden;
		}
		else {
			for(Plant plant : plantsInGarden) {
				List<String> plantTraits = Arrays.asList(plant.getTraits());
				if(plantTraits.contains(season.getSeason())) {
					plantsInSeason.add(plant);
				}
			}
		}
		return plantsInSeason;
	}
	
	/**
	 * Setter for season rating.
	 * Season ratings are updated by number of plants that bloom in that season.
	 */
	public void setSeasonRatings() {
		ArrayList<Integer> seasons = new ArrayList<Integer>();
		seasons.add(0);
		seasons.add(0);
		seasons.add(0);
		seasons.add(0);
		for(Plant plant : plantsInGarden) {
			List<String> plantTraits = Arrays.asList(plant.getTraits());
			if(plantTraits.contains(Season.SPRING.getSeason())) {
				seasons.set(0, seasons.get(0) + 1);
			}
			if(plantTraits.contains(Season.SUMMER.getSeason())) {
				seasons.set(1, seasons.get(1) + 1);
			}
			if(plantTraits.contains(Season.AUTUMN.getSeason())) {
				seasons.set(2, seasons.get(2) + 1);
			}
			if(plantTraits.contains(Season.WINTER.getSeason())) {
				seasons.set(3, seasons.get(3) + 1);
			}
		}
		ratings = seasons;
	}

	/**
	 * Getter for GardenWidth.
	 * @return width Width of Garden.
	 */
	public int getGardenWidth() {
		return width;
	}
	
	/**
	 * Getter for GardenHeight
	 * @return height Height of Garden.
	 */
	public int getGardenHeight() {
		return height;
	}
	
	/**
	 * Getter for plantsInGarden.
	 * @return plantsInGarden
	 */
	public ArrayList<Plant> getPlants() {
		return plantsInGarden;
	}
	
	/**
	 * Getter for this instance of plantsInGarden.
	 * @return plantsInGarden
	 */
	public ArrayList<Plant> getPlantsInGarden() {
		return this.plantsInGarden;
	}
	
	/**
	 * Getter for Season Enum.
	 * @return season
	 */
	public Season getSeason() {
		return season;
	}
	/**
	 * Setter for season
	 * @param season Enum for season.
	 */
	public void setSeason(Season season) {
		this.season = season;
	}
	
	/**
	 * Getter for rating value.
	 * @return ratings Integer values for garden rating.
	 */
	public ArrayList<Integer> getSeasonRatings() {
		return ratings;
	}

	
}
