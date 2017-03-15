package com.sh24wslistener.utils;


import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sh24wslistener.listener.WsListener;



public class parseJson {

	 public String jsonText;

	 static final Logger LOGGER = Logger.getLogger(WsListener.class);
	 

	 public String parseJsonToXml( String cadena, String cabecera, String fromsql, String prowid)  {
		 
		 String xmlString = null;
		 String xmlConsulta = null;
		 int nivel1 = 0;
		 int nivel2 = 0;
		 //System.out.println("Cadena:"+cadena);
		 //System.out.println("Cabecera:"+cabecera);
		 try {
			 
			  //Creating an empty XML Document
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
	        
	        
	        xmlConsulta = "SELECT XMLELEMENT(\""+cabecera+"\"";
	        // Root
	        Element root = doc.createElement(cabecera);
            doc.appendChild(root);
            
            // Comentario
            //Comment comment = doc.createComment("This is a comment");
            //root.appendChild(comment);            


            //Element child = doc.createElement(jsonobjeto1.get("Navn").toString());
            //child.s("name", "value");
            //root.appendChild(child);
            
			JSONArray jsonarray = new JSONArray(cadena);
			JSONObject jsonobjeto1 = new JSONObject();
			JSONObject jsonobjeto2 = new JSONObject();
			JSONObject jsonobjeto3 = new JSONObject();
			for (int i = 0; i < jsonarray.length(); i++) {
				jsonobjeto1 = jsonarray.getJSONObject(i);
				////System.out.println("xObjeto1:"+jsonobjeto1.get("Navn"));

				if (nivel1==1) {
					xmlConsulta += ")";
				}
				xmlConsulta += ",XMLELEMENT(\""+jsonobjeto1.get("Navn")+"\"";
				nivel1 = 1;
	            // Hijo
	            Element child = doc.createElement(jsonobjeto1.get("Navn").toString());
	            root.appendChild(child);
	            
				JSONArray nodos = new JSONArray(jsonobjeto1.get("nodes").toString());
				//System.out.println("     Nodes:"+nodos);
				
				for (int x = 0; x < nodos.length(); x++) {
					jsonobjeto2 = nodos.getJSONObject(x);
					String valor = null;
					if (jsonobjeto2.has("Valor")) {
						valor = jsonobjeto2.get("Valor").toString();
					}
					
					////System.out.println("         Objeto2: Campo:"+jsonobjeto2.get("Navn")+ " Valor:"+valor);
		            // Hijo
					
					if ((valor==null) || valor.trim().equals("")) valor="null";
					xmlConsulta += ",XMLELEMENT(\""+jsonobjeto2.get("Navn")+"\","+valor+")";
		            Element valores = doc.createElement(jsonobjeto2.get("Navn").toString());
		            valores.setTextContent(valor);
		            child.appendChild(valores);
		            					
				}
				
			}
			xmlConsulta += ")) RESULTADO";
			//System.out.println("******"+fromsql);
			xmlConsulta += " "+fromsql;
			if ( fromsql.toUpperCase().indexOf("ORDER BY")>0 ) {
				xmlConsulta = xmlConsulta.replace("ORDER BY", " AND ROWID='"+prowid+"'"+" ORDER BY ");
			}
			else {
				xmlConsulta += " AND ROWID='"+prowid+"'";
			}
			//System.out.println("FINAL ******"+fromsql);
			 //Output the XML to a string

            //set up a transformer
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            xmlString = sw.toString();
            
            System.out.println("Este es el xml:"+xmlConsulta);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("Error ParserJsonXML:"+ e);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("Error ParserJsonXML:"+ e);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("Error ParserJsonXML:"+ e);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error("Error ParserJsonXML:"+ e);
		}
		finally {
			LOGGER.info("XML generado: "+ xmlConsulta);
			return xmlConsulta;
		}
		 
	 }
}
