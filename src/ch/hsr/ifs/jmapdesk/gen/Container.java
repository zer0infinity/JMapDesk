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

import java.util.ArrayList;

public class Container {
	private ArrayList<Layer> layerlist = new ArrayList<Layer>();
	
	public void add(Layer layer) {
		layerlist.add(layer);
	}
	
	public ArrayList<Layer> getlayer() {
		return layerlist;
	}
	
	public void empty() {
		layerlist.clear();
	}
}
