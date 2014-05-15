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

import static org.junit.Assert.*;

import org.junit.Test;

import ch.hsr.ifs.jmapdesk.gen.Layer;

public class LayerTest {

	@Test
	public void testLayerStringStringStringStringString() {
		Layer layer1 = new Layer("shape1", "", "point", "test_class1", "0 0 0 0");
		Layer layer2 = new Layer("shape2", "", "line", "test_class2", "1 1 1 1");
		assertTrue(!layer1.equalsShape(layer2));
		assertTrue(!layer1.equalsShape(null));
		assertTrue(!layer2.equalsShape(null));
		assertEquals(layer1, layer1);
		assertEquals(layer2, layer2);
	}

	@Test
	public void testLayerString() {
		Layer layer1 = new Layer("testimage1");
		Layer layer2 = new Layer("testimage2");
		assertTrue(!layer1.equalsImage(layer2));
		assertTrue(!layer1.equalsImage(null));
		assertTrue(!layer2.equalsImage(null));
		assertEquals(layer1, layer1);
		assertEquals(layer2, layer2);
	}

	@Test
	public void testLayerStringStringStringString() {
		Layer layer1 = new Layer("www.test1.com", "wms1", "jpg", "on");
		Layer layer2 = new Layer("www.test2.com", "wms2", "png", "off");
		assertTrue(!layer1.equalsWMS(layer2));
		assertTrue(!layer1.equalsWMS(null));
		assertTrue(!layer2.equalsWMS(null));
		assertEquals(layer1, layer1);
		assertEquals(layer2, layer2);
	}

}
