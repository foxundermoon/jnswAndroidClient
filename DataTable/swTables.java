package com.example.swmobileapp.services;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

public class swTables {
	ArrayList<swTable> lstTables = new ArrayList();

	public swTables() {

	}

	public swTable getTable(int iIndex) {
		if (iIndex >= 0 && iIndex < lstTables.size()) {
			return lstTables.get(iIndex);
		} else
			return null;
	}
	public swTable getTable(String strName) 
	{
		
		for(int i=0;i<lstTables.size();i++)
		{
			if(lstTables.get(i).getTableName().equals(strName))
			{
				return lstTables.get(i);
			}
		}
		 
	    return null;
	}
	public int getTablesCount() {
		return lstTables.size();
	}

	public void AddTable(swTable table) {
		lstTables.add(table);
	}

	public String toXML() {
		if (lstTables.size() < 1)
			return "";

		StringBuilder sb = new StringBuilder(
				"<?xml version='1.0' encoding='utf-8'?>");
		// sb.append("<?xml version='1.0' encoding='utf-8'?>");

		sb.append("<swTables>");
		sb.append("<TablesCount>");
		sb.append(String.valueOf(getTablesCount()));
		sb.append("</TablesCount>");

		for (int i = 0; i < getTablesCount(); i++) {
			int iColumnsCount = 0;
			int iRowsCount = 0;
			iColumnsCount = lstTables.get(i).getColumnsCount();
			iRowsCount = lstTables.get(i).getRowsCount();
			if (iColumnsCount < 1 || iRowsCount < 1)
				break;
			sb.append("<Table" + String.valueOf(i) + ">");
			sb.append("<TableName>");
			sb.append(lstTables.get(i).getTableName());
			sb.append("</TableName>");
			sb.append("<ColumnsCount>");
			sb.append(String.valueOf(iColumnsCount));
			sb.append("</ColumnsCount>");
			sb.append("<RowsCount>");
			sb.append(String.valueOf(iRowsCount));
			sb.append("</RowsCount>");
			sb.append("<Columns>");
			for (int j = 0; j < iColumnsCount; j++) {
				sb.append("<Column" + String.valueOf(j) + ">");
				sb.append("<ColumnName>");
				sb.append(lstTables.get(i).getColumns().get(j).getColumnName());
				sb.append("</ColumnName>");
				sb.append("<ColumnType>");
				sb.append(lstTables.get(i).getColumns().get(j).getColumnType());
				sb.append("</ColumnType>");
				sb.append("</Column" + String.valueOf(j) + ">");
			}
			sb.append("</Columns>");
			sb.append("<Rows>");
			int iRows = lstTables.get(i).getRowsCount();
			for (int j = 0; j < iRows; j++) {
				sb.append("<Row" + String.valueOf(j) + ">");

				for (int k = 0; k < iColumnsCount; k++) {
					String strColName = lstTables.get(i).getColumns().get(k)
							.getColumnName();
					String strCol = "Col" + String.valueOf(k);

					sb.append("<" + strCol + ">");
					sb.append(lstTables.get(i).getRows().get(j)
							.GetCol(strColName));
					sb.append("</" + strCol + ">");
				}
				sb.append("</Row" + String.valueOf(j) + ">");
			}
			sb.append("</Rows>");
			sb.append("</Table" + String.valueOf(i) + ">");
		}

		sb.append("</swTables>");

		return sb.toString();
	}

	public String toXML2() throws ParserConfigurationException {
		if (lstTables.size() < 1)
			return "";
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);

		try {
			DocumentBuilder db = factory.newDocumentBuilder();
			Document doc = db.newDocument();
			Element xmlTables = doc.createElement("swTables");
			doc.appendChild(xmlTables);
			AddElement(doc, xmlTables, "TablesCount",
					String.valueOf(getTablesCount()));
			for (int i = 0; i < getTablesCount(); i++) {
				int iColumnsCount = 0;
				int iRowsCount = 0;
				iColumnsCount = lstTables.get(i).getColumnsCount();
				iRowsCount = lstTables.get(i).getRowsCount();

				if (iColumnsCount < 1 || iRowsCount < 1)
					break;

				Element table = AddElement(doc, xmlTables,
						"Table" + String.valueOf(i), "");
				AddElement(doc, table, "TableName", lstTables.get(i)
						.getTableName());
				AddElement(doc, table, "ColumnsCount",
						String.valueOf(iColumnsCount));
				AddElement(doc, table, "RowsCount", String.valueOf(iRowsCount));

				Element columns = AddElement(doc, table, "Columns", "");
				for (int j = 0; j < iColumnsCount; j++) {
					Element column = AddElement(doc, columns,
							"Column" + String.valueOf(j), "");
					AddElement(doc, column, "ColumnName", lstTables.get(i)
							.getColumns().get(j).getColumnName());
					AddElement(doc, column, "ColumnType", lstTables.get(i)
							.getColumns().get(j).getColumnType());
				}

				Element rows = AddElement(doc, table, "Rows", "");
				int iRows = lstTables.get(i).getRowsCount();
				for (int j = 0; j < iRows; j++) {
					Element row = AddElement(doc, rows,
							"Row" + String.valueOf(j), "");
					for (int k = 0; k < iColumnsCount; k++) {
						String strColName = lstTables.get(i).getColumns()
								.get(k).getColumnName();
						String strCol = "Col" + String.valueOf(k);
						AddElement(doc, row, strCol, lstTables.get(i).getRows()
								.get(j).GetCol(strColName));
					}
				}
			}

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.setOutputProperty("encoding", "utf-8");// ����������⣬�Թ���GBK����
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			t.transform(new DOMSource(doc), new StreamResult(bos));

			return bos.toString();
		} catch (Exception e) {
			return "";
		}

	}

	public void fromXML(String strXML) {
		// strXML =
		// "<?xml version=\"1.0\" encoding=\"gb2312\"?><swTables><TablesCount>1</TablesCount><Table0><TableName>��Ա</TableName><ColumnsCount>4</ColumnsCount><RowsCount>10</RowsCount><Columns><Column0><ColumnName>ID</ColumnName><ColumnType>�ַ�</ColumnType></Column0><Column1><ColumnName>����</ColumnName><ColumnType>�ַ�</ColumnType></Column1><Column2><ColumnName>����</ColumnName><ColumnType>�ַ�</ColumnType></Column2><Column3><ColumnName>��ַ</ColumnName><ColumnType>�ַ�</ColumnType></Column3></Columns><Rows><Row0><ID>0</ID><����>����0</����><����>0</����><��ַ>����ɽС��0</��ַ></Row0><Row1><ID>1</ID><����>����1</����><����>1</����><��ַ>����ɽС��1</��ַ></Row1><Row2><ID>2</ID><����>����2</����><����>2</����><��ַ>����ɽС��2</��ַ></Row2><Row3><ID>3</ID><����>����3</����><����>3</����><��ַ>����ɽС��3</��ַ></Row3><Row4><ID>4</ID><����>����4</����><����>4</����><��ַ>����ɽС��4</��ַ></Row4><Row5><ID>5</ID><����>����5</����><����>5</����><��ַ>����ɽС��5</��ַ></Row5><Row6><ID>6</ID><����>����6</����><����>6</����><��ַ>����ɽС��6</��ַ></Row6><Row7><ID>7</ID><����>����7</����><����>7</����><��ַ>����ɽС��7</��ַ></Row7><Row8><ID>8</ID><����>����8</����><����>8</����><��ַ>����ɽС��8</��ַ></Row8><Row9><ID>9</ID><����>����9</����><����>9</����><��ַ>����ɽС��9</��ַ></Row9></Rows></Table0></swTables>";
		// strXML =
		// "<?xml version=\"1.0\" encoding=\"gb2312\"?><swTables><TablesCount>1</TablesCount><Table0><TableName>��Ա</TableName><ColumnsCount>4</ColumnsCount><RowsCount>10</RowsCount><Columns><Column0><ColumnName>ID</ColumnName><ColumnType>�ַ�</ColumnType></Column0><Column1><ColumnName>Name</ColumnName><ColumnType>�ַ�</ColumnType></Column1><Column2><ColumnName>Ange</ColumnName><ColumnType>�ַ�</ColumnType></Column2><Column3><ColumnName>��ַ</ColumnName><ColumnType>�ַ�</ColumnType></Column3></Columns><Rows><Row0><ID>0</ID><Name>����0</Name><Ange>0</Ange><��ַ>����ɽС��0</��ַ></Row0></Rows></Table0></swTables>";

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setIgnoringElementContentWhitespace(true);

		try {
			/*
			 * StringReader sr = new StringReader(strXML); InputSource is = new
			 * InputSource(sr); is.setEncoding("utf-8");
			 */
			DocumentBuilder db = factory.newDocumentBuilder();

			InputStream inputStream = new ByteArrayInputStream(
					strXML.getBytes("utf-8"));// xmlΪҪ�������ַ�
			Document doc = db.parse(inputStream);
			Element tables = doc.getDocumentElement();
			if (tables == null)
				return;
			String strTablesCount = GetElementValue(tables, "TablesCount");
			if (strTablesCount == "")
				strTablesCount = "0";
			int iTables = Integer.parseInt(strTablesCount);
			if (iTables < 1)
				return;
			for (int i = 0; i < iTables; i++) {
				String elName = "Table" + String.valueOf(i);
				NodeList nodeList = tables.getElementsByTagName(elName);
				if (nodeList == null || nodeList.getLength() < 1)
					return;
				Element xmlTable = (Element) nodeList.item(0);

				if (xmlTable == null)
					return;
				String strTableName = GetElementValue(xmlTable, "TableName");
				String strColumsCount = GetElementValue(xmlTable,
						"ColumnsCount");
				String strRowsCount = GetElementValue(xmlTable, "RowsCount");
				if (strTableName == "" || strColumsCount == ""
						|| strRowsCount == "")
					return;
				int iColumnCount = Integer.parseInt(strColumsCount);
				int iRowsCount = Integer.parseInt(strRowsCount);
				if (iColumnCount < 1 || iRowsCount < 1)
					return;

				nodeList = tables.getElementsByTagName("Columns");
				if (nodeList == null || nodeList.getLength() < 1)
					return;
				Element xmlColumns = (Element) nodeList.item(0);

				if (xmlColumns == null)
					return;
				swTable table = new swTable(strTableName);
				for (int j = 0; j < iColumnCount; j++) {
					String eslName = "Column" + String.valueOf(j);
					nodeList = xmlColumns.getElementsByTagName(eslName);
					if (nodeList == null || nodeList.getLength() < 1)
						return;
					Element xmlColumn = (Element) nodeList.item(0);
					String strColumnName = GetElementValue(xmlColumn,
							"ColumnName");
					String strColumnType = GetElementValue(xmlColumn,
							"ColumnType");
					table.AddColumn(new swColumn(strColumnName, strColumnType));
				}

				nodeList = tables.getElementsByTagName("Rows");
				if (nodeList == null || nodeList.getLength() < 1)
					return;
				Element xmlRows = (Element) nodeList.item(0);

				for (int j = 0; j < iRowsCount; j++) {
					String erName = "Row" + String.valueOf(j);
					nodeList = xmlRows.getElementsByTagName(erName);
					if (nodeList == null || nodeList.getLength() < 1)
						return;
					Element xmlRow = (Element) nodeList.item(0);

					swRow row = table.CreateNewRow();
					for (int k = 0; k < table.getColumnsCount(); k++) {
						String clName = "Col" + String.valueOf(k);
						String strVal = GetElementValue(xmlRow, clName);
						row.setCol(table.getColumns().get(k).getColumnName(),
								strVal);
					}
					table.AddRow(row);
				}
				AddTable(table);
			}

		} catch (Exception e) {
			return;
		}

	}

	public String GetElementValue(Element elementParent, String strElentName) {
		try {
			NodeList nodeList = elementParent
					.getElementsByTagName(strElentName);
			if (nodeList == null || nodeList.getLength() < 1)
				return "";

			Text xmlCurrentText = (Text) nodeList.item(0).getChildNodes()
					.item(0);
			if (xmlCurrentText == null)
				return "";
			String strVal = xmlCurrentText.getData();
			if (strVal.isEmpty() || strVal == "")
				return "";
			else
				return strVal;
		} catch (Exception e) {
			return "";
		}
	}

	public Element AddElement(Document xmlDoc, Element xmlElementParent,
			String strElementName, String strElementValue) {
		try {
			Element xmlChildElemnt = xmlDoc.createElement(strElementName);
			Text xmlValue = xmlDoc.createTextNode(strElementValue);
			xmlChildElemnt.appendChild(xmlValue);
			xmlElementParent.appendChild(xmlChildElemnt);
			return xmlChildElemnt;
		} catch (Exception e) {

		}
		return null;
	}

}
