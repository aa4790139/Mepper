package org.mepper.app.action;

import java.awt.event.ActionEvent;

import org.zhiwu.app.Application;
import org.zhiwu.app.action.AppAction;


/**
 * <B>HelpAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2009-11-5 created
 * @since map editor Ver 1.0
 * 
 */
public class HelpAction  extends AppAction {
	private static final long serialVersionUID = 1L;
	public static final String ID = "help";

	public HelpAction(Application app) {
		super(app,ID);
	}

	/**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	}

	@Override
	public String getID() {
		return ID;
	}
	
}
