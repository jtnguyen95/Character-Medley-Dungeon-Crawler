import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.Scanner;

/**This class generates a level that is traversable by the hero
 * 
 * @author jonathannguyen
 *
 */
public class Level implements Serializable {
	/**level is used as the character grid representation of the map
	 * 
	 */
	private char level[][];
	
	/**this creates a character grid which represents the map
	 * t
	 */
	public Level(){
		level = new char[4][4];
	}
	
	/**this generates the level by calling in a number parameter and accessing the corresponding txt file
	 * 
	 * @param levelNum is the level number
	 */
	public void generateLevel(int levelNum){
		try{
			Scanner reader = new Scanner(new File("Level" + levelNum + ".txt"));
			do{
				for(int ii = 0; ii < 4; ii++){
					for(int jj = 0; jj < 4; jj++){
						level[ii][jj] = reader.next().charAt(0);
					}
				}
			}while(reader.hasNext());
			reader.close();
		}catch(FileNotFoundException fnf){
			System.out.println("File not found.");
		}
	}
	
	/**this method gets the room that the hero is in
	 * 
	 * @param p is the point being compared
	 * @return
	 */
	char getRoom(Point p){
		int xCoord = (int) p.getX();
		int yCoord = (int) p.getY();
		if((p.getX() > -1 && p.getX() < 4) && (p.getY() > -1 && p.getY() < 4)){
			return level[xCoord][yCoord];
		}
		else{
			return 'N';
		}
	}
	
	/**
	 * this method displays the map to the panel
	 * 
	 * @param p is the point where the hero is
	 * @param g is the graphics component so that this method can draw to the screen
	 */
	public void displayMap(Point p, Graphics g){
		for(int x = 0; x < 4; x++){
			for(int y = 0; y < 4; y++){
				if(p.getX() == x && p.getY() == y){
					g.drawString("* ", 650+ (y*30), 50 + (x*30));
				}
				else{
					g.drawString(level[x][y] + " ", 650 + (y*30), 50 + (x*30));
				}
			}
		}
	}
	
	/** this finds the start location of the map and sets the point to it and returns the point
	 * 
	 * @return p the starting location point
	 */
	public Point findStartLocation(){
		Point p = new Point();
		for (int ii = 0; ii < 4; ii++){
			for(int jj = 0; jj < 4; jj++){
				if( level[ii][jj] == 's'){
					p.setLocation(ii, jj);
				}
			}
		}
		return p;
	}
}
