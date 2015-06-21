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

import java.util.regex.Pattern;


/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public abstract class EventHandler {


	protected ObjectEventDemo debugger = null;

	/**
	 * 
	 * 
	 * @param debugger
	 */
	public EventHandler (ObjectEventDemo debugger) {
		this.debugger = debugger;

	}

	/**
	 * Returns patterns to match the methods on which this event handler
	 * should be invoked. For example, if this event handler should be used
	 * for all add...Listener methods, then use a pattern like "add.*Listener"
	 * (this is also the default).
	 * 
	 * @return
	 */
	public Pattern[] getEventHandlerMethodPatterns() {
		return new Pattern[]{Pattern.compile("add.*Listener")};

	}


}
