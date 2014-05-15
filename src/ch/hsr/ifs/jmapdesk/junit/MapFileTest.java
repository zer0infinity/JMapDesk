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

package ch.hsr.ifs.jmapdesk.junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

import ch.hsr.ifs.jmapdesk.mapfile.MapFile;

public class MapFileTest {

	@Test
	public void testReadMapfile() {
		String sample = 
			"# Mapfile" + "\n" +
			"#" + "\n" +
			"# Workshop SOGI FG5 2007-08-30, Fribourg, Teil 5 Grafik." + "\n" +
			"#" + "\n" +
			"# Aufgabe 2b" + "\n" +
			"#" + "\n" +
			"\n" +
			"MAP" + "\n" +
			"  # Header Sektion" + "\n" +
			"  NAME Schweiz" + "\n" +
			"  EXTENT 470000 62000 860000 320000" + "\n" +  
			"  UNITS METERS" + "\n" +
			"  SHAPEPATH \"data\"" + "\n" +
			"  SIZE 600 400" + "\n" +
			"  IMAGETYPE JPEG" + "\n" +
			"\n" +
			"  # SYMBOL Sektion" + "\n" +
			"  SYMBOL" + "\n" +
			"     NAME 'square'" + "\n" +
			"     TYPE VECTOR" + "\n" +
			"     FILLED TRUE" + "\n" +
			"     POINTS 0 0 0 1 1 1 1 0 0 0 END" + "\n" +
			"  END" + "\n" +
			"\n" +
			"  SYMBOL" + "\n" +
			"    NAME 'circle'" + "\n" +
			"    TYPE ELLIPSE" + "\n" +
			"    POINTS 1 1 END" + "\n" +
			"    FILLED TRUE" + "\n" +
			"  END" + "\n" +
			"\n" +
			"  # LAYER Sektion" + "\n" +
			"  LAYER" + "\n" +
			"    NAME kantone" + "\n" +
			"    TYPE POLYGON" + "\n" +
			"    DATA kantone" + "\n" +
			"    STATUS ON" + "\n" +
			"\n" +
			"    # CLASS Sektion" + "\n" +
			"    CLASS" + "\n" +
			"      NAME \"Kantone\"" + "\n" +
			"      STYLE" + "\n" +
			"        COLOR 255 255 220 # hellgelb" + "\n" +
			"        OUTLINECOLOR 0 0 0 # schwarz" + "\n" +
			"      END" + "\n" +
			"    END" + "\n" +
			"  END" + "\n" +
			"\n" +
			"END # Map File";
		
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("testin.map"));
			bw.write(sample);
			bw.close();
		} catch (IOException e) {
			fail(e.getMessage());
		}
		File in = new File("testin.map");
		MapFile mapfile = new MapFile(in);
		assertEquals(mapfile.getSize().getHeight(), 400);
		assertEquals(mapfile.getSize().getWidth(), 600);
		assertEquals(mapfile.getFullExtent().getMinX(), 470000);
		assertEquals(mapfile.getFullExtent().getMinY(), 62000);
		assertEquals(mapfile.getFullExtent().getWidth(), 390000);
		assertEquals(mapfile.getFullExtent().getHeight(), 258000);
		assertEquals(mapfile.getMapName(), "Schweiz");
		assertEquals(mapfile.getLayers().get(0).getName(), "kantone");
		assertEquals(mapfile.getLayers().get(0).getStatus(), "ON");
		assertEquals(mapfile.getLayers().get(0).getClassname(), "Kantone");
		assertEquals(mapfile.getLayers().get(0).getType(), "POLYGON");
		in.delete();
	}
}
