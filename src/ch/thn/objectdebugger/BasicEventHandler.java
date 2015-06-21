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

import java.awt.AWTEvent;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuKeyEvent;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuListener;
import javax.swing.event.MouseInputListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.tree.ExpandVetoException;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class BasicEventHandler extends EventHandler implements  MouseListener, MouseMotionListener, MouseWheelListener,
KeyListener, ActionListener, CaretListener, ChangeListener, ComponentListener, ContainerListener,
DocumentListener, FocusListener, ItemListener, ListDataListener, ListSelectionListener,
TableModelListener, CellEditorListener, WindowListener, HierarchyBoundsListener, HierarchyListener,
InputMethodListener, PropertyChangeListener, AncestorListener, UndoableEditListener, HyperlinkListener,
VetoableChangeListener, TreeExpansionListener, TreeSelectionListener, TreeWillExpandListener,
InternalFrameListener, MenuDragMouseListener, MenuKeyListener, MenuListener, MouseInputListener,
PopupMenuListener, RowSorterListener, TableColumnModelListener, TreeModelListener, AdjustmentListener,
AWTEventListener, TextListener, WindowFocusListener, WindowStateListener {



	/**
	 * 
	 * 
	 * 
	 * @param debugger
	 */
	public BasicEventHandler(ObjectEventDemo debugger) {
		super(debugger);
	}


	@Override
	public void keyPressed(KeyEvent e) {
		debugger.processEvent("keyPressed", e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		debugger.processEvent("keyReleased", e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
		debugger.processEvent("keyTyped", e);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		debugger.processEvent("mouseDragged", e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		debugger.processEvent("mouseMoved", e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		debugger.processEvent("mouseClicked", e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		debugger.processEvent("mouseEntered", e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		debugger.processEvent("mouseExited", e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		debugger.processEvent("mousePressed", e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		debugger.processEvent("mouseReleased", e);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		debugger.processEvent("mouseWheelMoved", e);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		debugger.processEvent("valueChanged.ListSelectionEvent", e);
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		debugger.processEvent("contentsChanged", e);
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		debugger.processEvent("intervalAdded", e);
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		debugger.processEvent("intervalRemoved", e);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		debugger.processEvent("itemStateChanged", e);
	}

	@Override
	public void focusGained(FocusEvent e) {
		debugger.processEvent("focusGained", e);
	}

	@Override
	public void focusLost(FocusEvent e) {
		debugger.processEvent("focusLost", e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		debugger.processEvent("changedUpdate", e);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		debugger.processEvent("insertUpdate", e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		debugger.processEvent("removeUpdate", e);
	}

	@Override
	public void componentAdded(ContainerEvent e) {
		debugger.processEvent("componentAdded", e);
	}

	@Override
	public void componentRemoved(ContainerEvent e) {
		debugger.processEvent("componentRemoved", e);
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		debugger.processEvent("componentHidden", e);
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		debugger.processEvent("componentMoved", e);
	}

	@Override
	public void componentResized(ComponentEvent e) {
		debugger.processEvent("componentResized", e);
	}

	@Override
	public void componentShown(ComponentEvent e) {
		debugger.processEvent("componentShown", e);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		debugger.processEvent("stateChanged", e);
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		debugger.processEvent("caretUpdate", e);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		debugger.processEvent("actionPerformed", e);
	}

	@Override
	public void editingCanceled(ChangeEvent e) {
		debugger.processEvent("editingCanceled", e);
	}

	@Override
	public void editingStopped(ChangeEvent e) {
		debugger.processEvent("editingStopped", e);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		debugger.processEvent("tableChanged", e);
	}

	@Override
	public void ancestorMoved(HierarchyEvent e) {
		debugger.processEvent("ancestorMoved", e);
	}

	@Override
	public void ancestorResized(HierarchyEvent e) {
		debugger.processEvent("ancestorResized", e);
	}

	@Override
	public void hierarchyChanged(HierarchyEvent e) {
		debugger.processEvent("hierarchyChanged", e);
	}

	@Override
	public void caretPositionChanged(InputMethodEvent e) {
		debugger.processEvent("caretPositionChanged", e);
	}

	@Override
	public void inputMethodTextChanged(InputMethodEvent e) {
		debugger.processEvent("inputMethodTextChanged", e);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		debugger.processEvent("propertyChange", e);
	}

	@Override
	public void ancestorAdded(AncestorEvent e) {
		debugger.processEvent("ancestorAdded", e);
	}

	@Override
	public void ancestorMoved(AncestorEvent e) {
		debugger.processEvent("ancestorMoved", e);
	}

	@Override
	public void ancestorRemoved(AncestorEvent e) {
		debugger.processEvent("ancestorRemoved", e);
	}

	@Override
	public void undoableEditHappened(UndoableEditEvent e) {
		debugger.processEvent("undoableEditHappened", e);
	}

	@Override
	public void hyperlinkUpdate(HyperlinkEvent e) {
		debugger.processEvent("hyperlinkUpdate", e);
	}

	@Override
	public void vetoableChange(PropertyChangeEvent e) throws PropertyVetoException {
		debugger.processEvent("vetoableChange", e);
	}

	@Override
	public void treeWillCollapse(TreeExpansionEvent e) throws ExpandVetoException {
		debugger.processEvent("treeWillCollapse", e);
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
		debugger.processEvent("treeWillExpand", e);
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		debugger.processEvent("valueChanged.TreeSelectionEvent", e);
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent e) {
		debugger.processEvent("treeCollapsed", e);
	}

	@Override
	public void treeExpanded(TreeExpansionEvent e) {
		debugger.processEvent("treeExpanded", e);
	}



	@Override
	public void windowActivated(WindowEvent e) {
		debugger.processEvent("windowActivated", e);
	}



	@Override
	public void windowClosed(WindowEvent e) {
		debugger.processEvent("windowClosed", e);
	}



	@Override
	public void windowClosing(WindowEvent e) {
		debugger.processEvent("windowClosing", e);
	}



	@Override
	public void windowDeactivated(WindowEvent e) {
		debugger.processEvent("windowDeactivated", e);
	}



	@Override
	public void windowDeiconified(WindowEvent e) {
		debugger.processEvent("windowDeiconified", e);
	}



	@Override
	public void windowIconified(WindowEvent e) {
		debugger.processEvent("windowIconified", e);
	}



	@Override
	public void windowOpened(WindowEvent e) {
		debugger.processEvent("windowOpened", e);
	}



	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		debugger.processEvent("internalFrameActivated", e);
	}



	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		debugger.processEvent("internalFrameClosed", e);
	}



	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		debugger.processEvent("internalFrameClosing", e);
	}



	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		debugger.processEvent("internalFrameDeactivated", e);
	}



	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		debugger.processEvent("internalFrameDeiconified", e);
	}



	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		debugger.processEvent("internalFrameIconified", e);
	}



	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		debugger.processEvent("internalFrameOpened", e);
	}



	@Override
	public void menuCanceled(MenuEvent e) {
		debugger.processEvent("menuCanceled", e);
	}



	@Override
	public void menuDeselected(MenuEvent e) {
		debugger.processEvent("menuDeselected", e);
	}



	@Override
	public void menuSelected(MenuEvent e) {
		debugger.processEvent("menuSelected", e);
	}



	@Override
	public void menuKeyPressed(MenuKeyEvent e) {
		debugger.processEvent("menuKeyPressed", e);
	}



	@Override
	public void menuKeyReleased(MenuKeyEvent e) {
		debugger.processEvent("menuKeyReleased", e);
	}



	@Override
	public void menuKeyTyped(MenuKeyEvent e) {
		debugger.processEvent("menuKeyTyped", e);
	}



	@Override
	public void menuDragMouseDragged(MenuDragMouseEvent e) {
		debugger.processEvent("menuDragMouseDragged", e);
	}



	@Override
	public void menuDragMouseEntered(MenuDragMouseEvent e) {
		debugger.processEvent("menuDragMouseEntered", e);
	}



	@Override
	public void menuDragMouseExited(MenuDragMouseEvent e) {
		debugger.processEvent("menuDragMouseExited", e);
	}



	@Override
	public void menuDragMouseReleased(MenuDragMouseEvent e) {
		debugger.processEvent("menuDragMouseReleased", e);
	}



	@Override
	public void columnAdded(TableColumnModelEvent e) {
		debugger.processEvent("columnAdded", e);
	}



	@Override
	public void columnMarginChanged(ChangeEvent e) {
		debugger.processEvent("columnMarginChanged", e);
	}



	@Override
	public void columnMoved(TableColumnModelEvent e) {
		debugger.processEvent("columnMoved", e);
	}



	@Override
	public void columnRemoved(TableColumnModelEvent e) {
		debugger.processEvent("columnRemoved", e);
	}



	@Override
	public void columnSelectionChanged(ListSelectionEvent e) {
		debugger.processEvent("columnSelectionChanged", e);
	}



	@Override
	public void sorterChanged(RowSorterEvent e) {
		debugger.processEvent("sorterChanged", e);
	}



	@Override
	public void popupMenuCanceled(PopupMenuEvent e) {
		debugger.processEvent("popupMenuCanceled", e);
	}



	@Override
	public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
		debugger.processEvent("popupMenuWillBecomeInvisible", e);
	}



	@Override
	public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
		debugger.processEvent("popupMenuWillBecomeVisible", e);
	}



	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		debugger.processEvent("treeNodesChanged", e);
	}



	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		debugger.processEvent("treeNodesInserted", e);
	}



	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		debugger.processEvent("treeNodesRemoved", e);
	}



	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		debugger.processEvent("treeStructureChanged", e);
	}



	@Override
	public void eventDispatched(AWTEvent e) {
		debugger.processEvent("eventDispatched", e);
	}



	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		debugger.processEvent("adjustmentValueChanged", e);
	}



	@Override
	public void windowStateChanged(WindowEvent e) {
		debugger.processEvent("windowStateChanged", e);
	}



	@Override
	public void windowGainedFocus(WindowEvent e) {
		debugger.processEvent("windowGainedFocus", e);
	}



	@Override
	public void windowLostFocus(WindowEvent e) {
		debugger.processEvent("windowLostFocus", e);
	}



	@Override
	public void textValueChanged(TextEvent e) {
		debugger.processEvent("textValueChanged", e);
	}

}
