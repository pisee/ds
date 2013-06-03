package org.lds.xmltools.engine;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

@SuppressWarnings({"rawtypes", "unchecked"}) 
public class MyVelocityEngine {

	public VelocityEngine velocityEngine = null;
	public InputStream classInputStream;
	public String path = null;
	public String fileName = null;
	public static final String TEMPLATE_FOLDER = "template/";

	private Properties getProperties(boolean useClassLoader) {
		Properties properties = new Properties();
		if (useClassLoader) {
			properties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
			properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		} else {
			properties.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
			properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_CACHE, "true");
			properties.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			properties.setProperty("file.resource.loader.modificationCheckInterval", "1");
		}
		String characterSet = "UTF-8";
		properties.setProperty(VelocityEngine.INPUT_ENCODING, characterSet);
		properties.setProperty(VelocityEngine.OUTPUT_ENCODING, characterSet);
		properties.setProperty(VelocityEngine.ENCODING_DEFAULT, characterSet);
		properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS, "org.apache.velocity.runtime.log.NullLogSystem");
		
		return properties;

	}

	public String generate(Map inputMap, String templateName, boolean useClassLoader){
		StringWriter writer = new StringWriter();
		try {
			velocityEngine = new VelocityEngine();
			
			Properties properties = getProperties(useClassLoader);
			if (!useClassLoader) {
				int tmpIdx = templateName.lastIndexOf('/');
				String templatePath = templateName.substring(0, tmpIdx);
				templateName = templateName.substring(tmpIdx + 1);
				properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, templatePath);
			} else {
				templateName = TEMPLATE_FOLDER + templateName;
			}
			velocityEngine.init(properties);
			Template javaClassTemplate = null;
			try {
				javaClassTemplate = velocityEngine.getTemplate(templateName);
			} catch (Exception e) {
				e.printStackTrace();
//				PluginLogger.error("error occurred.", e);
			}

			VelocityContext context = new VelocityContext();
			Set keySet = inputMap.keySet();
			Iterator iter = keySet.iterator();
			String key = null;
			String value = null;
			while (iter.hasNext()) {
				key = (String) iter.next();
				if (inputMap.get(key) instanceof String) { // leeyh
					value = (String) inputMap.get(key);
					context.put(key, value);
				} else if (inputMap.get(key) instanceof ArrayList) {
					ArrayList arrayList = (ArrayList) inputMap.get(key);
					context.put(key, arrayList);
				}else {
					context.put(key, inputMap.get(key));
				}
			}
			if(javaClassTemplate != null)
				javaClassTemplate.merge(context, writer);
		} catch (Exception e) {
			e.printStackTrace();
//			PluginLogger.error(exception);
		}
		return writer.toString();
	}

	public static void main(String[] args){
		ArrayList list = new ArrayList();
        list.add("ArrayList element 1");
        list.add("ArrayList element 2");
        list.add("ArrayList element 3");
        list.add("ArrayList element 4");

        MyVelocityEngine engine = new MyVelocityEngine();
        Map map = new HashMap();
        map.put("list", list);
        String generate = engine.generate(map, "example.vm", true);
        
        System.out.println(generate);
	}
}

