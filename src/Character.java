import java.io.Serializable;

/**This is an abstract class for characters used in the dungeon crawler game. This abstract class is used for the Hero and enemy class.
 * 
 * @author jonathannguyen
 *
 */
public abstract class Character implements Serializable {
	/**name is the characters name
	 * quip is the characters catchphrase
	 * level is the level of the hero
	 * hp is the heros health
	 * gold is the amount of gold the hero has
	 * 
	 */
	private String name;
	private String quip;
	private int level;
	private int hp;
	private int gold;
	
	/**Constructs a Character object
	 * 
	 * @param n name
	 * @param qp quip
	 * @param h hp
	 * @param lvl player's level
	 * @param gld gold amount
	 */
	public Character(String n, String qp, int h, int lvl, int gld){
		name = n;
		quip = qp;
		hp = h;
		level = lvl;
		gold = gld;
	}
	
	/**Attack method for classes that use Character
	 * 
	 * @param c the character that's being attacked
	 */
	abstract void attack(Character c);
	
	/**returns the object's name
	 * 
	 * @return
	 */
	public String getName(){
		return name;
	}
	
	/**returns the object's quip
	 * 
	 * @return quip catchphrase
	 */
	String getQuip(){
		return quip;
	}
	
	/**returns the object's health
	 * 
	 * @return hp the health
	 */
	int getHp(){
		return hp;
	}
	
	/**returns the object's level
	 * 
	 * @return level (heros level)
	 */
	int getLevel(){
		return level;
	}
	
	/**Returns the objects's gold
	 * 
	 * @return gold (hero's gold)
	 */
	public int getGold(){
		return gold;
	}
	
	/**Increases the level of the object
	 * 
	 */
	public void increaseLevel(){
		heal(level);
		level++;
		if(level < 4){
			hp = hp*2;
		}
	}
	
	/**heals the object based on level
	 * 
	 * @param h object's level
	 */
	public void heal(int l){
		if(l == 3){
			hp = 40;
		}
		else{
			hp = 20;
		}
	}
	
	/**Object takes damage based on h
	 * 
	 * @param h damage amount
	 */
	public void takeDamage(int h){
		hp -= h;
	}
	
	/**Adds gold to object's collection
	 * 
	 * @param g gold amount
	 */
	public void collectGold(int g){
		gold += g;
	}
	
	/**Displays the characters name, hp, level, and gold.
	 * 
	 */
	public void display(){
		System.out.println("Name: " +getName() + " HP: " + getHp() + " Level: " + getLevel() + " Gold: " + getGold());
	}
	
}