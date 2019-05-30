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
package org.mepper.editor.tile;

import java.awt.Graphics2D;

import org.mepper.editor.map.Map;

/**
 * <B>CandidateTile</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-22 created
 * @since org.mepper.editor.tile Ver 1.0
 * 
 */
public class CandidateTile extends AbstractCompositeTile{
	
	@Override
	public void draw(int x, int y, Graphics2D g,Map map) {
		g.drawImage(image, x, y, null);
	}

}
