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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import ch.thn.guiutil.component.LabelledComponentPanel;
import ch.thn.guiutil.component.MultiSplitPanel;
import ch.thn.guiutil.tree.UITreeModel;
import ch.thn.objectdebugger.ObjectEventDemo;
import ch.thn.objectdebugger.ObjectNodeValue;
import ch.thn.objectdebugger.ObjectNodeValue.ObjectNodeValueListener;
import ch.thn.util.tree.ListTreeNode;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectEventDemoPanel extends MultiSplitPanel
implements TreeSelectionListener, ListSelectionListener, ObjectNodeValueListener {
	private static final long serialVersionUID = 3456039167557651126L;


	private ObjectEventTree objectTree = null;

	private UITreeModel<ListTreeNode<ObjectNodeValue>> objectTreeModel = null;

	private ObjectEventDemoList lstEventInterfaces = null;
	private ObjectEventDemoList lstEventMethods = null;
	private ObjectEventDemoList lstEventObjects = null;


	private LabelledComponentPanel pComponentInfo = null;

	private Collection<ObjectNodeValue> previousNodes = null;

	private Map<String, Map<String, JLabel>> currentMethodValueLabels = null;

	private ObjectEventDemo eventDemo = null;

	/**
	 * 
	 * 
	 * @param debugger
	 */
	public ObjectEventDemoPanel(ObjectEventDemo eventDemo) {
		super(MultiSplitPanel.ORIENTATION_HORIZONTAL_RIGHT);

		this.eventDemo = eventDemo;

		objectTreeModel = new UITreeModel<ListTreeNode<ObjectNodeValue>>(eventDemo.getObjectTree());

		objectTree = new ObjectEventTree(objectTreeModel);
		objectTree.setCellRenderer(new ObjectEventTreeCellRenderer(objectTree));
		objectTree.addTreeSelectionListener(this);
		objectTree.setBorder(BorderFactory.createTitledBorder("Object tree"));

		expandTree();

		addSplitComponent(new JScrollPane(objectTree));


		lstEventInterfaces = new ObjectEventDemoList(ObjectEventDemoList.TYPE_EVENT_INTERFACE);
		lstEventInterfaces.addListSelectionListener(this);
		lstEventInterfaces.setBorder(BorderFactory.createTitledBorder("Event Interfaces"));

		addSplitComponent(new JScrollPane(lstEventInterfaces));


		lstEventMethods = new ObjectEventDemoList(ObjectEventDemoList.TYPE_EVENT_METHOD);
		lstEventMethods.addListSelectionListener(this);
		lstEventMethods.setBorder(BorderFactory.createTitledBorder("Event Interface Methods"));

		addSplitComponent(new JScrollPane(lstEventMethods));


		lstEventObjects = new ObjectEventDemoList(ObjectEventDemoList.TYPE_EVENT_OBJECT);
		lstEventObjects.addListSelectionListener(this);
		lstEventObjects.setBorder(BorderFactory.createTitledBorder("Events"));

		addSplitComponent(new JScrollPane(lstEventObjects));



		pComponentInfo = new LabelledComponentPanel(1, false, true);
		pComponentInfo.setBorder(BorderFactory.createTitledBorder("Event values"));

		addSplitComponent(new JScrollPane(pComponentInfo));


		previousNodes = new HashSet<>();
		currentMethodValueLabels = new HashMap<>();
	}

	/**
	 * 
	 * 
	 */
	public void expandTree() {
		for (int i = 0; i < objectTree.getRowCount(); i++) {
			objectTree.expandRow(i);
		}
	}

	/**
	 * 
	 * 
	 * @param nodes
	 */
	private void updateEventInterfaces(Map<String, Collection<ObjectNodeValue>> nodes) {

		lstEventInterfaces.setNodesToDisplay(nodes);
		lstEventInterfaces.selectAll();

		updateEventMethods(lstEventInterfaces.getSelected());
	}

	/**
	 * 
	 * 
	 * @param methods
	 */
	private void updateEventMethods(Map<String, Collection<ObjectNodeValue>> nodes) {

		lstEventMethods.setNodesToDisplay(nodes);
		lstEventMethods.selectAll();

		updateEventObjects(lstEventMethods.getSelected());

	}

	/**
	 * 
	 * 
	 * @param methods
	 */
	private void updateEventObjects(Map<String, Collection<ObjectNodeValue>> nodes) {

		lstEventObjects.setNodesToDisplay(nodes);
		lstEventObjects.setSelectedIndex(0);

		updateComponentInfo(lstEventObjects.getSelected());

	}

	/**
	 * 
	 * 
	 * @param eventConfig
	 */
	private void updateComponentInfo(Map<String, Collection<ObjectNodeValue>> nodes) {
		for (ObjectNodeValue n : previousNodes) {
			n.removeObjectNodeValueListener(this);
		}

		previousNodes.clear();

		//Add new nodes
		for (Collection<ObjectNodeValue> n : nodes.values()) {
			previousNodes.addAll(n);

			//Listen to new nodes
			for (ObjectNodeValue node : n) {
				node.addObjectNodeValueListener(this);
			}
		}

		pComponentInfo.removeAll();
		currentMethodValueLabels.clear();

		for (Entry<String, Collection<ObjectNodeValue>> e : nodes.entrySet()) {
			String eventClassName = e.getKey();

			JLabel lSection = new JLabel(eventClassName);
			lSection.setOpaque(true);
			lSection.setBackground(Color.white);
			pComponentInfo.addLabelComponent("", lSection);

			currentMethodValueLabels.put(eventClassName, new LinkedHashMap<String, JLabel>());

			for (ObjectNodeValue n : e.getValue()) {
				for (Method m : n.getEventClassMethods(eventClassName).values()) {

					if (!currentMethodValueLabels.get(eventClassName).containsKey(m.getName())) {
						JLabel l = new JLabel("?");
						l.setFont(l.getFont().deriveFont(Font.PLAIN));
						pComponentInfo.addLabelComponent(m.getName() + ":", l);

						currentMethodValueLabels.get(eventClassName).put(m.getName(), l);
					}
				}
			}

		}

		pComponentInfo.revalidate();
		pComponentInfo.repaint();

	}


	@Override
	public void valueChanged(TreeSelectionEvent e) {
		Map<String, Collection<ObjectNodeValue>> selected = new HashMap<>();
		Set<Component> selectedComponents = new HashSet<>();

		if (e.getSource() == objectTree) {
			TreePath[] paths = objectTree.getSelectionPaths();

			if (paths != null) {
				for (TreePath path : paths) {
					@SuppressWarnings("unchecked")
					ListTreeNode<ObjectNodeValue> node = (ListTreeNode<ObjectNodeValue>)path.getLastPathComponent();
					selected.put(node.toString(), new ArrayList<ObjectNodeValue>());
					selected.get(node.toString()).add(node.getNodeValue());

					if (node.getNodeValue().getObject() instanceof Component) {
						selectedComponents.add((Component)node.getNodeValue().getObject());
					}
				}
			}

			eventDemo.showSelectedComponentBounds(selectedComponents);

			updateEventInterfaces(selected);
		}

	}


	@Override
	public void valueChanged(ListSelectionEvent e) {

		if (e.getSource() == lstEventInterfaces) {
			updateEventMethods(lstEventInterfaces.getSelected());
		} else if (e.getSource() == lstEventMethods) {
			updateEventObjects(lstEventMethods.getSelected());
		} else if (e.getSource() == lstEventObjects) {
			updateComponentInfo(lstEventObjects.getSelected());
		}

	}

	@Override
	public void valuesChanged(ObjectNodeValue node, String eventName, String eventClassName) {

		if (!currentMethodValueLabels.containsKey(eventClassName)) {
			return;
		}

		for (Entry<String, String> e : node.getMethodValues(eventClassName).entrySet()) {
			String methodName = e.getKey();
			String value = e.getValue();

			currentMethodValueLabels.get(eventClassName).get(methodName).setText(value);

		}

	}


}
