package com.dwarakarun.engine;

import java.util.Iterator;
import java.util.Map;

public class ScriptingSystem extends GameSystem {
    
    ScriptsComponent behaviors;

    public ScriptingSystem(Engine eng) {
        super(eng);
        deps = new Class[] {};
    }

    @Override
    public void init() {
        behaviors = eng.getComponent(ScriptsComponent.class);
    }

    @Override
    public void update() {
        if (behaviors == null) return;
        
        java.util.Iterator iter = behaviors.iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            BehaviorComponent bc = (BehaviorComponent)entry.getValue();
            if (bc.entityName == null) {
                bc.entityName = (String)entry.getKey();
            }
            // Ensure engine is set if not already
            if (bc.eng == null) {
                bc.setEngine(eng);
                bc.start();
            }
            bc.update(1.0f); // TODO: Pass real delta time
        }
    }
}
