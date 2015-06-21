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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import ch.thn.objectdebugger.ObjectNodeValue;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectNodeValueListModel implements ListModel<ObjectNodeValue> {


	private List<ObjectNodeValue> data = null;
	private Set<ListDataListener> listeners = null;


	/**
	 * 
	 * 
	 * @param data
	 */
	public ObjectNodeValueListModel(List<ObjectNodeValue> data) {
		this.data = new LinkedList<>();

		listeners = new HashSet<>();

		addEntries(data);
	}

	/**
	 * 
	 * 
	 */
	public void clear() {
		int size = data.size();
		data.clear();

		fireListDataListeners(ListDataEvent.INTERVAL_REMOVED, 0, size);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int size() {
		return data.size();
	}

	/**
	 * Adds a new entry. Only adds new entry if an entry with the same value
	 * does not exist yet.
	 * 
	 * @param entry
	 */
	public void addEntry(ObjectNodeValue entry) {
		data.add(entry);

		fireListDataListeners(ListDataEvent.INTERVAL_ADDED, data.size() - 1, data.size());
	}

	/**
	 * Adds all new entries. Only adds new entry if an entry with the same value
	 * does not exist yet.
	 * 
	 * @param entries
	 */
	public void addEntries(Collection<ObjectNodeValue> entries) {
		data.addAll(entries);

		fireListDataListeners(ListDataEvent.INTERVAL_ADDED, data.size() - entries.size(), data.size());
	}


	/**
	 * 
	 * 
	 * @param type
	 * @param index0
	 * @param index1
	 */
	private void fireListDataListeners(int type, int index0, int index1) {
		for (ListDataListener l : listeners) {
			l.contentsChanged(new ListDataEvent(this, type, index0, index1));
		}
	}


	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}

	@Override
	public ObjectNodeValue getElementAt(int index) {
		return data.get(index);
	}

	@Override
	public int getSize() {
		return data.size();
	}

}
