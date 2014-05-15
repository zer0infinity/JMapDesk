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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class MapGen {
	
	private static final long serialVersionUID = 1L;
	private Properties properties = new Properties();
	private final String P_OGRINFO = "ogrinfo.location";
	private Container c;
	private final String shape = "shape";
	private final String image = "image";
	private final String wms = "wms";
	private LayerPanel layerpanel;
	
	public MapGen(LayerPanel layerpanel, Container c) {
		this.layerpanel = layerpanel;
		this.c = c;
	}
	
	/**
	 * Open Shapefile
	 */
	public void open_shp() {
		JFileChooser fileChooser = file_browser();
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith("shp") || file.isDirectory();
			}
			public String getDescription() {
				return "Shape (*.shp)";
			}
		});
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File[] file = fileChooser.getSelectedFiles();
			get_file_info(file);
		}
	}
	
	/**
	 * Open Imagefile
	 */
	public void open_image() {
		JFileChooser fileChooser = file_browser();
		fileChooser.setFileFilter(new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith("gif")
							|| file.getName().toLowerCase().endsWith("png")
							|| file.getName().toLowerCase().endsWith("jpg")
							|| file.getName().toLowerCase().endsWith("jpeg")
							|| file.getName().toLowerCase().endsWith("tiff")
							|| file.getName().toLowerCase().endsWith("tif") 
							|| file.isDirectory();
			}
			public String getDescription() {
				return "Image (*.gif; *.jpg; *.png; *.tiff)";
			}
		});
		if(fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			File[] file = fileChooser.getSelectedFiles();
			get_filename(file);
		}
	}
	
	/**
	 * File Browser
	 * 
	 * @param type Type of the file
	 */
	private JFileChooser file_browser() {
		loadProperties();
		String P_BROWSE_LOCATION = "browse.location";
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setMultiSelectionEnabled(true);
		if(properties.containsKey(P_BROWSE_LOCATION)) {
			fileChooser.setCurrentDirectory(new File(properties.getProperty(P_BROWSE_LOCATION)));
		}
		return fileChooser;
	}
	
	/**
	 * Save File
	 */
	public void file_save() {
		loadProperties();
		String P_BROWSE_LOCATION = "browse.location";
		if (!c.getlayer().isEmpty()) {
			JFileChooser fileChooser = new JFileChooser() {
				
				private static final long serialVersionUID = 1L;
				
				@Override
	            public void approveSelection() {
	                File f = getSelectedFile();
	        		if (!f.getName().endsWith("map")) {
	        			f = new File(f.toString() + ".map");
	        		}
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
			fileChooser.setFileFilter(new FileFilter() {
				public boolean accept(File file) {
					return file.getName().toLowerCase().endsWith(".map") || file.isDirectory();
				}
				public String getDescription() {
					return "Map (*.map)";
				}
			});
			if(properties.containsKey(P_BROWSE_LOCATION)) {
				fileChooser.setCurrentDirectory(new File(properties.getProperty(P_BROWSE_LOCATION)));
			}
			if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getName();
				String path = fileChooser.getCurrentDirectory().toString();
				write_map(path, filename);
			}
		}
	}
	
	/**
	 * Get Filename from Imagefile
	 * 
	 * @param file Imagefiles
	 */
	private void get_filename(File[] file) {
		for (int i = 0; i < file.length; i++) {
			String filename = file[i].getName();
			layerpanel.create_item(new Layer(filename));
		}
	}
	
	/**
	 * Get Infos with "ogrinfo" from shapefiles
	 * 
	 * @param file Shapfiles
	 */
	private void get_file_info(File[] file) {
		for (int i = 0; i < file.length; i++) {
			loadProperties();
			String shapefile = file[i].toString();
			String[] cmd = { properties.getProperty(P_OGRINFO), "-al", "-so", shapefile };
			ProcessBuilder pb = new ProcessBuilder(cmd);
			Process process = null;
			try {
				process = pb.start();
			} catch (IOException e) {
				System.out.println(e);
			}
			extract_layer(process);
		}
	}
	
	/**
	 * Extract the needed parts from "ogrinfo" output
	 * 
	 * @param p Process
	 */
	private void extract_layer(Process p) {
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String str;
        String regex = ".*: ";
        String name = null, data = null, type = null, class_name = null, extent = null;
        try {
			while ((str = br.readLine()) != null) {
				if (str.toLowerCase().contains("layer name:")) {
					name = str.replaceAll(regex, "");
					data = str.replaceAll(regex, "");
					class_name = str.replaceAll(regex, "");
				} else if (str.toLowerCase().contains("geometry:")) {
					str = str.replaceAll(" String", "");
					type = str.replaceAll(regex, "");
				} else if (str.toLowerCase().contains("extent:")) {
					String[] remove = { "\\(", "\\)", ",", "- " };
					for (int i = 0; i < remove.length; i++) {
						str = str.replaceAll(remove[i], "");
					}
					extent = str.replaceAll(regex, "");
				}
			}
			br.close();
		} catch (IOException e) {
			System.out.println(e);
		}
		layerpanel.create_item(new Layer(name, data, type, class_name, extent));
	}
	
	/**
	 * Write Mapfile
	 * 
	 * @param path Filepath
	 * @param filename FileName
	 */
	public void write_map(String path, String filename) {
		MapFile mapfile = new MapFile();
		boolean match = false;
		int i = filename.lastIndexOf('.');
		if (i > 0 && i < filename.length() - 1) {
			match = true;
		}
		BufferedWriter bw = null;
		String extent = calc_extent(c.getlayer());
		try {
			if (match == false){
				bw = new BufferedWriter(new FileWriter(path + System.getProperty("file.separator") + filename + ".map"));
			} else if (match == true) {
				bw = new BufferedWriter(new FileWriter(path + System.getProperty("file.separator") + filename));
			}
			bw.write(mapfile.get_start(extent));
			boolean found_wms = false;
			boolean found_point = false;
			for (int j = 0; j < c.getlayer().size(); j++) {
				if (c.getlayer().get(j).getMode() == wms && !found_wms) {
					bw.write(mapfile.add_wms_pre());
					found_wms = true;
				}
				if (c.getlayer().get(j).getType() != null) {
					if (c.getlayer().get(j).getType().equalsIgnoreCase("point")) {
						bw.write(mapfile.add_symbol());
						found_point = true;
					}
				}
				if (found_wms && found_point) {
					break;
				}
			}
			for (int j = 0; j < c.getlayer().size(); j++) {
				if (c.getlayer().get(j).getMode() == shape) {
					bw.write(mapfile.add_layer(c.getlayer().get(j)));
				} else if (c.getlayer().get(j).getMode() == image) {
					bw.write(mapfile.add_image(c.getlayer().get(j).getName()));
				} else if (c.getlayer().get(j).getMode() == wms) {
					bw.write(mapfile.add_wms(c.getlayer().get(j)));
				}
			}
			bw.write(mapfile.get_end());
			bw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Calc Extent of the Shapefiles
	 * Takes the smallest UpperLeft Coordinates and
	 * biggest/largest/greatest width and height
	 * 
	 * @param layerlist Layers
	 * @return
	 */
	private String calc_extent(ArrayList<Layer> layerlist) {
		Double extent1 = null, extent2 = null, extent3 = null, extent4 = null;
		for (int i = 0; i < layerlist.size(); i++) {
			if (layerlist.get(i).getMode() == shape) {
				StringTokenizer tk = new StringTokenizer(layerlist.get(i).getExtent(), " ");
				Double tmp = Double.valueOf(tk.nextToken());
				if (extent1 == null) {
					extent1 = tmp;
				} else if (tmp < extent1) {
					extent1 = tmp;
				}
				tmp = Double.valueOf(tk.nextToken());
				if (extent2 == null) {
					extent2 = tmp;
				} else if (tmp < extent2) {
					extent2 = tmp;
				}
				tmp = Double.valueOf(tk.nextToken());
				if (extent3 == null) {
					extent3 = tmp;
				} else if (extent3 < tmp) {
					extent3 = tmp;
				}
				tmp = Double.valueOf(tk.nextToken());
				if (extent4 == null) {
					extent4 = tmp;
				} else if (extent4 < tmp) {
					extent4 = tmp;
				}
			}
		}
		if (extent1 == null) {
			extent1 = 0.0;
		}
		if (extent2 == null) {
			extent2 = 0.0;
		}
		if (extent3 == null) {
			extent3 = 0.0;
		}
		if (extent4 == null) {
			extent4 = 0.0;
		}
		BigDecimal bg = new BigDecimal(extent1);
		extent1 = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		bg = new BigDecimal(extent2);
		extent2 = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		bg = new BigDecimal(extent3);
		extent3 = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		bg = new BigDecimal(extent4);
		extent4 = bg.setScale(6, BigDecimal.ROUND_HALF_DOWN).doubleValue();
		return extent1 + " " + extent2 + " " + extent3 + " " + extent4;
	}
	
	/**
	 * load Properties
	 */
	private void loadProperties() {
		try {
			properties.load(new FileInputStream("jmapdesk.cfg"));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
