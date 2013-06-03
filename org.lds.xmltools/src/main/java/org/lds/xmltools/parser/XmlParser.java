package org.lds.xmltools.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class XmlParser {
	private Document document = null;
	private List<DomObject> objList = new ArrayList<DomObject>();
	
	public List<DomObject> getObjList() {
		return objList;
	}

	private String xmlPath;
	
	public Document getDocument() {
		return document;
	}

	public XmlParser(String xmlPath){
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(xmlPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.xmlPath = xmlPath;
		createDocument(inputStream);
	}

	private void createDocument(InputStream inputStream) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
			document = builder.parse(inputStream);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	private String getNodeAttr(Node node, String key) {
		String attribute = null;

		NamedNodeMap nnm = node.getAttributes();
		if (nnm != null && nnm.getNamedItem(key) != null) {
			Node n = nnm.getNamedItem(key);
			attribute = n.getNodeValue();
		}
		return attribute;
	}
	
	public void parse(Node node, boolean isConvert){
		String id = getNodeAttr(node, "id");
		String text = getNodeAttr(node, "text");
		String convertText = "$$"+id;

		if(isConvert){
			Element el = (Element)node;
			el.setAttribute("text", convertText);
			objList.add(new DomObject(id, convertText, text, ""));
		}else{
			objList.add(new DomObject(id, text, text, ""));
		}
	}
	
	public void overwrite(){
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer;
			transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(xmlPath));
			transformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
}
