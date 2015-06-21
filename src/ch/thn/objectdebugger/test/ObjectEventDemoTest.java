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
package ch.thn.objectdebugger.test;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.thn.objectdebugger.ObjectEventDemo;
import ch.thn.objectdebugger.ObjectNodeValue;
import ch.thn.util.tree.ListTreeNode;
import ch.thn.util.tree.printer.TreeNodePlainTextPrinter;

/**
 *
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ObjectEventDemoTest {



	public static void main(String[] args) {

		ObjectTreeTestPanel testPanel = new ObjectTreeTestPanel();

		JFrame f = new JFrame("ObjectEventDemo");

		f.getContentPane().add(testPanel);



		ObjectEventDemo debugger = new ObjectEventDemo(f);
		debugger.setLayeredPane(f.getLayeredPane());

		debugger.update();

		TreeNodePlainTextPrinter<ListTreeNode<ObjectNodeValue>> treePrinter = new TreeNodePlainTextPrinter<>();
		System.out.println(treePrinter.print(debugger.getObjectTree()));




		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMinimumSize(new Dimension(800, 500));
		f.setLocationRelativeTo(null);
		f.setVisible(true);



		JFrame fInfo = debugger.createEventInfoFrame();
		fInfo.setVisible(true);

	}


	/***************************************************************************
	 * 
	 * 
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public static class ObjectTreeTestPanel extends JPanel {
		private static final long serialVersionUID = 1057247592754927845L;


		public ObjectTreeTestPanelCenter pCenter = null;

		private JLabel lNorth = null;
		private JLabel lSouth = null;

		private JButton bEast = null;
		private JButton bWest = null;


		public ObjectTreeTestPanel() {

			setLayout(new BorderLayout());


			pCenter = new ObjectTreeTestPanelCenter();

			lNorth = new JLabel("North");
			lSouth = new JLabel("South");

			bEast = new JButton("East");
			bWest = new JButton("West");

			add(pCenter, BorderLayout.CENTER);
			add(lNorth, BorderLayout.NORTH);
			add(lSouth, BorderLayout.SOUTH);
			add(bEast, BorderLayout.EAST);
			add(bWest, BorderLayout.WEST);

		}


	}


	/***************************************************************************
	 * 
	 * 
	 * 
	 * @author Thomas Naeff (github.com/thnaeff)
	 *
	 */
	public static class ObjectTreeTestPanelCenter extends JPanel {
		private static final long serialVersionUID = 8946884447831601108L;


		public JPanel pCenterNorth = null;
		public JPanel pCenterSouth = null;
		public JPanel pCenterCenter = null;

		private JLabel lCenterNorth1 = null;
		private JLabel lCenterNorth2 = null;
		private JLabel lCenterSouth = null;

		private JTextField tfCenterCenter = null;


		public ObjectTreeTestPanelCenter() {

			setLayout(new BorderLayout());

			pCenterNorth = new JPanel();
			pCenterSouth = new JPanel();
			pCenterCenter = new JPanel();

			lCenterNorth1 = new JLabel("Center North 1");
			lCenterNorth2 = new JLabel("Center North 2");
			lCenterSouth = new JLabel("Center South");

			tfCenterCenter = new JTextField("Test", 20);

			pCenterNorth.add(lCenterNorth1);
			pCenterNorth.add(lCenterNorth2);
			pCenterCenter.add(tfCenterCenter);
			pCenterSouth.add(lCenterSouth);

			add(pCenterNorth, BorderLayout.NORTH);
			add(pCenterCenter, BorderLayout.CENTER);
			add(pCenterSouth, BorderLayout.SOUTH);
		}


	}


}
