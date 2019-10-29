import java.util.ArrayList;
import java.util.Calendar;

/**
 * LibrarySystem.java - Class for the library system information.
 * @author Kevin Prince
 *
 */
public class LibrarySystem {
	public ArrayList<Media> inventory;
	public ArrayList<Media> comingSoon;
	public ArrayList<User> users;
	public static LibrarySystem librarySystem;
	private Calendar systime;
	private boolean midnightUpdated;
	
	private LibrarySystem() {
		inventory = new ArrayList<Media>();
		comingSoon = new ArrayList<Media>();
		users = new ArrayList<User>();
		systime = Calendar.getInstance();
		midnightUpdated = false;
		systime.setLenient(true);
	}
	/**
	 * Returns the instance of the library system - since there should only ever be one.
	 * @return the current LibrarySystem.
	 */
	public static LibrarySystem getInstance() {
		if(librarySystem == null) {
			librarySystem = new LibrarySystem();
		}
		return librarySystem;
	}
	
	/**
	 * Creates a new average user using the passed in information.  May be modified to create different types of users at a later date.
	 * @param name
	 * @param dateOfBirth
	 * @param address
	 * @param email
	 * @param phoneNumber
	 * @param username
	 * @param password
	 * @param id
	 */
	public void createAverageAccount(String name, String dateOfBirth, String address,
			String email, String phoneNumber, String username,
			String password, int id) {
		if(!checkDuplicateAccount(email,phoneNumber)) {
			users.add(new AverageUser(name, dateOfBirth, address,
			email, phoneNumber, username,
			password, id));
		} else
			System.out.println("That email or phone number is already in use.  Try another.");
	}
	
	/**
	 * Checks for duplicate accounts within the system.
	 * @param email - an email for account creation
	 * @param phoneNumber - a phone number for account creation.
	 * @return true if the account is a duplicate
	 */
	private boolean checkDuplicateAccount(String email, String phoneNumber) {
		for(User user : users) {
			if(user.getEmail().equals(email) || user.getPhoneNumber().equals(phoneNumber))
				return true;
		}
		return false;
	}
	
	public void login(String username,String password) {
		//TODO: Create a login function once InputHandler is complete.
	}
	
	public Calendar returnSystime() {
		return this.systime;
	}
	
	/**
	 * Change the current systime to the passed in systime, and calls midnight update if it is currently midnight.
	 * Since the time 00:00 is likely to occur multiple times, the variable midnightUpdated is used to create a threshold.
	 * The method midnight update will only run if midnight update is false, after which it will be made true.
	 * Then, the midnightUpdated member will be made false once midnight has passed, preventing multiple updates.
	 * @param SysTime
	 */
	public void updateSystime(Calendar SysTime) {
		this.systime = SysTime;
		if(systime.get(Calendar.HOUR_OF_DAY) == 0 && systime.get(Calendar.MINUTE) == 0 && !midnightUpdated)
			midnightUpdate();
		else
			midnightUpdated = false;
	}
	
	/**
	 * Calls the update methods for various classes once systime hits midnight.
	 */
	private void midnightUpdate() {
		//TODO: Call fee/media update methods once implimented.
		midnightUpdated = true;
	}
	
	public void search(String searchterm) {
		//TODO: Impliment call to the search class.
	}
}
