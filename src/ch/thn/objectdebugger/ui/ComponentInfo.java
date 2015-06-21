/**
 * 
 */
package ch.thn.objectdebugger.ui;

import java.awt.Component;
import java.awt.Point;

import javax.swing.JPanel;


/**
 * The Class ComponentInfo.
 *
 * @author Thomas Naeff (github.com/thnaeff)
 */
public class ComponentInfo {

	private int xOverlayPosition = 0;

	private int yOverlayPosition = 0;

	private Component thisComponent = null;


	/**
	 * Instantiates a new component info.
	 *
	 * @param thisComponent the this component
	 */
	public ComponentInfo(Component thisComponent) {
		this.thisComponent = thisComponent;
	}


	/**
	 * Calculate position.
	 *
	 * @param overlayPanel the overlay panel
	 */
	public void calculatePosition(JPanel overlayPanel) {
		if (overlayPanel == null || !overlayPanel.isShowing() || !thisComponent.isShowing()) {
			return;
		}

		Point pParent = overlayPanel.getLocationOnScreen();
		Point pComponent = thisComponent.getLocationOnScreen();
		xOverlayPosition = (int) (pComponent.getX() - pParent.getX());
		yOverlayPosition = (int) (pComponent.getY() - pParent.getY());
	}


	/**
	 * Gets the component.
	 *
	 * @return the component
	 */
	public Component getComponent() {
		return thisComponent;
	}

	/**
	 * Gets the x overlay position.
	 *
	 * @return the x overlay position
	 */
	public int getXOverlayPosition() {
		return xOverlayPosition;
	}

	/**
	 * Gets the y overlay position.
	 *
	 * @return the y overlay position
	 */
	public int getYOverlayPosition() {
		return yOverlayPosition;
	}


}
