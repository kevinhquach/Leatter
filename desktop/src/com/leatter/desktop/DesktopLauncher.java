package com.leatter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.leatter.LeatterGame;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Leatter";
		config.width = 1280;
		config.height = 720;
		config.resizable = false;
		new LwjglApplication(new LeatterGame(), config);
	}
}