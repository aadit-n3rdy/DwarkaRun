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

public abstract class InputHandlerSystem extends GameSystem {
    private static final Logger logger = LogManager.getLogger("ENGINE");
    private static final Marker marker = MarkerManager.getMarker("KeySystem");

    public InputHandlerSystem(Engine engine) {
        super(engine);
        logger.info(marker, "Constructor Done");
    }

    public abstract void handleKeyInput(int key, int scancode,int action, int mods);

    public abstract float getXMove();
    public abstract  float getYMove();

}
