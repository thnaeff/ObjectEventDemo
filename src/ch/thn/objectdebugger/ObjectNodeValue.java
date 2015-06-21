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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.thn.util.ReflectionUtil;

/**
 * 
 * 
 * 
 * 
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectNodeValue {

	private static int idCounter = 0;
	private int id = 0;

	private static final List<String> excludeMethods = new ArrayList<>();
	static {
		excludeMethods.add("");
		excludeMethods.add("");
		excludeMethods.add("");
		excludeMethods.add("toString");
		excludeMethods.add("wait");
		excludeMethods.add("hashCode");
		excludeMethods.add("getClass");
		excludeMethods.add("notify");
		excludeMethods.add("notifyAll");
	}


	private Object obj = null;
	private Object parent = null;

	private Set<ObjectNodeValueListener> listeners = null;

	/** &lt;methods invoked on the local object obj&gt; */
	private LinkedList<Method> objectMethods = null;
	/** &lt;object method name, parameter types of object method, which should only be event interfaces&gt; */
	private Map<String, Collection<Class<?>>> eventInterfaces = null;
	/** &lt;event interface class name, methods of event interface&gt; */
	private Map<String, Collection<Method>> eventInterfaceMethods = null;
	/** &lt;event interface method name, parameter types of event interface method &gt; */
	private Map<String, Collection<Class<?>>> eventClasses = null;
	/** &lt;event class name, &lt;method name, method of event class&gt;&gt; */
	private Map<String, Map<String, Method>> eventClassMethods = null;
	/** &lt;class name, &lt;method name, returned value&gt;&gt; */
	private Map<String, Map<String, String>> eventClassMethodValues = null;


	private String fieldName = null;

	/**
	 * 
	 * 
	 * @param obj
	 */
	public ObjectNodeValue(Object obj) {
		ObjectNodeValue.idCounter++;
		id = ObjectNodeValue.idCounter;

		this.obj = obj;

		listeners = new HashSet<>();

		objectMethods = new LinkedList<>();
		eventInterfaces = new LinkedHashMap<>();
		eventInterfaceMethods = new LinkedHashMap<>();
		eventClasses = new LinkedHashMap<>();
		eventClassMethods = new LinkedHashMap<>();
		eventClassMethodValues = new LinkedHashMap<>();

	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * 
	 * @param parent
	 */
	protected void setParent(Object parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * 
	 * @param l
	 */
	public void addObjectNodeValueListener(ObjectNodeValueListener l) {
		listeners.add(l);
	}

	/**
	 * 
	 * 
	 * @param l
	 */
	public void removeObjectNodeValueListener(ObjectNodeValueListener l) {
		listeners.remove(l);
	}

	/**
	 * 
	 * 
	 * @param eventName
	 * @param eventObjectName
	 */
	private void fireObjectNodeValueListeners(String eventName, String eventObjectName) {
		for (ObjectNodeValueListener l : listeners) {
			l.valuesChanged(this, eventName, eventObjectName);
		}
	}

	/**
	 * Adds a new method and its parameter type which has been invoked on the
	 * internal object
	 * 
	 * @param m
	 * @param parameterType
	 */
	protected void addInvokedMethod(Method m) {
		objectMethods.add(m);

		addEventInterfaces(m.getName(), new HashSet<>(Arrays.asList(m.getParameterTypes())));

	}

	/**
	 * 
	 * 
	 * @param methodName
	 * @param interfaceClasses
	 */
	private void addEventInterfaces(String methodName, Set<Class<?>> interfaceClasses) {

		if (!eventInterfaces.containsKey(methodName)) {
			eventInterfaces.put(methodName, interfaceClasses);
		} else {
			eventInterfaces.get(methodName).addAll(interfaceClasses);
		}


		for (Class<?> c : interfaceClasses) {
			addEventInterfaceMethods(c.getSimpleName(), new HashSet<>(Arrays.asList(c.getMethods())));
		}

	}

	/**
	 * 
	 * 
	 * @param eventInterfaceClassName
	 * @param eventInterfaceMethods
	 */
	private void addEventInterfaceMethods(String className, Set<Method> interfaceMethods) {

		if (!eventInterfaceMethods.containsKey(className)) {
			eventInterfaceMethods.put(className, interfaceMethods);
		} else {
			eventInterfaceMethods.get(className).addAll(interfaceMethods);
		}

		for (Method m : interfaceMethods) {
			addEventClasses(m.getName(), new HashSet<>(Arrays.asList(m.getParameterTypes())));
		}

	}

	/**
	 * 
	 * 
	 * @param interfaceMethodName
	 * @param eventMethodParameterTypes
	 */
	private void addEventClasses(String interfaceMethodName, Set<Class<?>> eventMethodParameterTypes) {

		if (!eventClasses.containsKey(interfaceMethodName)) {
			eventClasses.put(interfaceMethodName, eventMethodParameterTypes);
		} else {
			eventClasses.get(interfaceMethodName).addAll(eventMethodParameterTypes);
		}

		for (Class<?> c : eventMethodParameterTypes) {
			addEventClassMethods(c.getSimpleName(), new HashSet<>(Arrays.asList(c.getMethods())));
		}

	}

	/**
	 * 
	 * 
	 * @param className
	 * @param classMethods
	 */
	private void addEventClassMethods(String className, Set<Method> classMethods) {

		if (!eventClassMethods.containsKey(className)) {
			eventClassMethods.put(className, new HashMap<String, Method>());
		}

		for (Method m : classMethods) {
			if (excludeMethods.contains(m.getName())) {
				continue;
			}

			//Only use methods without any parameters
			if (m.getParameterTypes().length > 0) {
				continue;
			}

			eventClassMethods.get(className).put(m.getName(), m);

		}

	}

	/**
	 * 
	 * 
	 * @return
	 */
	public List<Class<?>> getEventInterfaces() {
		List<Class<?>> l = new LinkedList<>();

		for (Collection<Class<?>> c : eventInterfaces.values()) {
			l.addAll(c);
		}

		return l;
	}

	/**
	 * 
	 * 
	 * @param interfaceClassNames
	 * @return
	 */
	public List<Method> getEventInterfaceMethods(Collection<String> interfaceClassNames) {
		List<Method> l = new LinkedList<>();

		for (String name : interfaceClassNames) {
			if (eventInterfaceMethods.containsKey(name)) {
				l.addAll(eventInterfaceMethods.get(name));
			}
		}

		return l;
	}

	/**
	 * 
	 * 
	 * @param interfaceMethodNames
	 * @return
	 */
	public List<Class<?>> getEventClasses(Collection<String> interfaceMethodNames) {
		List<Class<?>> l = new LinkedList<>();

		for (String name : interfaceMethodNames) {
			if (eventClasses.containsKey(name)) {
				l.addAll(eventClasses.get(name));
			}
		}

		return l;
	}

	/**
	 * 
	 * 
	 * @param eventClassNames
	 * @return
	 */
	public List<Method> getEventClassMethods(Collection<String> eventClassNames) {
		List<Method> l = new LinkedList<>();

		for (String name : eventClassNames) {
			if (eventClassMethods.containsKey(name)) {
				l.addAll(eventClassMethods.get(name).values());
			}
		}

		return l;
	}

	/**
	 * 
	 * 
	 * @param eventClassName
	 * @return
	 */
	public Map<String, Method> getEventClassMethods(String eventClassName) {
		return eventClassMethods.get(eventClassName);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getObjectDescription() {
		if (fieldName == null && parent != null) {
			String tempFieldName = ReflectionUtil.getFieldName(obj, parent);

			if (tempFieldName == null) {
				fieldName = "?";
			} else {
				fieldName = tempFieldName;
			}
		}

		return obj.getClass().getSimpleName() + " [" + getId() + "] " + " (" + fieldName + ")";
	}

	/**
	 * 
	 * 
	 * @param eventName
	 * @param eventObject
	 */
	public void updateMethodValues(String eventName, Object eventObject) {
		String eventClassName = eventObject.getClass().getSimpleName();

		if (!eventClassMethodValues.containsKey(eventClassName)) {
			eventClassMethodValues.put(eventClassName, new LinkedHashMap<String, String>());
		}

		Map<String, Method> methods = getEventClassMethods(eventClassName);
		if (methods == null) {
			return;
		}

		for (Method m : methods.values()) {
			Object value = null;

			try {
				value = m.invoke(eventObject, new Object[]{});
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				value = e.getMessage();
			}

			if (value instanceof Component) {
				if (parent != null) {
					value = value.getClass().getSimpleName() + " [" + getId() + "] " + " (" + ReflectionUtil.getFieldName(value, parent) + ")";
				} else {
					value = value.getClass().getSimpleName() + " [" + getId() + "] ";
				}
			}

			eventClassMethodValues.get(eventClassName).put(m.getName(), value == null ? "null" : value.toString());
		}

		fireObjectNodeValueListeners(eventName, eventClassName);

	}

	/**
	 * 
	 * 
	 * @param eventClassName
	 * @return
	 */
	public Map<String, String> getMethodValues(String eventClassName) {
		return eventClassMethodValues.get(eventClassName);
	}


	/**
	 * 
	 * 
	 * @return
	 */
	public Object getObject() {
		return obj;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	protected Object getParentObject() {
		return parent;
	}

	@Override
	public String toString() {
		return getObjectDescription();
	}

	/***************************************************************************
	 * 
	 * 
	 *
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public interface ObjectNodeValueListener {

		/**
		 * 
		 * 
		 * @param node
		 * @param eventName
		 * @param eventClassName
		 */
		public void valuesChanged(ObjectNodeValue node, String eventName, String eventClassName);

	}

}
