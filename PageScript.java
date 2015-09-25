package com.bionic.jse.wiki;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageScript {
	public PageScript() {};
	private static final Logger logger = Logger.getLogger(PageUtils.class.getName());
	
	public static void invokeMethod(PageUtils utils, String filename){
	BufferedReader in = null;
	try {
		in = new BufferedReader(new FileReader(filename));
		String command;
		
		while ((command = in.readLine()) != null){
			try {
				List<String> items = Arrays.asList(command.split("\\s*,\\s*"));
				String methodName = items.get(0);
				String parameters[] = new String [items.size() - 1];
				for (int i = 1; i < items.size(); i++){
					parameters[i - 1] = items.get(i);
				}
				Method[] declaredMethods = PageUtils.class.getMethods();
				for (Method m: declaredMethods){
					if (m.getName().equals(methodName)) {						
						Class<?>[] pType  = m.getParameterTypes();
						Method method = PageUtils.class.getMethod(methodName, pType);
						try {
							method.invoke(utils, parameters);
							} catch (IllegalArgumentException e) {
								logger.log(Level.SEVERE,e.getMessage());
							} catch (IllegalAccessException e) {
								logger.log(Level.SEVERE,e.getMessage());
							} catch (InvocationTargetException e) {
								logger.log(Level.SEVERE,e.getMessage());
							}
					}				
				}						
			} catch (SecurityException e) {
				logger.log(Level.SEVERE,e.getMessage());
			} catch (NoSuchMethodException e) {
				logger.log(Level.SEVERE,e.getMessage());
			}			
		}
	} catch (IOException e) { } 
	  finally { if (in != null)
		try {
			in.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE,e.getMessage());
		} 
	   }		
	 }
}


