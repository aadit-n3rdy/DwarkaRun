package com.dwarakarun.engine;

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


public class AnimatorSystem extends GameSystem implements Runnable {
  public AnimatorSystem(Engine eng) {
    super(eng);
    deps = new Class[] {};
    System.out.println("AS Constructor done");
  }

  SpriteDetails sd = new SpriteDetails();
  SpriteComponent sc;

  @Override
  public void init() {
    System.out.println("AS init");
    sc = eng.getComponent(SpriteComponent.class);
  }

  @Override
  public void run() {
	  while (true) {
		  update();
		  try {
			  Thread.sleep(5);
		  } catch(Exception e) {
		  }
	  }
  }

  @Override
  public void update() {
    Iterator iter = sc.iterator();
    while(iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      String name = (String)entry.getKey();
      sd.changeCurrSprite(name);
    }
  }
} 
