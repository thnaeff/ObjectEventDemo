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
package ch.thn.objectdebugger.processors;

import java.util.List;

import javax.swing.JColorChooser;

import ch.thn.util.object.ObjectTreeProcessor;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class JColorChooserProcessor extends ObjectTreeProcessor<JColorChooser> {

	/**
	 * 
	 * 
	 */
	public JColorChooserProcessor() {
		super(JColorChooser.class);
	}

	@Override
	public List<?> getInternalObjects(JColorChooser obj) {
		return createSingleItemList(obj.getSelectionModel());
	}

}
