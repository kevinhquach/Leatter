package com.leatter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class GameScreen implements Screen
{
	private OrthographicCamera cam;

	private EntityPlayer player;
	private ArrayList<EntityEnemy> enemyEntities;
	private boolean started = false;
	private int timer = 0;
	private String bonus = "";
	private int bonusCount[] = new int[26];
	private String bonusMessage = "";
	private List<String> lines = null;
	private LeatterGame game;

	public GameScreen(LeatterGame game)
	{
		this.game = game;
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.shapeRenderer = new ShapeRenderer();
		game.font = new BitmapFont();
		game.font.setColor(Color.BLACK);

		player = new EntityPlayer(game.batch, game.shapeRenderer, game.font, 5000, 5000, 200); //making the player at the center with a size of 200
		enemyEntities = new ArrayList<EntityEnemy>();
	}

	@Override
	public void render(float delta) //happens every frame, acts as a loop
	{
		//background colour
		Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (started == false) //first launch
		{
			try
			{
				lines = Files.readAllLines(Paths.get("words.txt"), StandardCharsets.UTF_8); //making an array of all bonus words
			} catch (IOException e){
				// file couldn't be opened
			}
			generateEnemies();
			started = true;
		}
		
		if (enemyEntities.size() == 0) //no enemies left, you win
		{
			game.setScreen(new WinScreen(game));
			dispose();
		}
		
		if (timer % 1000 == 0) //every ~25 seconds, a new bonus word is created and the collected letters are erased
		{
			for (int i = 0; i < bonusCount.length; i++)
			{
				bonusCount[i] = 0; //resetting the counts of the letters the bonus word had
			}
			bonusMessage = "Get the following word for a size bonus: ";
			bonus = lines.get((int) (lines.size() * Math.random())); //getting a random bonus word
			for (int i = 0; i < bonus.length(); i++)
			{
				bonusCount[(int) bonus.charAt(i) - 97]++; //adding the counts of the letters the bonus word has
			}
			player.setBonusLetters(""); //erasing collected letters
		}
		timer++;
		
		handleInput();
		for (int i = 0; i < enemyEntities.size(); i++) //randomly move each enemy
		{
			enemyEntities.get(i).moveRandomly(timer);
		}
		checkBoundaries();
		handleEating();
		handlePlayerEating();
		handleBonusLetters();
		
		//camera, player is always centred
		cam.position.x = (float) player.getX();
		cam.position.y = (float) player.getY();
		cam.update();

		game.batch.setProjectionMatrix(cam.combined);
		game.shapeRenderer.setProjectionMatrix(cam.combined);

		//showing boundaries with lines
		game.shapeRenderer.begin(ShapeType.Filled);
			game.shapeRenderer.setColor(Color.DARK_GRAY);
			game.shapeRenderer.rectLine(0, 0, 10000, 0, 2);
			game.shapeRenderer.rectLine(0, 10000, 10000, 10000, 2);
			game.shapeRenderer.rectLine(0, 0, 0, 10000, 2);
			game.shapeRenderer.rectLine(10000, 0, 10000, 10000, 2);
			game.shapeRenderer.end();
		
		//outer square background
		game.shapeRenderer.begin(ShapeType.Filled);
			game.shapeRenderer.setColor(0.855f, 0.855f, 0.855f, 1);
			game.shapeRenderer.rect(0, 0, 10000, 10000);
			game.shapeRenderer.end();
		
		//inner square background
		game.shapeRenderer.begin(ShapeType.Filled);
			game.shapeRenderer.setColor(0.922f, 0.922f, 0.922f, 1);
			game.shapeRenderer.rect(2500, 2500, 5000, 5000);
			game.shapeRenderer.end();
		
		//grid lines, spaced 500 units apart from each other
		game.shapeRenderer.begin(ShapeType.Filled);
			game.shapeRenderer.setColor(0.812f, 0.812f, 0.812f, 1);
			for (int i = 500; i < 10000; i += 500)
			{
				game.shapeRenderer.rectLine(0, i, 10000, i, 2);
				game.shapeRenderer.rectLine(i, 0, i, 10000, 2);
			}
			game.shapeRenderer.end();
		
		//entities
		game.shapeRenderer.begin(ShapeType.Filled);
			player.rendererDraw();
			for (EntityEnemy enemyEntity : enemyEntities)
			{
				enemyEntity.rendererDraw();
			}
			game.shapeRenderer.end();

		//rendering text
		game.cameraBatch.begin();
			game.font.draw(game.cameraBatch, bonusMessage + bonus, Gdx.graphics.getHeight() / 2, Gdx.graphics.getHeight() - 10); //bonus message
			game.font.draw(game.cameraBatch, "Collected letters: " + player.getBonusLetters(), 10, Gdx.graphics.getHeight() - 10);
			for (int i = 0; i < 26; i++)
			{
				game.font.draw(game.cameraBatch, String.valueOf((char) (i + 97)) + ": " + String.valueOf(player.getBonusLettersCount()[0]), 10, Gdx.graphics.getHeight() - (25 + i * 15));
			}
			game.font.draw(game.cameraBatch, "Enemies left: " + String.valueOf(enemyEntities.size()), 10, 57);
			game.font.draw(game.cameraBatch, "Size: " + String.valueOf(player.getSize()), 10, 42);
			game.font.draw(game.cameraBatch, "Position: (" + String.valueOf((int) player.getX()) + ", " + String.valueOf((int) player.getY()) + ")", 10, 27);
			game.cameraBatch.end();

		//batching
		game.batch.begin();
			player.batchDraw();
			for (EntityEnemy enemyEntity : enemyEntities)
			{
				enemyEntity.batchDraw();
			}
		game.batch.end();
		
		//moving entities
		player.translate(player.getDeltaX(), player.getDeltaY());
		for (int i = 0; i < enemyEntities.size(); i++)
		{
			enemyEntities.get(i).translate(enemyEntities.get(i).getDeltaX(), enemyEntities.get(i).getDeltaY());
		}
	}

	public void handleBonusLetters()
	{
		//boolean that checks if collected letters satisfies bonus word
		boolean[] giveBonus = new boolean[bonus.length()];

		//for each letter of the bonus word, check if there are enough collected bonus letters are equal to it
		for (int i = 0; i < bonus.length(); i++)
		{
			if (bonus.equals(" ") == false)
			{
				if (player.getBonusLettersCount()[bonus.charAt(i) - 97] >= bonusCount[bonus.charAt(i) - 97] && bonusCount[bonus.charAt(i) - 97] != 0)
				{
					giveBonus[i] = true;
				}
			}
		}
		
		//all letters of the bonus word are collected
		if (booleanAreAllTrue(giveBonus))
		{
			for (int i = 0; i < giveBonus.length; i++) //resetting bonus check
			{
				giveBonus[i] = false;
			}
			for (int i = 0; i < bonusCount.length; i++) //resetting bonus letter counts
			{
				bonusCount[i] = 0;
			}
			player.setSize((int) (player.getSize() * 1.5));
			player.setBonusLetters("");
			bonusMessage = "1.5x size multiplier!";
			bonus = " "; //probably not the best way, but prevents multiplying as no ' ' can be collected
		}
	}

	public boolean booleanAreAllTrue(boolean[] boolArray)
	{
		for (int i = 0; i < boolArray.length; i++)
		{
			if (boolArray[i] == false)
			{
				return false;
			}
		}
		return true;
	}

	public void handleEating()
	{
		int maxCount = enemyEntities.size();

		//for every enemy player, check if an enemy player is overlapping it (completely)
		for (int i = 0; i < maxCount; i++)
		{
			for (int j = 0; j < maxCount; j++)
			{
				if (enemyEntities.get(i).isOverlapping(enemyEntities.get(j)))
				{
					if (enemyEntities.get(i).getSize() > enemyEntities.get(j).getSize()) //an entity is larger than another
					{
						enemyEntities.get(i).setSize(enemyEntities.get(i).getSize() + enemyEntities.get(j).getSize()); //adding the two sizes
						enemyEntities.remove(j); //removing the smaller entity
						maxCount--; //there is 1 less entity now
					}
					else if (enemyEntities.get(j).getSize() > enemyEntities.get(i).getSize()) //same as above but reverse entities checked
					{
						enemyEntities.get(j).setSize(enemyEntities.get(j).getSize() + enemyEntities.get(i).getSize());
						enemyEntities.remove(i);
						maxCount--;
					}
				}
			}
		}
	}
	
	public void checkBoundaries()
	{
		for (int i = 0; i < enemyEntities.size(); i++)
		{
			enemyEntities.get(i).checkBoundaries();
		}
		player.checkBoundaries();
	}

	public void handlePlayerEating()
	{
		int maxCount = enemyEntities.size();
		
		for (int i = 0; i < maxCount; i++)
		{
			if (player.isOverlapping(enemyEntities.get(i))) //checking if you are overlapping something or if something is overlapping you
			{
				if (player.getSize() > enemyEntities.get(i).getSize()) //you're bigger
				{
					player.setSize(player.getSize() + enemyEntities.get(i).getSize());
					player.setBonusLetters(player.getBonusLetters() + enemyEntities.get(i).getBonus()); //get the bonus letter
					enemyEntities.remove(i);
					maxCount--;
				}
				else if (enemyEntities.get(i).getSize() > player.getSize()) //entity is larger than you, you get eaten and lose
				{
					enemyEntities.get(i).setSize(enemyEntities.get(i).getSize() + player.getSize());
					game.setScreen(new GameOverScreen(game));
					dispose();
				}
			}
		}
	}

	public void generateEnemies()
	{
		//generate 2000 cells anywhere in a 10000 by 10000 area with a random letter and an area between 100 and 2500
		for (int i = 0; i < 2000; i++)
		{
			enemyEntities.add(new EntityEnemy(game.batch, game.shapeRenderer, game.font, String.valueOf((char) ((int) 26 * Math.random() + 97)), 10000 * Math.random(),10000 * Math.random(), (int) (2500 * Math.random() + 100)));
		}
	}

	private void handleInput()
	{
		//zooming
		if (Gdx.input.isKeyPressed(Input.Keys.A) && cam.zoom < 4) //maximum zoom level of 4x
		{
			cam.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Z) && cam.zoom > 1) //minimum zoom level of 1x
		{
			cam.zoom -= 0.02;
		}
		player.handleMovement();
	}

	@Override
	public void show(){
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}
}
