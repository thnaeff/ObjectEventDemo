/**
 * 
 */
package ch.thn.objectdebugger.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.util.Collection;
import java.util.LinkedHashMap;

import javax.swing.JLayeredPane;

import ch.thn.guiutil.effects.OverlayPanel;


/**
 * @author Thomas Naeff (github.com/thnaeff)
 *
 */
public class ComponentOverlayPanel extends OverlayPanel {
	private static final long serialVersionUID = 6811358824805250141L;

	private ComponentInfo componentInfo = null;
	private Collection<ComponentInfo> selectedComponents = null;

	private String infoText = null;

	private int infoTextX = 0;
	private int infoTextY = 0;


	/**
	 * 
	 * 
	 * @param layeredPane
	 */
	public ComponentOverlayPanel(JLayeredPane layeredPane) {
		super(layeredPane, true);

		setLayout(null);

	}

	/**
	 * 
	 * 
	 * @param componentInfo
	 */
	public void showComponentBounds(ComponentInfo componentInfo) {
		this.componentInfo = componentInfo;

		repaint();
	}

	/**
	 * 
	 * 
	 * @param componentInfos
	 */
	public void showSelectedComponentBounds(Collection<ComponentInfo> componentInfos) {
		this.selectedComponents = componentInfos;

		repaint();
	}

	public void showInfoBox(int infoX, int infoY, String text) {

		this.infoText = text;
		this.infoTextX = infoX;
		this.infoTextY = infoY;

		repaint();
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (componentInfo != null) {
			g.setColor(Color.red);
			g.drawRect(componentInfo.getXOverlayPosition(),
					componentInfo.getYOverlayPosition(),
					componentInfo.getComponent().getWidth() - 1,
					componentInfo.getComponent().getHeight() - 1);
		}

		if (selectedComponents != null && selectedComponents.size() > 0) {
			g.setColor(Color.green.darker());
			for (ComponentInfo componentInfo : selectedComponents) {
				g.drawRect(componentInfo.getXOverlayPosition() + 1,
						componentInfo.getYOverlayPosition() + 1,
						componentInfo.getComponent().getWidth() - 3,
						componentInfo.getComponent().getHeight() - 3);
			}
		}

		if (infoText != null) {
			paintLabel((Graphics2D)g, infoTextX, infoTextY, infoText, 3, 5, this.getBounds(), 10, 5);
		}

	}


	private void paintLabel(Graphics2D g2d, int x, int y, String text, int lineSpace, int padding, Rectangle boundaries, int xOffset, int yOffset) {

		LinkedHashMap<TextLayout, String> l = new LinkedHashMap<TextLayout, String>();
		FontRenderContext frc = g2d.getFontRenderContext();
		Font font = g2d.getFont();

		int currentPos = 0;
		int widestLineWidth = 0;
		int highestTextHeight = 0;
		int totalHeight = 0;


		//Split all lines
		while (currentPos < text.length()) {
			int nextPos = text.indexOf('\n', currentPos);

			String currentText = null;

			if (nextPos == -1) {
				currentText = text.substring(currentPos);
			} else {
				currentText = text.substring(currentPos, nextPos);
			}

			TextLayout tl = new TextLayout(currentText, font, frc);

			if (tl.getBounds().getWidth() > widestLineWidth) {
				widestLineWidth = (int)tl.getBounds().getWidth();
			}

			if (tl.getBounds().getHeight() > highestTextHeight) {
				highestTextHeight = (int)tl.getBounds().getHeight();
			}

			totalHeight += tl.getBounds().getHeight() + lineSpace;

			l.put(tl, currentText);

			if (nextPos == -1) {
				break;
			} else {
				currentPos = nextPos + 1;
			}
		}

		int width = widestLineWidth + 2 * padding;
		int height = totalHeight + 2 * padding;

		if (boundaries != null) {
			if (x + width + xOffset > boundaries.getWidth()) {
				x -= width + xOffset;
			} else {
				x += xOffset;
			}

			if (y + height + yOffset > boundaries.getHeight()) {
				y -= height + yOffset;
			} else {
				y += yOffset;
			}
		} else {
			x += xOffset;
			y += yOffset;
		}

		g2d.setColor(new Color(255, 255, 0, 190));
		g2d.fillRect(x, y, width, height);

		g2d.setColor(new Color(0, 0, 255, 150));
		g2d.drawRect(x, y, width, height);

		int currentTextPosY = y + padding;

		g2d.setColor(Color.black);
		for (TextLayout tl : l.keySet()) {
			currentTextPosY += lineSpace + tl.getBounds().getHeight();
			g2d.drawString(l.get(tl), x + padding, currentTextPosY);
		}

	}


}
