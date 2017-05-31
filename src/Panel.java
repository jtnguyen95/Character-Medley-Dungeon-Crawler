import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**This is the panel class and is essentially the driver that runs the dungeon master game. This class includes the logic for the game and the panel contents for the window.
 * 
 * @author jonathannguyen
 *
 */
public class Panel extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener{
	/**hero is the game character
	 * level is the level where the hero traverses the map
	 * file f is the data of the player
	 * enemy et is the enemy being generated to be fought 
	 * item it is the item being generated that can be picked up by the hero
	 * i is the item generator that generates a new item every time an item is picked up
	 * e is the item generator that generates a new enemy every time an enemy dies
	 * room is used to determine which room that hero enters
	 * selection represents keys #1-5 for player choices
	 * counter is used for an enemy to attack once when a battle sequence starts
	 * background, character, enemy, and map are all image variables that hold images in the panel
	 * inRoom is used to see if a room's function is running, keyPressed is used to determine if a key has been pressed, 
	 * cantMove restricts the player from moving when they're inside a room
	 * continueGame is used to continue the game when a sequence in a room has ended
	 * 
	 */
	private Hero hero;
	private Level level = new Level();
	private File f = new File("Player.dat");
	private EnemyGenerator e = new EnemyGenerator();
	private Enemy en;
	private ItemGenerator i = new ItemGenerator();
	private Item it;
	private char room;
	private int selection;
	private int counter = 0;
	private Image background, character, enemy, map;
	private boolean inRoom = false, keyPressed = false, cantMove = false, continueGame = false;
	
	/**This is the panel that constructs the game inside a window
	 * 
	 */
	public Panel(){
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.setBackground(Color.white);
		boolean newChar = true;
		String name = "";
		if(newChar){
			name = JOptionPane.showInputDialog(null, "What is your name?");
			newChar = false;
		}
		level.generateLevel(1);
		hero = new Hero(name, "gg", level.findStartLocation());
		System.out.println("character created");
		
		en = e.generateEnemy(1);
		it = i.generateItem();
		if(f.exists()){
			try{
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
				hero = (Hero) in.readObject();
				JOptionPane.showMessageDialog(null, "Your new character has been overwritten with a previous save file.");
				if(hero.getLevel() < 4){
					level.generateLevel(hero.getLevel());
					hero.setLocation(level.findStartLocation());
				}
				else{
					level.generateLevel((hero.getLevel()-1));
					level.generateLevel(hero.getLevel());
					hero.setLocation(level.findStartLocation());
				}
				in.close();
			}catch( IOException e ){
				System.out.println("Error processing file.");
			}
			catch( ClassNotFoundException e ){
				System.out.println("Could not find class.");
			}
		}
		else{
			System.out.println("File not found.. Creating a new charcter. Complete the first level to save.");
		}
		
		try {
			background = ImageIO.read(new File("background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			character = ImageIO.read(new File("character.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**This determines what is happening in the panel and displays and panel contents. It runs different methods when the hero is in a certain room.
	 * key 1 is attack, key 2 is run away, key 3 is potion
	 * 
	 */
	public void paintComponent(Graphics g){
		g.setColor(Color.white);
		setMapImage();
		g.drawImage(background, 0, 0, 800, 650, null);
		g.drawImage(character, 120, 200, 200, 200, null);
		g.drawImage(map, 610, 10 , 175, 175, null);
		
		level.displayMap(hero.getLocation(), g);
		g.drawString(hero.getName(), 10, 30);
		g.drawString("HP: " + hero.getHp(), 10, 50);
		g.drawString("GOLD: " + hero.getGold(), 10, 70);
		g.drawString("ITEMS: ", 10, 90);
		for(int ii = 0; ii < hero.getItems().size(); ii++){
			 g.drawString((ii + 1) + ". " + hero.getItems().get(ii).getName(), 10, (100+(20*(ii+1))));
		}
		
		if(room == 'm' && inRoom){
			battle(g);
		}
		
		if(room == 's' && inRoom){
			shop(g);
		}
		
		if(room == 'i' && inRoom){
			itemInteraction(g);
		}
		if(room == 'f' && inRoom){
			goToNextLevel(g);
		}
	}
	
	/**This the battle sequence where the hero fights a random enemy. if the hero has a potion, the hero has an option of using it
	 * key 1 is attack, key 2 is run away, key 3 is potion. the hero collects the enemy's item and gold after it has defeated it.
	 * 
	 * @param g is the graphics component so that this method can draw to the screen
	 */
	private void battle(Graphics g){
		cantMove = true;
		if(counter == 0){
			g.drawString("You've been attacked!", 50, 400);
			counter++;
		}
		if(hero.getHp() <= 0){ 
			g.drawString("GAMEOVER. You died!", 350, 300);
		}
		boolean hasPotion = false;
		if(en.getHp() > 0){
			setMonsterImage();
			g.drawImage(enemy, 450, 200, 200, 200, null);
			g.drawString("What would you like to do?", 50, 420);
			g.drawString("1. Attack.", 50, 440);
			g.drawString("2. Run away.", 50, 460);
			g.drawString(en.getName(), 150, 50);
			g.drawString("HP:  " + en.getHp(), 150, 30);
		}
		for(int ii = 0; ii < hero.getItems().size(); ii++){
			if(hero.getItems().get(ii).getName().equals("Health Potion")){
				hasPotion = true;
			}
		}
		if(hasPotion && en.getHp() > 0){
			g.drawString("3. Use potion.", 50, 480);
		}
		if(selection == 1 && keyPressed && en.getHp() > 0){
			keyPressed = false;
				hero.attack(en);
				en.attack(hero);
				if(en.getHp() < 1 && hero.getHp() > 0){
					hero.collectGold(en.getGold());
					if(hero.getItems().size() < 5){
						hero.pickUpItem(((Enemy) en).getItem());
					}
				}
		}
		else if(selection == 2 && keyPressed)
		{
			keyPressed = false;
				int escapeDirection = ((int)(Math.random()*3)+1);
				switch(escapeDirection)
				{
				case 1:{
					if(hero.getLocation().getX()-1 < 0){
						break;
					}
					else
					{
						hero.goNorth(level);
						g.drawString("Escape successful!", 50, 390);
						cantMove = false;
						inRoom = false;
						return;
					}
				}
				case 2:{
					if(hero.getLocation().getY()+1 > 4){
						break;
					}
					else{
						hero.goEast(level);
						g.drawString("Escape successful!", 50, 390);
						cantMove = false;
						inRoom = false;
						return;
					}	
				}
				case 3:{
					if(hero.getLocation().getY()-1 < 0){
						break;
					}
					else
					{
						hero.goWest(level);
						g.drawString("Escape successful!", 50, 390);
						cantMove = false;
						inRoom = false;
						return;
					}
				}
				default:{
					if(hero.getLocation().getX()+1 > 4){
						break;
					}
					else
					{
						hero.goSouth(level);
						g.drawString("Escape successful!", 50, 390);
						cantMove = false;
						inRoom = false;
						return;
					}
				}
			}
		}
		else if((selection == 3) && (hasPotion) && (keyPressed)){
			int potionIndex = 0;
			for(int ii = 0; ii < hero.getItems().size(); ii++)
			{
				if(hero.getItems().get(ii).getName().equals("Health Potion")){
					potionIndex = ii;
				}
			}
			hero.heal(hero.getLevel());
			hero.getItems().remove(potionIndex);
			g.drawString("Healed!", 150, 440);
			hasPotion = false;
		}
		if(en.getHp() < 1 && hero.getHp() > 0){
			g.drawString(en.getName() + ": " + en.getQuip(), 50, 400);
			g.drawString(hero.getName() + " defeated the " + en.getName(), 50, 420);
			g.drawString(hero.getName() + " picked up " + en.getGold() + " gold.", 50, 440);
			g.drawString(hero.getName() + " picked up " + ((Enemy) en).getItem().getName(), 50, 460);
			g.drawString(hero.getName() + ": \"" + hero.getQuip() + "\"", 50, 480);
			g.drawString("Press the spacebar to continue the game!", 50, 500);
			if(continueGame){
				en = e.generateEnemy(hero.getLevel());
				cantMove = false;
				inRoom = false;
				continueGame = false;
			}
		}
	}
	
	/**this method allows the hero to sell his or her items for gold. the shop is located at the start location of every floor. 
	 * uses keys #1-5
	 * 
	 * @param g is the graphics component so that this method can draw to the screen
	 */
	private void shop(Graphics g){
		cantMove = true;
		if(hero.getItems().size() > 0){	
			g.drawString("Shopkeeper: Which item would you like to sell? \nOptions:\n0. Exit.", 50, 400);
		}
		else{
			g.drawString("Shopkeeper: You have no items to sell!", 50, 400);
			cantMove = false;
			return;
		}

		if(selection == 0 && keyPressed){
			keyPressed = false;
			g.drawString("Shopkeeper: See you next time!", 50, 400);
			cantMove = false;
		}
		if(selection == 1 && keyPressed){
			keyPressed = false;
			hero.collectGold(hero.getItems().get(0).getValue());
			hero.removeItem(0);
			g.drawString("Sold!", 50, 400);
		}
		if(selection == 2 && keyPressed){
			keyPressed = false;
			if(hero.getItems().size() > 1){
				hero.collectGold(hero.getItems().get(1).getValue());
				hero.removeItem(1);
				g.drawString("Sold!", 50, 400);
			}
			else{
				g.drawString("Invalid selection, try again!", 50, 400);
			}
		}
		if(selection == 3 && keyPressed){
			keyPressed = false;
			if(hero.getItems().size() > 2){
				hero.collectGold(hero.getItems().get(1).getValue());
				hero.removeItem(1);
				g.drawString("Sold!", 50, 400);
			}
			else{
				g.drawString("Invalid selection, try again!", 50, 400);
			}
		}
		if(selection == 4 && keyPressed){
			keyPressed = false;
			if(hero.getItems().size() > 3){
				hero.collectGold(hero.getItems().get(1).getValue());
				hero.removeItem(1);
				g.drawString("Sold!", 50, 400);
			}
			else{
				g.drawString("Invalid selection, try again!", 50, 400);
			}
		}
		if(selection == 5 && keyPressed){
			keyPressed = false;
			if(hero.getItems().size() > 4){
				hero.collectGold(hero.getItems().get(1).getValue());
				hero.removeItem(1);
				g.drawString("Sold!", 50, 400);
			}
			else{
				g.drawString("Invalid selection, try again!", 50, 400);
			}
		}
	}
	
	
	/**This method allows the hero to traverse a new level once he has leveled up and completed the previous floor
	 * 
	 * @param g is the graphics component so that this method can draw to the screen
	 */
	private void goToNextLevel(Graphics g)
	{
		cantMove = true;
		g.drawString("Congratulations! You beat the floor.", 50, 400);
		g.drawString("Press spacebar to continue.", 50, 420);
		if(continueGame){
			g.drawString("works.", 50, 40);
			hero.increaseLevel();
			level.generateLevel(hero.getLevel());
			hero.setLocation(level.findStartLocation());
			try
			{
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
				out.writeObject(hero);
				out.close();
			}catch( IOException e ) 
			{
				System.out.println("Error processing file.");
			}
			cantMove = false;
			inRoom = false;
			continueGame = false;
		}
	}
	
	/**This method is used when the user finds an item. The user can have upto 5 items
	 * 
	 * @param g is the graphics component so that this method can draw to the screen
	 */
	private void itemInteraction(Graphics g){
		cantMove = true;
		g.drawString(hero.getName() + " found a " + it.getName(), 50, 400);
		if(hero.getItems().size() > 4){
			g.drawString("Your inventory is full. Couldn't pick up.", 50, 440);
		}
		g.drawString("Press spacebar continue.", 50, 420);
		if(continueGame){
			if(hero.getItems().size() > 4){
				cantMove = false;
				inRoom = false;
			}
			else{
				hero.pickUpItem(it);
				it = i.generateItem();
				cantMove = false;
				inRoom = false;
				continueGame = false;
			}
		}
	}
	
	/**This method sets the image of the mini-map depending on the level of the hero.
	 * 
	 */
	public void setMapImage(){
		if(hero.getLevel() == 1){
			try {
				map = ImageIO.read(new File("level1.gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(hero.getLevel() == 2){
			try {
				map = ImageIO.read(new File("level2.gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(hero.getLevel() == 3 || hero.getLevel() - 1 == 3){
			try {
				map = ImageIO.read(new File("level3.gif"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**this method sets the monster's image depending on the monsters name
	 * 
	 * 
	 */
	public void setMonsterImage(){
		if(en.getName().equals("Goblin")){
			try {
				enemy = ImageIO.read(new File("goblin.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(en.getName().equals("Orc")){
			try {
				enemy = ImageIO.read(new File("orc.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(en.getName().equals("Kobold")){
			try {
				enemy = ImageIO.read(new File("kobold.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(en.getName().equals("Giant Rat")){
			try {
				enemy = ImageIO.read(new File("mole.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(en.getName().equals("Gnoll")){
			try {
				enemy = ImageIO.read(new File("gnoll.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(en.getName().equals("Snake")){
			try {
				enemy = ImageIO.read(new File("snake.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(en.getName().equals("Froglok")){
			try {
				enemy = ImageIO.read(new File("froglok.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(en.getName().equals("Troll")){
			try {
				enemy = ImageIO.read(new File("troll.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**allows the panel to be focusable
	 * 
	 */
	public boolean isFocusTraversable(){
		return true;
	}
	
	/** this method moves the hero around and allows it to enter a room. this sets the inRoom to true
	 *  this also sets the selection variable to #1-5 depending on which number key is pressed.
	 * 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int x = (int) hero.getLocation().getX();
		int y = (int) hero.getLocation().getY();
		if(!cantMove){
			if(e.getKeyCode() == KeyEvent.VK_W){
				if(x > 0){
					hero.setLocation(new Point(x-1, y));
					room = level.getRoom(new Point (x-1,y));
					if(room == 'm' || room == 's' || room == 'i' || room == 'f'){
						inRoom = true;
					}
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_A){
				if(y > 0){
					hero.setLocation(new Point(x, y-1));
					room = level.getRoom(new Point (x,y-1));
					if(room == 'm' || room == 's' || room == 'i' || room == 'f'){
						inRoom= true;
					}
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_S){
				if(x < 3){
					hero.setLocation(new Point(x+1, y));
					room = level.getRoom(new Point (x+1,y));
					if(room == 'm' || room == 's' || room == 'i' || room == 'f'){
						inRoom = true;
					}
				}
			}
			else if(e.getKeyCode() == KeyEvent.VK_D){
				if(y < 3){
					hero.setLocation(new Point(x, y+1));
					room = level.getRoom(new Point (x,y+1));
					if(room == 'm' || room == 's' || room == 'i' || room == 'f'){
						inRoom = true;
					}
				}
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_1){
			selection = 1;
			keyPressed = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_2){
			selection = 2;
			keyPressed = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_3){
			selection = 3;
			keyPressed = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_4){
			selection = 4;
			keyPressed = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_5){
			selection = 5;
			keyPressed = true;
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE){
			continueGame = true;
		}
	}
	
	/** this runs the panel and causes it to refresh constantly
	 * 
	 */
	@Override
	public void run() {
		while (true){
			repaint();
			try {
				Thread.sleep(16);
			}catch (InterruptedException e) {}
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}