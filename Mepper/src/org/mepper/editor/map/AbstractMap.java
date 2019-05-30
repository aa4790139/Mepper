/* AbstractMap.java 1.0 2010-2-2
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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.event.EventListenerList;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

import org.mepper.editor.coordinate.CoordinateTranslator;
import org.mepper.editor.tile.Tile;
import org.mepper.io.StorablePropertySupporter;
import org.zhiwu.utils.AppLogging;

/**
 * <B>AbstractMap</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-11 created
 * @since org.mepper.editor.map Ver 1.0
 *  
 */
public abstract class AbstractMap extends StorablePropertySupporter implements Map{
	protected int tileWidth;
	protected int tileHeight;
	protected MapOffset offset;
	protected List<Layer> layers;
	protected int row;
	protected int column;
	protected Dimension size;
	protected CoordinateTranslator coordinateTranslator;
	private transient final EventListenerList listenerList= new EventListenerList();
	private final LayerListener layerListener = new LayerAdapter() {
		@Override
		public void visibleChanged(LayerEvent e) {
			fireLayerVisibleChanged();			
		}
		
		@Override
		public void selectionChanged(LayerEvent e) {
			fireLayerSelectionChanged();
		}
	};

	public AbstractMap() {
		size=new Dimension();
		offset = new MapOffset();
		layers =new LinkedList<Layer>();
	}

	@Override
	public int getTileWidth() {
		return tileWidth;
	}

	@Override
	public int getTileHeight() {
		return tileHeight;
	}

	@Override
	public MapOffset getOffset() {
		return offset;
	}

	@Override
	public void setOffset(MapOffset offset) {
		this.offset  = offset;
	}

	@Override
	public void setTileStep(int tileWidth, int tileHeight) {
		this.tileWidth=tileWidth;
		this.tileHeight = tileHeight;
		
		offset.stepX = tileWidth;
		offset.stepY = tileHeight;
		
		fireOffsetChanged();
	}
	
	@Override
	public Map create() {
		try {
			Map other =this.getClass().newInstance();
			other.setName(name);
			other.setTileStep(tileWidth, tileHeight);
			other.setExtension(row, column);
			other.setSize((Dimension) size.clone());
			return other;
//			ByteArrayOutputStream bos=new ByteArrayOutputStream();
//			ObjectOutputStream oos=new ObjectOutputStream(bos);
//			oos.writeObject(this);
//			
//			ByteArrayInputStream bis=new ByteArrayInputStream(bos.toByteArray());
//			ObjectInputStream ois=new ObjectInputStream(bis);
//			return (Map) ois.readObject();
		} catch (Exception e) {
			AppLogging.handleException(e);
		} 
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)){
			return false;
		}
		if(!(obj instanceof Map )&&
				(!this.getClass().equals(obj.getClass()))){
			return false;
		}
		
		Map other = (Map)obj;
		Dimension d= other.getExtension();
		
		if(other.getLayerCount()!=getLayerCount() || 
				d.width != row || d.height!=column){
			return false;
		}
		
		for(int i=0,j=getLayerCount();i<j;i++){
			if(!other.getLayer(i).equals(getLayer(i))){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void addLayer(Layer l, int index) {
		l.setOccupieArea(new Dimension(row, column));
		addLayerListener(l);
		layers.add(index, l);
		fireLayerVisibleChanged();
//		fireMapChanged();
	}


	private void addLayerListener(Layer l) {
		l.removeLayerListener(layerListener);
		l.addLayerListener(layerListener);
	}

	@Override
	public Layer getLayer(int index) {
		return layers.get(index);
	}
	
	@Override
	public int getLayer(String name) {
		for(int i=0,j=layers.size();i<j;i++){
			Layer l=layers.get(i);
			if(l.getName().equals(name)){
				return i;
			}
		}
		return -1;
	}

	@Override
	public Layer removeLayer(int index) {
		Layer l = layers.remove(index);
		l.removeLayerListener(layerListener);
		fireLayerVisibleChanged();
		return l;
	}

	
	@Override
	public void removeLayer(Layer layer) {
		layers.remove(layer);
		layer.removeLayerListener(layerListener);
		fireLayerVisibleChanged();
	}

	@Override
	public int getLayerCount() {
		return layers.size();
	}

	@Override
	public List<Layer> getAllSelectedLayers() {
		List<Layer> selections=new ArrayList<Layer>();
		for(Layer l:layers){
			if(l.isSelected()){
				selections.add(l);
			}
		}
		return selections;
	}
	
	@Override
	public List<Layer> getLayers(Object key, Object value) {
		return Collections.unmodifiableList(layers);
	}
	
	/**
	 * Gets the first selected layer.
	 * @return the selected layer
	 */
	@Override
	public Layer getSelectedLayer(){
		for(Layer l:layers){
			if(l.isSelected()){
				return l;
			}
		}
		return null;
	}
	
	public List<Layer> getSelectedLayers(){
		List<Layer> selectedLayers= new LinkedList<Layer>();
		for(Layer l:this.layers){
			if(l.isSelected()){
				selectedLayers.add(l);
			}
		}
		return selectedLayers;
	} 
	
	@Override
	public int getSelectedIndex() {
		for(int i=0,j=layers.size();i<j;i++){
			if (layers.get(i).isSelected()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void setExtension(int rows, int columns) {
		this.row=rows;
		this.column=columns;
	}

	@Override
	public Dimension getExtension() {
		return new Dimension(row,column);
	}

	@Override
	public void setSize(int width, int height) {
		size.width = width;
		size.height = height;
	}

	@Override
	public void setSize(Dimension size) {
		this.size=size;
	}

	@Override
	public Dimension getSize() {
		return size;
	}

	

	@Override
	public void setCoordinateTranslator(CoordinateTranslator t) {
		this.coordinateTranslator=t;
	}

	@Override
	public Point screenToMap(Point screen) {
		return coordinateTranslator.screenToMap(screen,offset);
	}

	@Override
	public Point mapToScreen(Point mapPoint) {
		return coordinateTranslator.mapToScreen(mapPoint,offset);
	}

	
	@Override
	public Tile getTileCover(int x, int y) {
		for(Layer l:getSelectedLayers()){
			Tile t = l.getTileCover(x,y);
			if(t!= null){
				return t;
			}
		}
		return null;
	}
	
	@Override
	public Point getTileStart(int x, int y) {
		return getSelectedLayer().getTileStart(x,y);
	}
	
	@Override
	public Rectangle getTileBounds(int x, int y) {
		Layer l = getSelectedLayer();
		return l ==null? new Rectangle(): l.getTileBounds(x,y);
	}
	
	@Override
	public Rectangle isCover(int x, int y, int width, int height) {
		for(Layer l:getSelectedLayers()){
			Rectangle bounds = l.getTileCover(x,y,width,height);
			if(bounds!= null){
				return bounds;
			}
		}
		return null;
	}
	
	@Override
	public Dimension getExtension(Point startPoint, Point finishPoint) {
		int x= finishPoint.x - startPoint.x;
		int y= finishPoint.y - startPoint.y;
		
		x += x>=0?1:-1;// the coordianate system start from zero
		y += y>=0? 1:-1;// the coordianate system start from zero
		
		return new Dimension(x,y);
	}

	@Override
	public void draw(int x,int y,Graphics2D g) {
		for(Layer l:layers){
			if (l.isVisible()) {
				l.draw(x, y, g,this);
			}
		}
	}
	
	/// map event
	@Override
	public void addMapListener(MapListener l) {
		listenerList.add(MapListener.class, l);
	}

	@Override
	public void removeMapListener(MapListener l) {
		listenerList.remove(MapListener.class, l);
	}
	
	protected void fireLayerSelectionChanged() {
		MapEvent e = new MapEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == MapListener.class) {
				((MapListener) listeners[i + 1]).layerSelectionChanged(e);
			}
		}
	}
	protected void fireLayerVisibleChanged() {
		MapEvent e = new MapEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == MapListener.class) {
				((MapListener) listeners[i + 1]).layerVisibleChanged(e);
			}
		}
	}

	
	protected void fireTileAddedChanged(MapEvent e) {
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == MapListener.class) {
				((MapListener) listeners[i + 1]).tileAdded(e);
			}
		}
	}
	
	protected void fireOffsetChanged() {
		MapEvent e = new MapEvent(this);
		Object[] listeners = listenerList.getListenerList();
		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
			if (listeners[i] == MapListener.class) {
				((MapListener) listeners[i + 1]).offsetChanged(e);
			}
		}
	}
	
//	private void fireTileRemoved(MapEvent e) {
//		Object[] listeners = listenerList.getListenerList();
//		for (int i = 0, j = listeners.length - 1; i < j; i += 2) {
//			if (listeners[i] == MapListener.class) {
//				((MapListener) listeners[i + 1]).tileRemoved(e);
//			}
//		}
//	}
	
	
	////    add  remove
	
	
	@Override
	public void addTile(int[][] area,Tile[] tile) {
		for(int i=0,j=getLayerCount();i<j;i++){
			Layer l= layers.get(i);
			if(l.isSelected()){
				UndoableEdit edit =	insertTile(l,area,tile);
				fireUndoableEditUpdate(new UndoableEditEvent(
						this, edit));//FIXME undo for all modify layers
			}
		}
	}

	private UndoableEdit insertTile(Layer l,int[][] area, Tile[] tile) {
//		Rectangle bounds ;
		Tile[] oldTiles=new Tile[area.length];
		int[][] oldArea = new int[area.length][2];
		for(int i=0,j=area.length;i<j;i++){
			oldArea[i][0]=area[i][0];
			oldArea[i][1]=area[i][1];
//			bounds = l.getTileBounds(area[i][0],area[i][1]);
//			if(bounds != null){
				oldTiles[i] = l.getTileAt(area[i][0],area[i][1]);
//			}
		}
		l.insertTile(area, tile);
		UndoableEdit edit = new InsertUndo(oldArea, oldTiles, l);
		fireTileAddedChanged(new MapEvent(this));
		return edit;
		
	}

	@Override
	public void removeTile(int[][] area) {
		Tile[] tiles= new Tile[area.length];
		addTile(area, tiles);
//		for(Layer l:layers){
//			if(l.isSelected()){
//				UndoableEdit edit = removeTile(l, area);
//				fireUndoableEditUpdate(new UndoableEditEvent(this, edit));
//			}
//		}	
//		fireTileRemoved(new MapEvent(this, area));
	}
	
//	private UndoableEdit removeTile(Layer l,int[][] area){
//		Tile[] oldTiles=new Tile[area.length];
//		int[][] oldArea = new int[area.length][2];
//		for(int i=0,j=area.length;i<j;i++){
//			oldArea[i][0]=area[i][0];
//			oldArea[i][1] = area[i][1];
//			oldTiles[i] = l.getTileAt(area[i][0],area[i][1]);
//		}
//		l.remove(p)
//	}
	
	
	///  undo event
	@Override
	public void addUndoableEditListener(UndoableEditListener l) {
		listenerList.add(UndoableEditListener.class, l);		
	}
	
	@Override
	public void removeUndoableEditListener(UndoableEditListener l) {
		listenerList.remove(UndoableEditListener.class, l);
	}
	
	private void fireUndoableEditUpdate(UndoableEditEvent e) {
		// Guaranteed to return a non-null array
		Object[] listeners = listenerList.getListenerList();
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length-2; i>=0; i-=2) {
		    if (listeners[i]==UndoableEditListener.class) {
			// Lazily create the event:
			// if (e == null)
			// e = new ListSelectionEvent(this, firstIndex, lastIndex);
			((UndoableEditListener)listeners[i+1]).undoableEditHappened(e);
		    }	       
		}
	} 
	
	class InsertUndo extends AbstractUndoableEdit{
		private  int[][] oldArea;
		private  Tile[] oldTiles;
		private final Layer layer;
		
		public InsertUndo(int[][] oldArea,Tile[] oldTiles,Layer layer) {
			this.oldArea = oldArea;
			this.oldTiles = oldTiles;
			this.layer =layer;
		}
		
		@Override
		public void undo() throws CannotUndoException {
			super.undo();
			undoredo();
		}
		
		@Override
		public void redo() throws CannotRedoException {
			super.redo();
			undoredo();
		}
		
		private void undoredo(){
			InsertUndo  e=(InsertUndo) insertTile(layer, oldArea, oldTiles);
			this.oldArea =e.oldArea;
			this.oldTiles=e.oldTiles;
		}
	}
}

















