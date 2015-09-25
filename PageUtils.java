package com.bionic.jse.wiki;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.bionic.jse.wiki.Page.Priority;

public class PageUtils{
	private static HashSet<Page> list = null;
	private static int newId = 0;
	private static final Logger logger =
	        Logger.getLogger(PageUtils.class.getName());
	
	
	
	public PageUtils(){
		list = new HashSet<Page>();
		FileHandler handler;
		try {
			handler = new FileHandler(this.getClass().getSimpleName().toLowerCase().concat(".log"));
		    SimpleFormatter formatter = new SimpleFormatter(); 
		    handler.setFormatter(formatter);
			logger.addHandler(handler);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void addPage(String pageName, String URL){
		try {
			Page page = new Page(pageName, new URL(URL));
			logger.log(Level.INFO, "Adding page ".concat(page.getPageName()));
			if (list.add(page)){
				newId = page.getId();
				logger.log(Level.INFO, "SUCCESS");
				page.setLastUpdated(LocalDate.now());
			} else {			
				logger.log(Level.WARNING, "The page is present in the database");
				Page.setNewId(newId);
			}
		}
		catch (MalformedURLException ex) {
			logger.log(Level.SEVERE, "The URL format is not valid - " + ex.getMessage());
		}
	}
	
	public void removePage(String pageName){
		boolean wasRemoved = false;
		logger.log(Level.INFO,"Attempting to delete the page with name: " + pageName);
		for (Page p: list){
			if (p.getPageName().equals(pageName)){
				list.remove(p);
				wasRemoved = true;
			}
		}
		if (wasRemoved) {
			logger.log(Level.INFO, "The page was succesfully removed");
		} else {
			logger.log(Level.WARNING,"The page could not be found in the Database");
		}
	}
	
	public Set<? extends Page> getList() {
		return list;
	}
	
	public static void save(String filename) {
			logger.log(Level.INFO, "Saving the structure to " + filename);
			try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename));)
			{
				for (Page p: list){
					out.writeObject(p);  
				}
				logger.log(Level.INFO, "SUCCESS");
			}
			catch (IOException ei){
				logger.log(Level.SEVERE, ei.getMessage());
			}			
	}
	
	public static void load(String filename) {
		logger.log(Level.INFO, "Loading structure from " + filename);
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename));)
		{
			list.clear();
			while (true){
				list.add((Page) in.readObject());
			}			
		}
		catch (IOException ei){
			if (ei.getMessage() == null) {
				logger.log(Level.INFO, "SUCCESS");
			} else {
				logger.log(Level.SEVERE, ei.getMessage());
			}
		} 
		catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public Page getPageByURL (String url){
		for (Page p: list){
			if (p.getUrl().toString().equals(url)) {
				return p;
			}
		} 
		return null;
	}
	
	public void markAsRead (String url){
		logger.log(Level.INFO, "Marking the page with url " + url + " as read");
		if (!(getPageByURL(url) == null)){
			Page p = getPageByURL(url);
			p.setRead(true);
			p.setLastUpdated(LocalDate.now());
			logger.log(Level.INFO, "SUCCESS");
		} else {
			logger.log(Level.SEVERE, "Page with url " + url + " not found in the DB");
		}
	}
	
	public void increasePriority (String url){
		logger.log(Level.INFO, "Increasing the priority for " + url);
		if (!(getPageByURL(url) == null)){
			Page p = getPageByURL(url);
			if (p.getPagePriority().ordinal() == (Priority.values().length - 1)){
				logger.log(Level.WARNING, "The page has the highest priority");
			} else {
				p.setPagePriority(Priority.values()[p.getPagePriority().ordinal() + 1]);;
				p.setLastUpdated(LocalDate.now());
				logger.log(Level.INFO, "SUCCESS");
			}
			
		} else {
			logger.log(Level.SEVERE, "Page with url " + url + " not found in the DB");
		}
	}
	
	public void decreasePriority (String url){
		logger.log(Level.INFO, "Decreasing the priority for " + url);
		if (!(getPageByURL(url) == null)){
			Page p = getPageByURL(url);
			if (p.getPagePriority().ordinal() == 0){
				logger.log(Level.WARNING, "The page has the lowest priority");
			} else {
				p.setPagePriority(Priority.values()[p.getPagePriority().ordinal() - 1]);;
				p.setLastUpdated(LocalDate.now());
				logger.log(Level.INFO, "SUCCESS");
			}			
		} else {
			logger.log(Level.SEVERE, "Page with url " + url + " not found in the DB");
		}
	}
	
	public void generateUnreadReport(String filename) throws IOException {
		PrintStream report = new PrintStream(
			     new FileOutputStream(filename, false));
		logger.log(Level.INFO, "Generating the 'Unread Pages' report");
		String header = "<table border = 1>\n"
				+ "<tr>\n"
				+ "<td><b>Name<b></td>\n"
				+ "<td><b>URL</b></td>\n"
				+ "<td><b>Created</b></td>\n"
				+ "<td><b>Lastmodified</b></td>\n"
				+ "</tr>\n";
		String footer = "</table>";
		if (!(list.equals(null))){
			report.append(header);
			for (Page p: list){
				if (!p.isRead()){					
					report.append("<tr>\n");
					String line = String.format("<td>%s</td>%n<td>%s</td>%n<td>%s</td>%n<td>"
							+ "%s</td>%n", 
							p.getPageName(), 
							p.getUrl(),
							p.getCreated().toString(),
							p.getLastUpdated().toString());
					report.append(line);
					report.append("</tr>\n");
				}
			}
			report.append(footer);
			logger.log(Level.INFO, "SUCCESS");
		} else {
			logger.log(Level.SEVERE, "The list is empty");
		}
		report.close();
	}
	
	public void generatePriorityReport(String filename, String priority) throws IOException {
		PrintStream report = new PrintStream(
			     new FileOutputStream(filename, false));
		logger.log(Level.INFO, "Generating the 'Page by Priority' report");
		String header = "<table border = 1>\n"
				+ "<tr>\n"
				+ "<td><b>Name<b></td>\n"
				+ "<td><b>URL</b></td>\n"
				+ "<td><b>Priority</b></td>\n"
				+ "<td><b>Created</b></td>\n"
				+ "<td><b>Lastmodified</b></td>\n"
				+ "</tr>\n";
		String footer = "</table>";
		if (!(list.equals(null))){
			if ((Integer.parseInt(priority) > 0) && (Integer.parseInt(priority) < Priority.values().length)){
				report.append(header);
				for (Page p: list){
					if ((p.getPagePriority().ordinal() == Integer.parseInt(priority))){					
						report.append("<tr>\n");
						String line = String.format("<td>%s</td>%n<td>%s</td>%n<td>%s</td>%n<td>%s</td>%n<td>%s</td>%n", 
								p.getPageName(), 
								p.getUrl(),
								p.getPagePriority().toString(),
								p.getCreated().toString(),
								p.getLastUpdated().toString());
						report.append(line);
						report.append("</tr>\n");
					}
				}
				report.append(footer);
				logger.log(Level.INFO, "SUCCESS");
			} else {
				logger.log(Level.SEVERE, "Priority is out of range");
			}
		} else {
			logger.log(Level.SEVERE, "The list is empty");
		}
		report.close();
	}
	
}
