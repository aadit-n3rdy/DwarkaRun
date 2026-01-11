package com.dwarakarun.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.util.*;

public class Engine {
	private static int instanceCount = 0;

	private Deque<Class> depsort;

	private Boolean shouldEnd;

	private HashMap<Class, GameSystem> systems;
	private HashMap<Class, Component> components;
	private WindowSystem ws;
    // logger
    public static final Logger logger = LogManager.getLogger("ENGINE");
    private static final Marker marker = MarkerManager.getMarker("Engine");

	public <T extends GameSystem> T getSystem(Class<T> cl) {
		GameSystem sys = systems.get(cl);
		return (T)sys;
	}

	public <T extends GameSystem> void addSystem(T sys) {
		String name = sys.getClass().getCanonicalName();
		logger.debug(marker,"Registering system " + name);
        // to handle inheritance
        if (GameSystem.class == sys.getClass().getSuperclass())
            systems.put(sys.getClass(), sys);
        else
            systems.put(sys.getClass().getSuperclass(), sys);
	}

	public <T> void addComponent (Component<T> c) {
		String name = c.getClass().getCanonicalName();
		logger.debug(marker,"Registering component " + name);
		components.put(c.getClass(), c);
	}

	public <T extends Component> T getComponent(Class<T> cl) {
		String name = cl.getClass().getCanonicalName();
		logger.debug(marker,"Getting component " + name);
		return (T)components.get(cl);
	}

	public Engine() {
		if (instanceCount >= 1) {
            logger.error(marker,"Attempting to launch multiple Engine instances");
			throw new Error("ERROR: Attempting to launch multiple Engine instances");
		}
		instanceCount = 1;

		systems = new HashMap<Class, GameSystem>();
		components = new HashMap<Class, Component>();
		shouldEnd = false;
	}

	private void insertWithDeps(Class sys, Class[] deps, Set<Class> visited) {
		if (visited.contains(sys)) {
			return;
		}
		logger.debug(marker,"Inserting sys " + sys);
		try {
		for (Class dep: deps) {
			if (!visited.contains(dep)) {
				logger.debug(marker,"Inserting dependency " + sys);
				insertWithDeps(dep, getSystem(dep).getDependencies(), visited);
				visited.add(dep);
			}
		}
		} catch (Exception e) {
			depsort.push(sys);
			visited.add(sys);
			return;
		}
		depsort.push(sys);
		visited.add(sys);
	}

	public void init() {
		depsort = new ArrayDeque<Class>(systems.size());
		Set<Class> visited = new HashSet<Class>();
		Iterator<HashMap.Entry<Class, GameSystem>> sys= systems.entrySet().iterator();
		while (sys.hasNext() && visited.size() != systems.size()) {
			HashMap.Entry<Class, GameSystem> s = sys.next();
			insertWithDeps(s.getKey(), s.getValue().getDependencies(), visited);
		}

		logger.debug(marker,depsort);

		Iterator<Class> sysNames = depsort.descendingIterator();
		GameSystem gs;
		while (sysNames.hasNext()) {
			gs = getSystem(sysNames.next());
      String sys_name = gs.getClass().getCanonicalName();
      gs.init();
		}

		ws = getSystem(WindowSystem.class);
	}

	public void end() {
		shouldEnd = true;
	}

	private void deinit() {

		GameSystem gs;
		for (Class s : depsort) {
			gs = getSystem(s);
			gs.end();
		}
	}


	public void run() {
		Iterator<HashMap.Entry<Class, GameSystem>> sys= systems.entrySet().iterator();
		GameSystem cur;
		while (sys.hasNext()) {
			cur = sys.next().getValue();
			if (cur instanceof Runnable) {
				Thread t = new Thread((Runnable)cur);
				t.start();
			}
//			sys.next().getValue().update();
		}
		while (!shouldEnd) {
			ws.clear();
			sys= systems.entrySet().iterator();
			while (sys.hasNext()) {
				cur = sys.next().getValue();
				if (!(cur instanceof Runnable)) {
					cur.update();
				}
			}
			ws.swapBuffers();
			ws.pollEvents();
		}
		deinit();
	}
}
