/* CandidateTile.java 1.0 2010-2-2
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

import java.awt.image.BufferedImage;


/**
 * <B>CandidateTile</B>
 * hold a image which used to generate tile by the specified Shape.
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2010-12-26 created
 * @since org.map.editor.tile.manager Ver 1.0
 * 
 */
public class ImageResources implements ImageObject{
	
	private BufferedImage image;
	private String name;

	@Override
	public boolean equals(Object obj) {
		if(obj==null){
			return false;
		}
		if(obj instanceof ImageResources){
			ImageResources other=(ImageResources)obj;
			return image.equals(other.getImage());
		}
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public BufferedImage getImage() {
		return image;
	}

	@Override
	public void setImage(BufferedImage image) {
		this.image=image;
	}


}

















