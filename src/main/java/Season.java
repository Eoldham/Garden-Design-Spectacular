package src.main.java;

/**
 * The Season Enum contains all season options.
 * 
  *@author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 */
public enum Season {

	SPRING("Spring"), SUMMER("Summer"), AUTUMN("Autumn"), WINTER("Winter"), ALL_SEASONS("All Seasons");

	private String name;

	/**
	 * toString method for Season name.
	 * 
	 * @param s String for season name.
	 */
	private Season(String s) {
		name = s;
	}

	/**
	 * Getter for season as a string.
	 * 
	 * @return name String name of season.
	 */
	public String getSeason() {
		return name;
	}
}
