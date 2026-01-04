package com.dwarakarun.engine;

public abstract class BehaviorComponent {
    protected Engine eng;
    public String entityName;
    
    public BehaviorComponent() {
    }

    public void setEngine(Engine eng) {
        this.eng = eng;
    }

    public void start() {}
    public void update(float dt) {}
}
