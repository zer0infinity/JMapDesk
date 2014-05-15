/**
 * 
 * JMapDesk - UMN MapServer for your Desktop
 * Copyright (C) 2008
 * HSR University of Applied Science Rapperswil
 * IFS Institute for Software
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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class Log {
	
	private Properties properties = new Properties();
	private JButton closebutton;
	private JButton copybutton;
	private JScrollPane sp;
	private JDialog dialog;
	private String logtext = "";
	
	/**
	 * show warnings from shp2img
	 * 
	 * @param size MainGUI windowsize
	 */
	void show_log(Dimension size) {
		loadProperties();
		dialog = new JDialog();
		dialog.setTitle("JMapDesk - Mapfile Warning Log");
		dialog.setSize(size.width, 170);
		dialog.setResizable(false);
		dialog.setAlwaysOnTop(true);
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		final JTextArea ta = new JTextArea();
		ta.setText(logtext);
		ta.setDragEnabled(true);
		ta.setEditable(false);
		ta.setComponentPopupMenu(popupMenu(ta));
		
		sp = new JScrollPane(ta);
		sp.setWheelScrollingEnabled(true);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		sp.setBounds(0,0, size.width-5, 120);
		
		copybutton = new JButton();
		copybutton.setBounds(size.width-230, 122, 110, 20);
		copybutton.setText("Copy");
		copybutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ta.selectAll();
				ta.copy();
			}
		});
		closebutton = new JButton();
		closebutton.setBounds(size.width-120, 122, 110, 20);
		closebutton.setText("Close");
		closebutton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		
		// Escape = Close Window
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		};
		dialog.getRootPane().registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		set_location(size);
		
		JComponent[] components = { sp, closebutton, copybutton };
		for (JComponent comp: components) {
			panel.add(comp);
		}
		dialog.add(panel);
		dialog.setVisible(true);
	}
	
	public void set_logtext(String logtext) {
		this.logtext = logtext;
	}

	/**
	 * update warning log window location
	 * 
	 * @param size MainGUI windowsize
	 */
	void set_location(Dimension size) {
		if (dialog != null) {
			loadProperties();
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int height = screensize.height;
			int posx = Integer.valueOf(properties.getProperty("position.x"));
			int posy = Integer.valueOf(properties.getProperty("position.y"));
			if (posy - 170 < 0 && posy + size.height + 170 > height) {
				dialog.setBounds(size.width - 605, size.height - 160, 600, 170);
				closebutton.setLocation(480, 122);
				copybutton.setLocation(370, 122);
				sp.setSize(595, 120);
			} else if (posy + size.height + 170 > height) {
				dialog.setBounds(posx, posy - 170, size.width, 170);
				update_location(size);
			} else {
				dialog.setBounds(posx, posy + size.height, size.width, 170);
				update_location(size);
			}
		}
	}
	
	/**
	 * update location
	 * 
	 * @param size MainGUI windowsize
	 */
	private void update_location(Dimension size) {
		closebutton.setLocation(size.width-120, 122);
		copybutton.setLocation(size.width-230, 122);
		sp.setSize(size.width-5, 120);
	}

	/**
	 * close warning log if opened
	 */
	void close_log() {
		if (dialog != null)
			if (dialog.isShowing())
				dialog.dispose();
	}
	
	/**
	 * load Properties 
	 */
	private void loadProperties() {
		try {
			properties.load(new FileInputStream(JMapDesk_GUI.PROPSFILE));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	boolean isShowing() {
		if (dialog == null || !dialog.isShowing()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * contextmenu (just copy)
	 * 
	 * @param ta textarea
	 * @return PopupMenu
	 */
	JPopupMenu popupMenu(final JTextArea ta) {
		final String copy = "Copy";
		final String copyall = "Copy All";
		JPopupMenu pm = new JPopupMenu();
		ActionListener itemListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand() == copy) {
					ta.copy();
				} else if (e.getActionCommand() == copyall) {
					ta.selectAll();
					ta.copy();
				}
			}
		};
		String[] items = { copy, copyall };
		for (String item: items) {
			JMenuItem menuItem = new JMenuItem(item);
			menuItem.addActionListener(itemListener);
			pm.add(menuItem);
		}
		return pm;
	}
}
