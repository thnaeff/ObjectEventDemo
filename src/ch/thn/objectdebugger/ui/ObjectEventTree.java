/**
 *    Copyright 2014 Thomas Naeff (github.com/thnaeff)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package ch.thn.objectdebugger.ui;

import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import ch.thn.objectdebugger.ObjectNodeValue;
import ch.thn.util.tree.ListTreeNode;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectEventTree extends JTree {
	private static final long serialVersionUID = 8194467764424712410L;


	/**
	 * 
	 * 
	 * 
	 * @param model
	 */
	public ObjectEventTree(TreeModel model) {
		super(model);

		ToolTipManager.sharedInstance().registerComponent(this);

	}


	@Override
	public String getToolTipText(MouseEvent e) {
		if (getRowForLocation(e.getX(), e.getY()) == -1) {
			return null;
		}

		TreePath curPath = getPathForLocation(e.getX(), e.getY());

		@SuppressWarnings("unchecked")
		ObjectNodeValue node = ((ListTreeNode<ObjectNodeValue>)curPath.getLastPathComponent()).getNodeValue();

		Object obj = node.getObject();

		String toolTip = null;

		if (obj instanceof JLabel) {
			toolTip = ((JLabel)obj).getText();
		} else if (obj instanceof JTextComponent) {
			toolTip = ((JTextComponent)obj).getText();
		} else if (obj instanceof JButton) {
			toolTip = ((JButton)obj).getText();
		} else if (obj instanceof JMenuItem) {
			toolTip = ((JMenuItem)obj).getText();
		} else if (obj instanceof JMenu) {
			toolTip = ((JMenu)obj).getText();
		} else if (obj instanceof JColorChooser) {
			toolTip = ((JColorChooser)obj).getColor().toString();
		} else if (obj instanceof JFrame) {
			toolTip = ((JFrame)obj).getTitle();
		} else if (obj instanceof JCheckBox) {
			toolTip = ((JCheckBox)obj).getText();
		} else if (obj instanceof JRadioButton) {
			toolTip = ((JRadioButton)obj).getText();
		} else if (obj instanceof JSpinner) {
			toolTip = ((JSpinner)obj).getValue().toString();
		} else {
			toolTip = obj.toString();
		}

		return toolTip;

	}



}
