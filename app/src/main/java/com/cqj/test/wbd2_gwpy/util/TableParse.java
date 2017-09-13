package com.cqj.test.wbd2_gwpy.util;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TableParse {

	/**
	 * 将html表格里的数据解析出来
	 * 
	 * @param tableStr
	 * @return 大list的size是行，小list的size是列
	 */
	public static ArrayList<ArrayList<String>> getTableData(String tableStr) {
		Document doc = Jsoup.parse(tableStr);
		Element infoTable = doc.getElementsByAttributeValue("class",
				"dataTables").first();
		Elements tableLineInfos = infoTable.select("tr");
		ArrayList<ArrayList<String>> tableData = new ArrayList<ArrayList<String>>();
		for (Element lineInfo : tableLineInfos) {
			Elements lineInfoContent = lineInfo.select("td");
			ArrayList<String> data = new ArrayList<String>();
			for (Element content : lineInfoContent) {
				data.add(content.text().toString());
			}
			tableData.add(data);
		}
		return tableData;
	}
}
