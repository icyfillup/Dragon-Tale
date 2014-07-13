package GameState;


import Main.GamePanel;
import TileMap.*;
import Entity.*;
import Entity.Enemies.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level1State extends GameState
{
	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	private HUD hud;

	public Level1State(GameStateManager gsm)
	{
		this.gsm = gsm;
		init();
	}

	public void init() 
	{
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(1);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 0.1);
		
		player = new Player(tileMap);
		player.setPosition(100,100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
	}

	private void populateEnemies()
	{
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		
		Point[] points = new Point[] {
										new Point(200, 100), 
										new Point(860, 200), 
										new Point(1525, 200),
										new Point(1680, 200),
										new Point(1800, 200)};
		
		for(int i = 0; i < points.length; i++)
		{
			s = new Slugger(tileMap);
		s.setPosition(points[i].x, points[i].y);
		enemies.add(s);
		}
		
	}
	
	public void update()
	{
//		update player
		player.update();
//		Screen follow player
		tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety());
	
//		set Background
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
//		player enemies
		player.checkAttack(enemies);
		
//		update all enemies
		for(int i = 0; i < enemies.size(); i++) 
		{
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead())
			{
				enemies.remove(i);
				i--; 
				explosions.add(new Explosion(e.getx(), e.gety()));
			}
		}
//		update explosion
		for(int i = 0; i < explosions.size(); i++)
		{
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) 
			{
				explosions.remove(i);
				i--;
			}
		}
		
	}

	public void draw(Graphics2D g)
	{
//		draw bg
		bg.draw(g);
		
//		draw tilemap
		tileMap.draw(g);
		
//		draw player
		player.draw(g);
		
//		draw all enemies
		for(int i = 0; i < enemies.size(); i++) 
		{
			enemies.get(i).draw(g);
		}
//		draw explosion
		for(int i = 0; i < explosions.size(); i++)
		{

			explosions.get(i).setMapPosition((int)tileMap.getX(), (int)tileMap.getY());
			explosions.get(i).draw(g);
		}
		
//		draw HUD
		hud.draw(g);
	}

	public void keyPressed(int k)
	{
		if(k == KeyEvent.VK_LEFT) { player.setLeft(true); }
		if(k == KeyEvent.VK_RIGHT) { player.setRight(true); }
		if(k == KeyEvent.VK_UP) { player.setUp(true); }
		if(k == KeyEvent.VK_DOWN) { player.setDown(true); }
		if(k == KeyEvent.VK_W) { player.setJumping(true); }
		if(k == KeyEvent.VK_E) { player.setGliding(true); }
		if(k == KeyEvent.VK_R) { player.setScratch(); }
		if(k == KeyEvent.VK_F) { player.setFiring(); }
		
		if(k == KeyEvent.VK_C) { showLevel1StateConfig(); }
	}

	public void keyReleased(int k)
	{
		if(k == KeyEvent.VK_LEFT) { player.setLeft(false); }
		if(k == KeyEvent.VK_RIGHT) { player.setRight(false); }
		if(k == KeyEvent.VK_UP) { player.setUp(false); }
		if(k == KeyEvent.VK_DOWN) { player.setDown(false); }
		if(k == KeyEvent.VK_W) { player.setJumping(false); }
		if(k == KeyEvent.VK_E) { player.setGliding(false); }
	}

	public void showLevel1StateConfig()
	{
		tileMap.showTileMapConfig();
		
		System.out.println("----------------------showLevel1StateConfig()----------------------");
		System.out.println();
		
		System.out.println("tileMap.setPosition(GamePanel.WIDTH / 2 - player.getx(), GamePanel.HEIGHT / 2 - player.gety()): ");
		System.out.println("GamePanel.WIDTH / 2 - player.getx(): " + (GamePanel.WIDTH / 2 - player.getx()));
		System.out.println("GamePanel.HEIGHT / 2 - player.gety(): " + (GamePanel.HEIGHT / 2 - player.gety()));
		System.out.println();
		
		player.setMapObjectConfig(true);
	}
	
}
