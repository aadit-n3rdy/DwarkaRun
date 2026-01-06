package com.dwarakarun.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.nio.file.*;
import java.util.*;
import java.lang.System.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.stb.STBImage.*;

public class SpriteDetails {
    private static final Logger logger = LogManager.getLogger("ENGINE");
    private static final Marker marker = MarkerManager.getMarker("SpriteDetails");
  public SpriteDetails() {
    logger.info(marker,"Constructor Done");
  }
  
  //no_of_sprites/curr_sprite/sprite_time/time_since_curr_sprite_last_changed
  public static long shinobiSprite[] = {8,0,35,System.currentTimeMillis()};
  public static long vampSprite[] = {6,0,55,System.currentTimeMillis()};


  //no of sprites in spritesheet
  public int getSpriteQuant(String name) {
    if(name=="shinobiSprite")
      return (int)shinobiSprite[0];
    else if(name=="vampSprite")
      return (int)vampSprite[0];
    return 1;
  }

  //current sprite count
  public int getCurrSprite(String name) {
    if(name=="shinobiSprite") {
      return (int)shinobiSprite[1];
    } else if(name=="vampSprite") {
      return (int)vampSprite[1];
    }
    return 1;
  }

  //change current sprite count
  public int changeCurrSprite(String name) {
    if(System.currentTimeMillis()-getLastTime(name)>getSpriteTimeInterval(name)) {
      changeLastTime(name);
      if(name=="shinobiSprite") {
          shinobiSprite[1]++;
          shinobiSprite[1] %= (shinobiSprite[0]+1);
          return (int)shinobiSprite[1];
        } else if(name=="vampSprite") {
          vampSprite[1]++;
          vampSprite[1] %= (vampSprite[0]+1);
          return (int)vampSprite[1];
        }
    }
    return 1;
}

//sprite time Interval
  public int getSpriteTimeInterval(String name) {
    if(name=="shinobiSprite")
      return (int)shinobiSprite[2];
    else if(name=="vampSprite")
      return (int)vampSprite[2];
    return 1;
  }

  //get last activated time of sprite and change it
  public long getLastTime(String name) {
    if(name=="shinobiSprite")
      return shinobiSprite[3];
    else if(name=="vampSprite")
      return vampSprite[3];
    return 1;
  }

  //change last activated sprite time
  public long changeLastTime(String name) {
    if(name=="shinobiSprite") {
      shinobiSprite[3] = System.currentTimeMillis();
      return shinobiSprite[3];
    }
    else if(name=="vampSprite") {
      vampSprite[3] = System.currentTimeMillis();
      return vampSprite[3];
    }
    return 1;
  }
}
