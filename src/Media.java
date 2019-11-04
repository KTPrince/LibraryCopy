
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
/**
 * Media.java - Abstract superclass for all media items.
 * @author Kevin Prince
 *
 */
public abstract class Media {
	public static int MEDIA_COUNT = 0;

	protected String title;
	protected String genre;
	protected String description;
	protected String yearOfRelease;
	protected int rating;
	protected String comment;
	protected ArrayList<String> ratingList;
	protected boolean newRelease;
	protected boolean checkedOut;
	protected int id;
	protected Date lastBorrowDate;
	protected Date lastDueDate;
	protected ArrayList<User> waitlist;
	protected int renewCount;
	protected int copies;
	protected int checkoutLength; //Length of standard checkout in DAYS.
	
	public Media(String title,String genre,String description,String year,boolean newRelease,int copies) {
		MEDIA_COUNT++;
		this.title = title;
		this.genre = genre;
		this.description = description;
		this.yearOfRelease = year;
		this.newRelease = newRelease;
		this.copies = copies;
		this.renewCount = 0;
		this.checkedOut = false;
		this.id = MEDIA_COUNT;
		this.waitlist = new ArrayList<User>();
		this.ratingList = new ArrayList<String>();
		LibrarySystem.getInstance().inventory.add(this);
	}
	
	public Media checkout() {
		//TODO: Add hold functionality.
		if(!this.checkedOut) {
			this.checkedOut = true;
			System.out.println("You have successfully checked out " + this.title + " on " + LibrarySystem.getInstance().returnSystime().getTime());
			setDueDates();
			System.out.println("This item is due on: " + this.lastDueDate.toString());
			return this;
		} else {
			//If this item IS checked out, it'll check it's copies for an available one.
			if(this.hasCopies()) {
				System.out.println("Checking for copies...");
				for(Media m:this.returnCopies()) {
					if(!m.isCheckedOut()) {
						System.out.println("Copy found!");
						m.checkout();
						return m;
					}
				}
			} 
			Scanner s = TempDriver.s;
			System.out.println("There are no copies of this item available.");
			System.out.println("Would you like to be added to the waitlist? (Y/N)");
			String result = s.nextLine();
			if(result.toUpperCase().equals("Y"))
				this.waitlist.add(InputHandler.currentUser);
			return null;
		}	
	}
	
	public void renew() {
		if(this.checkedOut && this.renewCount < 3) {
			renewCount++;
			setDueDates();
			System.out.println("Success! New due date is: " + this.lastDueDate.toString());
			System.out.println("This has been renewed " + this.renewCount + " times.");
		} else {
			if(renewCount >= 3)
				System.out.println("You cannot renew this item: it has already been renewed 3 times.");
			else
				System.out.println("You can't renew an item you haven't checked out!");
		}
	}
	
	/**
	 * Resets renew counts and sets checked out to false.  Notifies the next user on the waitlist that the item is now avaliable.
	 * TODO: May be modified later to call a user method that sets a timer to claim the hold.  Modify Hold system to accomodate copies.
	 */
	public void returnMedia() {
		checkedOut = false;
		renewCount = 0;
		for(Media m:this.returnCopies()) {
			if(!m.getWaitlist().isEmpty()) {
				m.getWaitlist().get(0).notify("An item on your wishlist is available: " + this.title);
				m.getWaitlist().remove(0);
				return;
			}
		}
	}
	
	public void placeHold(User user) {
		waitlist.add(user);
	}
	
	public boolean isCheckedOut() {
		return this.checkedOut;
	}
	
	public String returnCheckoutTimestamp() {
		return this.lastBorrowDate.toString();
	}
	
	public int returnId() {
		return this.id;
	}
	
	public boolean hasCopies() {
		return this.copies > 1;
	}
	
	public int returnTimesRenewed() {
		return this.renewCount;
	}
	
	public String toString() {
		return "Placeholder";
	}
	
	/**
	 * Compares the current date to the last due date.  If it's after, the item is overdue.
	 * @return true is the due date has passed.
	 */
	public boolean isOverdue() {
		return LibrarySystem.getInstance().returnSystime().getTime().after(this.lastDueDate);
	}
	
	/**
	 * This method sets the due dates of the media item.  It does so by storing the current date in the lastBorrowDate member, then it adds an 
	 * amount of days equal to the checkoutLength to the calendar.  It stores that value into the lastDueDate member, then restores the calendar.
	 */
	private void setDueDates() {
		Calendar temp = (Calendar) LibrarySystem.getInstance().returnSystime().clone();
		this.lastBorrowDate = temp.getTime();
		LibrarySystem.getInstance().returnSystime().add(Calendar.DAY_OF_YEAR, checkoutLength);
		this.lastDueDate = LibrarySystem.getInstance().returnSystime().getTime();
		LibrarySystem.getInstance().updateSystime(temp);
	}
	
	/**
	 * This method sets the rating of a media from 1 - 5, the user can also adds comments.
	 * @param rate
	 */
	public void addRating(int rate, String comment){
		if(rate >= 1 && rate <=5) {
			this.rating = rate;
			this.comment = comment;
			//System.out.println("Your rating for " + this.title + ": " + this.rating);
			//System.out.println("Your comment for " + this.title + ": " + this.comment);
			
			/**
			 *  This addRating variable will be user to store the rating as a string and comment 
			 *  that can be added to an array list. The array list will display the total ratings
			 *  from each user.
			 */
			String addRating = "Rate: " + Integer.toString(this.rating) + "\nComment: " + this.comment;
			ratingList.add(addRating);
		}
		else
			System.out.println("Rating must be from 1 - 5");
	}
	
	/**
	 * Displays the total rating for a media
	 */
	public void displayRating(){
		ArrayList<String> allRatings = new ArrayList<String>();
		for(Media m:this.returnCopies()) {
			allRatings.addAll(m.getRatingList());
		}
			if(!allRatings.isEmpty()) {
				System.out.println(this.title + " has a total of " + allRatings.size() + " ratings: ");
				for (String rating : allRatings)
					System.out.println(rating);
			}
			else
				System.out.println(this.title + " has no rating");
	}
	
	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}

	public void setLastBorrowDate(Date lastBorrowDate) {
		this.lastBorrowDate = lastBorrowDate;
	}

	public void setLastDueDate(Date lastDueDate) {
		this.lastDueDate = lastDueDate;
	}

	public void setWaitlist(ArrayList<User> waitlist) {
		this.waitlist = waitlist;
	}

	public void setRenewCount(int renewCount) {
		this.renewCount = renewCount;
	}
	
	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getGenre() {
		return genre;
	}

	public String getDescription() {
		return description;
	}

	public String getYearOfRelease() {
		return yearOfRelease;
	}

	public int getRating() {
		return rating;
	}
	
	public String getComment() {
		return comment;
	}

	public boolean isNewRelease() {
		return newRelease;
	}

	public Date getLastBorrowDate() {
		return lastBorrowDate;
	}

	public Date getLastDueDate() {
		return lastDueDate;
	}

	public ArrayList<User> getWaitlist() {
		return waitlist;
	}

	public int getRenewCount() {
		return renewCount;
	}

	public int getCopies() {
		return copies;
	}

	public int getCheckoutLength() {
		return checkoutLength;
	}

	protected Object[] copyMediaData() {
		Object[] data = new Object[8];
		data[0] = this.title;
		data[1] = this.genre;
		data[2] = this.description;
		data[3] = this.yearOfRelease;
		data[4] = this.newRelease;
		data[5] = this.copies;
		return data;
	}
	
	public abstract void copy();
	
	public void copyCountUp() {
		for(Media m:this.returnCopies())
			m.addCopyCount();
	}

	public ArrayList<String> getRatingList(){
		return ratingList;
	}
	
	//Returns all copies of this media, based on title.  INCLUDES SELF!
	public ArrayList<Media> returnCopies() {
		ArrayList<Media> copies = new ArrayList<Media>();
		for(Media m:LibrarySystem.getInstance().inventory) {
			if(m.title.equals(this.title))
				copies.add(m);
		}
		return copies;
	}
	
	private void addCopyCount() {
		this.copies++;
	}
	
	public void setRatingsList(ArrayList<String> ratings) {
		this.ratingList = ratings;
	}
}
