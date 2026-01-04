package com.dwarakarun.dwaraka;

import com.dwarakarun.engine.*;

public class BackgroundBehavior extends BehaviorComponent {
    TransformComponent tc;
    String otherBackgroundName;
    int focus; // 1 = this is background_for, 0 = background_for_dup
    
    // We need to know which one this is to handle the coupled logic.
    // Or we can just implement the generic "move left and wrap around" logic.
    // The original logic:
    // tc.get("background_for").getX() > -1986f
    // checks BOTH backgrounds to decide whether to move.
    
    // To simplify: I'll make a ScrollerBehavior that just moves left.
    // And a LooperBehavior that resets position?
    // The original logic is tightly coupled between the two backgrounds.
    
    // Let's copy the logic as faithfully as possible or simplify it.
    // Original: 
    // changePos(-speed, 0f, tc.get("background_for"));
    // changePos(-speed, 0f, tc.get("background_for_dup"));
    // This happens every frame (if conditions met).
    
    // I will implement a simpler "InfiniteScrollBehavior" that moves left and resets when too far.
    // But the reset logic relies on the other background's position?
    // "tc.get("background_for_dup").setX(1100f);" when background_for is at -786.
    
    // I will pass the name of the "pair" background in the constructor or via a setter?
    // BehaviorComponent doesn't support custom constructors easily with the generic loading, 
    // but here we instantiate it manually in Game.java, so we can add setters.

    public BackgroundBehavior(String otherBg) {
        this.otherBackgroundName = otherBg;
    }

    @Override
    public void start() {
        tc = eng.getComponent(TransformComponent.class);
    }

    @Override
    public void update(float dt) {
        Transform t = tc.get(entityName);
        Transform other = tc.get(otherBackgroundName);
        
        if (t == null || other == null) return;

        float speed = 1.0f;
        
        // Move left
        t.setX(t.getX() - speed);
        
        // Loop logic
        // Original: if(name=="background_for" && (t.getX()<=-786f && t.getX()>=-800f))
        if (t.getX() <= -786f) { // Simplified condition
             // The original code sets the OTHER background to 1100f? 
             // "tc.get("background_for_dup").setX(1100f);" when "background_for" loops.
             // This seems to imply they leapfrog each other.
             // Actually, if THIS one goes off screen, IT should reset to the back.
             // But the code resets the OTHER one?
             // No, wait.
             // if(name=="background_for" ...) tc.get("background_for_dup").setX(1100f);
             // This resets the DUP when the MAIN is at -786.
             // That sounds wrong. -786 is likely offscreen left.
             // If MAIN is offscreen left, DUP should be visible. 
             // Why reset DUP to 1100 (right)?
             
             // Let's re-read AnimatorSystem.java carefully.
             // if(name=="background_for" && (t.getX()<=-786f && t.getX()>=-800f)) {
             //    tc.get("background_for_dup").setX(1100f);
             // }
             // This resets the DUP when the MAIN reaches a certain point.
             // Maybe the DUP was the one trailing and now needs to jump ahead?
             
             // To be safe, I will implement exactly what was there.
             // But I'm attaching this behavior to EACH entity.
             // So when THIS entity is "background_for", it checks itself and resets the other.
             
             if (entityName.equals("background_for")) {
                  if (t.getX() <= -786f && t.getX() >= -800f) {
                      other.setX(1100f);
                  }
             } else if (entityName.equals("background_for_dup")) {
                  if (t.getX() <= -786f && t.getX() >= -800f) {
                      other.setX(1100f);
                  }
             }
        }
    }
}
