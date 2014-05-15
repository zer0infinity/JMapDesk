package ch.hsr.ifs.jmapdesk;

import ch.hsr.ifs.jmapdesk.ui.JMapDesk_GUI;

public class StartJMapDesk {
	
	public static final boolean DEBUG = true;
	
	public static void main(String[] args) {
//		new JMapDesk();
		JMapDesk_GUI gui = new JMapDesk_GUI();
		gui.show();
	}
}
