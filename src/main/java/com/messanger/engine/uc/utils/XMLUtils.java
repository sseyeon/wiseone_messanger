package com.messanger.engine.uc.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLUtils {

    /**
     * 
     * @param path
     * @param xpathString
     * @return
     * @throws Exception
     */
    public static String getString(String path, String xpathString) throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(path);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile(xpathString);
        return (String)expr.evaluate(doc, XPathConstants.STRING);
    }

    /**
     * 
     * @param path
     * @param xpathString
     * @return
     * @throws Exception
     */
    public static Element getNode(String path, String xpathString) throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(path);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile(xpathString);
        return (Element)expr.evaluate(doc, XPathConstants.NODE);
    }
    
    /**
     * 
     * @param path
     * @param xpathString
     * @return
     * @throws Exception
     */
    public static NodeList getNodeSet(String path, String xpathString) throws Exception {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(path);
        XPathFactory factory = XPathFactory.newInstance();
        XPath xpath = factory.newXPath();
        XPathExpression expr = xpath.compile(xpathString);
        return (NodeList)expr.evaluate(doc, XPathConstants.NODESET);
    }

}
