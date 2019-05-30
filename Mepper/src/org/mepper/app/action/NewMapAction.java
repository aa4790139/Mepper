package org.mepper.app.action;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

import org.mepper.editor.gui.NewMapDialog;
import org.mepper.resources.ProjectManager;
import org.zhiwu.app.AppDialog;
import org.zhiwu.app.AppDialogListener;
import org.zhiwu.app.AppManager;
import org.zhiwu.app.Application;
import org.zhiwu.app.DialogEvent;
import org.zhiwu.app.action.AppAction;


/**
 * <B>NewMapAction</B>
 * 
 * @author Zhi-Wu Chen. Email: <a href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2011-9-29 created
 * @since org.mepper.app.action Ver 1.0
 * 
 */
public class NewMapAction extends AppAction {
	public static final String ID = "new.map";
	private final ProjectManager manager;

	public NewMapAction(Application app,ProjectManager manager) {
		super(app, ID);
		this.manager =manager;
	}

	/**
	 * Create a new Map. Before create a map, it is necessary to ask the
	 * user to save their map, if any.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		app.setEnable(false);
		if (manager.getCurrentProject() == null) {
			JOptionPane.showMessageDialog(app.getView().getComponenet(), AppManager.getResources().getString("please.select.a","project"));
			app.setEnable(true);
			return ;
		}
		final AppDialog dialog=new NewMapDialog(app,manager);
		dialog.showDialog(new AppDialogListener(){
			@Override
			public void optionSelection(DialogEvent evt) {
				dialog.dispose();
				app.setEnable(true);
			}
		}); 
	} 
	
	@Override
	public String getID() {
		return ID;
	}

}