package com.dwarakarun.dwaraka;

import com.dwarakarun.engine.*;

public class Game {
	public Game() {
	}
	public static void main(String args[]) {
		Engine e = new Engine();

		SpriteComponent sc = new SpriteComponent();
		e.addComponent(sc);

		TransformComponent tc = new TransformComponent();
		e.addComponent(tc);

		ColliderComponent cc = new ColliderComponent();
		e.addComponent(cc);

        PlayerComponent playerComponent = new PlayerComponent("shinobiSprite");
        e.addComponent(playerComponent);
        e.addSystem(new DwarakaInputHandlerSystem(e));
		e.addSystem(new WindowSystem(e));
		e.addSystem(new ColliderSystem(e));
		e.addSystem(new RendererSystem(e));
		e.addSystem(new AnimatorSystem(e));
		e.addSystem(new ObstacleSystem(e));

		e.init();

		String spritePath = "../assets/sprite.png";
		String spritePath1 = "../assets/shinobi/Shinobi/Attack_2.png";
//		String spritePath2 = "../assets/vampire/Vampire_Girl/Attack_4.png";
		String spritePath3 = "../assets/vampire/Countess_Vampire/Run.png";
		String spritePath4 = "../assets/shinobi/Samurai/Attack_2.png";
		String spritePath5 = "../assets/shinobi/Shinobi/Run.png";

		String background = "../assets/background.jpg";
		String background_for = "../assets/background_forest.jpg";
		String background_for_dup = "../assets/background_forest.jpg";


		Sprite s = new Sprite(spritePath5);
//		Sprite s2 = new Sprite(spritePath3);
		Sprite s3 = new Sprite(background_for);
		Sprite s3_dup = new Sprite(background_for_dup);

		Transform t1 = new Transform(0,10,0);
		tc.set("background_for",t1);

		Transform t2 = new Transform(100,450,1);
		tc.set("shinobiSprite",t2);

		Transform t3 = new Transform(10,450,1);
//		tc.set("vampSprite",t3);

		Transform t1_dup = new Transform(0,10,0);
		tc.set("background_for_dup",t1_dup);


		s.load();
//		s2.load();
		s3.load();
		s3_dup.load();

		Collider c1 = new Collider(50, 128);
		cc.set("shinobiSprite", c1);
//		cc.set("vampSprite", c2);


		sc.set("shinobiSprite", s);
//		sc.set("vampSprite", s2);
		sc.set("background_for",s3);
		sc.set("background_for_dup",s3_dup);

		Sprite ob_s;
		Collider ob_c;
		Transform ob_t;
		for (int i = 0; i < 3; i++) {
			ob_s = new Sprite("../assets/spike.png");
			ob_s.load();
			ob_s.scale(0.5f);
			ob_c = new Collider(120, 150);
			ob_t = new Transform(400 * i, 0, 1);
			sc.set("obstacle" + i, ob_s);
			cc.set("obstacle" + i, ob_c);
			tc.set("obstacle" + i, ob_t);
		}

		s.scale(1f);
//		s2.scale(1f);
		s3.scale(3f);
		s3_dup.scale(3f);

		System.out.println("Done engine init");

		//SpriteDetails sd = new SpriteDetails(spritePath1);
		//    sd.getSpriteTime();

		e.run();
	}
}

