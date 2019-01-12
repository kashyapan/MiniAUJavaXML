package utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Helper {

	public static String getPlaceholders(int n) {
		StringBuilder sb = new StringBuilder();

		sb.append("(");

		for (int i = 0; i < n; i++) {
			sb.append("?");
			if (i != n - 1) {
				sb.append(" ,");
			}
		}
		sb.append(")");
		return sb.toString();
	}

	public static void XMLToTable(String filePath, String tagName) {

		File xmlFile = new File(filePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		String sql = new String("INSERT INTO `" + tagName + "` VALUES ");
		System.out.println(sql);

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);
			document.getDocumentElement().normalize();
			NodeList nodeList = document.getElementsByTagName(tagName);

			Connection conn = DB.getInstance().getConnection();
			
			System.out.println(nodeList.getLength());

			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {

					Element element = (Element) node;
					NodeList columnList = element.getChildNodes();

					List<String> row = new ArrayList<>();

					for (int j = 0; j < columnList.getLength(); j++) {

						Node column = columnList.item(j);

						if (column.getNodeType() == Node.ELEMENT_NODE) {
							String columnValue = column.getTextContent();
							row.add(columnValue);

						}

					}
					
					System.out.println(row);

					String insertSql = sql + getPlaceholders(row.size());
					System.out.println(insertSql);
					insertRow(insertSql, row);

				}

			}

		} catch (ParserConfigurationException | SAXException | IOException | ClassNotFoundException
				| InstantiationException | IllegalAccessException | SQLException e) {
			
			System.out.println("Error");
			e.printStackTrace();
		}

	}

	public static void TableToXML (String filePath , String rootElementName , String tableName) {
		

		String sql = "select * from " + tableName; 
		Connection conn;
		Document document ;
		
		try {
			conn = DB.getInstance().getConnection();

			PreparedStatement ps = conn.prepareStatement(sql);

			DocumentBuilder documentBuilder = XMLUtil.getInstance().getDocumentBuilder() ;
			
			document = documentBuilder.newDocument();
			Element root = document.createElement(rootElementName);
			document.appendChild(root);
			
			Element row ;
			
			
			ResultSet rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();

			List<String> columnNames = new ArrayList<>();
			List<String> values = new ArrayList<>();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {
				String columnName = rsmd.getColumnLabel(i);
				columnNames.add(columnName);
			}

			while (rs.next()) {

				values.clear();
				

				Element table = document.createElement(tableName) ;

				for (String column : columnNames) {

					String value = rs.getString(column);
					values.add(value);

					row = document.createElement(column) ;
					row.appendChild( document.createTextNode(value)) ;
					table.appendChild(row) ;
				}
				//Append in XML 
				
				root.appendChild(table) ;
				
				System.out.println(values);
			}

			System.out.println(columnNames);
		
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(new File(filePath));

			transformer.transform(domSource, streamResult);
		
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException | TransformerException | ParserConfigurationException e1) {
		
			e1.printStackTrace();
		}

				


	}
	
	
	private static void insertRow(String insertSql, List<String> row)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

		Connection conn = DB.getInstance().getConnection();
		PreparedStatement prepStat = conn.prepareCall(insertSql);

		for (int k = 0; k < row.size(); k++) {
			String value = row.get(k);
			prepStat.setString(k + 1, value);
		}

		prepStat.executeUpdate();
	}

}
