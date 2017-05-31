import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**This class is used to create an Arraylist of enemies and to generate random enemies from that ArrayList
 * 
 * @author jonathannguyen
 *
 */
public class EnemyGenerator{
	/**this holds all the enemys information in an array list
	 * 
	 */
	private ArrayList<Enemy> enemyList;
	
	/**Creates an ArrayList of Enemy objects based on content from the text file "Enemylist.txt"
	 * 
	 */
	public EnemyGenerator(){
		enemyList = new ArrayList<Enemy>();
		ItemGenerator itemGenerator = new ItemGenerator();
		String[] params;
		String line;
		try{
			Scanner reader = new Scanner(new File("EnemyList.txt"));
			do{
				line = reader.nextLine();
				params = line.split(",");
				Item i = itemGenerator.generateItem();
				Enemy en = new Enemy(params[0], params[1], Integer.parseInt(params[2]), 1, 2, i);
				enemyList.add(en);
			}while(reader.hasNextLine());
			reader.close();
		}catch(FileNotFoundException fnf){
			System.out.println("File not found.");
		}
	}
	
	/**Generates an enemy based on the hero object's level with a random item and gold.
	 * 
	 * @param level level of hero
	 * @return returns the Enemy object
	 */
	public Enemy generateEnemy(int level){
		ItemGenerator itemGenerator = new ItemGenerator();
		Item enItem = itemGenerator.generateItem();
		Enemy e = enemyList.get((int) (Math.random()*enemyList.size()-1));
		for(int ii = 1; ii < level; ii++){
			e.increaseLevel();
		}
		int newGold = (int) (Math.random()*(e.getGold()*(e.getLevel()*1.5))+e.getGold());
		Enemy ee = new Enemy(e.getName(), e.getQuip(), e.getHp(), e.getLevel(), newGold, enItem);
		return ee;
	}
}
