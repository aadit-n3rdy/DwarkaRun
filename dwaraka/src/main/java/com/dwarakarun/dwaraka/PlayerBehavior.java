package com.dwarakarun.dwaraka;

import com.dwarakarun.engine.*;

public class PlayerBehavior extends BehaviorComponent {
    KeySystem ks;
    TransformComponent tc;

    @Override
    public void start() {
        ks = new KeySystem();
        tc = eng.getComponent(TransformComponent.class);
    }

    @Override
    public void update(float dt) {
        Transform t = tc.get(entityName);
        if (t != null) {
            float dx = ks.getXMove(entityName);
            float dy = ks.getYMove(entityName);
            t.setX(t.getX() + dx);
            t.setY(t.getY() + dy);
        }
    }
}
