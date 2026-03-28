package snake;

//import java.awt.Color;
//import java.awt.Graphics;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Board extends JPanel implements ActionListener{// JPanel is a component  2. here they using interface (implements)
	private Image apple;
	private Image dot;
	private Image head;
	 
//	Variables
	private final int B_WIDTH = 300;
	private final int B_HEIGHT = 300;

	private final int ALL_DOTS=900;
    private final int DOT_SIZE=10;
    private final int RANDOM_POSITION=29;
    
    ///score
    private int score = 0;
    /// level system
    private int level = 1;
    private int delay =140;
    private int highScore = 0;
    //difficulty
     private int difficulty = 2;
    //wall/obstacles
     private final int MAX_OBSTACLES=10;
     private int obstacles=0;
     
     private int obstacleX[]=new int[MAX_OBSTACLES];
     private int obstacleY[]=new int[MAX_OBSTACLES];//these arrays stores the position of obstacles
     
    
    
    private int apple_x;
    private int apple_y;
	
private final int x[] = new int[ALL_DOTS];
private final int y[] = new int[ALL_DOTS];


private boolean paused = false;

private boolean leftDirection = false;
private boolean rightDirection = true;
private boolean upDirection = false;
private boolean downDirection = false;

private boolean inGame = true;
private boolean inMenu = true;

//variable for mouth anime
private boolean mouthOpen=false;
	
	private int dots;
	private Timer timer;
	
	
	

	
	////Add sound method
	private void playSound(String soundFile) {
		try {
			AudioInputStream audioInputStream=AudioSystem.getAudioInputStream(getClass().getResource("/"+soundFile));
			Clip clip =AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}catch(Exception e) {
			System.out.println("Sound error:"+ soundFile);
			e.printStackTrace();
		}
	}
	
	//difficulty method
	private void applyDifficulty() {
		switch(difficulty) {
		case 1:delay = 180; break; //easy
		case 2:delay = 140; break;//medium
		case 3:delay = 100; break;//Hard
		
		}
	
	}
	
	// obstacles in hard mode
	private void createObstacles() {
		for(int i = 0;i < MAX_OBSTACLES; i++) {
			
			boolean invalidPosition;
			do {
				invalidPosition=false;
				
			int r=(int)(Math.random() * RANDOM_POSITION);
			obstacleX[i]=r* DOT_SIZE;
			
			r=(int)(Math.random() * RANDOM_POSITION);
			obstacleY[i]=r*DOT_SIZE;
			
			for(int j=0;j<dots;j++) {
				if(obstacleX[i] == x[j] && obstacleY[i] == y[j]) {
					invalidPosition=true;
					break;
				}
			}
			if(obstacleX[i] == apple_x && obstacleY[i] == apple_y) {
				invalidPosition =true;
			} 
			}
			while(invalidPosition);
		}
		obstacles=MAX_OBSTACLES;
		}
	
	
	
//constructor
	Board(){
		addKeyListener(new TAdapter()); //actionListener's function
		
		
	   setBackground(Color.BLACK);//BG color
	   setPreferredSize(new Dimension(300,300));
	   setFocusable(true);
	
	loadImages();
	initGame();//function calling
	}
	
	public void loadImages() {
		ImageIcon i1= new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
		apple = i1.getImage();
		ImageIcon i2= new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
		dot=i2.getImage();
		ImageIcon i3= new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
	    head = i3.getImage();
	}
	
//function
public void initGame() {
	dots= 3;
	
	for(int i=0;i < dots;i++) {
		y[i] = 50;
     	x[i] = 50 - i* DOT_SIZE ; 
	}
	
	locateApple();
	
	applyDifficulty();
	
	if(difficulty == 3) {
		createObstacles();
	}
	timer = new Timer(delay,this);// here 140 millisec is delay
	//stop game from starting immediately
	if(!inMenu) {
	timer.start();
	}
	if(inMenu) {
		playSound("sounds/Menu1.wav");
	}
	
}  

//restart game ...1.restart method

public void restartGame() {
	score = 0;
	level = 1;
	delay = 140;
	dots = 3;
	obstacles =0;
	
	leftDirection = false;
	rightDirection = true;
	upDirection = false;
	downDirection = false;
	inMenu = false;
	inGame = true;
	paused = false;
	
	if(timer !=null) {
	timer.stop();
	}
	initGame();
}


// apple should not spawn on snake
public void locateApple() {
	
//int r=(int)(Math.random()*RANDOM_POSITION);
//apple_x = r * DOT_SIZE;
//r=(int)(Math.random()*RANDOM_POSITION);
//apple_y = r * DOT_SIZE;
	boolean onSnake;
	
	do {
		onSnake = false;
		
		 int r=(int)(Math.random()*RANDOM_POSITION);
		 apple_x = r * DOT_SIZE;
		  r = (int)(Math.random() * RANDOM_POSITION);
		  apple_y = r * DOT_SIZE;
		  
		  for(int i=0;i<dots;i++) {
			if(x[i] == apple_x && y[i] == apple_y) {
				onSnake = true;
				break;
			}
		  }
	           
	} while(onSnake);
}
 
//method

public void paintComponent(Graphics g) {
	super.paintComponent(g);
	
	draw(g);
}
public void draw(Graphics g) {
	
	if(inMenu) {
	g.setColor(Color.GREEN);
	g.setFont(new Font("Arial",Font.BOLD,22));
	g.drawString("SNAKE GAME",70,90);
	
	g.setFont(new Font("Arial",Font.PLAIN,14));
	g.drawString("Select Difficulty", 70, 130);
	g.drawString("1 - Easy", 90, 155);
	g.drawString("2 - Medium", 90, 175);
	g.drawString("3 - Hard", 90, 195);
	g.drawString("press ENTER to start", 60, 230);
	
	}
	else if(inGame) {
		
		// draw grid
		g.setColor(new Color(40,40,40));
		
		for(int i=0;i<300;i +=DOT_SIZE) {
			g.drawLine(i,0,i, 300); //vertical lines
			g.drawLine(0, i,300 , i); //horizontal line
			g.setColor(new Color(30,30,30));
		}
		
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString("Score : " + score, 10, 20);
		g.drawString("Level : " + level, 200, 20);
		g.drawString("High Score : " + highScore, 80, 35);

	   g.drawImage(apple, apple_x,apple_y,this);
	
	   g.setColor(Color.RED);
	   
	   for(int i =0;i<obstacles;i++) {
		   g.fillRect(obstacleX[i], obstacleY[i],DOT_SIZE,DOT_SIZE);//Draw obstacles
	   }
	for(int i = 0;i <dots;i++ ) {
		if(i==0) {
			g.drawImage(head,x[i],y[i],this);
			
			
			
			//snake eyes
			g.setColor(Color.WHITE);
			if(rightDirection) {
				g.fillOval(x[i]+6, y[i]+2, 3, 3);
				g.fillOval(x[i]+6, y[i]+6, 3, 3);
				}
			if(leftDirection) {
				g.fillOval(x[i]+1, y[i]+2, 3, 3);
				g.fillOval(x[i]+1, y[i]+6, 3, 3);
				}
			if(upDirection) {
				g.fillOval(x[i]+2, y[i]+1, 3, 3);
				g.fillOval(x[i]+6, y[i]+1, 3, 3);
				}
			if(downDirection) {
				g.fillOval(x[i]+2, y[i]+6, 3, 3);
				g.fillOval(x[i]+6, y[i]+6, 3, 3);
				}
			
			//mouth ANIME
			if(mouthOpen) {
				g.setColor(Color.BLUE);
				
				if(rightDirection) {
					g.fillRect(x[i] +9, y[i]+4, 2, 2);
				}
				if(leftDirection) {
					g.fillRect(x[i] -1, y[i]+4, 2, 2);
				}
				if(upDirection) {
					g.fillRect(x[i] +4, y[i]-1, 2, 2);
				}
				if(downDirection) {
					g.fillRect(x[i] +4, y[i]+9, 2, 2);
				}
				mouthOpen=false;
			}
		}else {
			g.drawImage(dot,x[i],y[i],this);
		}
	}
	
	if(paused) {
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial",Font.BOLD,20));
		g.drawString("PAUSED", 110, 150);
		
		g.setFont(new Font("Arial",Font.PLAIN,14));
		g.drawString("press SPACE to Resume", 70, 180);
	}
	Toolkit.getDefaultToolkit().sync();
	}else {
		gameOver(g);
	}
}

public void gameOver(Graphics g) {
	String msg = "Game Over!";
	String scoreMsg = "Score :" + score;
	String levelMsg = "Level : " +level;
	
	Font font = new Font("Arial",Font.BOLD,16);
	FontMetrics metrics = getFontMetrics(font);
	
	
	g.setColor(Color.WHITE);
	g.setFont(font);
	
	g.drawString(msg, (300 -  metrics.stringWidth(msg))/2,120);// length calculations
	g.drawString(scoreMsg, (300 - metrics.stringWidth(scoreMsg)) / 2, 150);
	g.drawString(levelMsg, (300 - metrics.stringWidth(levelMsg))/2, 180);
	g.drawString("Press R to Restart", (300 - metrics.stringWidth("Press R to Restart"))/2, 210);
}

public void move() {
     for(int i= dots;i>0;i--) {
    	x[i] = x[i-1];
    	y[i] = y[i-1];
    	
     }
     if(leftDirection) { // left side : minus -
    	 x[0] = x[0] - DOT_SIZE;
    	 
     }
     if(rightDirection) { // right side : plus +
    	 x[0] = x[0] + DOT_SIZE;
     }
     if(upDirection) {// up : minus
    	y[0] = y[0] - DOT_SIZE; 
     }
     if(downDirection) {// down : plus
    	 y[0] = y[0] + DOT_SIZE;
     }
    
}

public void checkApple() {
	if((x[0] == apple_x) && (y[0] == apple_y)) {
		dots++;
		score += 10;
		mouthOpen=true;
		playSound("sounds/eat.wav");
		
		if(score % 50 == 0) {
			level++;
			delay -=10;
			timer.setDelay(delay);
		}
		locateApple();
	}
	
	if(score > highScore) {
	 highScore=score;	
	}
}

public void checkCollision() {
	for(int i = dots;i>0;i--) {
		if((i>4) && (x[0] == x[i]) && (y[0] == y[i])) {
			inGame =  false;
		}
	}
	if(y[0] >= 300) {
		inGame = false;
	}
	if(x[0] >= 300) {
		inGame = false;
	}
	if(y[0] < 0) {
		inGame = false;
	}
	if(x[0] < 0) {
		inGame = false;
	}
	//collision with obstacles
	for(int i =0;i<obstacles;i++) {
		if(x[0] == obstacleX[i] && y[0]==obstacleY[i]) {
			inGame = false;
		}
	}
	if(!inGame) {
		playSound("sounds/gameover.wav");
		timer.stop();
	}
}
public void actionPerformed(ActionEvent ae) {
	if(!inMenu && inGame && !paused) {
	checkApple();
	checkCollision();
    move();	
	}
    repaint();
}

//inner class

public class TAdapter extends KeyAdapter{
	@Override
	public void keyPressed(KeyEvent e) {
		
		int key = e.getKeyCode();
		
		if(inMenu) {
			if(key == KeyEvent.VK_1) {
				difficulty = 1;
				playSound("sounds/Menu1.wav");
			}
			if(key == KeyEvent.VK_2) {
				difficulty = 2;
				playSound("sounds/Menu1.wav");
			}
			if(key == KeyEvent.VK_3) {
				difficulty = 3;
				playSound("sounds/Menu1.wav");
			}
		}
		
		if(key == KeyEvent.VK_ENTER && inMenu) {
			playSound("sounds/Menu1.wav");
			inMenu = false;
			inGame = true;
			initGame();
			return;
		}
		if(key == KeyEvent.VK_SPACE) {
			paused = !paused;
		}
		
		if(key == KeyEvent.VK_LEFT && (!rightDirection) ) {
			leftDirection = true;
			upDirection = false;
			downDirection = false;
		}
		
		if(key == KeyEvent.VK_RIGHT && (!leftDirection) ) {
			rightDirection = true;
			upDirection = false;
			downDirection = false;
		}
		
		if(key == KeyEvent.VK_UP && (!downDirection) ) {
			upDirection = true;
			leftDirection = false;
			rightDirection = false;
		}
		
		if(key == KeyEvent.VK_DOWN && (!upDirection) ) {
			downDirection = true;
		    leftDirection = false;
			rightDirection = false;
		}
	
	if(!inGame && key == KeyEvent.VK_R) {
		restartGame();
		
	}
	
	}
}
}
