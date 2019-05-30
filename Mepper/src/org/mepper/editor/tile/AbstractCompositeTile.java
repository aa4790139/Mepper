/* AbstractCompositeTile.java 1.0 2010-2-2
 * 
 * Copyright (c) 2010 by Chen Zhiwu
 * All rights reserved.
 * 
 * The copyright of this software is own by the authors.
 * You may not use, copy or modify this software, except
 * in accordance with the license agreement you entered into 
 * with the copyright holders. For details see accompanying license
 * terms.
 */
package org.mepper.editor.tile;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * <B>AbstractCompositeTile</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-11 created
 * @since org.mepper.editor.tile Ver 1.0
 * 
 */
public abstract class AbstractCompositeTile extends AbstractTile implements CompositeTile{
	private Tile[][] tiles = new Tile[1][1];
	
	@Override
	public void setOccupieArea(Dimension occupie) {
		int width = Math.abs(occupie.width);
		int height = Math.abs(occupie.height);
		int w=Math.min(width, Math.abs(occupieArea.width));
		int h=Math.min(height, Math.abs(occupieArea.height));
		
		Tile[][] newTiles = new Tile[width][height];
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				newTiles[i][j]=tiles[i][j];
			}
		}
		tiles= newTiles;
		this.occupieArea = occupie;
		fireOccupieChanged();
	}
	
	@Override
	public void add(Tile tile, int x,int y) {
		if(!effective(x,y) ){
			return;
		}
		if(tile != null){
			Rectangle bounds ;
			Dimension d= tile.getOccupieArea();
			for(int i=x,m=x+d.width;i<m;i++){
				for(int j=y,n=y+d.height;j<n;j++){
					bounds=getTileBounds(i,j);
					if(bounds != null){
						tiles[bounds.x][bounds.y]=null;
					}
				}
			}
		}
		tiles[x][y] = tile;
	}

	private boolean effective( int x,int y) {
		return x >=0 && x<occupieArea.width &&
				y >=0 && y< occupieArea.height ;
	}

	@Override
	public Tile remove( int x,int y) {
		if(!effective(x, y)){
			return null;
		}
		Tile old = tiles[x][y] ;
		tiles[x][y] = null;
		return old;
	}

	@Override
	public void removeAll(int x, int y, Dimension size) {
		tiles =new Tile[occupieArea.width][occupieArea.height];
	}

	@Override
	public Tile[][] getTiles() {
		Tile[][] newTiles=new Tile[occupieArea.width][occupieArea.height];
		for(int i=0;i<occupieArea.width;i++){
			for(int j=0;j<occupieArea.height;j++){
				newTiles[i][j]=tiles[i][j];
			}
		}
		return newTiles;
	}
	
	@Override
	public Tile getTileAt(int x,int y) {
		try {
			return tiles[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}
	
	@Override
	public Tile getTileCover(int x, int y) {
		Point point=getTileStart(x, y);
		if(point == null){
			return null;
		}
		return tiles[point.x][point.y];
	}
	@Override
	public Point getTileStart(int x, int y) {
		Rectangle bounds =getTileBounds(x, y);
		if(bounds == null){
			return null;
		}
		return bounds.getLocation();
	}
	
	@Override
	public Rectangle getTileBounds(int x, int y) {
		return getTileCover(x, y, 0, 0);
	}
	
	@Override
	public Rectangle getTileCover(int x, int y,int width,int height) {
		Rectangle bounds =null;
		
		int i= x - Tile.MAX_EXTENSION;
		i= i<0?0:i;
		int q=y -Tile.MAX_EXTENSION;
		q= q<0?0:q;
		
		int m = x+ (width>0?width-1:0);
		int n = y+ (height>0?height-1:0);
		m=m < occupieArea.width?  m:occupieArea.width-1;
		n=n < occupieArea.height? n:occupieArea.height-1;
		
		for(; i<=m ;i++){
			for(int j=q ;j<=n;j++){
				Tile tile=tiles[i][j];
				if(tile ==null){
					continue;
				}
				Dimension d=tile.getOccupieArea();
				if(d.width+i > x && d.height+j>y){
					bounds =new Rectangle();
					bounds.setLocation(i,j);
					bounds.setSize(tile.getOccupieArea());
					return bounds;
				}
			}
		}
		return bounds;
	}
	

	@Override
	public Point[] indexOf(Tile tile) {
		List<Point>  points=new ArrayList<Point>();
		for(int i=0,m=tiles.length;i<m;i++){
			for(int j=0,n=tiles[0].length;j<n;j++){
				if(tiles[i][j].equals(tile)){
					points.add(new Point(i,j));
				}
			}
		}
		return points.toArray(new Point[points.size()]);
	}

}
