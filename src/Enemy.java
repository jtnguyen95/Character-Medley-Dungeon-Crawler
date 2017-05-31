/**Enemy object that has an item and gold that the hero can collect after defeating
 * 
 * @author jonathannguyen
 *
 */
public class Enemy extends Character{
	/**item is the item the enemy hold
	 * 
	 */
	private Item item;
	
	/**Creates an Enemy object with a name, quip, hp, lvl, and gold amount.
	 * 
	 * @param name is name of enemy
	 * @param quip is the enemys catcphrase
	 * @param h is the health
	 * @param lvl is the level
	 * @param gld is the gold
	 * @param i is enemy's item
	 */
	public Enemy(String name, String quip, int h, int lvl, int gld, Item i){
		super(name, quip, h, lvl, gld);
		item = i;
	}
	
	/**returns the enemy's item
	 * 
	 * @return item (the neemys item)
	 */
	public Item getItem(){
		return item;
	}
	
	/**Attacks another object for an amount based on this object's level.
	 * 
	 */
	@Override
	void attack(Character c){
		int hp1 = (int) (Math.random()*(this.getLevel()*1.5)+getLevel());
		c.takeDamage(hp1);
		System.out.println(this.getName() + " attacks the " + c.getName() + " for " + hp1 + "HP.");
	}
}
