package com.leatter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EntityPlayer extends Entity
{
	private BitmapFont font;
	private String bonusLetters;
	private int[] bonusLettersCount;

	public EntityPlayer(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, double d, double e, int size)
	{
		super(batch, shapeRenderer, d, e, size); //invoking the Entity super constructor
		this.font = font;
		this.bonusLetters = "";
		this.bonusLettersCount = new int[26];
		this.glyphLayout = new GlyphLayout();
		this.glyphLayout.setText(font, "You!");
	}

	public void batchDraw()
	{
		font.draw(batch, "You!", (float) this.getX() - glyphLayout.width / 2, (float) this.getY() + glyphLayout.height / 2);
	}
	public void setBonusLetters(String letters)
	{
		if (letters.length() == 1 + this.bonusLetters.length()) //adding only a character
		{
			bonusLettersCount[(int)letters.charAt(letters.length() - 1) - 97]++; //increase that count of characters
		}
		else //resetting the bonus letters
		{
			for (int i = 0; i < bonusLettersCount.length; i++)
			{
				bonusLettersCount[i] = 0; //resetting the counts
			}
		}

		this.bonusLetters = letters;
	}
	public String getBonusLetters()
	{
		return this.bonusLetters;
	}
	public int[] getBonusLettersCount()
	{
		return this.bonusLettersCount;
	}
	public void handleMovement()
	{
		this.setDeltaX(0); //reset displacements
		this.setDeltaY(0);
		this.setMaxVelo(100 / Math.sqrt(this.getSize() / 10.0) + 1); //finding new max velocity

		if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
		{
			this.setIsDeccelerating(true);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
		{
			this.setIsDeccelerating(true);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN))
		{
			this.setIsDeccelerating(true);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
		{
			this.setIsDeccelerating(true);
		}
		
		if (this.isDeccelerating())
		{
			if (this.getSize() <= 60000) //you're not super big
			{
				this.setVeloX(this.getVeloX() * (this.getSize() * (9.0 / 5980000) + (26901.0 / 29900))); //you decelerate slower as you get bigger, from *0.9 to *0.99
				this.setVeloY(this.getVeloY() * (this.getSize() * (9.0 / 5980000) + (26901.0 / 29900)));
			}
			else //you're big
			{
				this.setVeloX(this.getVeloX() * 0.99); //deceleration is constant from here and larger sizes as more deceleration feels silly
				this.setVeloY(this.getVeloY() * 0.99);
			}
			
			//actually moving
			this.setDeltaX(this.getDeltaX() + this.getVeloX());
			this.setDeltaY(this.getDeltaY() + this.getVeloY());
			
			//if your velocity is basically 0
			if (((this.getVeloX() < 0 && this.getVeloX() * -1 <= 0.0001) || (this.getVeloX() > 0 && this.getVeloX() <= 0.0001)) && ((this.getVeloY() < 0 && this.getVeloY() * -1 <= 0.0001) || (this.getVeloY() > 0 && this.getVeloY() <= 0.0001)))
			{
				this.setIsDeccelerating(false); //you're not decelerating (because you're basically not moving)
				this.setIsAccelerating(false); //any movement will require acceleration
			}
		}
		
		if (this.isAccelerating() == false)
		{
			this.setVeloX(this.getMaxVelo() / 1024); //start at a low velocity
			this.setVeloY(this.getMaxVelo() / 1024);
			this.setIsAccelerating(true); //you will be accelerating
		}
		
		if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
		{
			if (this.getVeloX() > this.getMaxVelo() * -0.98) //not max speed
			{
				this.setVeloX(this.getVeloX() + this.getMaxVelo() / -20); //increase speed (accelerate)
			}
			else //at max speed
			{
				this.setVeloX(this.getMaxVelo() * -1); //constant (max) speed
			}
			this.setDeltaX(this.getDeltaX() + this.getVeloX()); //actually move
		}
		if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
		{
			if (this.getVeloX() < this.getMaxVelo() * 0.98)
			{
				this.setVeloX(this.getVeloX() + this.getMaxVelo() / 20);
			}
			else
			{
				this.setVeloX(this.getMaxVelo());
			}
			this.setDeltaX(this.getDeltaX() + this.getVeloX());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
		{
			if (this.getVeloY() > this.getMaxVelo() * -0.98)
			{
				this.setVeloY(this.getVeloY() + this.getMaxVelo() / -20);
			}
			else
			{
				this.setVeloY(this.getMaxVelo() * -1);
			}
			this.setDeltaY(this.getDeltaY() + this.getVeloY());
		}
		if (Gdx.input.isKeyPressed(Input.Keys.UP))
		{
			if (this.getVeloY() < this.getMaxVelo() * 0.98)
			{
				this.setVeloY(this.getVeloY() + this.getMaxVelo() / 20);
			}
			else
			{
				this.setVeloY(this.getMaxVelo());
			}
			this.setDeltaY(this.getDeltaY() + this.getVeloY());
		}
	}
}