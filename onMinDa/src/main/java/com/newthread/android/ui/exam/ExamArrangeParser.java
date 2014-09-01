package com.newthread.android.ui.exam;

import com.newthread.android.util.Loger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Created by jindongping on 14-9-1.
 */
public class ExamArrangeParser {
    public void parse(String html) {
        Document doc = Jsoup.parse(html);
        Elements elements=doc.select("table.table_con");
//        List<ExamArrangeInfo> examArrangeInfos = new ArrayList<ExamArrangeInfo>();
//        Loger.V(elements.size());
        for(Element element :elements){
//            Loger.V(element.toString());
        }
    }
}
