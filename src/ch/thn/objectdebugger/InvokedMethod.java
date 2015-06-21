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

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class InvokedMethod {

	private ObjectNodeValue v = null;

	private Method m = null;

	private LinkedHashMap<Class<?>, LinkedList<ParameterTypeMethod>> parameterTypeMethods = null;

	/**
	 * 
	 * 
	 * @param v
	 * @param m
	 */
	public InvokedMethod(ObjectNodeValue v, Method m) {

		this.v = v;
		this.m = m;

		parameterTypeMethods = new LinkedHashMap<>();

		//invoked method -> event interface (parameter type of invoked method)
		//-> event methods (interface methods) -> event objects (parameter type of event method)
		//-> event object methods
		for (Class<?> c : m.getParameterTypes()) {
			parameterTypeMethods.put(c, new LinkedList<ParameterTypeMethod>());

			Method[] methods = c.getMethods();
			for (Method method : methods) {
				parameterTypeMethods.get(c).add(new ParameterTypeMethod(v, method));
			}

		}


	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Method getMethod() {
		return m;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public Set<Class<?>> getParameterTypes() {
		return parameterTypeMethods.keySet();
	}


	/**
	 * 
	 * 
	 * @return
	 */
	public Collection<LinkedList<ParameterTypeMethod>> getParameterTypeMethods() {
		return parameterTypeMethods.values();
	}


}
