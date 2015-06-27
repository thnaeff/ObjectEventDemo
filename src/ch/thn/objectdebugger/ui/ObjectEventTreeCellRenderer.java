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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.WeakHashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.tree.DefaultTreeCellRenderer;

import ch.thn.objectdebugger.ObjectNodeValue;
import ch.thn.objectdebugger.ui.ObjectEventDemoList.ObjectEventDemoListCellRenderer;
import ch.thn.util.tree.ListTreeNode;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectEventTreeCellRenderer extends DefaultTreeCellRenderer implements MouseListener {
	private static final long serialVersionUID = -1596390738443801320L;

	private static WeakHashMap<Object, JLabel> labels = new WeakHashMap<>();

	private static final Border borderActive = BorderFactory.createLineBorder(Color.green.darker());
	private static final Border borderInactive = BorderFactory.createLineBorder(Color.white);

	private JTree tree = null;

	/**
	 * 
	 * 
	 * @param tree
	 */
	public ObjectEventTreeCellRenderer(JTree tree) {
		this.tree = tree;
	}


	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean selected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {

		@SuppressWarnings("unchecked")
		ObjectNodeValue node = ((ListTreeNode<ObjectNodeValue>)value).getNodeValue();

		JLabel l = null;

		if (node == null) {
			return new JLabel();
		}

		if (!labels.containsKey(node.getObject())) {
			l = new JLabel(value.toString());
			l.setFont(l.getFont().deriveFont(Font.PLAIN));
		} else {
			l = labels.get(node.getObject());
		}



		//l.setToolTipText(node.getObject().toString());

		if (selected) {
			l.setOpaque(true);
			l.setBackground(ObjectEventDemoListCellRenderer.cSelected);
		} else {
			l.setBackground(null);
		}

		if (node.getObject() instanceof Component) {
			labels.put(node.getObject(), l);

			((Component)node.getObject()).addMouseListener(this);
		}

		return l;
	}


	@Override
	public void mouseClicked(MouseEvent e) {
	}


	@Override
	public void mouseEntered(MouseEvent e) {

		if (labels.containsKey(e.getSource())) {
			JLabel l = labels.get(e.getSource());

			l.setBorder(borderActive);

			//Is repainting the whole tree necessary?
			tree.invalidate();
			tree.repaint();
		}

	}


	@Override
	public void mouseExited(MouseEvent e) {

		if (labels.containsKey(e.getSource())) {
			JLabel l = labels.get(e.getSource());

			l.setBorder(borderInactive);

			//Is repainting the whole tree necessary?
			tree.invalidate();
			tree.repaint();
		}

	}


	@Override
	public void mousePressed(MouseEvent e) {
	}


	@Override
	public void mouseReleased(MouseEvent e) {
	}

}
