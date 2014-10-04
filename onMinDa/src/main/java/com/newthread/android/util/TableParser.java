package com.newthread.android.util;

import android.util.Log;
import com.newthread.android.bean.MyTable;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 翌日黄昏 on 2014/10/1.
 */
public class TableParser {
    private static final String TAG = "TableParser";


    public static MyTable parser(Element element) {
        Log.v(TAG, "tableParser");
        Elements rows = removeUnused(element.getElementsByTag("tr"));

        MyTable myTable = new MyTable(rows.size(), getTableSize(rows)[1]);
        setCells(rows, myTable);

        return myTable;
    }

    private static void setCells(Elements rows, MyTable myTable) {
        int rowIndex = 0;
        for (Element row : rows) {
            int colIndex = 0;
            Elements cols = row.getElementsByTag("td");
            if (cols.size() == 0) cols = row.getElementsByTag("th");
            for (Element col : cols) {
                while (myTable.getCell(rowIndex, colIndex) != null) colIndex++;
                int rowspan = 1;
                int colspan = 1;
                String rowspanStr = col.attr("rowspan");
                String colspanStr = col.attr("colspan");

                if(rowspanStr ==null || rowspanStr.equals("")) {
                    rowspan = 1;
                }else {
                    rowspan = Integer.parseInt(rowspanStr);
                }

                if(colspanStr ==null || colspanStr.equals("")) {
                    colspan = 1;
                }else {
                    colspan = Integer.parseInt(colspanStr);
                }
//                Log.v(TAG, "row=" + rowIndex + ", col=" + colIndex + ", rowspan=" + rowspan + ", colspan=" + colspan);

                while (--rowspan >= 0) {
                    myTable.setCell(matcherText(col), rowIndex + rowspan, colIndex);
                }
                while (--colspan >= 0) {
                    myTable.setCell(matcherText(col), rowIndex, colIndex + colspan);
                }
                colIndex++;
            }
            rowIndex++;
        }
    }

    private static String matcherText(Element col) {
        String regex = "</div>(.*?)</td>";
        Pattern pattern = Pattern.compile(regex);
        //删除换行符，避免影响匹配
        Matcher matcher = pattern.matcher(col.toString().replace("\n", ""));
        while (matcher.find()) {
            String str = matcher.group();
            //删除&nbsp;，</div> ， </td>
            return str.replace("&nbsp;", "").replace("</div>", "").replace("</td>", "");
        }
        return col.ownText();
    }

    private static Elements removeUnused(Elements rows) {
        return new Elements(rows.subList(0, 12));
    }

    private static int removeTable(Element row) {
        Elements elements = row.getElementsByTag("table");
        Log.v(TAG, "removeTable:size=" + elements.size() + "," + row.toString());

        if (elements.size() > 0) {
            Log.v(TAG, "has table!");
            Tag tag = Tag.valueOf("td");
            row = new Element(tag, "");
            Log.v(TAG, row.toString());
        }
        int trSize = 0;
        for (Element element : elements) {
            trSize += element.getElementsByTag("tr").size();
        }
        return trSize;
    }

    private static int[] getTableSize(Elements row) {
        int[] size = new int[2];
        for (Element element : row) {
//            removeTable(element);
            int tdLength = element.getElementsByTag("td").size();
            if (tdLength > size[1]) size[1] = tdLength;
        }
        return size;
    }
}
