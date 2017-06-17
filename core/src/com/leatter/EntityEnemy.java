package com.leatter;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class EntityEnemy extends Entity
{
	private BitmapFont font;
	private String bonus;

	public EntityEnemy(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font, String bonus, double d, double e, int size)
	{
		super(batch, shapeRenderer, d, e, size); //invoking the Entity super constructor
		this.bonus = bonus;
		this.font = font;
		this.glyphLayout = new GlyphLayout();
		this.glyphLayout.setText(font, bonus);
	}

	public void batchDraw()
	{
		font.draw(batch, bonus, (float) this.getX() - glyphLayout.width / 2, (float) this.getY() + glyphLayout.height / 2);
	}
	public String getBonus()
	{
		return this.bonus;
	}
	public void moveRandomly(int timer) //see EntityPlayer.handleMovement()
	{
		this.setDeltaX(0);
		this.setDeltaY(0);
		this.setMaxVelo(700 / Math.sqrt(this.getSize() / 10.0) + 1); //larger max velocity because randomy movement tends to result in switching directions a lot, never really reaching max speed, so increase max speed for higher base speed
		
		if (this.isDeccelerating() == true)
		{
			if (this.getSize() <= 60000)
			{
				this.setVeloX(this.getVeloX() * (this.getSize() * (9.0 / 5980000) + (26901.0 / 29900)));
				this.setVeloY(this.getVeloY() * (this.getSize() * (9.0 / 5980000) + (26901.0 / 29900)));
			}
			else
			{
				this.setVeloX(this.getVeloX() * 0.99);
				this.setVeloY(this.getVeloY() * 0.99);
			}
			
			this.setDeltaX(this.getDeltaX() + this.getVeloX());
			this.setDeltaY(this.getDeltaY() + this.getVeloY());
			
			if (((this.getVeloX() < 0 && this.getVeloX() * -1 <= 0.0001) || (this.getVeloX() > 0 && this.getVeloX() <= 0.0001)) && ((this.getVeloY() < 0 && this.getVeloY() * -1 <= 0.0001) || (this.getVeloY() > 0 && this.getVeloY() <= 0.0001)))
			{
				this.setIsDeccelerating(false);
				this.setIsAccelerating(false);
			}
		}
		if (this.isAccelerating() == false)
		{
			this.setVeloX(this.getMaxVelo() * (2 * Math.random() - 1) / 64); //multiplying speed from -1 to 1 for random speed and direction
			this.setVeloY(this.getMaxVelo() * (2 * Math.random() - 1) / 64);
			this.setIsAccelerating(true);
		}
		
		if (timer % (int) (10 * Math.random() + 1) == 0) //every random time (as opposed to constantly)
		{
			if (Math.random() < 0.9) //90% chance of moving
			{
				if (this.getVeloX() < 0)
				{
					if (this.getVeloX() > (-1 * this.getMaxVelo()) * 0.98)
					{
						this.setVeloX(this.getVeloX() + (-1 * this.getMaxVelo()) * (2 * Math.random() - 1) / 20); //multiplying speed from -1 to 1 for random speed and direction
					}
					else
					{
						this.setVeloX((-1 * this.getMaxVelo()) * (2 * Math.random() - 1));
					}
					this.setDeltaX(this.getDeltaX() + this.getVeloX());
				}
				else if (this.getVeloX() > 0)
				{
					if (this.getVeloX() < this.getMaxVelo() * 0.98)
					{
						this.setVeloX(this.getVeloX() + this.getMaxVelo() * (2 * Math.random() - 1) / 20);
					}
					else
					{
						this.setVeloX(this.getMaxVelo() * (2 * Math.random() - 1));
					}
					this.setDeltaX(this.getDeltaX() + this.getVeloX());
				}
				
				if (this.getVeloY() < 0)
				{
					if (this.getVeloY() > (-1 * this.getMaxVelo()) * 0.98)
					{
						this.setVeloY(this.getVeloY() + (-1 * this.getMaxVelo()) * (2 * Math.random() - 1) / 20);
					}
					else
					{
						this.setVeloY((-1 * this.getMaxVelo()) * (2 * Math.random() - 1));
					}
					this.setDeltaY(this.getDeltaY() + this.getVeloY());
				}
				else if (this.getVeloY() > 0)
				{
					if (this.getVeloY() < this.getMaxVelo() * 0.98)
					{
						this.setVeloY(this.getVeloY() + this.getMaxVelo() * (2 * Math.random() - 1) / 20);
					}
					else
					{
						this.setVeloY(this.getMaxVelo() * (2 * Math.random() - 1));
					}
					this.setDeltaY(this.getDeltaY() + this.getVeloY());
				}
			}
			else //10% chance of stopping
			{
				this.setIsDeccelerating(true);
			}
		}
	}
}