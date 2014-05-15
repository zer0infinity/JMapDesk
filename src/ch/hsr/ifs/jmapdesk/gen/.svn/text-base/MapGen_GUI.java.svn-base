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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.*;

public class MapGen_GUI extends JDialog implements ActionListener {
	
	/**
	 * Simple Map Generator
	 */
	private static final long serialVersionUID = 1L;
	
	private Properties properties = new Properties();
	private final String LBLADDSHP = "Add Shape...";
	private final String LBLADDIMG = "Add Image...";
	private final String LBLADDWMS = "Add WMS...";
	private final String LBLREMOVE = "Remove...";
	private final String LBLSAVE = "Save As Map...";
	private final String LBLREMOVEALL = "Remove All";
	private final String LBLMOVEUP = "Move up";
	private final String LBLMOVEDOWN = "Move down";
	private JButton arrow_up;
	private JButton arrow_down;
	private MapGen mapgen;
	private LayerPanel layerpanel;
	
	/**
	 * Show MapGenerator GUI
	 * 
	 * @param size WindowSize of JMapDesk
	 */
	public MapGen_GUI(Dimension size) {
		Container c = new Container();
		layerpanel = new LayerPanel(c);
		mapgen = new MapGen(layerpanel, c);
		
		loadProp();
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Map Generator 0.2.10");
		setResizable(false);
		setSize(225, size.height);
		set_location(size);
		setAlwaysOnTop(true);
		setIconImage(new ImageIcon(getClass().getResource("/ch/hsr/ifs/jmapdesk/ui/images/icon.jpg")).getImage());
		
		JPanel mainPanel = new JPanel(new BorderLayout());
		JScrollPane pane = layerpanel.getPanel(new Dimension(200, 340), mapgen);
	
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		
		String path = "/ch/hsr/ifs/jmapdesk/gen/images/";
		String iconAddShp = path + "add_shp.png";
		String iconAddImg = path + "add_img.png";
		String iconAddWMS = path + "add_wms.png";
		String iconremove = path + "eraser.png";
		String iconsave = path + "save.png";
		String iconclear = path + "clearall.png";
		final String[] buttonlbl = { LBLADDSHP, LBLADDIMG, LBLADDWMS, LBLREMOVE, LBLREMOVEALL, LBLSAVE };
		final String[] buttonicon = { iconAddShp, iconAddImg, iconAddWMS, iconremove, iconclear, iconsave };
		for (int i = 0; i < buttonlbl.length; i++) {
			JButton button = create_button(buttonlbl[i], buttonicon[i]);
			button.addActionListener(this);
			toolbar.add(button);
		}
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(null);
		controlPanel.setPreferredSize(new Dimension(20, 365));
		
		arrow_up = create_button(LBLMOVEUP, path + "arrow_up.png");
		arrow_up.setBounds(0, (size.height - 135)/2, 20, 30);
		arrow_up.addActionListener(this);
		arrow_down = create_button(LBLMOVEDOWN, path + "arrow_down.png");
		arrow_down.setBounds(0, (size.height - 75)/2, 20, 30);
		arrow_down.addActionListener(this);
		
		controlPanel.add(arrow_up);
		controlPanel.add(arrow_down);
		
		mainPanel.add(controlPanel, BorderLayout.EAST);
		mainPanel.add(toolbar, BorderLayout.SOUTH);
		mainPanel.add(pane, BorderLayout.CENTER);
		add(mainPanel);
		
		// Escape = Close Window
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		JRootPane rootPane = getRootPane();
		rootPane.registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand() == LBLADDSHP) {
			mapgen.open_shp();
		} else if (e.getActionCommand() == LBLADDIMG) {
			mapgen.open_image();
		} else if (e.getActionCommand() == LBLADDWMS) {
			new WMSDialog(layerpanel);
		} else if (e.getActionCommand() == LBLREMOVE) {
			layerpanel.remove_item();
		} else if (e.getActionCommand() == LBLREMOVEALL) {
			layerpanel.clear();
		} else if (e.getActionCommand() == LBLSAVE) {
			mapgen.file_save();
		}
		if (e.getActionCommand() == LBLMOVEUP) {
			String up = "up";
			layerpanel.move_item(up);
		} else if (e.getActionCommand() == LBLMOVEDOWN) {
			String down = "down";
			layerpanel.move_item(down);
		}
	}
	
	/**
	 * always Dock MapGen on the right/ left side on JMapDesk
	 * 
	 * @param size WindowSize of JMapDesk
	 */
	public void set_location(Dimension size) {
		if (this != null) {
			loadProp();
			Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
			int width = screensize.width;
			int height = screensize.height;
			int posx = Integer.valueOf(properties.getProperty("position.x"));
			int posy = Integer.valueOf(properties.getProperty("position.y"));
			if (posx + size.width + 225 > width && posx - 225 < 0) {
				setBounds(size.width - 230, (height - 600)/2, 225, 400);
				if (arrow_up != null && arrow_down != null) {
					arrow_up.setBounds(0, 135, 20, 30);
					arrow_down.setBounds(0, 165, 20, 30);
				}
			} else if (posx + size.width + 225 > width) {
				setBounds(posx - 225, posy, 225, size.height);
				set_location_arrows(size);
			} else { 
				setBounds(posx + size.width, posy, 225, size.height);
				set_location_arrows(size);
			}
		}
	}
	
	/**
	 * update location of arrows
	 * 
	 * @param size
	 */
	private void set_location_arrows(Dimension size) {
		if (arrow_up != null && arrow_down != null) {
			arrow_up.setBounds(0, (size.height - 135)/2, 20, 30);
			arrow_down.setBounds(0, (size.height - 75)/2, 20, 30);
		}
	}
	
	/**
	 * create button
	 * 
	 * @param lbl Set ActionCommand and ToolTip
	 * @param icon Icon
	 * @return Button
	 */
	private JButton create_button(String lbl, String icon) {
		JButton button = new JButton();
		button.setActionCommand(lbl);
		button.setIcon(new ImageIcon(getClass().getResource(icon)));
		button.setToolTipText(lbl);
		return button;
	}
	
	/**
	 * load Properties
	 */
	private void loadProp() {
		String PROPSFILE = "jmapdesk.cfg";
		try {
			FileInputStream f = new FileInputStream(PROPSFILE);
			properties.load(f);
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
