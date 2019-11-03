import java.util.ArrayList;

/**
 * A search function to search and sort the media within the LibrarySystem
 * @author Cameron Brandenburg
 */
public class LibrarySearch {

	boolean showMatureContent = false;
	String searchType;
	
	/**
	 * Sets the showMatureContent attribute based on the boolean parameter
	 * @param showMature The boolean of whether or not to show mature content
	 */
	public void showMatureContent(boolean showMature) {
		this.showMatureContent = showMature;
	}
	
	/**
	 * Searches the LibrarySystem for Media based on the titles of the Media
	 * @param searchFor The title of the media to be searched for
	 * @return An array list of the media that match the search parameters
	 */
	public ArrayList<Media> search(String searchFor) {
		ArrayList<Media> foundMedia = new ArrayList<Media>();
		ArrayList<Media> inv = LibrarySystem.getInstance().inventory;
		for (Media media : inv) {
			if (searchFor.equalsIgnoreCase(media.title)) {
				foundMedia.add(media);
			}
		}
		
		if (foundMedia.isEmpty())
			System.out.println("No media within the library matches the search parameter: \""
					+searchFor+"\"");
		
		return foundMedia;
	}
	
	/**
	 * Sorts an Array List of Media based on a parameter
	 * @param sortParam What parameter to sort the list by
	 */
	public void sortBy(String sortParam) {
		//TODO: implement sorting function
	}
	
}
