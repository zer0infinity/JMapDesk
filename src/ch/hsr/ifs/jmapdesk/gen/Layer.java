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

public class Layer {
	
	/**
	 * Object Layer: Shape, Image, WMS 
	 */
	private String name;
	private String type;
	private String data;
	private String class_name;
	private String extent;
	private String format;
	private String status;
	private String url;
	private String mode;
	
	// shape
	public Layer(String name, String data, String type, String class_name, String extent) {
		this(name);
		this.type = type;
		this.data = data;
		this.class_name = class_name;
		this.extent = extent;
		this.mode = "shape";
	}
	
	// image
	public Layer(String name) {
		this.name = name;
		this.mode = "image";
	}
	
	// wms
	public Layer(String url, String name, String format, String status) {
		this(name);
		this.url = url;
		this.format = format;
		this.status = status;
		this.mode = "wms";
	}
	
	public String getURL() {
		return url;
	}
	public String getStatus() {
		return status;
	}
	public String getFormat() {
		return format;
	}
	public String getMode() {
		return mode;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public String getData() {
		return data;
	}
	public String getClassName() {
		return class_name;
	}
	public String getExtent() {
		return extent;
	}
	
	
	// just implementation for junit
	public boolean equalsImage(Object anObject) {
		if (anObject instanceof Layer) {
			Layer aLayer = (Layer) anObject;
			return aLayer.getName().equals(name);
		}
		return false;
	}
	
	public boolean equalsShape(Object anObject) {
		if (anObject instanceof Layer) {
			Layer aLayer = (Layer) anObject;
			return aLayer.getName().equals(name)
				&& aLayer.getData().equals(data)
				&& aLayer.getType().equals(type)
				&& aLayer.getClassName().equals(class_name)
				&& aLayer.getExtent().equals(extent);
		}
		return false;
	}
	
	public boolean equalsWMS(Object anObject) {
		if (anObject instanceof Layer) {
			Layer aLayer = (Layer) anObject;
			return aLayer.getURL().equals(url)
				&& aLayer.getName().equals(name)
				&& aLayer.getFormat().equals(format)
				&& aLayer.getStatus().equals(status);
		}
		return false;
	}
}
