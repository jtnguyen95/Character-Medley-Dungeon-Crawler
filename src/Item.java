import java.io.Serializable;

/**this class creates an item object that has a gold value and name
 * 
 * @author jonathannguyen
 *
 */
public class Item implements Serializable {
	/**name is the items name
	 * goldvalue is the value of the item
	 * 
	 */
	private String name;
	private int goldValue;
	
	/**this constructs an item with a name and a gold value
	 * 
	 * @param n the name
	 * @param gv the gold value 
	 */
	public Item(String n, int gv){
		name = n;
		goldValue = gv;
	}
	
	/** this gets the name of the item
	 * 
	 * @return name the name
	 */
	public String getName(){
		return name;
	}
	
	/**retursn the gold value of the item
	 * 
	 * @return goldValue the gold value of the item
	 */
	public int getValue(){
		return goldValue;
	}
}
