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

import com.sh24wslistener.listener.WsListener;



public class parseJsonConsultas {

	 public String jsonText;

	 static final Logger LOGGER = Logger.getLogger(WsListener.class);
	 

	 @SuppressWarnings("finally")
	public String parseJsonToXml( String cadena,  String idws, String request, String esquema)  {
		 
		 String xmlString = null;
		 String xmlConsulta = null;
		 int nivel1 = 0;
		 int nivel2 = 0;
		 int campo = 1;
		 System.out.println("Cadena dentro:"+cadena);
		 System.out.println("Esquema:"+esquema);
		 //System.out.println("Cabecera:"+cabecera);
		 try {
			
			 System.out.println("Paso 0");
			  //Creating an empty XML Document
	        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();
	        
	        System.out.println("Paso 1");
	        
	        xmlConsulta = "SELECT XMLELEMENT(\""+"XXX"+"\"";

            
			JSONArray jsonarray = new JSONArray(esquema);
			JSONObject jsonobjeto1 = new JSONObject();
			JSONObject jsonobjeto2 = new JSONObject();
			JSONObject jsonobjeto3 = new JSONObject();
			
			String valor = null;
			String valor3 = null;
			for (int i = 0; i < jsonarray.length(); i++) {
				
				
				jsonobjeto1 = jsonarray.getJSONObject(i);

				JSONArray nodos = new JSONArray(jsonobjeto1.get("nodes").toString());
				// Si no tiene nodos es un valor
				valor = "null";
				/*if (jsonobjeto1.has("Valor")) {
					valor = jsonobjeto1.get("Valor").toString();
				}*/
				
				if ( nodos.length()==0  ) {
					valor = cadena.substring(cadena.indexOf('|',campo),cadena.indexOf('|',campo+1)   ).replace("|", "");;
					campo++;
				}
				//if ( valor.equals("") || valor==null) valor="null";
					
				xmlConsulta += ",XMLELEMENT(\""+jsonobjeto1.get("Navn")+"\",'"+valor+"'";
				xmlConsulta = xmlConsulta.replace(",,", ",null,");
				System.out.println("Objeto1: Campo:"+jsonobjeto1.get("Navn")+ " Valor:"+valor);
				
				
								
				for (int x = 0; x < nodos.length(); x++) {
					jsonobjeto2 = nodos.getJSONObject(x);
					JSONArray nodos2 = new JSONArray(jsonobjeto2.get("nodes").toString());
					valor = "null";
					if (jsonobjeto2.has("Valor")) {
							valor = jsonobjeto2.get("Valor").toString();
					}
					
					
					if ( nodos2.length()==0  ) {
						valor = cadena.substring(cadena.indexOf('|',campo),cadena.indexOf('|',campo+1)  ).replace("|", "");
						campo++;
					}
					
					//if ( valor.equals("") || valor==null) valor="null";
					
					xmlConsulta += ",XMLELEMENT(\""+jsonobjeto2.get("Navn")+"\",'"+valor +"'";
					
				
					System.out.println("         Objeto2: Campo:"+jsonobjeto2.get("Navn")+ " Valor:"+valor);
					
					for (int z = 0; z < nodos2.length(); z++) {
						jsonobjeto3 = nodos2.getJSONObject(z);
						valor3 = "null";
						//if (jsonobjeto3.has("Valor")) {
					 	//	valor3 = jsonobjeto3.get("Valor").toString();
						//}
						
						valor3 = cadena.substring(cadena.indexOf('|',campo),cadena.indexOf('|',campo+1)   ).replace("|", "");
						campo++;
						
						//if ( valor3.equals("") || valor3==null ) valor3="null";
						System.out.println("         		Objeto3: Campo:"+jsonobjeto3.get("Navn")+ " Valor:"+valor3);
			            // Hijo
						
						xmlConsulta += ",XMLELEMENT(\""+jsonobjeto3.get("Navn")+"\",'"+valor3+"')";

			            
			            					
					}
					
					xmlConsulta += ")";
		            					
				}
				
				xmlConsulta += ")";
				
			}
			xmlConsulta += ") RESULTADO";
			xmlConsulta += " FROM DUAL ";
			
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
			System.out.println("Json exception:"+ e);
			LOGGER.error("Error ParserJsonXML:"+ e);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Parse exception:"+ e);
			LOGGER.error("Error ParserJsonXML:"+ e);
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			System.out.println("Transformer Configuration exception:"+ e);
			e.printStackTrace();
			LOGGER.error("Error ParserJsonXML:"+ e);
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			System.out.println("Transformer exception:"+ e);
			e.printStackTrace();
			LOGGER.error("Error ParserJsonXML:"+ e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Cualquier exception:"+ e);
			e.printStackTrace();
			LOGGER.error("Error ParserJsonXML:"+ e);
		}		 
		finally {
			LOGGER.info("XML generado: "+ xmlConsulta);
			return xmlConsulta;
		}
		 
	 }
}
