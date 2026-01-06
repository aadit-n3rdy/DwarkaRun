package com.dwarakarun.engine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import java.lang.*;

public class GameSystem {
	protected Engine eng;
	protected Class[] deps;
    public static final Logger logger = LogManager.getLogger("ENGINE");
    private static final Marker marker = MarkerManager.getMarker("BaseSystem");
	public GameSystem(Engine eng) {
		this.eng = eng;
	}
	public void init() {
        logger.info(marker,"Base System Init!");
	}
	public void update() {
        logger.info(marker,"Base System Update");
	}
	public final Class[] getDependencies() {
		return deps;
	}
	public void end() {
        logger.info(marker,"Base System End");
	}
}
