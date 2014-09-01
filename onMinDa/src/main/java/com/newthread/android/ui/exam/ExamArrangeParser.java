package com.newthread.android.ui.exam;

import com.newthread.android.bean.ExamArrangeInfo;
import com.newthread.android.util.Loger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jindongping on 14-9-1.
 */
public class ExamArrangeParser {
    public List<ExamArrangeInfo> parse(String html) {
        List<ExamArrangeInfo> examArrangeInfos = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements fieldsets=doc.getElementsByTag("fieldset");
        for(Element fieldset:fieldsets){
            String state =fieldset.getElementsByTag("legend").text();
            Elements courseInfos = fieldset.getElementsByClass("t_con");
            for(Element courseInfo:courseInfos){
            ExamArrangeInfo examArrangeInfo = new ExamArrangeInfo();
                examArrangeInfo.setState(state);
                if(state.contains("已安排")){
                    Elements tds = courseInfo.getElementsByTag("td");
                    examArrangeInfo.setCourseId(tds.get(1).text());
                    examArrangeInfo.setCourseName(tds.get(2).text());
                    examArrangeInfo.setCourseType(tds.get(3).text());
                    examArrangeInfo.setCourseTeacher(tds.get(4).text());
                    examArrangeInfo.setCourseGrade(tds.get(5).text());
                    examArrangeInfo.setSeatNum(tds.get(6).text());
                    examArrangeInfo.setExamTime(tds.get(7).text());
                    examArrangeInfo.setExamAddress(tds.get(8).text());
                    examArrangeInfo.setExamType(tds.get(9).text());
                    examArrangeInfo.setExamModle(tds.get(10).text());
                    examArrangeInfo.setFinishSate(tds.get(11).text());
                }else if(state.contains("安排中")){
                    Elements tds = courseInfo.getElementsByTag("td");
                    examArrangeInfo.setCourseId(tds.get(1).text());
                    examArrangeInfo.setCourseName(tds.get(2).text());
                    examArrangeInfo.setCourseType(tds.get(3).text());
                    examArrangeInfo.setCourseTeacher(tds.get(4).text());
                    examArrangeInfo.setCourseGrade(tds.get(5).text());
                    examArrangeInfo.setExamAddress(tds.get(6).text());
                }else if(state.contains("未编排")){
                    Elements tds = courseInfo.getElementsByTag("td");
                    examArrangeInfo.setCourseId(tds.get(1).text());
                    examArrangeInfo.setCourseName(tds.get(2).text());
                    examArrangeInfo.setCourseType(tds.get(3).text());
                    examArrangeInfo.setExamAddress(tds.get(4).text());
                }
                examArrangeInfos.add(examArrangeInfo);
            }
        }
        return examArrangeInfos;
    }
}
