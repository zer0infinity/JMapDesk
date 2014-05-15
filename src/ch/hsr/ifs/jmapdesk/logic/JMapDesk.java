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

package ch.hsr.ifs.jmapdesk.logic;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import ch.hsr.ifs.jmapdesk.StartJMapDesk;
import ch.hsr.ifs.jmapdesk.mapfile.MapFile;
import ch.hsr.ifs.jmapdesk.ui.JMapDesk_GUI;
import ch.hsr.ifs.jmapdesk.ui.SplashScreen;
import ch.hsr.ifs.jmapdesk.ui.ViewComponent;

public class JMapDesk {
	
	public static final String VERSION = "1.0 BETA";
	public static final String LBL_WELCOME = "Welcome to JMapDesk";
	public static final String PROGRAMNAME = "JMapDesk";
	public static final String TEMPMAP = "_jmap_tmp.map";

	public static final String P_SHP2IMG = "shp2img.location";
	public static final String P_OGRINFO = "ogrinfo.location";
	
	private Properties properties = new Properties();
	private JMapDesk_GUI mapdeskgui;
	private Map map;
	private MapFile mapfile;
	private ViewComponent comp;
	
	public JMapDesk() {
//		if (!StartJMapDesk.DEBUG) {
//			new SplashScreen();
//		}
//		comp = new ViewComponent();
		mapdeskgui = new JMapDesk_GUI(this, comp, properties);
		mapdeskgui.show();
	}
	
	public String open_mapfile(File file) {
		mapfile = new MapFile(file);
		map = new Map(mapfile);
		String log = check_mapfile(file);
		loadMapfile();
		return log;
	}
	
	public boolean save_mapimage(BufferedImage bimage) {
		boolean isSaved = false;
		JFileChooser fileChooser = file_chooser();
		fileChooser.setDialogTitle("Save Image As...");
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".png") || file.isDirectory();
			}
			public String getDescription() {
				return "PNG (*.png)";
			}
		});
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".gif") || file.isDirectory();
			}
			public String getDescription() {
				return "GIF (*.gif)";
			}
		});
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(".jpg")
					|| file.getName().toLowerCase().endsWith(".jpeg")
					|| file.getName().toLowerCase().endsWith(".jpe")
					|| file.isDirectory();
			}
			public String getDescription() {
				return "JPEG (*.jpg, *.jpeg, *.jpe)";
			}
		});
		if(properties.containsKey(JMapDesk_GUI.P_BROWSE_LOCATION)) {
			fileChooser.setCurrentDirectory(new File(properties.getProperty(JMapDesk_GUI.P_BROWSE_LOCATION)));
		}
		if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			String filename = fileChooser.getSelectedFile().getName();
			String path = fileChooser.getCurrentDirectory().toString();
			String desc = fileChooser.getFileFilter().getDescription();
			String format = "jpg";
			if (desc.contains("png")) {
				format = "png";
			} else if (desc.contains("gif")) {
				format = "gif";
			} else {
				format = "jpg";
			}
			isSaved = true;
			try {
				ImageIO.write(bimage, format, new File(path + File.separator + filename + "." + format));
			} catch (IOException e1) {
				System.out.println(e1);
			}
		}
		return isSaved;
	}
	
	/**
	 * check if shp2img gives errors
	 * 
	 * @param mapFile
	 */
	String check_mapfile(File mapFile) {
		String text = "";
		String cmd = null;
		String shp2img = properties.getProperty("shp2img.location");
		File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "jmapdesk-maps");
		dir.mkdirs();
		String output = dir.toString() + File.separator + "shp2img_check.png";
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			String set_fwtools = "PATH=$PATH:" + shp2img.replace("/shp2img", "");
			String exec = shp2img + " -m " + mapFile + " -o " + output + " -map_debug 1";
			File tempfile = new File(System.getProperty("java.io.tmpdir") + File.separator + "_jmdesk_ss.sh");
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(tempfile));
				bw.write("#!/bin/bash");
				bw.newLine();
				bw.write(set_fwtools);
				bw.newLine();
				bw.write(exec);
				bw.close();
			} catch (IOException e) {
				System.out.println(e);
			}
			cmd = "sh " + tempfile;
			tempfile.delete();
		} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			String set_fwtools = "set FWTOOLS_DIR=" + shp2img.replace("\\bin\\shp2img.exe", "");
			String call_fwtools = "call \"%FWTOOLS_DIR%\\bin\\setfwenv.bat\"";
			cmd = "cmd /c " + set_fwtools + "&&" + call_fwtools + "&&"
				+ "shp2img.exe"	+ " -m " + "\"" + mapFile + "\"" + " -o " + "\"" + output + "\"" + " -map_debug 1";
		}
		Runtime runtime = Runtime.getRuntime();
		try {
			Process p = runtime.exec(cmd);
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			br = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((text = br.readLine()) != null) {
				sb.append(text + "\n").trimToSize();
			}
			br = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((text = br.readLine()) != null) {
				sb.append(text + "\n").trimToSize();
			}
			br.close();
			text = String.valueOf(sb).trim();
		} catch (IOException e) {
			System.out.println(e);
		}
		return text;
	}
	
	/**
	 * load mapfile
	 * 
	 * @param mapfile MapFile
	 */
	private void loadMapfile() {
		
	}
	
	/**
	 * Customized FileChooser with overwrite Feature
	 * 
	 * @return FileChooser
	 */
	public JFileChooser file_chooser() {
		JFileChooser fileChooser = new JFileChooser() {
			private static final long serialVersionUID = 1L;
			@Override
            public void approveSelection() {
                File f = getSelectedFile();
                if(f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(getTopLevelAncestor(),
                            "The selected file already exists. " +
                            "Do you want to overwrite it?",
                            "The file already exists",
                            JOptionPane.YES_NO_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    switch(result)  {
                    case JOptionPane.YES_OPTION:
                        super.approveSelection();
                        return;
                    case JOptionPane.NO_OPTION:
                        return;
                    case JOptionPane.CANCEL_OPTION:
                        cancelSelection();
                        return;
                    }
                }
                super.approveSelection();
            }
		};
		return fileChooser;
	}
	
	/**
	 * Check if shp2img exists 
	 */
	public boolean askForBinaryFile() {
		final String TXT_SELECT_BINARY = PROGRAMNAME+" needs the 'shp2img' binary to run!\r\n" +
				"The binary is part of the FWTools toolkit and can be downloaded for Windows and Linux.\r\n" +
				" Please specify the location of the 'shp2img' binary in the following dialog!";
		
		if(properties.containsKey(P_SHP2IMG)){
			if(new File(properties.getProperty(P_SHP2IMG)).exists()){
				return true;
			}
		}
		JFileChooser fileChooser = file_chooser();
		fileChooser.setDialogTitle("Please select 'shp2img' binary file...");
		int result = JOptionPane.showConfirmDialog(null, TXT_SELECT_BINARY, PROGRAMNAME, JOptionPane.OK_CANCEL_OPTION);
		boolean askbinary = true;
        switch(result)  {
        case JOptionPane.OK_OPTION:
    		fileChooser.setFileFilter(new FileFilter() {
    			String shp2img = null;
    			final String linux = "shp2img";
    			final String windows = "shp2img.exe";
    			public boolean accept(File file) {
    				if (System.getProperty("os.name").toLowerCase().contains("linux")) {
    					shp2img = linux;
    				} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
    					shp2img = windows;
    				}
    				return file.getName().toLowerCase().equals(shp2img) || file.isDirectory();
    			}
    			public String getDescription() {
    				if (System.getProperty("os.name").toLowerCase().contains("linux")) {
        				shp2img = linux;
    				} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
        				shp2img =  windows;
    				}
    				return shp2img;
    			}
    		});
    		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
    			properties.setProperty(P_SHP2IMG, fileChooser.getSelectedFile().getAbsolutePath());
    			if (System.getProperty("os.name").toLowerCase().contains("linux")) {
    				properties.setProperty(P_OGRINFO, fileChooser.getCurrentDirectory() + File.separator + "ogrinfo");
    			} else if (System.getProperty("os.name").toLowerCase().contains("windows")) {
    				properties.setProperty(P_OGRINFO, fileChooser.getCurrentDirectory() + File.separator + "ogrinfo.exe");
    			}
            	askbinary = true;
    		} else {
    			askbinary = false;
    			fileChooser.cancelSelection();
    		}
    		break;
        case JOptionPane.CANCEL_OPTION:
        	askbinary = false;
        	fileChooser.cancelSelection();
        	break;
        }
        return askbinary;
	}
}
