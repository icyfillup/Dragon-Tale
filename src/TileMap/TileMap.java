package TileMap;

import java.awt.*;
import java.awt.image.*;
import java.io.*;

import javax.imageio.ImageIO;

import Main.GamePanel;

public class TileMap 
{
//	position
	private double x;
	private double y;
	
//	bound
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
//	map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
//	tileset
	private BufferedImage tileset;
	private int numTileAcross;
	private Tile[][] tiles;
	
//	drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }
	
	public TileMap(int tileSize)
	{
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
		
//		System.out.println("GamePanel.HEIGHT: " + GamePanel.HEIGHT);
//		System.out.println("GamePanel.WIDTH: " + GamePanel.WIDTH);
//		System.out.println();
//		
//		System.out.println("numRowsToDraw: " + numRowsToDraw);
//		System.out.println("numColsToDraw: " + numColsToDraw);
//		System.out.println();
//		
//		System.out.println("this.x: " + this.x);
//		System.out.println("this.y: " + this.y);
//		System.out.println();
	}
	
	public void loadTiles (String s)
	{
		try 
		{
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTileAcross = tileset.getWidth() / tileSize;
			
//			System.out.println("tileset.getWidth(): " + tileset.getWidth());
//			System.out.println("tileSize:" + tileSize);
//			System.out.println("numTileAcross: " + numTileAcross);
//			System.out.println();
			
			tiles = new Tile[2][numTileAcross];
			BufferedImage subimage;
			for(int col = 0; col < numTileAcross; col++)
			{
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s)
	{
		try 
		{
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());

//			System.out.println("numCols: " + numCols);
//			System.out.println("numRows: " + numRows);
//			System.out.println();
			
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
//			System.out.println("width: " + width);
//			System.out.println("height: " + height);
//			System.out.println();
			
			String delims = "\\s+";
			
			for(int row = 0; row < numRows; row++)
			{
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++)
				{
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public int getTileSize() { return tileSize; }
	public int getX() { return (int)x; }
	public int getY() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height;}
	
	public int getType(int row, int col) 
	{
		int rc = map[row][col];
		int r = rc / numTileAcross;
		int c = rc % numTileAcross;
		
		return tiles[r][c].getType();
	}
	
	public void setPosition(double x, double y)
	{
		
//		System.out.println("Before setPosition():");
//		System.out.println("this.x: " + this.x);
//		System.out.println("this.y: " + this.y);
//		System.out.println();
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
//		System.out.println("After setPosition():");
//		System.out.println("this.x: " + this.x);
//		System.out.println("this.y: " + this.y);
//		System.out.println();
		
		fixBounds();
		
		colOffset = (int) -this.x / tileSize;
		rowOffset = (int) -this.y / tileSize;
	
//		System.out.println("colOffset: " + colOffset);
//		System.out.println("rowOffset: " + rowOffset);
//		System.out.println();
	}
	
	private void fixBounds()
	{
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;	
		
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g) 
	{
//		System.out.println("--------------------------------------------");
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++)
		{
			if(row >= numRows) break;
			for(int col = colOffset; col < colOffset + numColsToDraw; col++)
			{
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTileAcross;
				int c = rc % numTileAcross;
				
//				System.out.println("rc: " + rc);
//				System.out.println("numTileAcross: " + numTileAcross);
//				System.out.println("r: " + r);
//				System.out.println("c: " + c);
//				System.out.println();
				
				g.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);
			}
		}
		
//		for(int i = 0; i < 12; i++)
//		{
//			g.drawLine(i * 30, 0, i * 30, GamePanel.HEIGHT);
//		}
//		
//		for(int i = 0; i < 10; i++)
//		{
//			g.drawLine(0, i * 30, GamePanel.WIDTH, i * 30);
//		}
//		System.out.println("--------------------------------------------");
//		System.out.println();
	}
	
	
	
}
