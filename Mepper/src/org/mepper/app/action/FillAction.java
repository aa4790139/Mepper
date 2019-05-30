package org.mepper.app.action;

import java.awt.event.ActionEvent;

import org.zhiwu.app.Application;
import org.zhiwu.app.action.AppAction;


/**
 * <B>FillAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2009-11-5 created
 * @since map editor Ver 1.0
 * 
 */
public class FillAction extends AppAction{
	public static final long serialVersionUID = 1L;
	public static final String ID="fill";

	/**
	 * 
	 * @since map editor
	 * @param app
	 */
	public FillAction(Application app) {
		super(app,ID);
	}

	/** 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
	@Override
	public String getID() {
		return ID;
	}
	
} 
