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
package ch.thn.objectdebugger;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.event.DocumentEvent;

import ch.thn.guiutil.effects.BusyOverlay;
import ch.thn.objectdebugger.processors.JColorChooserProcessor;
import ch.thn.objectdebugger.processors.JComponentProcessor;
import ch.thn.objectdebugger.processors.JFrameProcessor;
import ch.thn.objectdebugger.processors.JListProcessor;
import ch.thn.objectdebugger.processors.JMenuProcessor;
import ch.thn.objectdebugger.processors.JTableProcessor;
import ch.thn.objectdebugger.processors.JTextFieldProcessor;
import ch.thn.objectdebugger.processors.JTreeProcessor;
import ch.thn.objectdebugger.ui.ComponentInfo;
import ch.thn.objectdebugger.ui.ComponentOverlayPanel;
import ch.thn.objectdebugger.ui.ObjectEventDemoPanel;
import ch.thn.util.ReflectionUtil;
import ch.thn.util.object.ObjectTree;
import ch.thn.util.tree.ListTreeNode;

/**
 * 
 * 
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectEventDemo implements ComponentListener, MouseListener, MouseMotionListener, ContainerListener {


	private ComponentDebuggerObjectTreeNode objectTree = null;

	private Map<Object, ObjectNodeValue> sourceObjects = null;
	private Map<Component, ComponentInfo> allComponents = null;

	private Set<EventHandler> eventHandlers = null;

	private ComponentOverlayPanel overlayPanel = null;
	private BusyOverlay busyOverlay = null;

	private ObjectEventDemoPanel demoPanel = null;

	private Component rootComponent = null;

	private volatile int updateRequests = 0;
	private volatile boolean updating = false;

	private boolean doRefresh = false;


	/**
	 * 
	 * 
	 * @param component
	 */
	public ObjectEventDemo(Component component) {
		this.rootComponent = component;

		objectTree = new ComponentDebuggerObjectTreeNode(component);

		objectTree.registerProcessor(new JFrameProcessor());
		objectTree.registerProcessor(new JComponentProcessor());
		objectTree.registerProcessor(new JTextFieldProcessor());
		objectTree.registerProcessor(new JColorChooserProcessor());
		objectTree.registerProcessor(new JListProcessor());
		objectTree.registerProcessor(new JTableProcessor());
		objectTree.registerProcessor(new JTreeProcessor());
		objectTree.registerProcessor(new JMenuProcessor());

		eventHandlers = new HashSet<>();
		addEventHandler(new BasicEventHandler(this));

		sourceObjects = new HashMap<>();
		allComponents = new HashMap<>();

	}

	/**
	 * Creates a {@link JFrame} with the event info and the event and object selection
	 * 
	 * @return
	 */
	public JFrame createEventInfoFrame() {
		JFrame fInfo = new JFrame("ObjectEventDemo");

		demoPanel = new ObjectEventDemoPanel(this);

		fInfo.getContentPane().add(demoPanel);

		fInfo.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fInfo.setMinimumSize(new Dimension(1200, 500));
		fInfo.setLocationRelativeTo(null);

		return fInfo;
	}

	/**
	 * Sets the layered pane which is used to show tool tip information, component
	 * boundaries and such.
	 * 
	 * @param layeredPane
	 */
	public void setLayeredPane(JLayeredPane layeredPane) {
		if (overlayPanel == null) {
			overlayPanel = new ComponentOverlayPanel(layeredPane);
			overlayPanel.setEventPassThrough(true, true);
			overlayPanel.setColor(null);
		} else {
			overlayPanel.deactivate();
		}

		busyOverlay = new BusyOverlay(layeredPane, "Updating object tree...", false);

		doRefresh = true;
	}

	/**
	 * 
	 * 
	 * @param eventHandler
	 */
	public void addEventHandler(EventHandler eventHandler) {
		eventHandlers.add(eventHandler);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public ObjectTree<ObjectNodeValue> getObjectTree() {
		return objectTree;
	}

	/**
	 * 
	 * 
	 */
	public void update() {

		if (updating) {
			//Do not start update again, just add requests
			updateRequests++;
			return;
		}

		//At least one update request needed
		updateRequests++;

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {

				updating = true;
				busyOverlay.setVisible(true, 500);

				while (updateRequests > 0) {
					updateRequests = 0;

					objectTree.removeChildNodes();
					objectTree.buildTree();

					if (updateRequests > 0) {
						//Stop further execution if a new update request arrived
						continue;
					}

					//Set all the parent objects. They are needed to access the label names.
					//And also other processing of all the nodes...
					for (ListTreeNode<ObjectNodeValue> node : objectTree) {
						Object parentObject = null;
						ObjectNodeValue value = node.getNodeValue();

						if (!node.isRootNode()) {
							parentObject = node.getParentNode().getNodeValue().getObject();
						}

						value.setParent(parentObject);

						sourceObjects.put(value.getObject(), value);

						addListenersToObject(value);

						if (updateRequests > 0) {
							//Stop further execution if a new update request arrived
							break;
						}

					}

					if (updateRequests > 0) {
						//Stop further execution if a new update request arrived
						continue;
					}

					if (demoPanel != null) {
						demoPanel.expandTree();
					}

					if (updateRequests > 0) {
						//Stop further execution if a new update request arrived
						continue;
					}

					updateDone();

				}

				busyOverlay.setVisible(false);
				updating = false;

			}
		});


		synchronized (objectTree) {
			t.start();
		}

	}

	/**
	 * 
	 * 
	 */
	private void updateDone() {
		for (ListTreeNode<ObjectNodeValue> node : objectTree) {
			ObjectNodeValue value = node.getNodeValue();

			if (value.getObject() instanceof Container) {
				Container c = (Container)value.getObject();

				allComponents.put(c, new ComponentInfo(c));

				c.addComponentListener(this);
				c.addMouseListener(this);
				c.addMouseMotionListener(this);
				c.addContainerListener(this);
			}
		}
	}

	/**
	 * Adds all available listeners to the object in the given node value
	 * 
	 * @param container The {@link Container} to add the listeners to
	 * @param maxLevel
	 */
	private void addListenersToObject(ObjectNodeValue node) {
		Object obj = node.getObject();

		Class<?> c = obj.getClass();
		//		System.out.println(c.getSimpleName());


		Method[] methods = c.getMethods();

		for (Method m : methods) {
			String methodName = m.getName();

			Class<?>[] types = m.getParameterTypes();

			//Only with one single parameter
			if (types.length == 1) {
				Class<?> type = types[0];

				//Check if there is an event handler which matches the method parameter type
				for (EventHandler eventHandler : eventHandlers) {
					//Check if the current method matches and of the handlers patterns
					boolean matchingMethodFound = false;
					for (Pattern pattern : eventHandler.getEventHandlerMethodPatterns()) {
						Matcher matcher = pattern.matcher(methodName);
						if (matcher.find()) {
							matchingMethodFound = true;
						}
					}

					//Skip current handler if method does not match any of its patterns
					if (!matchingMethodFound) {
						continue;
					}

					if (type.isAssignableFrom(eventHandler.getClass())) {

						try {

							//							System.out.println("   -> " + methodName + "(" + eventHandler.getClass().getSimpleName() + ")");

							//Invoke method with one single parameter
							m.invoke(obj, eventHandler);

							node.addInvokedMethod(m);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							//
							continue;
						}

					}
				}


			}

		}


	}


	/**
	 * Process an event with an {@link EventObject}
	 * 
	 * @param eventName
	 * @param eventObject
	 */
	protected void processEvent(String eventName, EventObject eventObject) {
		ObjectNodeValue node = sourceObjects.get(eventObject.getSource());

		//System.out.println(node.getObjectDescription() + ": " + eventName + "(" + eventObject.getClass().getSimpleName() + ")");

		if (node == null) {
			System.err.println("No source object found for " + eventObject.getSource());
			return;
		}

		node.updateMethodValues(eventName, eventObject);

	}


	/**
	 * Process an event with a {@link DocumentEvent}
	 * 
	 * @param eventName
	 * @param eventObject
	 */
	protected void processEvent(String eventName, DocumentEvent eventObject) {
		ObjectNodeValue node = sourceObjects.get(eventObject.getDocument());

		//System.out.println(node.getObjectDescription() + ": " + eventName + "(" + eventObject.getClass().getSimpleName() + ")");

		if (node == null) {
			System.err.println("No source object found for " + eventObject.getDocument());
			return;
		}

		node.updateMethodValues(eventName, eventObject);

	}

	/**
	 * 
	 * 
	 */
	private void refreshOverlayPanel() {
		Rectangle bounds = rootComponent.getBounds();
		//Set bounds x and y to 0, because if the root component is a frame
		//for example these values might be !=0 which would place the overlay
		//panel outside of the frame.
		bounds.x = 0;
		bounds.y = 0;
		overlayPanel.setBounds(bounds);

		doRefresh = false;
	}

	/**
	 * 
	 * 
	 * @param obj
	 * @param parent
	 * @return
	 */
	private String getObjectString(Object obj, Object parent) {
		if (obj == null) {
			return "null";
		}

		String id = "?";

		if (sourceObjects.containsKey(obj)) {
			id = "" + sourceObjects.get(obj).getId();
		}

		if (parent == null) {
			return obj.getClass().getSimpleName() + " [" + id + "]";
		}

		return obj.getClass().getSimpleName() + " [" + id + "] " + " (" + ReflectionUtil.getFieldName(obj, parent) + ")";
	}

	/**
	 * 
	 * 
	 * @param components
	 */
	public void showSelectedComponentBounds(Collection<Component> components) {
		Collection<ComponentInfo> componentInfos = new HashSet<ComponentInfo>();

		for (Component component : components) {
			allComponents.get(component).calculatePosition(overlayPanel);

			componentInfos.add(allComponents.get(component));

		}

		if (overlayPanel != null) {
			overlayPanel.showSelectedComponentBounds(componentInfos);
		}

	}


	@Override
	public void componentHidden(ComponentEvent e) {

	}

	@Override
	public void componentMoved(ComponentEvent e) {


	}

	@Override
	public void componentResized(ComponentEvent e) {

		//Resize overlay panel so it fills the whole area
		if (overlayPanel != null && e.getComponent() == rootComponent) {
			refreshOverlayPanel();
		}

	}

	@Override
	public void componentShown(ComponentEvent e) {

		//Resize overlay panel so it fills the whole area
		if (overlayPanel != null && e.getComponent() == rootComponent) {
			refreshOverlayPanel();
		}

	}

	@Override
	public void componentAdded(ContainerEvent e) {
		//Resize overlay panel so it fills the whole area
		if (overlayPanel != null && e.getComponent() == rootComponent) {
			refreshOverlayPanel();
		}

		update();
	}

	@Override
	public void componentRemoved(ContainerEvent e) {
		//Resize overlay panel so it fills the whole area
		if (overlayPanel != null && e.getComponent() == rootComponent) {
			refreshOverlayPanel();
		}

		update();
	}


	@Override
	public void mouseDragged(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//Info box follows mouse
		if (overlayPanel != null) {
			Object source = e.getSource();
			Component parentContainer = null;
			Component parentParentContainer = null;

			if (source instanceof Component) {
				parentContainer = ((Component)e.getSource()).getParent();

				if (parentContainer instanceof Component) {
					parentParentContainer = parentContainer.getParent();
				}
			}

			ComponentInfo recordedComponent = allComponents.get(e.getComponent());

			overlayPanel.showInfoBox(
					e.getX() + recordedComponent.getXOverlayPosition(),
					e.getY() + recordedComponent.getYOverlayPosition(),
					getObjectString(source, parentContainer)  + "\n" +
							"- Added to: " + getObjectString(parentContainer, parentParentContainer) + "\n" +
							"- Mouse coordinates: x=" + e.getX() + ", y=" + e.getY()
					);
		}

	}

	@Override
	public void mouseClicked(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

		if (doRefresh) {
			//Makes sure the overlay panel is refreshed properly if not automatically done yet
			refreshOverlayPanel();
		}

		allComponents.get(e.getComponent()).calculatePosition(overlayPanel);

		if (overlayPanel != null) {
			overlayPanel.showComponentBounds(allComponents.get(e.getComponent()));
		}


	}

	@Override
	public void mouseExited(MouseEvent e) {


	}

	@Override
	public void mousePressed(MouseEvent e) {


	}

	@Override
	public void mouseReleased(MouseEvent e) {


	}




	/***************************************************************************
	 * 
	 * 
	 * 
	 *
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public static class ComponentDebuggerObjectTreeNode extends ObjectTree<ObjectNodeValue> {

		/**
		 * 
		 * 
		 * @param obj
		 */
		public ComponentDebuggerObjectTreeNode(Object obj) {
			super(obj);

		}

		@Override
		protected ObjectNodeValue newObject(Object obj) {
			return new ObjectNodeValue(obj);
		}


	}





}