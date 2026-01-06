package com.dwarakarun.engine;

import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.*;
import java.util.*;

public class ColliderSystem extends GameSystem {
	protected List<Collision> collisions;
	protected TransformComponent tc;
	protected ColliderComponent cc;
    private static final Marker marker = MarkerManager.getMarker("ColliderSystem");

	public ColliderSystem(Engine eng) {
		super(eng);
		this.eng = eng;
		this.deps = new Class[] {};
		collisions = new ArrayList<Collision>();
        logger.info(marker,"Constructor done");
	}

	@Override
	public void init() {
		tc = eng.getComponent(TransformComponent.class);
		cc = eng.getComponent(ColliderComponent.class);
	}

	private boolean isOverlapping(Transform t1, Collider c1, Transform t2, Collider c2) {
		float x1 = t1.getX();
		float y1 = t1.getY();
		float w1 = c1.getWidth();
		float h1 = c1.getHeight();

		float x2 = t2.getX();
		float y2 = t2.getY();
		float w2 = c2.getWidth();
		float h2 = c2.getHeight();

		if ((x1 <= x2 && x2 <= x1 + w1) || (x2 <= x1 && x1 <= x2 + w2)) {
			if ((y1 <= y2 && y2 <= y1 + h1) || (y2 <= y1 && y1 <= y2 + h2)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void update() {
		collisions.clear();

		Iterator<Map.Entry<String, Collider>> it0 = cc.iterator();
		while (it0.hasNext()) {
			Iterator <Map.Entry<String, Collider>> it1 = cc.iterator();
			Map.Entry<String, Collider> i = it0.next();
			while (it1.hasNext() && !it1.next().getKey().equals(i.getKey())) {}
			while (it1.hasNext()) {
				Map.Entry<String, Collider> j = it1.next();
				if (i.getKey().equals(j.getKey())) {
					continue;
				}
				Transform t1, t2;
				String n1 = i.getKey(), n2 = j.getKey();
				Collider c1 = i.getValue();
				Collider c2 = j.getValue();
				t1 = tc.get(i.getKey());
				t2 = tc.get(j.getKey());
				if (isOverlapping(t1, c1, t2, c2)) {
					collisions.add(new Collision(n1, n2));
                    logger.debug(marker,"Collision: " + n1 + " " + n2);
				}
			}
		}
	}

	public Iterator<Collision> getCollisions() {
		return collisions.iterator();
	}
}
