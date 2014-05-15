/**
 * This file is part of JMapDesk.
 * 
 * JMapDesk is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JMapDesk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JMapDesk.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ch.hsr.ifs.jmapdesk.ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

public class ViewComponent extends JPanel {

	/**
	 * Showing Map
	 */
	private static final long serialVersionUID = 1L;

	public ViewComponent() {
		addMouseMotionListener(new MouseMotionListener() {
			public void mouseDragged(MouseEvent e) {

			}
			public void mouseMoved(MouseEvent e) {}
		});
		addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
						|| (e.getButton() == MouseEvent.BUTTON1 && e.isControlDown())) {
					// Zoom In
					
				} else if (e.getButton() == MouseEvent.BUTTON3) {
					// Zoom Out
					
				}
			}
			public void mouseExited(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {

			}
			public void mouseReleased(MouseEvent e) {

			}
			public void mouseEntered(MouseEvent arg0) {}
		});
		
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() < 0) {
					// wheel moved UP
					// Zoom In
					
				} else {
					// wheel moved DOWN
					// Zoom Out
					
				}
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {

	}
}
