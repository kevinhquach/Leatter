package com.leatter;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Entity
{
	SpriteBatch batch;
	GlyphLayout glyphLayout;
	ShapeRenderer shapeRenderer;

	private Color color;
	private double posX;
	private double posY;
	private double deltaX;
	private double deltaY;
	private double veloX;
	private double veloY;
	private double maxVelo;
	private boolean isAccelerating;
	private boolean isDeccelerating;
	private int size;
	private float radius;

	public Entity(SpriteBatch batch, ShapeRenderer shapeRenderer, double d, double e, int size)
	{
		this.batch = batch;
		this.shapeRenderer = shapeRenderer;
		this.posX = d;
		this.posY = e;
		this.isAccelerating = false;
		this.size = size;
		this.radius = (float) Math.sqrt(this.size/Math.PI);
		this.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
	}

	public void rendererDraw()
	{
		shapeRenderer.setColor(color);
		shapeRenderer.circle((float) posX, (float) posY, radius);
	}
	public void translate(double offsetX, double offsetY)
	{
		posX += offsetX;
		posY += offsetY;
	}
	public double getX()
	{
		return this.posX;
	}
	public double getY()
	{
		return this.posY;
	}
	public void setX(double x)
	{
		posX = x;
	}
	public void setY(double y)
	{
		posY = y;
	}
	public void setDeltaX(double deltaX)
	{
		this.deltaX = deltaX;
	}
	public void setDeltaY(double deltaY)
	{
		this.deltaY = deltaY;
	}
	public double getDeltaX()
	{
		return this.deltaX;
	}
	public double getDeltaY()
	{
		return this.deltaY;
	}
	public void setVeloX(double veloX)
	{
		this.veloX = veloX;
	}
	public void setVeloY(double veloY)
	{
		this.veloY = veloY;
	}
	public double getVeloX()
	{
		return this.veloX;
	}
	public double getVeloY()
	{
		return this.veloY;
	}
	public void setMaxVelo(double maxVelo)
	{
		this.maxVelo = maxVelo;
	}
	public double getMaxVelo()
	{
		return this.maxVelo;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int m)
	{
		this.size = m;
		this.radius = (float) Math.sqrt(this.size/Math.PI);
	}
	public float getRadius()
	{
		return radius;
	}
	public boolean isAccelerating()
	{
		return this.isAccelerating;
	}
	public void setIsAccelerating(boolean acceling)
	{
		this.isAccelerating = acceling;
	}
	public boolean isDeccelerating()
	{
		return this.isDeccelerating;
	}
	public void setIsDeccelerating(boolean Decceling)
	{
		this.isDeccelerating = Decceling;
	}
	public boolean isOverlapping (Entity entity)
	{
		double distance = Math.sqrt((this.posX - entity.posX) * (this.posX - entity.posX) + (this.posY - entity.posY) * (this.posY - entity.posY));
	    if (distance <= ((this.radius) + (entity.radius)))
	    {
	    	return true;
	    }
	    else
	    {
	        return false;
	    }
	}
	public void checkBoundaries()
	{
		//without accounting for radius, you're restricted to the centre of your entity as opposed to the outside of it
		if (this.posX - this.radius < 0)
		{
			this.posX = this.radius;
		}
		else if (this.posX + this.radius > 10000)
		{
			this.posX = 10000 - this.radius;
		}
		
		if (this.posY - this.radius < 0)
		{
			this.posY = this.radius;
		}
		else if (this.posY + this.radius > 10000)
		{
			this.posY = 10000 - this.radius;
		}
	}
}