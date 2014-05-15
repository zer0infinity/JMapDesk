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

package ch.hsr.ifs.jmapdesk.gen;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class LayerPanel {
	/**
	 * Shows the Layers
	 */
	private Vector<String> vector = new Vector<String>();
	private JList list = null;
	private String selected;
	private Container c;
	private final String up = "up";
	private final String down = "down";
	
	public LayerPanel(Container c) {
		this.c = c;
	}
	
	/**
	 * create panel with scroll, if needed
	 * 
	 * @param dim Set Size of Panel
	 * @return ScrollPane
	 */
	JScrollPane getPanel(Dimension dim, MapGen mapgen) {
		list = new JList(vector);
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setComponentPopupMenu(popupMenu(mapgen, this));
		list.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (!list.isSelectionEmpty()) {
					selected = list.getSelectedValue().toString();
				}
			}
		});
		list.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 38 && !list.isSelectionEmpty()) {
					move_item(up);
				} else if (e.getKeyCode() == 40 && !list.isSelectionEmpty()) {
					move_item(down);
				} else if (e.getKeyCode() == 127 || e.getKeyCode() == 8 && !list.isSelectionEmpty()) {
					remove_item();
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyTyped(KeyEvent e) {}
		});
		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setPreferredSize(dim);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		return scrollPane;
	}
	
	/**
	 * show layer
	 * 
	 * @param layer Layer
	 */
	void create_item(Layer layer) {
		boolean match = false;
		for (int i=0; i < c.getlayer().size(); i++) {
			if (c.getlayer().get(i).getName().equalsIgnoreCase(layer.getName())) {
				match = true;
				break;
			}
		}
		if (c.getlayer().isEmpty() || !match) {
			vector.add(layer.getName());
			c.add(layer);
			list.updateUI();
		}
	}
	
	/**
	 * move item up or down in the list
	 * 
	 * @param move Move Direction
	 */
	void move_item(String move) {
		ArrayList<Layer> alist = new ArrayList<Layer>(c.getlayer());
		clear();
		for (int i = 0; i < alist.size(); i++) {
			if (move == down && (alist.get(i).getName() == selected) && alist.size() > (i+1)) {
				alist.add(i, alist.get(i+1));
				i = i + 1;
			} else if (move == up && (alist.get(i).getName() == selected) && (0 <= i-1)) {
				alist.add(i-1, alist.get(i));
				i = i + 1;
			}
		}
		for (int i = 0; i < alist.size(); i++) {
			create_item(alist.get(i));
		}
		list.setSelectedValue(selected, true);
	}
	
	/**
	 * Remove Item from the lsit
	 */
	void remove_item() {
		ArrayList<Layer> alist = new ArrayList<Layer>(c.getlayer());
		clear();
		for (int i = 0; i < alist.size(); i++) {
			if (!alist.get(i).getName().equals(selected)) {
				create_item(alist.get(i));
			}
		}
		if (alist.isEmpty()) {
			clear();
		}
	}
	
	/**
	 * clear all internal lists
	 */
	void clear() {
		c.empty();
		list.removeAll();
		list.updateUI();
		vector.clear();
	}
	
	/**
	 * Contextmenu
	 *
	 * @return PopupMenu
	 */
	private JPopupMenu popupMenu(final MapGen mapgen, final LayerPanel layerpanel) {
		JPopupMenu popupMenu = new JPopupMenu();
		
		final String lblmoveup = "Move up";
		final String lbladdshp = "Add Shape...";
		final String lbladdimg = "Add Image...";
		final String lbladdwms = "Add WMS...";
		final String lblremove = "Remove Layer...";
		final String lblmovedown = "Move down";

		String[] menuItems = new String[] { lblmoveup, lbladdshp, lbladdimg, lbladdwms, lblremove, lblmovedown };
		ActionListener mouseListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == lblmoveup) {
					move_item(up);
				} else if (event.getActionCommand() == lbladdshp) {
					mapgen.open_shp();
				} else if (event.getActionCommand() == lbladdimg) {
					mapgen.open_image();
				} else if (event.getActionCommand() == lbladdwms) {
					new WMSDialog(layerpanel);
				} else if (event.getActionCommand() == lblremove) {
					remove_item();
				} else if (event.getActionCommand() == lblmovedown) {
					move_item(down);
				}
			}
		};
		for (int i = 0; i < menuItems.length; i++) {
			if (i == 1 || i == 5) {
				popupMenu.addSeparator();
			}
			JMenuItem menuItem = new JMenuItem(menuItems[i]);
			menuItem.addActionListener(mouseListener);
			popupMenu.add(menuItem);
		}
		return popupMenu;
	}
}
