import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * 
 * @author jonathannguyen
 *
 */
public class ItemGenerator{
	/**
	 * 
	 */
	private ArrayList<Item> itemList;
	
	/**
	 * 
	 */
	public ItemGenerator(){
		itemList = new ArrayList<Item>();
		String[] params;
		String line;
		
		try{
			Scanner reader = new Scanner(new File("ItemList.txt"));
			do{
				line = reader.nextLine();
				params = line.split(",");
				Item i = new Item(params[0], Integer.parseInt(params[1]));
				itemList.add(i);
			}while(reader.hasNextLine());
			
			reader.close();
		}catch(FileNotFoundException fnf){
			System.out.println("File not found.");
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Item generateItem(){
		Item i = itemList.get((int) (Math.random()*itemList.size()-1));
		Item ii = new Item(i.getName(),i.getValue());
		return ii;
	}
}
