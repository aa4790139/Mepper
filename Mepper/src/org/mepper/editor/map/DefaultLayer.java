/* DefaultLayer.java 1.0 2010-2-2
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
package org.mepper.editor.map;

import java.awt.Graphics2D;
import java.awt.Point;

import javax.swing.event.EventListenerList;

import org.mepper.editor.tile.AbstractCompositeTile;
import org.mepper.editor.tile.Tile;

/**
 * <B>DefaultLayer</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-11 created
 * @since org.mepper.editor.map Ver 1.0
 * 
 */
public class DefaultLayer  
extends AbstractCompositeTile 
implements Layer{

	protected boolean selected;
	protected boolean visible;
	protected final boolean selectable;
	private transient final EventListenerList listenerList=new EventListenerList();
//	private final List<Layer> linkedLayers = new LinkedList<Layer>();
	
	public DefaultLayer() {
		this("");
	}

	public DefaultLayer(String name) {
		this(name,true,true);
	}

	public DefaultLayer(String name, boolean selected, boolean visible) {
		this.name = name;
		this.selected = selected;
		this.visible = visible;
		selectable = true;
	}


	@Override
	public boolean isVisible() {
		return visible;
	}

	@Override
	public void setVisible(boolean b) {
		this.visible=b;
		fireVisibleChanged();
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelection(boolean b) {
		if (canSelecte()) {
			this.selected=b;
		}
		fireSelectionChanged();
	}

	@Override
	public void insertTile(int[][] area,Tile[] tile) {
		for(int i=0,j=area.length;i<j;i++){
			add(tile[i], area[i][0],area[i][1]);
		}
	}
	

	@Override
	public void removeTile(int[][] area) {
		for(int i=0,j=area.length;i<j;i++){
			remove(area[i][0],area[i][1]);
		}
	}

	@Override
	public void draw(int x, int y, Graphics2D g,Map map) {
		Tile tile;
		for(int i=0;i<occupieArea.width;i++){
			for(int j=0;j<occupieArea.height;j++){
				tile = getTileAt(i,j);
				Point p= map.mapToScreen(new Point(i,j));
				if(tile != null){
					tile.draw(p.x+x, p.y+y, g,map);
				}
			}
		}
		
//		for(int i=0,j=linkedLayers.size();i<j;i++){
//			linkedLayers.get(i).draw(x, y, g, map);
//		}
	}
	
	@Override
	public boolean canSelecte() {
		return this.selectable;
	}
	
	
	@Override
	public String toString() {
		return name;
	}

	@Override
	public void addLayerListener(LayerListener l) {
		listenerList.add(LayerListener.class, l);
	}

	@Override
	public void removeLayerListener(LayerListener l) {
		listenerList.remove(LayerListener.class, l);
	}
	
	private void fireSelectionChanged() {
		LayerEvent e= new LayerEvent(this);
		Object[] listeners=listenerList.getListenerList();
		for(int i=0,j=listeners.length-1;i<j;i+=2){
			if (listeners[i]== LayerListener.class) {
				((LayerListener)listeners[i+1]).selectionChanged(e);
			}
		}
	}	
	private void fireVisibleChanged() {
		LayerEvent e= new LayerEvent(this);
		Object[] listeners=listenerList.getListenerList();
		for(int i=0,j=listeners.length-1;i<j;i+=2){
			if (listeners[i]== LayerListener.class) {
				((LayerListener)listeners[i+1]).visibleChanged(e);
			}
		}
	}

//	@Override
//	public void addLinkedLayer(int index, Layer l) {
//		linkedLayers.add(index, l);
//		fireVisibleChanged();
//	}
//
//	@Override
//	public void removeLinkedLayer(int index) {
//		linkedLayers.remove(index);
//		fireVisibleChanged();
//	}
//
//	@Override
//	public void getLinkedLayer(int index) {
//		linkedLayers.get(index);
//	}
//
//	@Override
//	public void getLinkedLayerCount() {
//		linkedLayers.size();
//	}	
}
