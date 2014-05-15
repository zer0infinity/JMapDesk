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

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.hsr.ifs.jmapdesk.gen.MapGen_GUI;
import ch.hsr.ifs.jmapdesk.logic.JMapDesk;
import ch.hsr.ifs.jmapdesk.util.ImageToClipboard;

public class JMapDesk_GUI extends JFrame {
	
	/**
	 * JMapDesk GUI
	 */
	private static final long serialVersionUID = 1L;
	
//	public static final String PROPSFILE = "jmapdesk.cfg";
//	public static final String P_WINDOWS_POS_X = "position.x";
//	public static final String P_WINDOWS_POS_Y = "position.y";
//	public static final String P_BROWSE_LOCATION = "browse.location";
//	
//	private Properties properties;
//	private final String imagefolder = "/ch/hsr/ifs/jmapdesk/ui/images/";
//	
//	private JMapDesk mapdesk;
//	private MapGen_GUI mapgengui;
//	private Log log;
//	private ViewComponent comp;
//	
//	private JToolBar toolbar = new JToolBar();
//	private JMenuBar mbar = new JMenuBar();
//	private JPanel mainPanel = new JPanel();
	
	
	/**
	 * 
	 * @param mapdesk
	 * @param map
	 * @param comp
	 */
//	public JMapDesk_GUI(JMapDesk mapdesk, ViewComponent comp, Properties properties) {
//		log = new Log();
//		this.mapdesk = mapdesk;
//		this.comp = comp;
//		this.properties = properties;
//		
//		loadProperties();
//	}

	/**
	 * Show JMapDesk GUI
	 * 
	 * @param mapdesk
	 * @param programVersion Version Number
	 */
	public void show() {
//		// File Menu
//		final String LBL_FILE = "File";
//		final String LBL_OPEN = "Open Mapfile...";
//		final String LBL_CLOSE = "Close";
//		final String LBL_EXIT = "Exit";
//		
//		ActionListener filelistener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getActionCommand() == LBL_OPEN) {
//					delete_tempmap();
//					JFileChooser fc = mapdesk.file_chooser();
//					fc.setDialogTitle("Open Mapfile...");
//					fc.setFileFilter(new FileNameExtensionFilter("*.map; *.txt", new String[] { "map", "txt" }));
//					if(properties.containsKey(P_BROWSE_LOCATION)) {
//						fc.setCurrentDirectory(new File(properties.getProperty(P_BROWSE_LOCATION)));
//					}
//					if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
//						File file = fc.getSelectedFile();
//						File path = fc.getCurrentDirectory();
//						log.close_log();
//						String logtext = mapdesk.open_mapfile(new File(path + File.separator + file));
//						mainPanel.remove(comp);
//						mainPanel.add(comp);
//						setSize(800, 600);
//						setTitle(JMapDesk.PROGRAMNAME + " - " + file.getName());
//						if (!logtext.isEmpty()) {
//							log.set_logtext(logtext);
//							log.show_log(getSize());
//						}
//						properties.setProperty(P_BROWSE_LOCATION, fc.getCurrentDirectory().getAbsolutePath());
//					}
//				} else if (e.getActionCommand() == LBL_CLOSE) {
//
//				} else if (e.getActionCommand() == LBL_EXIT) {
//					exit();
//				}
//			}
//		};
//		JMenu filemenu = new JMenu(LBL_FILE);
//		filemenu.setMnemonic(KeyEvent.VK_F);
//
//		final String[] openLabel = { LBL_OPEN, LBL_CLOSE, LBL_EXIT };
//		final int[] openEvent = { KeyEvent.VK_O, KeyEvent.VK_W, KeyEvent.VK_Q };
//		for (int i = 0 ; i < openLabel.length; i++) {
//			if (i == 2) {
//				filemenu.addSeparator();
//			}
//			JMenuItem menuItem = new JMenuItem(openLabel[i]);
//			menuItem.setAccelerator(KeyStroke.getKeyStroke(openEvent[i], InputEvent.CTRL_MASK));
//			menuItem.addActionListener(filelistener);
//			filemenu.add(menuItem);
//		}
//
//		// Edit Menu
//		final String LBL_EDIT = "Edit";
//		final String LBL_COPY = "Copy to Clipboard";
//		final String LBL_COPYFILE = "Copy to File...";
//		
//		ActionListener editlistener = new ActionListener() {
//			
//			private BufferedImage bimage = null;
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getActionCommand() == LBL_COPY) {
//					copy_event();
//				} else if (e.getActionCommand() == LBL_COPYFILE) {
//					copy_event();
//				}
//			}
//			/**
//			 * make a screenshot of the map to clipboard
//			 */
//			private void copy_event() {
//				setCursor(new Cursor(Cursor.WAIT_CURSOR));
//				bimage = new BufferedImage(comp.getWidth(), comp.getHeight(), BufferedImage.TYPE_INT_RGB);
//				Graphics2D g2d = bimage.createGraphics();
//				comp.paint(g2d);
//				g2d.dispose();
//		        ImageToClipboard img2clip = new ImageToClipboard(bimage);
//		        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(img2clip, null);
//		        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
//			}
//		};
//		JMenu editmenu = new JMenu(LBL_EDIT);
//		editmenu.setMnemonic(KeyEvent.VK_E);
//		
//		final String[] editLabel = { LBL_COPY, LBL_COPYFILE };
//		final int[] editEvent = { KeyEvent.VK_C, KeyEvent.VK_S };
//		for (int i = 0; i < editLabel.length; i++) {
//			JMenuItem menuItem = new JMenuItem(editLabel[i]);
//			menuItem.setAccelerator(KeyStroke.getKeyStroke(editEvent[i], InputEvent.CTRL_MASK));
//			menuItem.addActionListener(editlistener);
//			editmenu.add(menuItem);
//		}
//		
//		// View Menu
//		final String LBL_ACTION = "View";
//		final String LBL_ZOOM_IN = "Zoom In";
//		final String LBL_ZOOM_OUT = "Zoom Out";
//		final String LBL_ZOOM_EXTENTS = "Zoom To Full Extents";
//		final String LBL_REFRESH = "Refresh";
//		final String LBL_LOG = "Mapfile Warning Log";
//		
//		ActionListener viewlistener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getActionCommand() == LBL_ZOOM_IN) {
//
//				} else if (e.getActionCommand() == LBL_ZOOM_OUT) {
//
//				} else if (e.getActionCommand() == LBL_ZOOM_EXTENTS) {
//
//				} else if (e.getActionCommand() == LBL_REFRESH) {
//
//				} else if (e.getActionCommand() == LBL_LOG) {
//					if (!log.isShowing()) {
//						log.show_log(getSize());
//					} else {
//						log.close_log();
//					}
//				}
//			}
//		};
//		JMenu viewmenu = new JMenu(LBL_ACTION);
//		viewmenu.setMnemonic(KeyEvent.VK_V);
//		
//		final String[] viewLabel = { LBL_ZOOM_IN, LBL_ZOOM_OUT, LBL_ZOOM_EXTENTS, LBL_REFRESH, LBL_LOG };
//		final String[] viewEvent = { "+", "-", "e", "F5", "F1" };
//		for (int i = 0; i < viewLabel.length; i++) {
//			if (i == 4) {
//				viewmenu.addSeparator();
//			}
//			JMenuItem menuItem = new JMenuItem(viewLabel[i]);
//			menuItem.setAccelerator(KeyStroke.getKeyStroke(viewEvent[i]));
//			menuItem.addActionListener(viewlistener);
//			viewmenu.add(menuItem);
//		}
//		
//		// Tools Menu
//		final String LBL_TOOLS = "Tools";
//		final String LBL_MAPGEN = "Map Generator";
//		ActionListener toolslistener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if (e.getActionCommand() == LBL_MAPGEN) {
//					if (mapgengui == null) {
//						mapgengui = new MapGen_GUI(getSize());
//					} else if (!mapgengui.isShowing()){
//						mapgengui = new MapGen_GUI(getSize());
//					} else if (mapgengui.isShowing()) {
//						mapgengui.dispose();
//					}
//				}
//			}
//		};
//		JMenu toolsmenu = new JMenu(LBL_TOOLS);
//		toolsmenu.setMnemonic(KeyEvent.VK_T);
//		
//		final String[] toolsLabel = { LBL_MAPGEN };
//		final String[] toolsEvent = { "F11" };
//		for (int i = 0; i < toolsLabel.length; i++) {
//			JMenuItem menuItem = new JMenuItem(toolsLabel[i]);
//			menuItem.setAccelerator(KeyStroke.getKeyStroke(toolsEvent[i]));
//			menuItem.addActionListener(toolslistener);
//			toolsmenu.add(menuItem);
//		}
//		
//		// Help Menu
//		final String LBL_HELP = "Help";
//		final String LBL_ABOUT = "About...";
//		
//		ActionListener helplistener = new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				if(e.getActionCommand() == LBL_ABOUT) {
//					final String TXT_ABOUT =
//						JMapDesk.PROGRAMNAME + " " + JMapDesk.VERSION + "\r\n" +
//						"Copyright (C) 2008" + "\r\n" +
//						"http://gis.hsr.ch/wiki/JMapDesk\r\n" +
//						"HSR Hochschule fuer Technik Rapperswil\r\n" +
//						"GISpunkt/ IFS Institut fuer Software\r\n\r\n" +
//						"Splashscreen by tpdkdesign.net\r\n" +
//						"Java version: " + System.getProperty("java.version") + "\r\n" +
//						"OS Name: " + System.getProperty("os.name");
//					JOptionPane.showMessageDialog(null, TXT_ABOUT, "JMapDesk - About...", JOptionPane.INFORMATION_MESSAGE);
//				}
//			}
//		};
//		JMenu helpmenu = new JMenu(LBL_HELP);
//		helpmenu.setMnemonic(KeyEvent.VK_H);
//		
//		final String[] helpLabel = { LBL_ABOUT };
//		final String[] helpEvent = { "F12" };
//		for (int i = 0; i < helpLabel.length; i++) {
//			JMenuItem menuItem = new JMenuItem(helpLabel[i]);
//			menuItem.setAccelerator(KeyStroke.getKeyStroke(helpEvent[i]));
//			menuItem.addActionListener(helplistener);
//			helpmenu.add(menuItem);
//		}
//		
//		final JMenu[] menuItem = { filemenu, editmenu, viewmenu, toolsmenu, helpmenu };
//		for (int i = 0; i < menuItem.length; i++) {
//			mbar.add(menuItem[i]);
//		}
//		
//		// Toolbar
//		toolbar.setFloatable(false);
//		
//		final String LBL_T_OPEN = "document-open.png";
//		final String LBL_T_ZOOM_IN = "zoom-in.png";
//		final String LBL_T_ZOOM_OUT = "zoom-out.png";
//		final String LBL_T_ZOOM_EXTENTS = "zoom-best-fit.png";
//		final String LBL_T_ZOOM_REFRESH = "reload.png";
//		final String LBL_T_COPY = "camera.png";
//		
//		final String[] toolbarLabel = { LBL_OPEN, LBL_ZOOM_IN, LBL_ZOOM_OUT, LBL_ZOOM_EXTENTS, LBL_REFRESH, LBL_COPY };
//		final String[] toolbarImage = { LBL_T_OPEN, LBL_T_ZOOM_IN, LBL_T_ZOOM_OUT, LBL_T_ZOOM_EXTENTS, LBL_T_ZOOM_REFRESH, LBL_T_COPY };
//		for (int i = 0; i < toolbarLabel.length; i++) {
//			JButton button = new JButton();
////			button.setIcon(new ImageIcon(getClass().getResource(imagefolder + toolbarImage[i])));
//			button.setToolTipText(toolbarLabel[i]);
//			button.setActionCommand(toolbarLabel[i]);
//			if (i == 0) {
//				button.addActionListener(filelistener);
//			} else if (i == 4) {
//				toolbar.addSeparator();
//			} else if (i == 5) {
//				button.addActionListener(editlistener);
//			} else {
//				button.addActionListener(viewlistener);
//			}
//			toolbar.add(button);
//		}
//		
//		// mainpanel
//		mainPanel.setLayout(new BorderLayout());
//		mainPanel.add(toolbar, BorderLayout.NORTH);
//		
//		WindowListener windowlistener = new WindowListener() {
//			@Override
//			public void windowActivated(WindowEvent e) {}
//			@Override
//			public void windowClosed(WindowEvent e) {}
//			@Override
//			public void windowClosing(WindowEvent e) {
//				exit();
//			}
//			@Override
//			public void windowDeactivated(WindowEvent e) {}
//			@Override
//			public void windowDeiconified(WindowEvent e) {}
//			@Override
//			public void windowIconified(WindowEvent e) {}
//			@Override
//			public void windowOpened(WindowEvent e) {}
//		};
		// JFrame
//		add(mainPanel);
//		setJMenuBar(mbar);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setResizable(true);
//		setLayout(null);
//		setSize(800, 600);
//		addWindowListener(windowlistener);
//		setIconImage((new ImageIcon(getClass().getResource(imagefolder + "icon.jpg")).getImage()));
//		setTitle(JMapDesk.PROGRAMNAME + " - no mapfile");
//		pack();
		
//		if(properties.containsKey(P_WINDOWS_POS_X) && properties.containsKey(P_WINDOWS_POS_Y)) {
//			setLocation(Integer.parseInt(properties.getProperty(P_WINDOWS_POS_X)),Integer.parseInt(properties.getProperty(P_WINDOWS_POS_Y)));
//		} else {
//			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//			setLocation(dim.width/2, dim.height/2);
//		}
//		setVisible(true);
	}
//	
//	/**
//	 * save Properties
//	 */
//	public void storeProperties() {
//		properties.setProperty(P_WINDOWS_POS_X, String.valueOf(getLocationOnScreen().x));
//		properties.setProperty(P_WINDOWS_POS_Y, String.valueOf(getLocationOnScreen().y));
//		try {
//			properties.store(new FileOutputStream(PROPSFILE), "JMapDesk Properties");
//		} catch (IOException e) {
//			System.out.println(e);
//		}
//	}
//	
//	/**
//	 * load Properties
//	 */
//	public void loadProperties() {
//		try {
//			properties.load(new FileInputStream(PROPSFILE));
//		} catch (IOException e) {
//			System.out.println(e);
//		}
//		mapdesk.askForBinaryFile();
//	}
//	
//	/**
//	 * delete tempfile and exit
//	 */
//	private void exit() {
//		delete_tempmap();
//		storeProperties();
//		System.exit(0);
//	}
//	
//	/**
//	 * delete tempfile
//	 */
//	private void delete_tempmap() {
//		File tempfile = new File(JMapDesk.TEMPMAP);
//		tempfile.delete();
//	}
}
