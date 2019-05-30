/* AbstractTile.java 1.0 2010-2-2
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
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;
import javax.swing.event.EventListenerList;

import org.mepper.editor.map.Map;
import org.mepper.io.Storable;
import org.mepper.io.StorablePropertySupporter;
import org.zhiwu.utils.AppLogging;

/**
 * <B>AbstractTile</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-15 created
 * @since org.mepper.editor.tile Ver 1.0
 * 
 */
public abstract class AbstractTile extends StorablePropertySupporter implements Tile{
	protected transient BufferedImage image;
	protected Point startingPoint;
	protected int tileWidth,tileHeight;
	/**  <B>NOTE:</B> The occupie can be positive or negative  */
	protected Dimension occupieArea;//FIXME changes to private
	
	private transient static final Dimension DEFAULT_EXTENSION=new Dimension(1,1);
	private transient final EventListenerList listenerList=new EventListenerList();

	public AbstractTile() {
		setID(Storable.ILLEGAL_ID);
		occupieArea=DEFAULT_EXTENSION;
		startingPoint = new Point();
	}
	
	@Override
	public BufferedImage getImage() {
		return image;
	}

	
	@Override
	public void setImage(BufferedImage image) {
		this.image=image;
	}
	
	@Override
	public Point getStartingPoint() {
		return startingPoint;
	}

	@Override
	public void setStartingPoint(Point p) {
		this.startingPoint=p;
		fireStartingPointChanged();
	}

	@Override
	public Dimension getOccupieArea() {
		return occupieArea;
	}

	@Override
	public void setOccupieArea(Dimension d) {
		this.occupieArea =d;
		fireOccupieChanged();
	}

	@Override
	public int tileWidth() {
		return tileWidth;
	}

	@Override
	public int tileHeight() {
		return tileHeight;
	}

	@Override
	public void setTileStep(int width,int height) {
		this.tileWidth = width;
		this.tileHeight =height;
	}
	
	@Override
	public void draw(int x, int y, Graphics2D g,Map map) {
		g.drawImage(image, x, y, null);
	}
	
	@Override
	public String toString() {
		return "区块 [起始点("+startingPoint.x+","+startingPoint.y+") 占有区域("+
			occupieArea.width+","+occupieArea.height+") ]";
			
		
		
	}

	@Override
	public void addTileListener(TileListener l) {
		listenerList.add(TileListener.class, l);
	}

	@Override
	public void removeTileListener(TileListener l) {
		listenerList.remove(TileListener.class, l);
	}
	
	protected void fireOccupieChanged() {
		TileEvent e= new TileEvent(this);
		Object[] listeners=listenerList.getListenerList();
		for(int i=0,j=listeners.length-1;i<j;i+=2){
			if (listeners[i]== TileListener.class) {
				((TileListener)listeners[i+1]).occupieChanged(e);
			}
		}
	}
	
	protected void fireStartingPointChanged() {
		TileEvent e= new TileEvent(this);
		Object[] listeners=listenerList.getListenerList();
		for(int i=0,j=listeners.length-1;i<j;i+=2){
			if (listeners[i]== TileListener.class) {
				((TileListener)listeners[i+1]).startingPointChanged(e);
			}
		}
	}
	
	
	///  serialization	 
	byte[] imageData;
	private void writeObject(ObjectOutputStream out) throws IOException {
		imageToByte();
		out.defaultWriteObject();
	}
	
	private void imageToByte() {
		if(image != null){
			try {
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				ImageIO.write(image, "PNG", baos);
				baos.close();
				imageData = baos.toByteArray();
			} catch (IOException e) {
				AppLogging.handleException(e);
			}
			
		}
	}

	private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
		in.defaultReadObject();
		byteToImage();
	}

	private void byteToImage() {
		if(imageData != null){
			try {
				ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
				image  = ImageIO.read(bais);
				imageData = null;
			} catch (IOException e) {
				AppLogging.handleException(e);
			}
		}
	}
	
	/// end serializaton
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj ==null ){
			return false;
		}
		if(! (obj instanceof AbstractTile)){
			return false;
		}
		AbstractTile other = (AbstractTile)obj;
		return other.ID == ID;
	}
}
