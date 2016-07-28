package com.yyw.yhyc.order.helper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class JsonHelper<T extends Serializable> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * json字符串转为java对象
	 * @param jsonStr
	 * @param tClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public T fromObject(String jsonStr, Class<T> tClass) throws Exception {
		T t = null;
		
		try {
			JSONObject object = JSONObject.fromObject(jsonStr);
			t = (T) JSONObject.toBean(object, tClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return t;
	}
	
	/**
	 * json字符串转为java List集合
	 * @param jsonStr
	 * @param tClass
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<T> fromList(String jsonStr, Class<T> tClass) throws Exception {
		List<T> list = null;
		
		try {
			JSONArray array = JSONArray.fromObject(jsonStr);
			list = (List<T>) JSONArray.toCollection(array, tClass);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
}
