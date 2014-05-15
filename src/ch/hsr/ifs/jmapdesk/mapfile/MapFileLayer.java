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

import java.util.regex.Pattern;

public class MapFileLayer {
	
	/**
	 * Mapfile Layer Object
	 */
	private String name;
	private String classname;
	private String status;
	private String type;

	public MapFileLayer() {
		this.name = "";
		this.classname = "";
		this.status = "";
		this.type = "";
	}
	
	public String getClassname() {
		if(classname.equals("")) {
			return name;
		} else {
			return classname;
		}
	}
	public String getName() {
		return name;
	}
	public String getStatus() {
		return status;
	}
	public String getType() {
		return type;
	}
	public void setClassname(String classname) {
		this.classname = clean(classname);
	}
	public void setName(String name) {
		this.name = clean(name);
	}
	public void setStatus(String status) {
		this.status = clean(status);
	}
	public void setType(String type) {
		this.type = clean(type);
	}
	public String toString() {
		return this.getClass().getName() + " Name: " + name + " / Classname: " + classname + " / Status: " + status;
	}
	public boolean isOn() {
		return this.status.equalsIgnoreCase("on") || isDefault();
	}
	public boolean isDefault(){
		 return this.status.equalsIgnoreCase("default");
	}

	/**
	 * remove comments
	 * 
	 * @param remove Text String
	 * @return cleaned String
	 */
	private String clean(String remove) {
		String[] regex = { "#.*", "'", "\"" };
		for (int i = 0; i < regex.length; i++) {
			remove = Pattern.compile(regex[i]).matcher(remove).replaceAll("");
		}
		return remove;
	}
}
