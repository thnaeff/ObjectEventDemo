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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ch.thn.guiutil.Loader;
import ch.thn.guiutil.component.imageanimation.ImageAnimationLabelFading;
import ch.thn.objectdebugger.ObjectNodeValue;
import ch.thn.objectdebugger.ObjectNodeValue.ObjectNodeValueListener;
import ch.thn.util.valuerange.ImageAlphaGradient;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectEventDemoList extends JList<String> implements ObjectNodeValueListener {
	private static final long serialVersionUID = 2900971816403885162L;


	public static final int TYPE_EVENT_INTERFACE = 1;
	public static final int TYPE_EVENT_METHOD = 2;
	public static final int TYPE_EVENT_OBJECT = 3;


	private DefaultListModel<String> model = null;

	private int type = TYPE_EVENT_INTERFACE;


	private Map<String, Set<ObjectNodeValue>> displayedNodes = null;

	private ObjectEventDemoListCellRenderer cellRenderer = null;


	/**
	 * 
	 * 
	 * @param type
	 */
	public ObjectEventDemoList(int type) {
		super(new DefaultListModel<String>());

		this.model = (DefaultListModel<String>)getModel();
		this.type = type;

		displayedNodes = new HashMap<>();

		cellRenderer = new ObjectEventDemoListCellRenderer();
		setCellRenderer(cellRenderer);

	}

	/**
	 * 
	 * 
	 * @param nodes
	 */
	public void setNodesToDisplay(Map<String, Collection<ObjectNodeValue>> selectedValuesAndNodes) {

		model.removeAllElements();

		for (Collection<ObjectNodeValue> nodes : displayedNodes.values()) {
			for (ObjectNodeValue node : nodes) {
				node.removeObjectNodeValueListener(this);
			}
		}

		displayedNodes.clear();
		cellRenderer.reset();

		Set<String> allSelected = selectedValuesAndNodes.keySet();

		for (Collection<ObjectNodeValue> nodes : selectedValuesAndNodes.values()) {

			for (ObjectNodeValue node : nodes) {

				switch (type) {
				case TYPE_EVENT_INTERFACE:
					Collection<Class<?>> interfaces = node.getEventInterfaces();
					for (Class<?> c : interfaces) {
						addListItem(c.getSimpleName(), node);
					}
					break;
				case TYPE_EVENT_METHOD:
					Collection<Method> methods = node.getEventInterfaceMethods(allSelected);
					for (Method m : methods) {
						addListItem(m.getName(), node);
					}
					break;
				case TYPE_EVENT_OBJECT:
					Collection<Class<?>> classes = node.getEventClasses(allSelected);
					for (Class<?> c : classes) {
						addListItem(c.getSimpleName(), node);
					}
					break;
				default:
					break;
				}

			}

		}

	}

	/**
	 * 
	 * 
	 * @param display
	 * @param node
	 */
	private void addListItem(String display, ObjectNodeValue node) {
		if (display != null) {

			if (!displayedNodes.containsKey(display)) {
				displayedNodes.put(display, new HashSet<ObjectNodeValue>());

				//It is a new one
				//Only add it once to the list
				model.addElement(display);
			}

			//Link the displayed value to the node
			displayedNodes.get(display).add(node);

			node.addObjectNodeValueListener(this);
		}
	}

	/**
	 * 
	 * 
	 */
	public void selectAll() {
		setSelectionInterval(0, displayedNodes.size() - 1);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Map<String, Collection<ObjectNodeValue>> getSelected() {
		Map<String, Collection<ObjectNodeValue>> selected = new LinkedHashMap<>();

		List<String> selectedValues = getSelectedValuesList();

		for (String s : selectedValues) {
			selected.put(s, displayedNodes.get(s));
		}

		return selected;
	}


	@Override
	public void valuesChanged(ObjectNodeValue node, String eventName, String eventClassName) {

		switch (type) {
		case TYPE_EVENT_INTERFACE:

			break;
		case TYPE_EVENT_METHOD:
			cellRenderer.animate(eventName);
			break;
		case TYPE_EVENT_OBJECT:
			cellRenderer.animate(eventClassName);
			break;
		default:
			break;
		}

	}




	/**************************************************************************
	 * 
	 *
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	protected static class ObjectEventDemoListCellRenderer implements ListCellRenderer<String> {

		private Map<String, ImageAnimationLabelFading> displayedLabels = null;

		public final static Color cSelected = new Color(195, 246, 185);

		/**
		 * 
		 * 
		 */
		public ObjectEventDemoListCellRenderer() {

			displayedLabels = new WeakHashMap<>();

		}

		/**
		 * 
		 * 
		 */
		public void reset() {
			displayedLabels.clear();
		}

		@Override
		public Component getListCellRendererComponent(
				JList<? extends String> list, String value, int index,
				boolean isSelected, boolean cellHasFocus) {

			ImageAnimationLabelFading l = null;



			if (!displayedLabels.containsKey(value)) {
				l = new ImageAnimationLabelFading(list);
				l.setText(value);
				l.setIcon(Loader.loadIcon("/ledgreen.png"));
				l.addStep(new ImageAlphaGradient(ImageAlphaGradient.FADE_OUT, 15), 80, 0, 0);
				l.animate(1);

				l.setFont(l.getFont().deriveFont(Font.PLAIN));

				displayedLabels.put(value, l);
			} else {
				l = displayedLabels.get(value);
			}

			if (isSelected) {
				l.setOpaque(true);
				l.setBackground(cSelected);
			} else {
				l.setOpaque(false);
			}


			return l;
		}

		/**
		 * 
		 * 
		 * @param value
		 */
		public void animate(String value) {
			if (displayedLabels.containsKey(value)) {
				displayedLabels.get(value).animate(1);
			}
		}

	}


}
