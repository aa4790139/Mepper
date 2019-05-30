/* XMLWriter.java 1.0 2010-2-2
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
package org.mepper.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jdom.CDATA;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.ProcessingInstruction;
import org.mepper.editor.map.Layer;
import org.mepper.editor.map.Map;
import org.mepper.editor.tile.CompositeTile;
import org.mepper.editor.tile.CustomTile;
import org.mepper.editor.tile.DefaultTile;
import org.mepper.editor.tile.Tile;
import org.mepper.resources.LibraryResource;
import org.mepper.resources.ProjectResource;
import org.mepper.resources.PropertySupported;
import org.mepper.resources.StorableResource;
import org.mepper.utils.ImageUtil;
import org.zhiwu.xml.DefaultXMLManagerImpl;
import org.zhiwu.xml.XMLManager;

/**
 * <B>XMLWriter</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-3-2 created
 * @since org.map.editor.io Ver 1.0
 * 
 */
public class XMLWriter implements MapWriter{
	public static final String XMLSuffix = ".xml";
	public static final String ELEMENT_NAME_LIBRARY="library";
	public static final String ELEMENT_NAME_PROJECT="project";
	public static final String ELEMENT_NAME_TILE="tile";
	public static final String ELEMENT_NAME_CUSTOM="custom";
	public static final String ELEMENT_NAME_MAP="map";
	public static final String ELEMENT_NAME_COMPOSITE_TILE="composite";

	@Override
	public void writeMap(Map map, String path) throws IOException {
	}

	@Override
	public void writeMap(Map map, OutputStream out) throws IOException {
		Element root= new Element("mepper");
		Element mapElement = createElement();
		writeStorableObject(map, mapElement);
		setStorableName(map, mapElement);
		writeProperties(mapElement, map);
		writeMap(map, mapElement);
		root.addContent(mapElement);
		writeMepperFile(out, root);
	}

	private void writeMepperFile(OutputStream out, Element root) {
		Document doc = new Document(root);
		ProcessingInstruction pi=new ProcessingInstruction("mepper_resource", "version=\"1.1.2.2011032176\"");
		doc.addContent(pi);
		XMLManager manager =new DefaultXMLManagerImpl(doc);
		manager.saveToStream(out);
	}
	
	@Override
	public void writeResource(StorableResource resource, String path)
			throws IOException {
		File f=new File(getFileName(resource,path));
		FileOutputStream fos = new FileOutputStream(f);
		writeResource(resource,fos) ;
		fos.close();
	}
	
	@Override
	public void writeResource(StorableResource resource, OutputStream out)
			throws IOException {
		Element root=writeResource(resource);
		writeMepperFile(out, root);
	}
	
	private Element writeResource(StorableResource r) { 
		Element e=createElement();
		
		writeStorableObject(r, e);
		setResourceName(r,e);
		writeProperties(e,r);
		
		Storable source = r.getSource();
		if (source instanceof DefaultTile) {
			writeTile((DefaultTile) source,e); 
			return e; //tile have no child
		} 
		
		if(source instanceof CustomTile){
			writeCustom((CustomTile)source,e);
			return e;//Custom tile have no child
		}
		
		if(source instanceof Map){
			writeMap((Map)source,e);
			return e;
		}
		
		for(int i=0,j=r.getChildCount();i<j;i++){
			e.addContent(writeResource(r.getChild(i)));
		}
		return e;
	}
	
	private void writeMap(Map map, Element e) {
		Dimension ext =map.getExtension();
		e.setAttribute("rows",ext.width+"");
		e.setAttribute("columns", ext.height+"");
		e.setAttribute("tileWidth",map.getTileWidth()+"");
		e.setAttribute("tileHeight",map.getTileHeight()+"");
		e.setAttribute("type",map.getClass().getName());
		
		List<Tile> tiles=new LinkedList<Tile>();
		Element resourceElement = new Element("resources");
		
		for(int i=0,j=map.getLayerCount();i<j;i++){
			Layer l =map.getLayer(i);
			
			Element layerElement=new Element("layer");
			layerElement.setAttribute("index",i+"");
			layerElement.setAttribute("name", l.getName());
			writeProperties(layerElement, l);
			e.addContent(layerElement);
			
			writeCompositeTile(tiles, l, layerElement);
		}
		
		for(Tile t:tiles){
			Element tileElement  =createElement();
			setStorableName(t,tileElement);
			writeStorableObject(t, tileElement);
			writeProperties(tileElement, t);
			writeTile(t, tileElement);
			resourceElement.addContent(tileElement);
		}
		e.addContent(resourceElement);
	}

	private void writeCompositeTile(List<Tile> tiles, CompositeTile compositeTile,
			Element compositeTileElement) {
		Element tilesElement=new Element("tiles");
		compositeTileElement.addContent(tilesElement);
		
		Dimension d = compositeTile.getOccupieArea();
		for(int p=0;p<d.width;p++){
			for(int q=0;q<d.height;q++){
				Tile tile=compositeTile.getTileAt(p, q);
				if(tile == null){
					continue;
				}
				Element tileElement = createElement();
				setStorableName(tile,tileElement);
				tileElement.setAttribute("id",tile.getID()+"");
				tileElement.setAttribute("x",p+"");
				tileElement.setAttribute("y",q+"");
				addMapResource(tiles,tile);
				tilesElement.addContent(tileElement);
				
				if(tile instanceof CompositeTile){
					writeCompositeTile(tiles, (CompositeTile)tile, tileElement);
				}
			}
		}
	}

	private void addMapResource(List<Tile> tiles, Tile tile) {
		for(Tile t:tiles){
			if(t.equals(tile)){
				return ;
			}
		}
		tiles.add(tile);
	}

	private void writeCustom(CustomTile tile, Element e) {
		if(tile.isUsedColor()){
			Element colorElement = new Element("color");
			Color color=tile.getColor();
			colorElement.setAttribute("r", color.getRed()+"");
			colorElement.setAttribute("g", color.getGreen()+"");
			colorElement.setAttribute("b", color.getBlue()+"");
			e.addContent(colorElement);
		}else {
			writeimage(e, tile.getImage());
		}
		
	}

	private void writeTile(Tile tile, Element e) {
		Element occupieElement  = new Element("occupie");
		Dimension occupie = tile.getOccupieArea();
		occupieElement.setAttribute("width", occupie.width+"");
		occupieElement.setAttribute("height", occupie.height+"");
		e.addContent(occupieElement);
		
		Element startElement  = new Element("starting_point");
		Point start = tile.getStartingPoint();
		startElement.setAttribute("x", start.x+"");
		startElement.setAttribute("y", start.y+"");
		e.addContent(startElement);
		
		Element stepElement = new Element("step");
		stepElement.setAttribute("tileWidth",tile.tileWidth()+"");
		stepElement.setAttribute("tileHeight", tile.tileHeight()+"");
		e.addContent(stepElement);
		
		writeimage(e, tile.getImage());
		
		
		
		
	}

	private void writeimage(Element e,BufferedImage  image) {
		Element imageElement=new Element("image");
		imageElement.setAttribute("width", image.getWidth()+"");
		imageElement.setAttribute("height", image.getHeight()+"");
		
		int[] imageArray= ImageUtil.imageToArray(image);
		CDATA cdata=new CDATA(Arrays.toString(imageArray));
		imageElement.addContent(cdata);
		e.addContent(imageElement);
	}

	private void writeStorableObject(Storable r, Element e) {
		e.setAttribute("id", r.getID()+"");
		e.setAttribute("name", r.getName());
		e.setAttribute("description", r.getDescription());
	}
	
	private void setResourceName(StorableResource r,Element e){
		if(r instanceof LibraryResource ){
			 e.setName(ELEMENT_NAME_LIBRARY);
			 return;
		}
		if(r instanceof ProjectResource){
			e.setName(ELEMENT_NAME_PROJECT);
			 return;
		}
		Storable source = r.getSource();
		setStorableName(source,e);
	}
	
	private void setStorableName(Storable source, Element e) {
		if (source instanceof DefaultTile) {
			e.setName(ELEMENT_NAME_TILE);
			return;
		}
		if (source instanceof CustomTile) {
			e.setName(ELEMENT_NAME_CUSTOM);
			return;
		}
		if(source instanceof CompositeTile){
			e.setName(ELEMENT_NAME_COMPOSITE_TILE);
			return;
		}
		if (source instanceof Map) {
			e.setName(ELEMENT_NAME_MAP);
			return;
		}
		e.setName("unsupported_element");
	}

	private void writeProperties(Element e,PropertySupported property) {
		Element properties=new Element("properties");
		for(String name:property.propertyNames()){
			Element p=new Element("property");
			p.setAttribute("name", name);
			p.setAttribute("value", property.getProperty(name));
			properties.addContent(p);
		}
		e.addContent(properties);
	}

	private String getFileName(StorableResource resource, String path) {
		StringBuilder sb=new StringBuilder(path);
		if(path.endsWith(File.separator)){
			sb.append(File.separator);
		}
		sb.append(resource.getName());
		sb.append(".");
		sb.append(System.currentTimeMillis()+"");
		sb.append(".");
		sb.append(resource.getID());
		sb.append(XMLSuffix);
		return sb.toString();
	}

	
	
	
	private Element createElement() {
		return new Element("unsupported_element");
	}
	
	
	@Override
	public FileFilter[] getFileFilters() {
		return new FileFilter[]{new FileNameExtensionFilter("xml document", "xml")};
	}


}
