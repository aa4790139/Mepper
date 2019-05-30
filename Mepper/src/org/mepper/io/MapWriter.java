/* MapWriter.java 1.0 2010-2-2
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

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.filechooser.FileFilter;

import org.mepper.editor.map.Map;
import org.mepper.resources.StorableResource;


/**
 * <B>MapWriter</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-3-1 created
 * @since org.map.editor.io Ver 1.0
 * 
 */
public interface MapWriter extends Pluginable{
	void writeMap(Map map,String path) throws IOException;
	void writeMap(Map map,OutputStream out) throws IOException;
	
	void writeResource(StorableResource resource,String path) throws IOException;
	void writeResource(StorableResource resource,OutputStream out) throws IOException;
	FileFilter[] getFileFilters();
	

}
