/* CoverableTool.java 1.0 2010-2-2
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

import java.awt.Rectangle;

import org.mepper.editor.map.Layer;
import org.mepper.editor.map.Map;
import org.mepper.editor.tile.Tile;

/**
 * <B>CoverableTool</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-5-5 created
 * @since org.mepper.tool Ver 1.0
 * 
 */
public class CoverableTool extends SelectionTool {
	protected boolean cover;
	
	@Override
	public PaddingOption getOptionComponent() {
		if(component == null){
			PaddingOption option=new PaddingOption();
			option.setTool(this);
			component = option;
		}
		return (PaddingOption) component;
	}
	public void setCover(boolean b) {
		cover = b;
	}
	
	/**
	 * Gets the tile cover the specific point(x,y) of the selected layers of the map.
	 * In the non-cover({@link #cover}==null) state, the conflicting area will be selected.
	 * @return null if no tile cover the point.
	 * @see MapTileSelection#setSelection(Shape)
	 */
	protected Tile getTileCover(int x,int y,int width, int height){ 
		Map map = getMap();
		if(! cover){
			Rectangle tileStart = map.isCover(x,y,width,height);
			if (tileStart != null) {
				Tile t=null;
				for(Layer layer:map.getAllSelectedLayers()){
					if( (t=layer.getTileAt(tileStart.x, tileStart.y))!=null){
						setSelection(map.getBounds(tileStart.getLocation(), tileStart.getSize(), false));
						return t;
					}
				}
			}
		}
		return null;
	}
}
