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

package ch.hsr.ifs.jmapdesk.mapfile;

import java.awt.Dimension;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import ch.hsr.ifs.jmapdesk.logic.JMapDesk;

public class MapFile {
	private Dimension2D size;
	private String mapname = "(unnamed)";
	private Rectangle2D.Double fullExtent;
	private ArrayList<MapFileLayer> layers = new ArrayList<MapFileLayer>();
	private ArrayList<MapFileLayer> statuslist = new ArrayList<MapFileLayer>();
	private int fileHash = 0;
	private boolean refresh = false;
	private File file;
	private File tempmap = new File (JMapDesk.TEMPMAP);

	/**
	 * Copy MapFile to a temp MapFile, change the "imagepath" and "imageurl" if
	 * necessary. then reread the temp MapFile.
	 * 
	 * @param file MapFile
	 */
	public MapFile(File file) {
		this.file = file;
		layers.clear();
		readMapfile();
		fileHash = file.hashCode();
	}
	
	/**
	 * Copy MapFile to a temp MapFile, change the "imagepath" and "imageurl" if
	 * necessary.
	 */
	private void writeTempFile() {
		TagList tags = new TagList();
		BufferedReader br = null;
		BufferedWriter bw = null;
		String line = null;
		try {
			br = new BufferedReader(new FileReader(file));
			bw = new BufferedWriter(new FileWriter(tempmap));
			while ((line = br.readLine()) != null) {
				// replace "	" (tab) with a " " (space)
				line = Pattern.compile("\t").matcher(line).replaceAll(" ");
				StringTokenizer tk = new StringTokenizer(line, " ");
				if (tk.hasMoreTokens()) {
					String cmd = tk.nextToken();
					if (cmd.equalsIgnoreCase("layer") || cmd.equalsIgnoreCase("web")){
						tags.add(cmd);
					} else if ((tags.isCurrent("layer") || tags.isCurrent("web")) && cmd.equalsIgnoreCase("end")) {
						tags.removeCurrent();
					} else if (tags.isCurrent("layer") && cmd.equalsIgnoreCase("status")) {
						String status = tk.nextToken();
						if (status.equalsIgnoreCase("default")) {
							line = "    STATUS ON";
						}
					} else if (tags.isCurrent("web") && cmd.equalsIgnoreCase("imagepath")) {
						line = "    IMAGEPATH \""
							+ System.getProperty("java.io.tmpdir")
							+ File.separator + "jmapdesk-maps"
							+ File.separator + "\"";
					} else if (tags.isCurrent("web") && cmd.equalsIgnoreCase("imageurl")) {
						line = "    IMAGEURL \"/\"";
					}
				}
				bw.write(line);
				bw.newLine();
			}
			br.close();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse Mapfile to get the needed parts for the GUI
	 */
	private void readMapfile() {
		writeTempFile();
		
		TagList tags = new TagList();
		BufferedReader br = null;
		String line = null;
		String layername = null;
		try {
			br = new BufferedReader(new FileReader(tempmap));
			while ((line = br.readLine()) != null) {
				StringTokenizer tk = new StringTokenizer(line, " ");
				if (tk.hasMoreTokens()) {
					String cmd = tk.nextToken();
					if (cmd.equalsIgnoreCase("map")	|| cmd.equalsIgnoreCase("class")){
						tags.add(cmd);
					} else if(cmd.equalsIgnoreCase("layer")) {
						tags.add(cmd);
						layers.add(new MapFileLayer());
					} else if ((tags.isCurrent("map") || tags.isCurrent("class")
							|| tags.isCurrent("layer")) && cmd.equalsIgnoreCase("end")) {
						tags.removeCurrent();
					} else if (tags.isCurrent("map") && cmd.equalsIgnoreCase("size")) {
						size = new Dimension();
						double width = Double.valueOf(tk.nextToken().trim());
						double height = Double.valueOf(tk.nextToken().trim());
						size.setSize(width, height);
					} else if (tags.isCurrent("map") && cmd.equalsIgnoreCase("extent")) {
						fullExtent = new Rectangle2D.Double();
						fullExtent.x = Double.valueOf(tk.nextToken().trim());
						fullExtent.y = Double.valueOf(tk.nextToken().trim());
						fullExtent.width = Double.valueOf(tk.nextToken().trim()) - fullExtent.x;
						fullExtent.height = Double.valueOf(tk.nextToken().trim()) - fullExtent.y;
					} else if (tags.isCurrent("map") && cmd.equalsIgnoreCase("name")) {
						String name = "";
						while (tk.hasMoreTokens()) {
							name = name.concat(tk.nextToken() + " ");
						}
						if (mapname.equals("(unnamed)"))
							mapname = name.trim();
					} else if (tags.isCurrent("layer") && cmd.equalsIgnoreCase("name")) {
						layername = "";
						while (tk.hasMoreTokens()) {
							layername = layername.concat(tk.nextToken()) + " ";
						}
						layername = layername.trim();
						layers.get(layers.size() - 1).setName(layername);
					} else if (tags.isCurrent("layer") && cmd.equalsIgnoreCase("type")) {
						layers.get(layers.size() - 1).setType(tk.nextToken());
					} else if (tags.isCurrent("class") && tags.isParent("layer") && cmd.equalsIgnoreCase("name")) {
						String classname = "";
						while (tk.hasMoreTokens()) {
							classname = classname.concat(tk.nextToken()) + " ";
						}
						classname = classname.trim();
						layers.get(layers.size() - 1).setClassname(classname);
					} else if (tags.isCurrent("layer") && cmd.equalsIgnoreCase("status")) {
						String status = tk.nextToken();
						if (refresh) {
							boolean found = false;
							for (int i = 0; i < statuslist.size(); i++) {
								if (statuslist.get(i).getName().equals(layername)) {
									layers.get(layers.size() - 1).setStatus(statuslist.get(i).getStatus());
									found = true;
									break;
								}
							}
							if (!found) {
								layers.get(layers.size() - 1).setStatus(status);
							}
						} else {
							layers.get(layers.size() - 1).setStatus(status);
						}
					}
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Reload
	 * 
	 * @param statuslist LayerList
	 */
	public void reloadIfChanged(ArrayList<MapFileLayer> statuslist) {
		if (isChanged()) {
			refresh = true;
			this.statuslist = statuslist;
			this.layers.clear();
			readMapfile();
			refresh = false;
		}
	}
	 
	private boolean isChanged() {
		if (file.hashCode() != fileHash) {
			fileHash = file.hashCode();
			return true;
		}
		return false;
	}

	public Rectangle2D.Double getFullExtent() {
		return fullExtent;
	}

	public ArrayList<MapFileLayer> getLayers() {
		return layers;
	}

	public Dimension2D getSize() {
		return size;
	}

	public String getMapName() {
		return mapname;
	}
}
