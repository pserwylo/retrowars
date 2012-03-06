package com.serwylo.peter.retrowars.collisions;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.glutils.ImmediateModeRenderer10;
import com.badlogic.gdx.math.Rectangle;

public class QuadTree<T extends ICollidable>
{

	private ArrayList<QuadTree<T>> childNodes;
	private ArrayList<T> items;

	private static final int MAX_ITEMS_PER_NODE = 1;
	
	
	private Rectangle rect;
	
	public QuadTree( Rectangle rect )
	{
		this.childNodes = new ArrayList<QuadTree<T>>( 4 );
		this.items = new ArrayList<T>( 10 );
		this.rect = rect;
	}
    
    public void draw( Camera camera )
    {
    	if ( /*this.items.size() > 0 ||*/ true )
    	{
	    	ImmediateModeRenderer10 debug = new ImmediateModeRenderer10( 4 );
	    	debug.begin( camera.combined, GL10.GL_LINE_LOOP );
		    
	    		debug.vertex( rect.x, rect.y, 0 );
		    	debug.color( 1.0f, 1.0f, 1.0f, 1.0f );
		    	
		    	debug.vertex( rect.x + rect.width, rect.y , 0 );
		    	debug.color( 1.0f, 1.0f, 1.0f, 1.0f );
		    	
		    	debug.vertex( rect.x + rect.width, rect.y + rect.height, 0 );
		    	debug.color( 1.0f, 1.0f, 1.0f, 1.0f );
		    	
		    	debug.vertex( rect.x, rect.y + rect.height, 0 );
		    	debug.color( 1.0f, 1.0f, 1.0f, 1.0f );
	    	
		    debug.end();
    	}

    	if ( !this.isLeaf() )
    	{
    		for ( QuadTree<T> node : this.childNodes )
    		{
    			node.draw( camera );
    		}
    	}
    }
	
	/**
	 * Returns true if this node hasn't been split yet.
	 * @return
	 */
	public boolean isLeaf()
	{
		return this.childNodes.size() == 0;
	}
	
	public void update( T item )
	{
		this.removeFromTree( item, false );
		this.insert( item );
	}
	
	public void remove( T item )
	{
		this.removeFromTree( item, true );
	}
	
	/**
	 * Removes the specified {@link item} from the tree, and then if asked, will clean up any
	 * empty branches which are left. That is, if, after removal, the siblings of the tree it was 
	 * removed from have a combined total number of children which will fit in one node, then
	 * the branch will be removed (the siblings will be joined back into one bigger quad).
	 * @param item
	 * @param cleanWhenDone TODO: Implement this feature. I'm not quite sure on how yet...
	 * @return
	 */
	private boolean removeFromTree( T item, boolean cleanWhenDone )
	{
		if ( this.items.remove( item ) )
		{
			return true;
		}
		else
		{
			for ( QuadTree<T> node : this.childNodes )
			{
				if ( node.removeFromTree( item, cleanWhenDone ) )
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean insert( T item )
	{
		if ( !this.rect.contains( item.getBoundingRect() ) )
		{
			return false;
		}
	
		// If any children, then try to add to them.
		if ( this.isLeaf() )
		{
			if ( this.items.size() >= MAX_ITEMS_PER_NODE )
			{
				System.out.println( "Splitting (" + this.rect.x + ", " + this.rect.y + ", " + this.rect.width + ", " + this.rect.height + ") for (" + item.getBoundingRect().x + ", " + item.getBoundingRect().y + ")" );
				this.split();
			}
			this.addToThisNode( item );
		}
		else
		{
			this.addToThisNode( item );
		}
		return true;
	}
	
	/**
	 * Internal function for use by the QuadTree. It will forceably add the item at this node.
	 * That is, it will try to add it to the appropriate child node somewhere in the list of
	 * all decendants, but if it doesn't fit in any of them, then we will instead give up
	 * and add it to this node.
	 * 
	 * NOTE: This presumes that the item fits within this node, and does not re-check this.
	 * @param item
	 */
	private void addToThisNode( T item )
	{
		boolean success = false;
		for( QuadTree<T> child : this.childNodes )
		{
			if ( child.insert( item ) )
			{
				success = true;
				break;
			}
		}
		
		if ( !success )
		{
			// Who cares that we have more items than allowed, we don't have any
			// other choice other than to add it here.
			this.items.add( item );
		}
	}
	
	private void split()
	{
		this.childNodes.add( 
			new QuadTree<T>(
				new Rectangle(
					this.rect.x,
					this.rect.y,
					this.rect.width / 2,
					this.rect.height / 2 ) ) );
		this.childNodes.add( 
			new QuadTree<T>(
				new Rectangle(
					this.rect.x + this.rect.width / 2,
					this.rect.y,
					this.rect.width / 2,
					this.rect.height / 2 ) ) );
		this.childNodes.add( 
			new QuadTree<T>(
				new Rectangle(
					this.rect.x,
					this.rect.y + this.rect.height / 2,
					this.rect.width / 2,
					this.rect.height / 2 ) ) );
		this.childNodes.add( 
			new QuadTree<T>(
				new Rectangle(
					this.rect.x + this.rect.width / 2,
					this.rect.y + this.rect.height / 2,
					this.rect.width / 2,
					this.rect.height / 2 ) ) );
	}
	
}
