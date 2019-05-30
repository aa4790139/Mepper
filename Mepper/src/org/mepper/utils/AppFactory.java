package org.mepper.utils;

import javax.swing.JMenu;
import javax.swing.JSplitPane;

import org.zhiwu.utils.AppResources;

/**
 * <B>AppFactory</B>
 * 
 * @author 陈智武 Email: <a
 *         href="mailto:Java.mepper@gmail.com">Java.mepper@gmail.com</a>
 * @version Ver 1.0.01 2009-9-25 created
 * @since map editor Ver 1.0
 * 
 */
public class AppFactory {




	/**
	 * A convenient method for create a JSplitPane.
	 * 
	 * Creates a new <code>JSplitPane</code> with the specified orientation 
	 * 
	 * @param orientation
	 *            <code>JSplitPane.HORIZONTAL_SPLIT</code> or
	 *            <code>JSplitPane.VERTICAL_SPLIT</code>
	 * @exception IllegalArgumentException
	 *                if <code>orientation</code> is not one of HORIZONTAL_SPLIT
	 *                or VERTICAL_SPLIT
	 */
	public static JSplitPane createSplitPane(int orientation) {
		JSplitPane splitPane = new JSplitPane();

		splitPane.setDividerSize(8);
		splitPane.setOrientation(orientation);
		splitPane.setResizeWeight(0.8);
		splitPane.setAutoscrolls(true);
		splitPane.setContinuousLayout(true);
		splitPane.setOneTouchExpandable(true);

		return splitPane;
	}

	/**
	 * 
	 * @param menu
	 * @param string
	 * @param resource
	 */
	@Deprecated // 耦合性过高 单独在ResourceBundUtils中实现，可实现解耦
	public static void configureMenu(JMenu menu, String string,
			AppResources resource) {
		// TODO Auto-generated method stub

	}
}
