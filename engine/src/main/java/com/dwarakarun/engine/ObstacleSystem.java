package com.dwarakarun.engine;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

public class ObstacleSystem extends GameSystem {
	ColliderComponent cc;
	TransformComponent tc;
	Transform obst;
	ColliderSystem cs;
	Random rand;
    private static final Marker marker = MarkerManager.getMarker("ObstacleSystem");
	public ObstacleSystem(Engine eng) {
		super(eng);
		this.deps = new Class[] { ColliderSystem.class };
		rand = new Random();
	}

	@Override
	public void init() {
		this.cc = eng.getComponent(ColliderComponent.class);
		cs = eng.getSystem(ColliderSystem.class);
		tc = eng.getComponent(TransformComponent.class);
	}

	@Override
	public void update() {
		Iterator<Collision> iter = cs.getCollisions();
		Collision coll;
		obst = tc.get("obstacle");
		while (iter.hasNext()) {
			coll = iter.next();
			if (coll.contains("shinobiSprite")) {
				logger.info(marker,"Game over");
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					logger.error(marker,"Interrupted");
				}
				eng.end();
			}
		}

		Transform ob_t;
		float speed = 1.1f;
		for (int i = 0; i < 3; i++) {
			ob_t = tc.get("obstacle" + i);
			ob_t.setX(ob_t.getX() - speed);
			if (ob_t.getX() < -100) {
				ob_t.setX(1300);
				ob_t.setY(600 - rand.nextInt() % 400);
			}
		}

	}
}
