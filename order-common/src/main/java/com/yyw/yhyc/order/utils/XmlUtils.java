package com.yyw.yhyc.order.utils;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.util.*;

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

    /**
     * xml格式的字符 转成 map
     * @param xmlText xml格式的字符
     * @author liz
     * @since  2016-10-25
     * @return Map<String, Object>
     */
    public static Map<String, Object> xml2map(String xmlText) {
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xmlText);
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
        return  (Map<String, Object>) xml2map(doc.getRootElement());

    }

    private static Object xml2map(Element element) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<Element> elements = element.elements();
        if (elements.size() == 0) {
            map.put(element.getName(), element.getText());
            if (!element.isRootElement()) {
                return element.getText();
            }
        } else if (elements.size() == 1) {
            map.put(elements.get(0).getName(), xml2map(elements.get(0)));
        } else if (elements.size() > 1) {
            // 多个子节点的话就得考虑list的情况了，比如多个子节点有节点名称相同的
            // 构造一个map用来去重
            Map<String, Element> tempMap = new HashMap<String, Element>();
            for (Element ele : elements) {
                tempMap.put(ele.getName(), ele);
            }
            Set<String> keySet = tempMap.keySet();
            for (String string : keySet) {
                Namespace namespace = tempMap.get(string).getNamespace();
                List<Element> elements2 = element.elements(new QName(string, namespace));
                // 如果同名的数目大于1则表示要构建list
                if (elements2.size() > 1) {
                    List<Object> list = new ArrayList<Object>();
                    for (Element ele : elements2) {
                        list.add(xml2map(ele));
                    }
                    map.put(string, list);
                } else {
                    // 同名的数量不大于1则直接递归去
                    map.put(string, xml2map(elements2.get(0)));
                }
            }
        }

        return map;
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

//        List<Map<String,String>> list =  readXmlAsList(xml);
//        for(Map<String,String> map: list){
//            System.out.println(map);
//        }

        Map<String,Object> map = xml2map(xml);
        System.out.println("map = " + map);
    }
}
