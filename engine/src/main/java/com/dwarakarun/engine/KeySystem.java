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

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class KeySystem {
  Transform ts;
  
  public KeySystem() {
    System.out.println("Keysystem Constructor Done");
  }

  static float pos[] = {0f,0f};
  static float back[] = {0f, 0f};
  void moveSprite(int keycode,float move_speed) {
    //65 = a
    //87 = w
    //83 = s
    //68 = d
    if(keycode == 65) {
//      System.out.println("Move speed"+move_speed);
      pos[0] = -move_speed;
    } else if(keycode == 87) {
      pos[1]= -move_speed;
    } else if(keycode == 83) {
      pos[1] = move_speed;
    } else if(keycode == 68) {
      pos[0] = move_speed;
    }
  }

  void moveBackground() {
    back[0] = -2f;
  }

  float getXMove(String name) {
    if(name=="shinobiSprite") {
      return pos[0];
    } else {
      return back[0];
    }
  }

  float getYMove(String name) {
    if(name=="shinobiSprite") {
      return pos[1];
    } else {
      return back[1];
    }
  }

  void noMove() {
    pos[0] = 0;
    pos[1] = 0;
  }

  void noMoveBack() {
    back[0] = 0;
    back[1] = 0;
  }
}
