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

package ch.hsr.ifs.jmapdesk.wrapper;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ch.hsr.ifs.jmapdesk.util.MapPoint;

public class MapGenerator {

	private static final File dir = new File(System.getProperty("java.io.tmpdir") + File.separator + "jmapdesk_img");
	
	/**
	 * Check if shp2img is still on avaiable
	 * 
	 * @throws InvalidBinaryException
	 */
	private static void checkBinary(String shp2img) throws InvalidBinaryException {
		final String cmd = shp2img;
		try {
			if (systemCall(cmd).toLowerCase().contains("shp2img")) {
				throw new InvalidBinaryException();
			}
		} catch (IOException e) {
			throw new InvalidBinaryException();
		} catch (InterruptedException e) {
			throw new InvalidBinaryException();
		}
	}
	
	/**
	 * generate map with shp2img
	 * 
	 * @param mapFilePath Path of Mapfile
	 * @param minxy UpperLeft Coordinates
	 * @param maxxy width and height of the map
	 * @param layerArray Layers
	 * @return Image
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static Image generateImage(String shp2img, String mapFilePath, MapPoint minxy, MapPoint maxxy, String size, ArrayList<String> layerArray) throws InterruptedException, IOException {
		try {
			checkBinary(shp2img);
		} catch (InvalidBinaryException e1) {
			e1.printStackTrace();
		}
		dir.mkdirs();
		long version = System.currentTimeMillis();
		File imagefile = new File(dir.toString() + File.separator 
				+ "map-" + version + "-" + String.valueOf(minxy.x) + "-"
				+ String.valueOf(minxy.y) + "-" + String.valueOf(maxxy.x)
				+ "-" + String.valueOf(maxxy.y) + ".png");
		String layers = "";
		for (String string : layerArray) {
			layers = layers + string + " ";
		}
		if (System.getProperty("os.name").toLowerCase().contains("linux")) {
			final String set_fwtools = "PATH=$PATH:"
					+ shp2img.replace("/shp2img", "");
			final String exec = shp2img + " -m " + mapFilePath + " -i " + "png"
					+ " -o " + imagefile.getAbsolutePath() + " -l " + "\""
					+ layers.trim() + "\"" + " -e " + minxy.x + " " + minxy.y
					+ " " + maxxy.x + " " + maxxy.y + " -s " + size;
			File tempfile = new File(System.getProperty("java.io.tmpdir")
					+ File.separator + "_jmdesk_ss.sh");
			BufferedWriter bw;
			try {
				bw = new BufferedWriter(new FileWriter(tempfile));
				bw.write("#!/bin/bash");
				bw.newLine();
				bw.write(set_fwtools);
				bw.newLine();
				bw.write(exec);
				bw.close();
			} catch (IOException e) {
				System.out.println(e);
			}
			String cmd = "sh " + tempfile;
			systemCall(cmd);
			tempfile.delete();
		} else if (System.getProperty("os.name").toLowerCase().contains(
				"windows")) {
			final String set_fwtools = "set FWTOOLS_DIR="
					+ shp2img.replace("\\bin\\shp2img.exe", "");
			final String call_fwtools = "call \"%FWTOOLS_DIR%\\bin\\setfwenv.bat\"";
			final String cmd = "cmd /c " + set_fwtools + "&&" + call_fwtools + "&&"
					+ "shp2img.exe" + " -m " + "\"" + mapFilePath + "\""
					+ " -i " + "png" + " -o " + "\""
					+ imagefile.getAbsolutePath() + "\"" + " -l " + "\""
					+ layers.trim() + "\"" + " -e " + minxy.x + " " + minxy.y
					+ " " + maxxy.x + " " + maxxy.y + " -s " + size;
			systemCall(cmd);
		}
		return Toolkit.getDefaultToolkit().getImage(imagefile.getAbsolutePath());
	}

	/**
	 * Run shp2img
	 * 
	 * @param cmd shp2img with parameters
	 * @throws IOException
	 * @throws InterruptedException
	 */
	private static String systemCall(String cmd) throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process p = runtime.exec(cmd);
		StreamGobbler errorGobbler = new StreamGobbler(p.getErrorStream());
		StreamGobbler outputGobbler = new StreamGobbler(p.getInputStream());
		errorGobbler.start();
		outputGobbler.start();
		if (p.waitFor() == 0) {
			return outputGobbler.getRetVal();
		} else {
			return null;
		}
	}

	/**
	 * delete all generate map bitmaps
	 */
	public static void cleanUp() {
        if (dir.isDirectory()) {
            String[] file = dir.list();
            for (int i=0; i < file.length; i++) {
                new File(dir, file[i]).delete();
            }
            dir.deleteOnExit();
        }
	}
}
