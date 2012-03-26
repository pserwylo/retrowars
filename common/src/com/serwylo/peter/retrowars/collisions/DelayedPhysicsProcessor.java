package com.serwylo.peter.retrowars.collisions;

/**
 * Because collisions often end up in new physics bodies being created, they cannot
 * always be processed at the moment the collision is detected. In this case, we will
 * create objects which contain typed information about the things colliding, and process
 * them after the Box2D world has finished stepping.
 */
public interface DelayedPhysicsProcessor 
{
	
	public void process();
	
}
