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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

class WMSDialog extends JDialog {
	
	/**
	 * Dialog to add WMS
	 */
	private static final long serialVersionUID = 1L;
	private final static JTextField textField = new JTextField();
	
	/**
	 * Show Dialog
	 */
	public WMSDialog(final LayerPanel layerpanel) {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("WMS URL");
		setResizable(false);
		setModal(true);
		setSize(500, 160);
		setAlwaysOnTop(true);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width - 500) / 2, (dim.height - 350) / 2);
		setIconImage(new ImageIcon(getClass().getResource("/ch/hsr/ifs/jmapdesk/ui/images/icon.jpg")).getImage());

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(null);

		textField.setDragEnabled(true);
		textField.setBounds(10, 10, 420, 25);
		textField.setComponentPopupMenu(popupMenu());

		JLabel label = new JLabel();
		label.setBounds(20, 35, 500, 25);
		label.setText("Example: http://www.URL.com/request.asp?layers=LAYERS");

		JLabel label_status = new JLabel();
		label_status.setText("Status: ");
		label_status.setBounds(10, 70, 100, 20);

		final JComboBox box_status = new JComboBox();
		box_status.setBounds(120, 70, 100, 20);
		String[] status = { "on", "off", "default" };
		for (int i = 0; i < status.length; i++) {
			box_status.addItem(status[i]);
		}

		JLabel label_format = new JLabel();
		label_format.setText("Image Format: ");
		label_format.setBounds(10, 100, 100, 20);

		final JComboBox box_format = new JComboBox();
		box_format.setBounds(120, 100, 100, 20);
		String[] format = { "png", "jpg", "gif", "tiff" };
		for (int i = 0; i < format.length; i++) {
			box_format.addItem(format[i]);
		}

		JButton button_ok = new JButton("OK");
		button_ok.setBounds(275, 100, 100, 20);
		ActionListener okListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().equals("")) {
					show_error("No URL", "Please Enter an URL");
				} else {
					String temp = textField.getText().trim();
					String[] layers = null;
					String url = null;
					if (Pattern.compile("\\?").matcher(temp).find()) {
						layers = temp.split("\\?");
						url = layers[0];
						layers = layers[1].split("&");
						boolean found_layers = false;
						for (int i = 0; i < layers.length; i++) {
							if (layers[i].toLowerCase().contains("layers=")) {
								found_layers = true;
								String layer = layers[i].replace("layers=", "");
								layers = layer.split(",");
								break;
							}
						}
						if (found_layers) {
							String status = box_status.getSelectedItem().toString();
							String format = box_format.getSelectedItem().toString();
	
							for (int i = 0; i < layers.length; i++) {
								layerpanel.create_item(new Layer(url, layers[i], format, status));
							}
							dispose();
						} else {
							show_error("No Layers", "No Layers found");
						}
					} else {
						show_error("No Layers", "No Layers found");
					}
				}
			}
		};
		button_ok.addActionListener(okListener);

		JButton button_cancel = new JButton("Cancel");
		button_cancel.setBounds(380, 100, 100, 20);
		button_cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		JButton button_paste = new JButton(new ImageIcon(getClass().getResource("/ch/hsr/ifs/jmapdesk/gen/images/paste.png")));
		button_paste.setBounds(430, 10, 24, 24);
		button_paste.setToolTipText("Paste");
		button_paste.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.paste();
			}
		});

		JButton button_clear = new JButton(new ImageIcon(getClass().getResource("/ch/hsr/ifs/jmapdesk/gen/images/clear.png")));
		button_clear.setBounds(455, 10, 24, 24);
		button_clear.setToolTipText("Clear");
		button_clear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textField.setText("");
			}
		});
		
		// Enter = Ok Button
		getRootPane().registerKeyboardAction(okListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

		// Escape = Close Window
		ActionListener cancelListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
		getRootPane().registerKeyboardAction(cancelListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		JComponent[] toMainPanel = { button_ok, button_cancel, button_paste,
				button_clear, label_status, label_format, box_status,
				box_format, label, textField };
		for (int i = 0; i < toMainPanel.length; i++) {
			mainPanel.add(toMainPanel[i]);
		}
		
		add(mainPanel);
		setVisible(true);
	}
	
	/**
	 * Contextmenu
	 * 
	 * @return PopupMenu
	 */
	private JPopupMenu popupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		
		final String lblcut = "Cut";
		final String lblcopy = "Copy";
		final String lblpaste = "Paste";
		final String lblclear = "Clear";

		String[] menuItems = new String[] { lblcut, lblcopy, lblpaste, lblclear };
		ActionListener mouseListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if (event.getActionCommand() == lblcut) {
					textField.cut();
				} else if (event.getActionCommand() == lblcopy) {
					textField.copy();
				} else if (event.getActionCommand() == lblpaste) {
					textField.paste();
				} else if (event.getActionCommand() == lblclear) {
					textField.setText("");
				}
			}
		};
		for (int i = 0; i < menuItems.length; i++) {
			JMenuItem menuItem = new JMenuItem(menuItems[i]);
			menuItem.addActionListener(mouseListener);
			popupMenu.add(menuItem);
		}
		return popupMenu;
	}
	
	/**
	 * Set JOptionPane always on top
	 * 
	 * Known Bug:
	 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6690019
	 * 
	 * Workaround:
	 * http://bugs.sun.com/view_bug.do?bug_id=6519416
	 * 
	 * @param title Title
	 * @param message Show message
	 */
	private void show_error(String title, String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.WARNING_MESSAGE);
        JDialog dialog = pane.createDialog(title);
        dialog.setAlwaysOnTop(true);
        dialog.setVisible(true);                        
        dialog.dispose();
	}
}
