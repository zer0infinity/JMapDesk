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

package ch.hsr.ifs.jmapdesk.util;

import java.awt.geom.Point2D;

public class MapPoint {
	public double x;
	public double y;
	
	public MapPoint(double x, double y){
		this.x = x;
		this.y = y;
	}
	public MapPoint(MapPoint mp) {
		this.x = mp.x;
	 	this.y = mp.y;
	}
	public MapPoint(Point2D.Double p) {
		this.x = p.getX();
		this.y = p.getY();
	}
	public MapPoint(){
		this.x = 0;
		this.y = 0;
	}
	public void setLocation(double x,double y) {
		this.x = x;
		this.y = y;
	}
	public Point2D.Double toPoint() {
		return new Point2D.Double(x, y);
	}
	public void translate(double x, double y) {
		this.x += x;
		this.y += y;
	}
	public String toString() {
		return String.format("%.2f",x)+"/"+String.format("%.2f",y);
	}
	public void translate(MapPoint movement) {
		this.x += movement.x;
		this.y += movement.y;
	}
}
