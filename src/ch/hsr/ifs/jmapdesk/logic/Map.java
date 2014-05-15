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

package ch.hsr.ifs.jmapdesk.logic;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

import ch.hsr.ifs.jmapdesk.mapfile.MapFile;
import ch.hsr.ifs.jmapdesk.util.MapPoint;
import ch.hsr.ifs.jmapdesk.wrapper.InvalidBinaryException;
import ch.hsr.ifs.jmapdesk.wrapper.MapGenerator;

public class Map {
	
	private double width;
	private double height;
	private Rectangle2D.Double extent;
	private int level = 0;
	private MapPoint position;
	
	public Map(MapFile mapfile) {
		this.extent = mapfile.getFullExtent();
		this.width = mapfile.getSize().getWidth();
		this.height = mapfile.getSize().getHeight();
	}
	
	public double getUpperLeftX() {
		return extent.getMinX();
	}
	
	public double getUpperLeftY() {
		return extent.getMinY();
	}
	
	public double getMapWidth() {
		return extent.getWidth();
	}
	
	public double getMapHeight() {
		return extent.getHeight();
	}
	
	public double getImageHeight() {
		return height;
	}
	
	public double getImageWidth() {
		return width;
	}
	
	public void zoomOut() {
		if (level > 0) {
			level--;
		}
	}
	
	public void zoomExtent() {
		level = 0;
		position = new MapPoint(this.extent.getWidth()/2, this.extent.getHeight()/2);
	}

	public void zoomIn(Point pos) {
		if (level < 13) {
			adjustPosition(pos);
			level ++;
		}
	}

	private MapPoint translateScreenToExtents(Point movement) {
		MapPoint p = new MapPoint();
		p.x = getXFactor()*movement.x;
		p.y = getYFactor()*movement.y;
		return p;
	}
	
	public void adjustPosition(Point movement) {
		movement.translate(-(int)map_width/2, -(int)map_height/2);
		movement.y *= -1;
		MapPoint mp = translateScreenToExtents(movement);
		double x = ((mp.x)/Math.pow(2,level));
		double y = ((mp.y)/Math.pow(2,level));
		position.translate(x, y);
	}
	
	public void setPosition(Point pos) {
		MapPoint mp = translateScreenToExtents(pos);
		position.setLocation(mp.x, height-mp.y);
	}
}
