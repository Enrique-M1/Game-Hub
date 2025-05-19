package application;
import javafx.scene.shape.*;

public class Controller {
	public static final int MOVE = gameHub.MOVE ; 
	public static final int SIZE = gameHub.SIZE ;
	public static int XMAX = gameHub.XMAX ; 
	public static int YMAX = gameHub.YMAX ;  
	public static int [][] GRID = gameHub.GRID ; 
	
	public static void MoveR(Piece piece) {
		if(piece.a.getX() + MOVE <= XMAX - SIZE && piece.b.getX() + MOVE<= XMAX - SIZE &&
				piece.c.getX() + MOVE <= XMAX - SIZE && piece.d.getX() + MOVE<= XMAX - SIZE) {
			int movea = GRID[((int)piece.a.getX()/SIZE + 1)][((int)piece.a.getY()/SIZE)] ; 		
			int moveb = GRID[((int)piece.b.getX()/SIZE + 1)][((int)piece.b.getY()/SIZE)] ; 	
			int movec = GRID[((int)piece.c.getX()/SIZE + 1)][((int)piece.c.getY()/SIZE)] ; 	
			int moved = GRID[((int)piece.d.getX()/SIZE + 1)][((int)piece.d.getY()/SIZE)] ; 	
			if(movea == 0 && movea == moveb && moveb == movec && movec == moved) {
				piece.a.setX(piece.a.getX() + MOVE) ; 
				piece.b.setX(piece.b.getX() + MOVE) ; 
				piece.c.setX(piece.c.getX() + MOVE) ; 
				piece.d.setX(piece.d.getX() + MOVE) ; 
				
			}
		}
	}	
	public static void MoveL(Piece piece) {
		if(piece.a.getX() + MOVE >= 0 && piece.b.getX() + MOVE >= 0 &&
				piece.c.getX() + MOVE >= 0 && piece.d.getX() + MOVE >= 0) {
			int movea = GRID[((int)piece.a.getX()/SIZE - 1)][((int)piece.a.getY()/SIZE)] ; 		
			int moveb = GRID[((int)piece.b.getX()/SIZE - 1)][((int)piece.b.getY()/SIZE)] ; 	
			int movec = GRID[((int)piece.c.getX()/SIZE - 1)][((int)piece.c.getY()/SIZE)] ; 	
			int moved = GRID[((int)piece.d.getX()/SIZE - 1)][((int)piece.d.getY()/SIZE)] ; 	
			if(movea == 0 && movea == moveb && moveb == movec && movec == moved) {
				piece.a.setX(piece.a.getX() - MOVE) ; 
				piece.b.setX(piece.b.getX() - MOVE) ; 
				piece.c.setX(piece.c.getX() - MOVE) ; 
				piece.d.setX(piece.d.getX() - MOVE) ; 
				
			}
		}
	}	
	
	public static Piece makeRect() {
		int obj = (int)(Math.random()*100) ; 
		String name ; 
		Rectangle a = new Rectangle(SIZE - 1, SIZE - 1) ; 
		Rectangle b = new Rectangle(SIZE - 1, SIZE - 1) ; 
		Rectangle c = new Rectangle(SIZE - 1, SIZE - 1) ; 
		Rectangle d = new Rectangle(SIZE - 1, SIZE - 1) ; 
		
		if (obj < 15) { 
			a.setX(XMAX / 2 - SIZE);
			b.setX(XMAX / 2 - SIZE);
			b.setY(SIZE);
			c.setX(XMAX / 2);
			c.setY(SIZE);
			d.setX(XMAX / 2 + SIZE);
			d.setY(SIZE);
			name = "j";
		} else if (obj < 30) { 
			a.setX(XMAX / 2 + SIZE);
			b.setX(XMAX / 2 - SIZE);
			b.setY(SIZE);
			c.setX(XMAX / 2);
			c.setY(SIZE);
			d.setX(XMAX / 2 + SIZE);
			d.setY(SIZE);
			name = "l";
		} else if (obj < 45) { 
			a.setX(XMAX / 2 - SIZE);
			b.setX(XMAX / 2);
			c.setX(XMAX / 2 - SIZE);
			c.setY(SIZE);
			d.setX(XMAX / 2);
			d.setY(SIZE);
			name = "o";
		} else if (obj < 60) { 
			a.setX(XMAX / 2 + SIZE);
			b.setX(XMAX / 2);
			c.setX(XMAX / 2);
			c.setY(SIZE);
			d.setX(XMAX / 2 - SIZE);
			d.setY(SIZE);
			name = "s";
		} else if (obj < 75) { 
			a.setX(XMAX / 2 - SIZE);
			b.setX(XMAX / 2);
			c.setX(XMAX / 2);
			c.setY(SIZE);
			d.setX(XMAX / 2 + SIZE);
			name = "t";
		} else if (obj < 90) { 
			a.setX(XMAX / 2 + SIZE);
			b.setX(XMAX / 2);
			c.setX(XMAX / 2 + SIZE);
			c.setY(SIZE);
			d.setX(XMAX / 2 + SIZE + SIZE);
			d.setY(SIZE);
			name = "z";
		} else { 
			a.setX(XMAX / 2 - SIZE - SIZE);
			b.setX(XMAX / 2 - SIZE);
			c.setX(XMAX / 2);
			d.setX(XMAX / 2 + SIZE);
			name = "i";
		}
		return new Piece(a,b,c,d,name);
	}
}