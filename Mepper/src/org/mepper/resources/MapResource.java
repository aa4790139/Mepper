/* MapResource.java 1.0 2010-2-2
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
package org.mepper.resources;

import org.mepper.editor.map.Layer;
import org.mepper.editor.map.Map;
import org.mepper.editor.tile.Tile;

/**
 * <B>MapResource</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-26 created
 * @since org.mepper.resources Ver 1.0
 * 
 */
public class MapResource extends DefaultResource{

	public MapResource() {
		super(null);
	}

	private void readChildren(Map map) {
		for(int i=0,j=map.getLayerCount();i<j;i++){
			Layer layer =map.getLayer(i);
			DefaultResource layerResource=new DefaultResource(layer);
			addChild(layerResource);
			
			for(int x=0,w=layer.getOccupieArea().width;x<w;x++){
				for(int y=0,h=layer.getOccupieArea().height;y<h;y++){
					Tile tile = layer.getTileAt(x, y);
					if(tile !=null){
						DefaultResource tilerResource = new DefaultResource(tile);
						layerResource.addChild(tilerResource);
					}
				}
			}
		}		
	}
	
	
	

}
