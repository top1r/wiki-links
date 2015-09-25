package com.bionic.jse.wiki;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class PageRun {

	public static void main(String[] args) throws MalformedURLException  {
		// Hardcoded values for the pages
		PageUtils utils = new PageUtils();
		
		utils.addPage("London", "https://en.wikipedia.org/wiki/London");
		utils.addPage("London", "https://en.wikipedia.org/wiki/London");
		utils.addPage("Local government in London", "https://en.wikipedia.org/wiki/Local_government_in_London");
		PageScript.invokeMethod(utils, "script.csv");
	}
}
