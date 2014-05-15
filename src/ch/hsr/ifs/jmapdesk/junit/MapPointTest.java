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

import java.awt.Point;

import org.junit.Test;

import ch.hsr.ifs.jmapdesk.util.MapPoint;

public class MapPointTest {

	@Test
	public void testMapPointDoubleDouble() {
		MapPoint mp = new MapPoint(10, 20);
		assertNotNull(mp.x);
		assertNotNull(mp.y);
		assertEquals(mp.x, 10);
		assertEquals(mp.y, 20);
	}

	@Test
	public void testMapPointMapPoint() {
		MapPoint mp = new MapPoint(new MapPoint(10, 20));
		assertNotNull(mp.x);
		assertNotNull(mp.y);
		assertEquals(mp.x, 10);
		assertEquals(mp.y, 20);
	}

	@Test
	public void testMapPointPoint() {
		MapPoint mp = new MapPoint(new Point(10, 20));
		assertNotNull(mp.x);
		assertNotNull(mp.y);
		assertEquals(mp.x, 10);
		assertEquals(mp.y, 20);
	}

	@Test
	public void testMapPoint() {
		MapPoint mp = new MapPoint();
		assertNotNull(mp.x);
		assertNotNull(mp.y);
		assertEquals(mp.x, 0);
		assertEquals(mp.y, 0);
	}

	@Test
	public void testTranslateDoubleDouble() {
		MapPoint mp = new MapPoint(10, 20);
		mp.translate(30, 30);
		assertNotNull(mp.x);
		assertNotNull(mp.y);
		assertEquals(mp.x, 40);
		assertEquals(mp.y, 50);
	}
}
