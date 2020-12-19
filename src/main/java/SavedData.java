package src.main.java;

/**
 * The SavedData Class holds information on the state of save gardens.
 *@author Elijah Haberman
 * @author Emily Oldham
 * @author JC Sergent
 * @author Arthur Marino
 * @author Caroline Graham
 *
 */
public class SavedData implements java.io.Serializable {
	Garden garden;
	String fileName;
	
	/**
	 * Constructor for SavedData Object.
	 * Initializes the SavedData Object.
	 * @param fileName String for the name of the file.
	 * @param model Model Object.
	 */
	public SavedData(String fileName, Model model) {
		this.fileName = fileName;
		this.garden = model.garden; 
		
		
	}
	
	/**
	 * Getter for Garden Object.
	 * @return garden Garden Object.
	 */
	public Garden getGarden() {
		return garden;
	}
	
	/**
	 * Getter for fileName.
	 * @return fileName String Name of file.
	 */
	public String getFileName() {
		return fileName;
	}
}
