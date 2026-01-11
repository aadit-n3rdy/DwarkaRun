package com.dwarakarun.dwaraka;
import com.dwarakarun.engine.*;


public class DwarakaInputHandlerSystem extends InputHandlerSystem{
    TransformComponent tc;
    public DwarakaInputHandlerSystem(Engine engine) {
        super(engine);
        tc = eng.getComponent(TransformComponent.class);

    }

    @Override
    public void handleKeyInput(int key, int scancode, int action, int mods) {
        if ((action == KeyTable.REPEAT|| action == KeyTable.PRESS) && (key == KeyTable.W || key == KeyTable.A|| key == KeyTable.S|| key == KeyTable.D))
            moveSprite(key, 2f);
        if (action == KeyTable.RELEASE && (key == KeyTable.W || key == KeyTable.A|| key == KeyTable.S|| key == KeyTable.D))
            moveSprite(key, 0f);
    }

    static float pos[] = {0f,0f};
    void moveSprite(int keycode,float move_speed) {
        //65 = a
        //87 = w
        //83 = s
        //68 = d
        if(keycode == 65) {
            pos[0] = -move_speed;
        } else if(keycode == 87) {
            pos[1]= -move_speed;
        } else if(keycode == 83) {
            pos[1] = move_speed;
        } else if(keycode == 68) {
            pos[0] = move_speed;
        }
    }

    @Override
    public float getXMove() {
        return pos[0];
    }

    @Override
    public float getYMove() {
        return pos[1];
    }

    void noMove() {
        pos[0] = 0;
        pos[1] = 0;
    }


}
