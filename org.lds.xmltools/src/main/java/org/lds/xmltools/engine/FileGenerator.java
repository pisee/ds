package org.lds.xmltools.engine;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class FileGenerator {

	public static void generate(IFile file, String contents){
		
		try {
			createClass(file.getProject(), file.getFullPath().toString(), new ByteArrayInputStream(contents.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static void createClass(IProject project, String fullName, InputStream inputStream){
		String[] fileNameArr = fullName.split("/");
		String path = null;
		String fileName = null;
		for (int i = 0; i < fileNameArr.length; i++) {
			if (i == 0)
				path = fileNameArr[i];
			else if (i < fileNameArr.length - 1)
				path = path + "/" + fileNameArr[i];
			else
				fileName = fileNameArr[i].substring(0, fileNameArr[i].indexOf(".")) + ".txt";
		}

		if (path != null && !path.equals("") ) {
			createFolder(project, path);
		}
		final IFile ifile = project.getFile(new Path(path + "/" + fileName));
		try {
			if (ifile.exists()) {
				ifile.setContents(inputStream, true, false, null);
			} else {
				ifile.create(inputStream, true, null);
			}
			ifile.refreshLocal(IResource.DEPTH_ZERO, null);
			inputStream.close();
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void createFolder(IProject project, String path){
		try {
			IFolder folder = null;
			String[] pathArr = path.split("/");
			for (int i = 0; i < pathArr.length; i++) {
				if (i == 0)
					path = pathArr[i];
				else
					path = path + "/" + pathArr[i];
				if(path != null && !"".equalsIgnoreCase(path.trim())){
					folder = project.getFolder(path);
					if (!folder.exists()) {
						folder.create(false, true, null);
					}
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
}
