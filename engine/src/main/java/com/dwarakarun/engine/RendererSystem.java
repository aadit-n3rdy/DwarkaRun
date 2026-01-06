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

class RenderData {
	public Sprite s;
	public Transform t;
	public String name;

	public RenderData(String name, Sprite s, Transform t) {
		this.s = s;
		this.t = t;
		this.name = name;
	}
}


public class RendererSystem extends GameSystem {
  static int sprite_count;
  private static final Marker marker = MarkerManager.getMarker("RenderSystem");

	public RendererSystem(Engine eng) {
		super(eng);
		deps = new Class[] {WindowSystem.class};
        eng.logger.info(marker,"Constructor done");
	}

	SpriteComponent spriteComponent;
	TransformComponent transformComponent;
	WindowSystem windowSystem;
  AnimatorSystem as;
  SpriteDetails sd = new SpriteDetails();

	@Override
	public void init() {
        eng.logger.debug(marker,"init");

		glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		glLoadIdentity();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);  

		//TODO: Fetch and render AnimatedSprite as well once done
		spriteComponent = eng.getComponent(SpriteComponent.class);
		windowSystem = eng.getSystem(WindowSystem.class);
    as = eng.getSystem(AnimatorSystem.class); 
		transformComponent = eng.getComponent(TransformComponent.class);
        eng.logger.debug(marker,transformComponent);
	}

	protected void render(Sprite sp,int ColSprites, int currSprite, 
			float x, float y) {
    //ColSprites is no of sprites present in column of spritesheet
    //currSprite refers to the current sprite being rendered
		sp.bindTexture();
		glPushMatrix();
		//TODO: Replace with x and y coords
		//glTranslatef(100f, 200, 0);
	    glTranslatef(x, y, 0);

		glBegin(GL_QUADS);

		int width = sp.getWidth();
		int height = sp.getHeight();

    float sprite_tex_width = 1/(float)ColSprites;

    float pt0 = sp.getTexcoord(0, 0);
    float pt1 = sp.getTexcoord(0, 1);
    float pt2 = sp.getTexcoord(1, 0);
    float pt3 = sp.getTexcoord(1, 1);

		glTexCoord2f((sprite_tex_width*currSprite), 1-pt1);
		glVertex2f(0, 0);

		glTexCoord2f((sprite_tex_width*currSprite), 1-pt3);
		glVertex2f(0, height);

		glTexCoord2f((sprite_tex_width*(currSprite+1)), 1-pt3);
		glVertex2f((float) width /ColSprites, height);

		glTexCoord2f((sprite_tex_width*(currSprite+1)), 1-pt1);
		glVertex2f((float) width /ColSprites, 0);

		glEnd();

		glPopMatrix();
	}

	@Override 
	public void update() {
		glPushMatrix();
  
		glScalef(2f/windowSystem.getWidth(), -2f/windowSystem.getHeight(), 1.0f);
		glTranslatef(-windowSystem.getWidth()/2f, -windowSystem.getHeight()/2f, 0);

		LinkedList<RenderData> spriteOrder = new LinkedList<RenderData>();
		Iterator iter = spriteComponent.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry)iter.next();
			String name = (String)entry.getKey();
			Sprite sprite = (Sprite)entry.getValue();
			Transform t = transformComponent.get(name);
			RenderData data = new RenderData(name, sprite, t);
			if (t.getZ() == 0) {
				spriteOrder.addFirst(data);
			} else {
				spriteOrder.addLast(data);
			}
		}
		RenderData cur;
		while (spriteOrder.size() != 0) {
			cur = spriteOrder.removeFirst();
			String name = cur.name;
			Sprite sp = cur.s;
			Transform tr = cur.t;
			try {
				render(sp, sd.getSpriteQuant(name), sd.getCurrSprite(name), tr.getX(), tr.getY());
			} catch (Exception e) {
                eng.logger.error(marker,"Could not render sprite, missing transform component: " + name);
			}
		}
		glPopMatrix();
	}
}
