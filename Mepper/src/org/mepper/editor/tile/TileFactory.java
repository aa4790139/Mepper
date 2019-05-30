/* TileFactory.java 1.0 2010-2-2
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
import java.awt.Point;

/**
 * <B>TileFactory</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-4-24 created
 * @since org.mepper.editor.tile Ver 1.0
 * 
 */
public class TileFactory {

	public static CandidateTile createCandidateTile(Tile tile) {
		CandidateTile newTile = new CandidateTile();
		cloneTile(tile, newTile);
		return newTile;
	}

	private static void cloneTile(Tile tile, Tile newTile) {
		newTile.setID(tile.getID());
		newTile.setStartingPoint((Point) tile.getStartingPoint().clone());
		newTile.setOccupieArea((Dimension) tile.getOccupieArea().clone());
		newTile.setImage(tile.getImage());
		newTile.setTileStep(tile.tileWidth(), tile.tileHeight());
		newTile.setName(tile.getName());
	}

	public static DefaultTile createDefaultTile(Tile tile) {
		DefaultTile newTile = new DefaultTile();
		cloneTile(tile, newTile);
		return newTile;
	}

	public static CompositeTile createCompositeTile(Tile tile) {
		CompositeTile newTile = new DefaultCompositeTile();
		cloneTile(tile, newTile);
		return newTile;
	}

}
