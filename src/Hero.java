import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**This is the Hero class which stores the user's player data such as gold, level, health and name.
 * 
 * @author jonathannguyen
 *
 */
public class Hero extends Character implements Serializable {
	/**items is the arrayList that holds the items
	 * location is the point location of the hero
	 * 
	 */
	private ArrayList<Item> items = new ArrayList<Item>();
	private Point location;
	
	/**constructs a hero using a name, catchphrase, and start location
	 * 
	 * @param name name of hero
	 * @param quip catchprhase of hero
	 * @param start starting point
	 */
	public Hero(String name, String quip, Point start){
		super(name, quip, 100, 1, 0);
		location = start;
	}
	
	/**returns the hero's object ArrayList of items.
	 * 
	 * @return the items array
	 */
	public ArrayList<Item> getItems(){
		return items;
	}
	
	/**Picks up an item. Returns false if the Arraylist is full and doesn't pick up item.
	 * 
	 * @param i item being added
	 * @return false if arrayList full;
	 */
	public void pickUpItem(Item i){
		if(items.size() < 5){
			items.add(i);
		}
	}
	
	/**Removes specific item using item object
	 * 
	 * @param i item being removed
	 */
	public void removeItem(Item i){
		items.remove(i);
	}
	
	/**removes item based on index in ArrayList.
	 * 
	 * @param index used to search for item
	 */
	public void removeItem(int index){
		items.remove(index);
	}
	
	/**Returns the location of the hero on a coordinate plane.
	 * 
	 * @return location of the hero
	 */
	public Point getLocation(){
		return location;
	}
	
	/**Sets the location of the hero to a specific point.
	 * 
	 * @param p point being set as hero's location
	 */
	public void setLocation(Point p){
		location = p;
	}
	
	/**Makes the character go in the West direction if possible. Else, it returns a statement saying the user can't.
	 * 
	 * @param l level used to get the character of the room/location.
	 * @return the character used to create an interaction
	 */
	public char goWest(Level l){
		Point p = new Point();
		if(location.getY()-1 > -1){
			p.setLocation(location.getX(), location.getY()-1);
			setLocation(p);
		}
		else{
			System.out.println("Cannot move in that direction.");
		}
		char x = l.getRoom(location);
		return x;
	}
	
	/**Makes the character go in the South direction if possible. Else, it returns a statement saying the user can't.
	 * 
	 * @param l level used to get the character of the room/location.
	 * @return the character used to create an interaction
	 */
	public char goSouth(Level l){
		if(location.getX()+1 < 4){
			location.setLocation((location.getX()+1), (location.getY()));
		}
		else{
			System.out.println("Cannot move in that direction.");
		}
		char x = l.getRoom(location);
		return x;
	}
	
	/**Makes the character go in the North direction if possible. Else, it returns a statement saying the user can't.
	 * 
	 * @param l level used to get the character of the room/location.
	 * @return the character used to create an interaction
	 */
	public char goNorth(Level l){
		if(location.getX()-1 > -1){
			location.setLocation((location.getX()-1), (location.getY()));
		}
		else{
			System.out.println("Cannot move in that direction.");
		}
		char x = l.getRoom(location);
		return x;	
	}
	
	/**Makes the character go in the east direction if possible. Else, it returns a statement saying the user can't.
	 * 
	 * @param l level used to get the character of the room/location.
	 * @return the character used to create an interaction
	 */
	public char goEast(Level l){
		if(location.getY()+1 < 4){
			location.setLocation((location.getX()), (location.getY()+1));
		}
		else{
			System.out.println("Cannot move in that direction.");
		}
		char x = l.getRoom(location);
		return x;
	}
	
	/**Attack method overridden from Character class. Attacks character C based on enemy's name. 
	 * 
	 */
	@Override
	void attack(Character c) {
		int hp1 = (int) (Math.random()*(this.getLevel()*3)+getLevel());
		c.takeDamage(hp1);
		System.out.println(this.getName() + " attacks the " + c.getName() + " for " + hp1 + "HP.");
	}
}