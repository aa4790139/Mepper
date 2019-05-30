/* ImageListModel.java 1.0 2010-2-2
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
package org.mepper.gui;

import java.awt.Component;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import org.mepper.resources.ImageObject;
import org.mepper.utils.ImageUtil;


/**
 * <B>ImageListModel</B>.
 * a proxy of ListModel.
 * @author Zhi-Wu Chen. Email: <a
 *         href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2010-12-26 created
 * @since org.map.editor.gui Ver 1.0
 */
public class ImageListModel extends AbstractListModel{
	private final List<ImageObject> data;
	private final Map<String,Image> images;// for cache.
	private int imageWidth=48;
	private int imageHeight=24;
	
	/**
	 * The ListCellRender for the ImageListModel.
	 */
	private class  ImageTextCellRenderer extends JLabel implements ListCellRenderer{
		public ImageTextCellRenderer() {
			setOpaque(true);
		}
		@Override
		public Component getListCellRendererComponent(JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			
			if (value != null) {
//				setText(value.toString());
				Image image=getImage(value);
				setIcon(new ImageIcon(image));
			}
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			return this;
		}
	}
	private ImageTextCellRenderer renderer;
	public ImageListModel() {
//		renderer=new ImageTextCellRenderer();
		data=new ArrayList<ImageObject>();
		images=new HashMap<String, Image>();
	}
	
	public Image getImage(Object value) {
		Image im=images.get(value.toString());
		if(im==null){
			ImageObject io=getObject(value.toString());
			im=ImageUtil.scalImage(io.getImage(),imageWidth, imageHeight);
			images.put(value.toString(), im);
		}
		return im;
	}
	
	protected ImageObject getObject(Object key){
		if (key != null) {
			for(ImageObject t:data){
				if (t.getName().equals(key.toString())) {
					return t;
				}
			}
		}
		return null;
	}

	public void add(Collection<ImageObject> c){
		data.addAll(c);
	}
	
	public void add(ImageObject o){
		data.add(o);
		fireContentsChanged( o, 0, 1);
	}
	
	@Override
	public int getSize() {
		return data.size();
	}
	
	@Override
	public Object getElementAt(int index) {
		return data.get(index).getName();
	}
	
	public Object getData(int index){
		return data.get(index);
	}
	
	
	public ListCellRenderer getCellRenderer(){
		if(renderer ==null){
			renderer=new ImageTextCellRenderer();
		}
		return renderer;
	}

	public void setImageDimension(int width, int height) {
		imageWidth = width;
		imageHeight =height;
	}

	public void clear() {
		data.clear();
		images.clear();
	}

	public void remove(int index) {
		 ImageObject obj=data.remove(index);
		 fireContentsChanged( obj, index, index);
	}
	
	
}
