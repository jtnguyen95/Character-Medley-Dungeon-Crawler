import javax.swing.JFrame;
/**This class creates a frame for a panel object. this is the game window
 * 
 * @author jonathan nguyen 012210040
 *
 */
public class Frame extends JFrame{
	/**panel is the panel of the window
	 * 
	 */
	private Panel panel;
	
	/**constructs the window frame for the panel 
	 * 
	 */
	public Frame(){
		setBounds(0, 0, 800, 650);
		panel = new Panel();
		getContentPane().add(panel);
		Thread t = new Thread(panel);
		t.start();
	}
	/**this is the main function that creates the frame 
	 * 
	 * @param args command line 
	 */
	public static void main(String [] args){
		Frame f = new Frame();
		f.setTitle("Dungeon Master");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
