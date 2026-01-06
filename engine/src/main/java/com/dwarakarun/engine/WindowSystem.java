package com.dwarakarun.engine;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.nio.file.*;
import java.util.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;


public class WindowSystem extends GameSystem {
	private long handle;
	private int width;
	private int height;
	Texture texture;
  AnimatorSystem as;
    private static final Marker marker = MarkerManager.getMarker("WindowSystem");
	private void handleErrors() {
		int err = glGetError();
		while (err != GL_NO_ERROR) {
            logger.error(marker,"OpenGl error : {}",err);
			err = glGetError();
		}
	}

	public WindowSystem(Engine eng) {
		super(eng);
		deps = new Class[] {};
		logger.info(marker,"Constructor done");
	}

	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

	@Override
	public void init() {
		logger.info(marker,"init");
		if (!glfwInit()) {
			throw new IllegalStateException("Unable to init GLFW");
		}
		glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

		width = 1200;
		height = 720;

		handle = glfwCreateWindow(width, height, "Dwaraka Run", NULL, NULL);
		if (handle == NULL) {
			logger.info(marker,"NULL window");
		}

    KeySystem ks = new KeySystem();
    as = eng.getSystem(AnimatorSystem.class);
    glfwSetKeyCallback(handle, (handle, key, scancode, action, mods) -> {
      if(key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE ) {
        glfwSetWindowShouldClose(handle, true);
      } else {
        if( (action == GLFW_REPEAT || action == GLFW_PRESS) && (key == 65 || key == 68 || key == 87 || key == 83))
          ks.moveSprite(key,2f);

        if(action == GLFW_RELEASE && (key == 65 || key == 68 || key == 87 || key == 83)) {
          ks.moveSprite(key,0f);
        }
      }
    });

		glfwMakeContextCurrent(handle);
		glfwSwapInterval(1);
		glfwShowWindow(handle);

		GL.createCapabilities();

}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}

	public void swapBuffers() {
		glfwSwapBuffers(handle);
	}

	public void pollEvents() {
		glfwPollEvents();
	}

	@Override
	public void end() {
		glfwFreeCallbacks(handle);
		glfwDestroyWindow(handle);
		glfwTerminate();
	}

	@Override
	public void update() {
		if (glfwWindowShouldClose(handle)) {
			eng.end();
		}
	}
}
