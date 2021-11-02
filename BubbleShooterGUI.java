import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.event.*;
import java.awt.event.MouseEvent;
//import java.awt.image.BufferedImage;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.util.*;
import java.io.*;
public class BubbleShooterGUI extends JPanel implements ActionListener{
	//private static BufferedImage bubbleCannon;
	private static int gbHeight;
	private static int gbWidth;
	private static int rows;
	private static int columns;
	private static int screenWidth;
	private static int screenHeight;
	private static int dividerLoc;
	private static int bubbleSize;
	private static int shootingBallX;
	private static int shootingBallY;
	private static double imageAngleRad = 0;
	//private static JSplitPane top;
	private static JPanel gameBoard;
	private static JButton resetButton;
	private static Map<String, Color> colorMap;
	private static HashMap<Point, Point> positionMap;
	private static BubbleManager bm;
	private static Dimension screenSize;
	private static boolean ballMoving;
	private static boolean mouseDrag;
	private static Timer ballShotTimer;
	private static Point shotOrigin;
	private static Point shooterTarget;
	private static Line2D line = null;
	

	public BubbleShooterGUI(){
		super(new BorderLayout());
		rows = 7;
		columns = 7;
		bm = new BubbleManager(rows, columns, 8);
		screenSize = new Dimension(Toolkit.getDefaultToolkit().getScreenSize());
		screenWidth = (int) Math.round((screenSize.getWidth() * 0.5));
		screenHeight = (int) Math.round((screenSize.getHeight() * 0.8));
		System.out.println("Program was updated ");
		gbHeight = (int) Math.round((screenHeight * 0.85)-100);
		gbWidth = (int) Math.round(screenWidth * 0.6);
		bubbleSize = (int) Math.round((Math.min(gbWidth/(columns), gbHeight/rows)) * 0.6);
		colorMap = new HashMap<>(bm.getColorMap());
		positionMap = new HashMap<>();
		gameBoard = new GameBoard();
		shotOrigin = new Point(shootingBallX, shootingBallY);
		line = new Line2D.Double(100, 100, 200, 200);
		ballShotTimer = new Timer(10, this);
		ballShotTimer.setInitialDelay(0);

		MouseInputAdapter listener = new MouseInputAdapter(){
			@Override
			public void mousePressed(MouseEvent e){
				System.out.println("Mouse was pressed");
				ballMoving = true;
				mouseDrag = false;
            	shooterTarget = e.getPoint();
            	repaint();
        	}
        	@Override
 			public void mouseDragged(MouseEvent e){
 				System.out.println("Mouse was dragged");
 				shooterTarget = e.getPoint();
 				ballMoving = false;
 				//mouseDrag = true;
 				//Point start = new Point(shootingBallX, shootingBallY);
 				//Point end = e.getPoint();
 				if(line != null){
 					line.setLine(shootingBallX, shootingBallY, e.getX(), e.getY());
 					repaint();
 				}
 				//repaint();
 			}
 			@Override
 			public void mouseMoved(MouseEvent e) {}
 			@Override
        	public void mouseReleased(MouseEvent e){
        		
        		if(mouseDrag){
        			System.out.println("Mouse was released");
					ballShotTimer.setInitialDelay(0);
            		ballShotTimer.start();
            		mouseDrag = false;
            		ballMoving = true;
            		//setVisible(true);
            	}
            	repaint();
			}
		};
		gameBoard.addMouseListener(listener);
		gameBoard.addMouseMotionListener(listener);
		JPanel buttonPanel = createButtonPanel();
		resetButton.addActionListener(new java.awt.event.ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e){
				bm = new BubbleManager(rows, columns, 8-1);
				//gameBoard = new GameBoard();
				
			}
		});
		JSplitPane top = new JSplitPane(JSplitPane.VERTICAL_SPLIT, gameBoard, buttonPanel);
		dividerLoc = (int) Math.round(screenHeight * 0.90);
		top.setResizeWeight(1.0);
		top.setDividerLocation(dividerLoc);
		top.setOneTouchExpandable(true);
		top.setContinuousLayout(true);
		add(top, BorderLayout.CENTER);
		
	}
	
	public void actionPerformed(ActionEvent evt){
		if(!ballMoving){ballMoving = true; repaint();}
	}
				//}
	
	
	private class GameBoard extends JPanel{
		private int x, y;
		private int xDir, yDir;
		public GameBoard(){
			//JPanel gameBoard = new JPanel();
			shootingBallX = (int) Math.round((((bubbleSize+5)*rows/2) - (bubbleSize/2)));
			shootingBallY = (int) Math.round((((gbHeight+100)+5) - (bubbleSize+(5*rows)/2)));
			positionMap = new HashMap<>();
			
			setPreferredSize(new Dimension(gbWidth, gbHeight+100));
			setBackground(Color.WHITE);
			//setOpaque(true);
		}
	
		
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
			System.out.println("paintComponent called");
			final String cSplit = "(?<=.)";
			int xPos = 0;
			int yPos = 0;
			x = shootingBallX;
			y = shootingBallY;
			xDir = (bubbleSize/2);
			yDir = bubbleSize;
			Scanner boardScanner = new Scanner(bm.toString()).useDelimiter(cSplit);
			String thisColor;
			gameBoard.setVisible(true);
			g.setColor(Color.BLACK);
            g.fillOval(shootingBallX, shootingBallY, bubbleSize, bubbleSize);
            g.setColor(Color.BLACK);
			g2d.drawOval(shootingBallX, shootingBallY, bubbleSize, bubbleSize);
				//ballShotTimer.stop();
				while(ballMoving){
					//System.out.println("BALL WAS SHOT");
					if(x + xDir < 0 ||
               			x + xDir + bubbleSize > gbWidth) {
                		ballMoving = false;
            		}
            		if(y + yDir < 0 ||
               			y + yDir + bubbleSize > gbHeight) {
                		ballMoving = false;
            		}
            		x += xDir;
           			y += yDir;
            		g.setColor(Color.BLACK);
           			g.fillOval(x, y, bubbleSize, bubbleSize);
           			g.setColor(Color.BLACK);
					g2d.drawOval(x, y, bubbleSize, bubbleSize);
           			
            	}

            	
			if(mouseDrag){
				while(mouseDrag){
					System.out.println("Stuck in DRAG");
					
            		g2d.setPaint(Color.BLUE);
            		
            		if(line != null){g2d.draw(line);}
				}
			} else {
			for(int r = 0; r < rows; r++){
				xPos = 0;
				for(int c = 0; c < columns; c++){
					thisColor = boardScanner.next();
					if(boardScanner.hasNext() && yPos < gbHeight){
						//System.out.println("Board Generation attempted");
						g.setColor(colorMap.get(thisColor));
            			g.fillOval(xPos, yPos, bubbleSize, bubbleSize);
            			g.setColor(Color.WHITE);
						g2d.drawOval(xPos, yPos, bubbleSize, bubbleSize);
						positionMap.put(new Point(xPos, yPos), new Point(r, c));
						xPos += (bubbleSize+5);
					}
				}
				if(boardScanner.hasNext()){yPos += (int) Math.round(bubbleSize+5);}
			}
				
			}	
		}

				
	}
	
 		
 		
	public static JPanel createButtonPanel(){
		JPanel buttPanel = new JPanel();
		int bpWidth = (int) Math.round(screenWidth);
		int bpHeight = (int) Math.round(screenHeight * 0.9);
		buttPanel.setBackground(Color.RED);
		buttPanel.setPreferredSize(new Dimension(bpWidth, bpHeight));
		//buttPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		resetButton = new JButton("Reset Game");
		
		JButton extraButton = new JButton("Extra Button");
		extraButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e){
				System.out.println("Extra Button clicked");
			}
		});
		buttPanel.add(resetButton, Component.LEFT_ALIGNMENT);
		buttPanel.add(extraButton, Component.RIGHT_ALIGNMENT);
		return buttPanel;
	}

	public static void generateGUI(){
		final JFrame mainDisp = new JFrame("Bubble Shooter V.0.1");
		mainDisp.setPreferredSize(new Dimension(screenWidth, screenHeight));
		mainDisp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BubbleShooterGUI thisGUI = new BubbleShooterGUI();
		thisGUI.setOpaque(true);
		mainDisp.setMinimumSize(new Dimension(screenWidth, screenHeight));
		mainDisp.add(thisGUI);
		mainDisp.setContentPane(thisGUI);
		mainDisp.pack();
		mainDisp.setResizable(true);
		mainDisp.setVisible(true);
		
	}

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){
			public void run(){
				generateGUI();
			}
		});
	}

	}


