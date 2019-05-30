/* SelectionTool.java 1.0 2010-2-2
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
package org.mepper.tool;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import org.mepper.editor.EditorView;
import org.mepper.editor.Selection;
import org.mepper.editor.map.Layer;
import org.mepper.editor.map.Map;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.action.AbsAction;
import org.zhiwu.utils.AppResources;


/**
 * <B>SelectionTool</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-8 created
 * @since org.mepper.tool Ver 1.0
 * 
 */
public class SelectionTool extends AbstractTool{
	/// tool
	protected Dimension extensionStep = new Dimension(1,1);
	protected Dimension extension= extensionStep; // 绘制长宽
	protected Point startPoint; // map point ..// 填充起始点
	
	@Override
	public void mousePressed(MouseEvent e) {
		super.mousePressed(e);
		if (popupTrigger) {
			return;
		}
		begin(e);
		fireToolChanged();
	}

	
	protected void begin(MouseEvent e) {//记录填充起始点
		Point mousePoint = e.getPoint();
		EditorView view=editor.getActivateView();
		view.getComponent().requestFocus();
		Map map=editor.getActivateView().getMap(); 
		
		startPoint = view.screenToMap(mousePoint);
		setSelection(map.getBounds(startPoint, extensionStep,true));
	}
	
	
	protected void setSelection(Shape shape){//设置选择区域
		Selection s=editor.getActivateView().getSelection();
		s.reset();
		s.add(shape);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {//鼠标拖动
		if (SwingUtilities.isLeftMouseButton(e) && !popupTrigger) {
			finish(e);
			fireToolChanged();
		}
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {//鼠标释放
		if (SwingUtilities.isLeftMouseButton(e) && !popupTrigger) {
			finish(e);
			fireToolDone();
		}
	}
	
	protected void finish(MouseEvent e) {
		Point mousePoint = e.getPoint();
		EditorView view=editor.getActivateView();
		Map map=view.getMap(); 
		
		Point finishPoint=view.screenToMap(mousePoint);
		Dimension newExtension = map.getExtension(
				startPoint,finishPoint);
		newExtension = reviseExtension(newExtension);
		if(!newExtension.equals(extension)){
			setSelection(map.getBounds(startPoint, extension=newExtension,true));
		} 
	}
	
	private Dimension reviseExtension(Dimension other) {
		int facX = other.width<0 ? -1:1;
		int facY = other.height<0 ? -1:1;
		plusMinusTransition(other,facX,facY);
		
		int p;
		p= other.width  % extensionStep.width;
		other.width =( p == 0) ? other.width : other.width+extensionStep.width -p;
		p= other.height % extensionStep.height;
		other.height  =( p == 0) ? other.height : other.height+extensionStep.height -p;
		
		plusMinusTransition(other,facX,facY);
		return other;
	}

	private void plusMinusTransition(Dimension other, int facX, int facY) {
		other.width *= facX;
		other.height *= facY;
	}


	public void setExtensionStep(Dimension e) {
//		this.extensionStep.width  = Math.abs(e.width);
//		this.extensionStep.height = Math.abs(e.height);
		this.extensionStep = e;
	}
	
	protected int[][] getDrawArea() {
		// 指示因子,用于判断extension为负的情况
		int facX = extension.width < 0 ? -1 : 1;
		int facY = extension.height < 0 ? -1 : 1;

		int rows = extension.width * facX;
		int columns = extension.height * facY;

		int p=rows/extensionStep.width;
		int q=columns/extensionStep.height;
		int counter=0;
		int[][] area =new int[p * q][2];
		for (int i = 0 ; i < p;i++) {
			for (int j = 0 ; j < q;j++) {
				area[counter][0]=i*extensionStep.width*facX +startPoint.x;
				area[counter][1]=j*extensionStep.height*facY + startPoint.y;
				counter++;
			}
		}
		return area;
	}
	
	@Override
	public String getID() {
		return "tool.selection";
	}
	
	
	
	/// popup menu
	@Override
	protected void handlePopupMenu(final MouseEvent e) {
		Point screenPoint=e.getPoint();
		JPopupMenu menu= getSelectionPopupMenu(screenPoint);
        menu.show(editor.getActivateView().getComponent(), screenPoint.x, screenPoint.y);
	}
	
	
	
	
	protected JPopupMenu getSelectionPopupMenu(Point screenPoint) {
		JPopupMenu menu=  new JPopupMenu();
		addSelectionMenuTo(menu,screenPoint);
		return menu;
	}


	private void addSelectionMenuTo(JPopupMenu menu, Point screenPoint) {
		AppResources resources = AppManager.getResources();
		EditorView view=editor.getActivateView();
		final Map map =view.getMap();
		if(startPoint == null){
			startPoint= view.screenToMap(screenPoint);
		}
		if(extension ==null){
			extension = new Dimension();
			setSelection(map.getBounds(startPoint, extension, false));
		}
		
		addEditMenu(menu, map, map.getTileBounds(startPoint.x,startPoint.y));
		menu.add(new JSeparator());
		addSelectionMenu(menu, resources, map);
	}

	private void addSelectionMenu(JPopupMenu menu, AppResources resources,
			final Map map) {
		///selection layer
		JMenu selectionMenu = new JMenu();
		resources.configMenu(selectionMenu, "select");
		for(int i=map.getLayerCount()-1;i>-1;i--){
			Layer layer = map.getLayer(i);
			LayerSelectionAction layerAction = new LayerSelectionAction(layer);
			JCheckBoxMenuItem layerItem = new JCheckBoxMenuItem(layerAction);
			layerItem.setSelected(layer.isSelected());
			selectionMenu.add(layerItem);
		}
		menu.add(selectionMenu);
	}

	private void addEditMenu(JPopupMenu menu, final Map map,
			final Rectangle bounds) {
		/// edit menu
		Action copyAction =new AbsAction("copy") {
			@Override
			public void actionPerformed(ActionEvent e1) {
			}
		};
		copyAction.setEnabled(bounds != null);
		menu.add(copyAction);
		
		Action cutAction =new AbsAction("cut") {
			@Override
			public void actionPerformed(ActionEvent e1) {
			}
		};
		cutAction.setEnabled(bounds != null);
		menu.add(cutAction);
		
		Action pasteAction =new AbsAction("paste") {
			@Override
			public void actionPerformed(ActionEvent e1) {
			}
		};
		pasteAction.setEnabled(bounds != null);
		menu.add(pasteAction);
		
		Action deleteAction =new AbsAction("delete") {
			@Override
			public void actionPerformed(ActionEvent e1) {
					map.removeTile(getDrawArea());
			}
		};
		menu.add(deleteAction);
	}

	class LayerSelectionAction extends AbsAction{
		Layer layer ;
		
		public LayerSelectionAction(Layer layer) {
			super(layer.getName());
			setLayer(layer);
		}

		@Override
		public void actionPerformed(ActionEvent e) { 
			Map map =getMap();
			int layerCount = map.getLayerCount();
			for(int i=0;i<layerCount;i++){
				Layer l = map.getLayer(i);
				l.setSelection(false);
			}
			layer.setSelection(true);
		}
		public void setLayer(Layer layer){ 
			this.layer = layer;
		}
	}

}
