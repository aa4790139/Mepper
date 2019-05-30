/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImportImageEditorView.java
 *
 * Created on 2011-4-8, 19:27:28
 */

package org.mepper.resources.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.Action;
import javax.swing.JOptionPane;

import org.mepper.editor.AbstractEditorView;
import org.mepper.editor.DoubleBufferable;
import org.mepper.editor.Editor;
import org.mepper.editor.map.DefaultLayer;
import org.mepper.editor.map.Layer;
import org.mepper.editor.map.Map;
import org.mepper.editor.map.MapOffset;
import org.mepper.editor.tile.CandidateTile;
import org.mepper.editor.tile.Tile;
import org.mepper.editor.tile.TileEvent;
import org.mepper.editor.tile.TileFactory;
import org.mepper.editor.tile.TileListener;
import org.mepper.resources.DefaultResource;
import org.mepper.resources.ResourcesManager;
import org.mepper.resources.StorableResource;
import org.mepper.utils.ImageUtil;
import org.zhiwu.app.action.AbsAction;

/**
 * <B>TileEditorView</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-25 created
 * @since org.mepper.resource.gui Ver 1.0
 * 
 */
public final class TileEditorView extends AbstractEditorView implements DoubleBufferable{
	
	private DefaultResource source;
	private final ResourcesManager manager;
	
	private final Tile tile;
	private final PropertyChangeListener selectionListener=new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			updateBufferImage();
		}
	};
	
	private final TileListener tileListener=new TileListener() {
		@Override
		public void occupieChanged(TileEvent e) {
			updateBufferImage();
			updateResource();
		}

		@Override
		public void startingPointChanged(TileEvent e) {
			updateBufferImage();
			updateResource();
		}
	};
	
		
    public TileEditorView(Map map, Tile tile, ResourcesManager manager) {
    	super( map);
    	this.tile = tile;
    	this.manager =manager;
    	this.tile.addTileListener(tileListener);
    	selection.addPropertyChangeListener(selectionListener);

    	initResource();
    	
    	BufferedImage image=tile.getImage();
    	bounds.width = image.getWidth();
    	bounds.height = image.getHeight();
    	
		initSelection();
		initSize();
		
		updateBufferImage();
    }
    
	private void initResource() {
		source= manager.getResource(DefaultResource.class,tile.getID());
	}

	protected void updateResource() {
		if (source == null) {
			return;
		}
		source.setSource(TileFactory.createDefaultTile(tile));
	}

	private void initSelection() {
		Point p=tile.getStartingPoint();
		MapOffset offset= map.getOffset();
		offset.offsetX = p.x;
		offset.offsetY = p.y;
		
//    	offset.stepX =  map.getTileWidth();
//    	offset.stepY = map.getTileHeight();
    	
    	if(offset.stepX ==0 || offset.stepY ==0){
    		return;
    	}
    	
		Shape shape = map.getBounds(new Point(), tile.getOccupieArea(),false);
		selection.add(shape);
		selection.setClip(bounds);
	}

	@Override
	public BufferedImage getImage() {
		return bufferImage;
	}
	
	private void initSize() {
    	setPreferredSize(map.getSize());
    	setSize(map.getSize());
	}
	
	@Override
	public void activate(Editor editor) {
		super.activate(editor);
		editor.setUserObject(tile);
	}
	@Override
	public Collection<? extends Action> getActions() {
		LinkedList<Action> editorActions = new LinkedList<Action>();
		Action createTileAction = new AbsAction("tile.editor.view.create.tile") {
			@Override
			public void actionPerformed(ActionEvent e) {
				StorableResource r= manager.getCurrentNode();
				if(r== null || r==manager.getRoot()){
					JOptionPane.showConfirmDialog(TileEditorView.this, "至少选择一个库");
					return;
				}
				Tile newTile= TileFactory.createDefaultTile(tile);
				
				StorableResource resource= new DefaultResource(newTile);
				manager.createResourceID(resource);
				resource.setName(getMap().getName());
				manager.addResourceChild(resource);
			}
		};
		createTileAction.setEnabled(!selection.isEmpty());
		editorActions.add(createTileAction);
		
		Action shearAction = new AbsAction("shear") {
			@Override
			public void actionPerformed(ActionEvent e) {
				Area area = new Area(selection.getExactSelection());
				Point p = tile.getStartingPoint();
				AffineTransform at= new AffineTransform();
				at.setToTranslation(p.x, p.y);
				area.transform(at);
				BufferedImage image=ImageUtil.subImage(tile.getImage(), area);
				int width= image.getWidth(TileEditorView.this);
				int height = image.getHeight(TileEditorView.this);
				
				Tile t= new CandidateTile();
				t.setName(map.getName());
				t.setImage(image);
				t.setTileStep(width, height);
				tile.setOccupieArea(new Dimension(1,1));
				
				Map newMap = map.create();
				newMap.setID((int) System.currentTimeMillis());
				newMap.setName(map.getName()+(System.currentTimeMillis()%100));
				newMap.setTileStep(width, height);
				newMap.setExtension(1, 1);
				newMap.setSize(width, height);
				
				MapOffset offset=new MapOffset();
				offset.stepX = width;
				offset.stepY=height;
				newMap.setOffset(offset);
				
				Layer l= new DefaultLayer();
				l.add(t, 0, 0);
				newMap.addLayer(l, 0);
				editor.add(new TileEditorView(newMap,t,manager));
			}
		};
		shearAction.setEnabled(!selection.isEmpty());
		editorActions.add(shearAction);
		
		return editorActions;
	}
	
	
	@Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	g.drawImage(bufferImage, bounds.x, bounds.y, this);//绘制缓存图片
    }
	
	@Override
	protected void updateBufferImage() {
		Rectangle r=getMapBounds();
		bufferGraphics.clearRect(0, 0, r.width, r.height);
		
		fillBounds(bufferGraphics);//填充地图背景
    	drawBounds(bufferGraphics);// 绘制地图背景边缘
    	drawMap(bufferGraphics);//绘制地图
//    	drawGrid(bufferGraphics);//绘制网格
    	drawCoordinate(bufferGraphics);//绘制坐标
    	fillSelectionShadow(bufferGraphics);//填充选择区域背景
    	drawValidSelection(bufferGraphics);//绘制有效选择区域
    	drawExactSelection (bufferGraphics);//绘制实际选择区域
    	
    	validate();
    	repaint();
	}
	
	protected void drawExactSelection (Graphics2D g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setColor(Color.RED);
		
		Point p= tile.getStartingPoint();
		g2.translate(p.x, p.y);
		g2.draw(selection.getExactSelection());
		g2.dispose();
	}
	
	@Override
	protected void drawMap(Graphics2D g) {
		map.draw(0, 0,g);
	}
	


}
