package com.bionic.jse.wiki;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDate;

public class Page implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String pageName;
	private URL url;
	private int parentId;
	private boolean isRead = false;
	public enum Priority{
		MAY, HAVE_TO, MUST
	};
	private LocalDate lastUpdated = null;
	private LocalDate created = null;
	private Priority pagePriority;
	private static int newId = 0;
	
	

	public Page() {
		id = ++newId;
	}
	
	public Page(String pagename, URL url){
		id = ++newId;
		this.pageName = pagename; 
		this.url = url;
		pagePriority = Priority.HAVE_TO;
		lastUpdated = created = LocalDate.now();		
	}
	
	public Page(String pagename, URL url, Integer parentId){
		id = ++newId;
		this.pageName = pagename; 
		this.url = url;
		this.parentId = parentId;
		pagePriority = Priority.HAVE_TO;
		created = lastUpdated = LocalDate.now();		
	}	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParent(int parentId) {
		this.parentId = parentId;
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	public Priority getPagePriority() {
		return pagePriority;
	}

	public void setPagePriority(Priority pagePriority) {
		this.pagePriority = pagePriority;
	}

	public LocalDate getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDate lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public static int getNewId() {
		return newId;
	}

	public static void setNewId(int newId) {
		Page.newId = newId;
	}
	
	@Override
	public boolean equals(Object p){
		return this.url.equals(((Page)p).getUrl());
	}
	
	@Override
	public int hashCode(){
		return url.hashCode();
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
}
