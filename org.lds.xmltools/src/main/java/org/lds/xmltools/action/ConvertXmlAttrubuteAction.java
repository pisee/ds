package org.lds.xmltools.action;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.lds.xmltools.engine.FileGenerator;
import org.lds.xmltools.engine.MyVelocityEngine;
import org.lds.xmltools.parser.XmlParser;

@SuppressWarnings({"rawtypes", "unchecked"})
public class ConvertXmlAttrubuteAction implements IObjectActionDelegate {
	private IStructuredSelection structuredSelection;
	
	@Override
	public void run(IAction arg0) {
		for (Iterator i = structuredSelection.iterator(); i.hasNext();) {
			Object nextSelected = i.next();
			if (nextSelected instanceof IFile) {
				IFile file = (IFile) nextSelected;
				String path = file.getLocationURI().getPath();
				if(path.startsWith("/")){
					path = path.substring(1);
				}
				XmlParser xmlParser = new XmlParser(path);
				xmlParser.parse(xmlParser.getDocument().getDocumentElement(), true); 
				xmlParser.overwrite();
				
				MyVelocityEngine engine = new MyVelocityEngine();
		        Map map = new HashMap();
		        map.put("domObjectList", xmlParser.getObjList());
		        String contents = engine.generate(map, "genCVS.vm", true);
		        System.out.println(contents);
		        
		        FileGenerator.generate(file, contents);
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		structuredSelection = (IStructuredSelection) selection;
	}

	@Override
	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		
	}

}
