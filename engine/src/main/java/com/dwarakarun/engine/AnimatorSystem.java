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


public class AnimatorSystem extends GameSystem implements Runnable {
    private static final Marker marker = MarkerManager.getMarker("AnimatorSystem");
  public AnimatorSystem(Engine eng) {
    super(eng);
    deps = new Class[] {};
    logger.info(marker,"Constructor done");
  }

  SpriteDetails sd = new SpriteDetails();
  SpriteComponent sc;
  TransformComponent tc;
  KeySystem ks = new KeySystem();

  @Override
  public void init() {
    logger.info(marker,"init");
    sc = eng.getComponent(SpriteComponent.class);
    tc = eng.getComponent(TransformComponent.class);
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

  int focus=1; //focus = 1 means background in focus; 
               //0 means background_dup in focus
  @Override
  public void update() {
    Iterator iter = sc.iterator();
	float speed = 1.0f;
    while(iter.hasNext()) {
      Map.Entry entry = (Map.Entry)iter.next();
      String name = (String)entry.getKey();
      Sprite sprite = (Sprite)entry.getValue();
      Transform t = tc.get(name);
      if(name=="shinobiSprite") {
        changePos(ks.getXMove(name),ks.getYMove(name),t);
//        if(t.getX()>=612f || true) {
//          System.out.println("back:"+tc.get("background_for").getX());
//          System.out.println("back_dup:"+tc.get("background_for_dup").getX());
//          changePos(-2f,0f,t);
          if(tc.get("background_for").getX()>-1986f || tc.get("background_for_dup").getX()>-1986f) {
            changePos(-speed,0f,tc.get("background_for"));
            changePos(-speed,0f,tc.get("background_for_dup"));
          } else if(focus == 1) {
            changePos(-speed,0f,tc.get("background_for"));
          }else {
            changePos(-speed,0f,tc.get("background_for_dup"));
          }
//        }
      }
      //-1980
      if(name=="background_for" && (t.getX()<=-786f && t.getX()>=-800f)) {
//        System.out.println("Looping second background");
        tc.get("background_for_dup").setX(1100f);
        focus = 0;
      }
      if(name=="background_for_dup" && (t.getX()<=-786f && t.getX()>=-800f)) {
//        System.out.println("Looping first background");
        tc.get("background_for").setX(1100f);
        focus = 1;
      }

      sd.changeCurrSprite(name);
    }
  }

  public void changePos(float x,float y,Transform t) {
    t.setX(t.getX()+x);
    t.setY(t.getY()+y);
    //System.out.println("X:"+t.getX());
    //System.out.println("Y:"+t.getY());
  }
} 
