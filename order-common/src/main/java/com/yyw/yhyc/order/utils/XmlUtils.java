package com.yyw.yhyc.order.utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 2016/8/31.
 */
public class XmlUtils {

    /**
     * 从指定节点开始,递归遍历所有子节点
     * @author chenleixing
     */
    private static void getNodes(Element node,List<Map<String,String>> list){
        Map<String,String> map = null;
        //当前节点的名称、文本内容和属性
        map = new HashMap<String,String>();
        map.put(node.getName(),node.getTextTrim());
        list.add(map);
        //递归遍历当前节点所有的子节点
        List<Element> listElement=node.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            getNodes(e,list);//递归
        }
    }

    public static List<Map<String,String>> readXmlAsList(String xml){
        List<Map<String,String>> list = new ArrayList<Map<String,String>>();

        try {
            Document document = DocumentHelper.parseText(xml);
            Element root=document.getRootElement();//获取根节点
            getNodes(root,list);//从根节点开始遍历所有节点
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws DocumentException {
        String xml = "<?xml version=\"1.0\" encoding=\"ISO8859-1\"?>" +
                "             <DATA>" +
                "             <REQUEST>" +
                "               <NCB2BFIN>" +
                "                   <REQNBR>流程实例号</REQNBR>" +
                "                   <MCHNBR>商户编号</MCHNBR>" +
                "                   <REFORD>订单号</REFORD>" +
                "                   <SUBORD>订单支付号</SUBORD>" +
                "                   <CCYNBR>订单币种</CCYNBR>" +
                "                   <TRSAMT>订单金额</TRSAMT>" +
                "                   <ENDAMT>结账金额</ENDAMT>" +
                "                   <BBKNBR>付方分行号</BBKNBR>" +
                "                   <PAYACC>付方账号</PAYACC>" +
                "                   <ACCNAM>付方户名</ACCNAM>" +
                "                   <YURREF>业务参考号</YURREF>" +
                "                   <ENDDAT>付款日期</ENDDAT>" +
                "                   <RTNFLG>业务请求结果</RTNFLG>" +
                "                   <RTNDSP>结果描述</RTNDSP>" +
                "               </NCB2BFIN>" +
                "           <NCB2BFIN>" +
                "                   <REQNBR>流程实例号</REQNBR>" +
                "                   <MCHNBR>商户编号</MCHNBR>" +
                "                   <REFORD>订单号</REFORD>" +
                "                   <SUBORD>订单支付号</SUBORD>" +
                "                   <CCYNBR>订单币种</CCYNBR>" +
                "                   <TRSAMT>订单金额</TRSAMT>" +
                "                   <ENDAMT>结账金额</ENDAMT>" +
                "                   <BBKNBR>付方分行号</BBKNBR>" +
                "                   <PAYACC>付方账号</PAYACC>" +
                "                   <ACCNAM>付方户名</ACCNAM>" +
                "                   <YURREF>业务参考号</YURREF>" +
                "                   <ENDDAT>付款日期</ENDDAT>" +
                "                   <RTNFLG>业务请求结果</RTNFLG>" +
                "                   <RTNDSP>结果描述</RTNDSP>" +
                "               </NCB2BFIN>" +
                "             </REQUEST>" +
                "             </DATA>";

        List<Map<String,String>> list =  readXmlAsList(xml);
        for(Map<String,String> map: list){
            System.out.println(map);
        }
    }
}
