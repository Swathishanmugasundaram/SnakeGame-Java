package snake;

import javax.swing.*;//here javax x is for extended,swing is a java library

public class SnakeGame extends JFrame{
//	 Class constructor
	 SnakeGame(){
		 super("Snake Game");//title of the game
		 add(new Board());//object for Board class
		 pack();
		 
		 
		 setLocationRelativeTo(null);//here  left-x:300,top -y:300
		 setResizable(false);
		 
		 
	 }

	public static void main(String[] args) {
//	      SnakeGame snakegame= new SnakeGame();// without using constructor
		new SnakeGame().setVisible(true); // for visibile frame like linux window;//class object creating with with constructor
	}

}
