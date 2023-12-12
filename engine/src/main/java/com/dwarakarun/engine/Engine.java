package com.dwarakarun.engine;

import java.util.*;

public class Engine {
	private static int instanceCount = 0;

	private Deque<Class> depsort;

	private Boolean shouldEnd;

	private HashMap<Class, GameSystem> systems;
	private HashMap<Class, Component> components;
	private WindowSystem ws;

	public <T extends GameSystem> T getSystem(Class<T> cl) {
		GameSystem sys = systems.get(cl);
		return (T)sys;
	}

	public <T extends GameSystem> void addSystem(T sys) {
		String name = sys.getClass().getCanonicalName();
		System.out.println("Registering system " + name);
		systems.put(sys.getClass(), sys);
	}

	public <T> void addComponent (Component<T> c) {
		String name = c.getClass().getCanonicalName();
		System.out.println("Registering component " + name);
		components.put(c.getClass(), c);
	}

	public <T extends Component> T getComponent(Class<T> cl) {
		String name = cl.getClass().getCanonicalName();
		System.out.println("Getting component " + name);
		return (T)components.get(cl);
	}

	public Engine() {
		if (instanceCount >= 1) {
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
		System.out.println("Inserting sys " + sys);
		try {
		for (Class dep: deps) {
			if (!visited.contains(dep)) {
				System.out.println("Inserting dependency " + sys);
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

		System.out.println(depsort);

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
		while (!shouldEnd) {
			ws.clear();
			Iterator<HashMap.Entry<Class, GameSystem>> sys= systems.entrySet().iterator();
			while (sys.hasNext()) {
				sys.next().getValue().update();
			}
			ws.swapBuffers();
			ws.pollEvents();
		}
		deinit();
	}
}
