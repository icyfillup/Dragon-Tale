package Entity;

import java.awt.Color;
import java.awt.Rectangle;

import Main.GamePanel;
import TileMap.TileMap;
import TileMap.Tile;

public abstract class MapObject
{
//	tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
//	position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	
//	dimensions
	protected int width;
	protected int height;
	
//	collision box
	protected int cwidth;
	protected int cheight;
	
//	collision
	protected int currRow;
	protected int currCol;
	
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
//	animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
//	movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	
//	moverment Attributed
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
//	constructor
	public MapObject(TileMap tm)
	{
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public boolean intersects(MapObject o)
	{
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		
		return r1.intersects(r2);
	}
	
	public Rectangle getRectangle()
	{
		return new Rectangle((int)x - cwidth, (int)y - cheight, cwidth, cheight);
	}
	
	public void calculateCorners(double x, double y)
	{
		int leftTile = (int) (x - cwidth / 2) / tileSize;
		int rightTile = (int) (x + cwidth / 2 - 1) / tileSize;
		int topTile = (int) (y - cheight / 2) / tileSize;
		int bottomTile = (int) (y + cheight / 2 - 1) / tileSize;
		
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
	                leftTile < 0 || rightTile >= tileMap.getNumCols())
		{
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
	    }
		
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
		
	}
	
	public void checkTileMapCollision()
	{
//		Collision cases happens with the dx/dx first then the corners cases.
//		concentrate on the dx/dx  first.
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
	
		calculateCorners(x, ydest);
		
		if(dy < 0) 
		{
			if(topLeft || topRight)
			{
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else
			{
				ytemp += dy;
			}
		}
		if(dy > 0)
		{
			if(bottomLeft || bottomRight)
			{
//				System.out.println("touched ground");
//				System.out.println();
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
				
			}
			else
			{
				ytemp += dy;
			}
		}
//		System.out.println("ytemp: " + ytemp);
//		System.out.println("dy: " + dy);
//		System.out.println();
		
		calculateCorners(xdest, y);
		
		if(dx < 0)
		{
			if(topLeft || bottomLeft)
			{
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else 
			{
				xtemp += dx;
			}
		}
		if(dx > 0)
		{
			if(topRight || bottomRight)
			{
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else
			{
				xtemp += dx;
			}
		}
//		System.out.println("xtemp: " + xtemp);
//		System.out.println("dx: " + dx);
//		System.out.println();
		
		if(!falling)
		{
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight)
			{
				falling = true;
			}
		}
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	
	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy)
	{
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition()
	{
		xmap = tileMap.getX();
		ymap = tileMap.getY();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
//#######################################################################	
	
	boolean config;
	public void setMapObjectConfig(boolean b) { config = b; }
	public void showMapObjectConfig() 
	{
		System.out.println("----------------------showMapObjectConfig()----------------------");
		System.out.println();
		
		System.out.println("topLeft: " + topLeft);
		System.out.println("topRight: " + topRight);
		System.out.println("bottomLeft: " + bottomLeft);
		System.out.println("bottomRight: " + bottomRight);
		System.out.println();
		
		System.out.println("xmap: " + xmap);
		System.out.println("ymap: " + ymap);
		System.out.println();
		
		System.out.println("Position of x in level: " + x);
		System.out.println("Position of y in level: " + y);
		System.out.println("dx: " + dx);
		System.out.println("dy: " + dy);
		System.out.println();
		
		System.out.println("width: " + width);
		System.out.println("height: " + height);
		System.out.println("cwidth: " + cwidth);
		System.out.println("cheight: " + cheight);
		System.out.println();
		
		System.out.println("currRow: " + currRow);
		System.out.println("currCol: " + currCol);
		System.out.println();
		
		System.out.println("xdest: " + xdest);
		System.out.println("ydest: " + ydest);
		System.out.println("xtemp: " + xtemp);
		System.out.println("ytemp: " + ytemp);
		System.out.println();
		
		System.out.println("currentAction: " + currentAction);
		System.out.println("previousAction: " + previousAction);
		System.out.println("facingRight: " + facingRight);
		System.out.println();
		
		System.out.println("moveSpeed: " + moveSpeed);
		System.out.println("maxSpeed: " + maxSpeed);
		System.out.println("stopSpeed: " + stopSpeed);
		System.out.println("fallSpeed: " + fallSpeed);
		System.out.println("maxFallSpeed: " + maxFallSpeed);
		System.out.println("jumpStart: " + jumpStart);
		System.out.println("stopJumpSpeed: " + stopJumpSpeed);
		System.out.println();

		int leftTile = (int) ((x - cwidth / 2) / tileSize);
		System.out.println("leftTile: " + leftTile);
		System.out.println("(x - cwidth / 2) / tileSize): (" + x + " - " + (cwidth / 2) + ") / " + tileSize);
		
		int rightTile = (int) ((x + cwidth / 2 - 1) / tileSize);
		System.out.println("rightTile: " + rightTile);
		System.out.println("(x + cwidth / 2 - 1)  / tileSize): (" + x + " + " + (cwidth / 2 - 1) + ") / " + tileSize);
		
		int topTile = (int) ((y - cheight / 2) / tileSize);
		System.out.println("topTile: " + topTile);
		System.out.println("(y - cheight / 2) / tileSize): (" + y + " - " + (cheight / 2) + ") / " + tileSize);
		
		int bottomTile = (int) ((y + cheight / 2 - 1) / tileSize);
		System.out.println("bottomTile: " + bottomTile);
		System.out.println("(y + cheight / 2 - 1) / tileSize): (" + y + " + " + (cheight / 2 - 1) + ") / " + tileSize);
		System.out.println();
		
		System.out.println("Left Collision: " + (currCol * tileSize + cwidth / 2));
		System.out.println("(topLeft || bottomLeft) xtemp = currCol * tileSize + cwidth / 2: " + currCol + " * " + tileSize + " + " + (cwidth / 2));
		System.out.println("Right Collision: " + ((currCol + 1) * tileSize - cwidth / 2));
		System.out.println("(topRight || bottomRight) xtemp = (currCol + 1) * tileSize - cwidth / 2: " + (currCol + 1) + " * " + tileSize + " - " + (cwidth / 2));
		System.out.println("Top Collision: " + (currRow * tileSize + cheight / 2));
		System.out.println("(topLeft || topRight) ytemp = currRow * tileSize + cheight / 2: " + currRow + " * " + tileSize + " + " + ( cheight / 2));
		System.out.println("Bottom Collision: " + ((currRow + 1) * tileSize - cheight / 2));
		System.out.println("(bottomLeft || bottomRight) ytemp = (currRow + 1) * tileSize - cheight / 2: " + (currRow + 1) + " * " + tileSize + " - " + (cheight / 2));
		System.out.println();
		
		
	}
	
//#######################################################################	
	
	public boolean notOnScreen()
	{
		return	x + xmap + width < 0 || 
				x + xmap - width > GamePanel.WIDTH || 
				y + ymap + height < 0 ||
				y + ymap - height > GamePanel.HEIGHT;
	}	

	public void draw(java.awt.Graphics2D g)
	{
		g.setColor(Color.GREEN);
		if(facingRight) 
		{
			g.drawImage
					(	animation.getImage(), 
						(int) (x + xmap - width / 2), 
						(int) (y + ymap - height / 2), 
						null
					);
			g.fillOval((int)(x + xmap - width / 2), (int)(y + ymap - height / 2), 3, 3);
			
		}
		else
		{
			g.drawImage
					(	animation.getImage(),
						(int) (x + xmap - width / 2 + width),
						(int) (y + ymap - height / 2),
						-width,
						height,
						null
					);
			g.fillOval((int)(x + xmap - width / 2 + width), (int)(y + ymap - height / 2), 3, 3);
		}
		
		g.setColor(Color.red);
		
//		corners of the collision detection
		g.fillOval((int)x - 10 + (int)xmap, (int)y - 10 + (int)ymap, 3, 3);
		g.fillOval((int)x - 10 + (int)xmap, (int)y + 9 + (int)ymap, 3, 3);
		g.fillOval((int)x + 9 + (int)xmap, (int)y - 10 + (int)ymap, 3, 3);
		g.fillOval((int)x + 9 + (int)xmap, (int)y + 9 + (int)ymap, 3, 3);
		
//		center of the MapObject's coor
		g.fillOval((int)x + (int)xmap, (int)y + (int)ymap, 3, 3);
	}
}






