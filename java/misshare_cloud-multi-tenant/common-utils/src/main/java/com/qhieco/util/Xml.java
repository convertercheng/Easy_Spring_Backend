package com.qhieco.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by xujiayu on 17/9/17.
 */
public class Xml {
	private static XStream xStream = new XStream(new XppDriver(new NoNameCoder()) {

		@Override
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 重写这个方法，不再像XppDriver那样调用nameCoder来进行编译，而是直接返回节点名称，避免双下划线出现
				@Override
				public String encodeNode(String name) {
					return name;
				}
			};
		}
	});

	/**
	 * 对象转xml
	 *
	 * @param obj
	 * @return
	 */
	public static String toXMl(Object obj) {
		//使用注解设置别名必须在使用之前加上注解类才有作用
		try {
			xStream.processAnnotations(obj.getClass());
			String xml = xStream.toXML(obj);
			return xml;
		} catch (Exception e) {
			return null;
		}
	}

	public static Object fromXML(String xml) {
		XStreamWrapper xStream = new XStreamWrapper(new DomDriver());
		xStream.processAnnotations(WxpayData.class);
		return xStream.fromXML(xml);
	}

	/**
	 * Xml string转换成Map
	 *
	 * @param xmlStr
	 * @return
	 */
	public static Map<String, String> xmlString2Map(String xmlStr) {
		Map<String, String> map = new HashMap<String, String>();
		Document doc;
		try {
			doc = DocumentHelper.parseText(xmlStr);
			Element el = doc.getRootElement();
			map = recGetXmlElementValue(el, map);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 循环解析xml
	 *
	 * @param ele
	 * @param map
	 * @return
	 */
	@SuppressWarnings({"unchecked"})
	private static Map<String, String> recGetXmlElementValue(Element ele, Map<String, String> map) {
		List<Element> eleList = ele.elements();
		if (eleList.size() == 0) {
			map.put(ele.getName(), ele.getTextTrim());
			return map;
		} else {
			for (Iterator<Element> iter = eleList.iterator(); iter.hasNext(); ) {
				Element innerEle = iter.next();
				recGetXmlElementValue(innerEle, map);
			}
			return map;
		}
	}

	public static void main(String[] args) {
		System.out.println(xmlString2Map(null));

		System.out.println(xmlString2Map("<xml>" +
				"<appid><![CDATA[wx42a91e33c4b3a97b]]></appid>" +
				"<cash_fee><![CDATA[1]]></cash_fee>" +
				"<mch_id><![CDATA[1489065172]]></mch_id>" +
				"<nonce_str><![CDATA[OdJiA5uxj02mh0rg]]></nonce_str>" +
				"<out_refund_no_0><![CDATA[9-1512104199241]]></out_refund_no_0>" +
				"<out_trade_no><![CDATA[17-1512104148757]]></out_trade_no>" +
				"<refund_account_0><![CDATA[REFUND_SOURCE_UNSETTLED_FUNDS]]></refund_account_0>" +
				"<refund_channel_0><![CDATA[ORIGINAL]]></refund_channel_0>" +
				"<refund_count>1</refund_count>" +
				"<refund_fee>1</refund_fee>" +
				"<refund_fee_0>1</refund_fee_0>" +
				"<refund_id_0><![CDATA[50000604912017120102524551780]]></refund_id_0>" +
				"<refund_recv_accout_0><![CDATA[支付用户的零钱]]></refund_recv_accout_0>" +
				"<refund_status_0><![CDATA[SUCCESS]]></refund_status_0>" +
				"<refund_success_time_0><![CDATA[2017-12-01 12:56:40]]></refund_success_time_0>" +
				"<result_code><![CDATA[SUCCESS]]></result_code>" +
				"<return_code><![CDATA[SUCCESS]]></return_code>" +
				"<return_msg><![CDATA[OK]]></return_msg>" +
				"<sign><![CDATA[CF4CC4A793A8A0E364EFA8685E142DEF]]></sign>" +
				"<total_fee><![CDATA[1]]></total_fee>" +
				"<transaction_id><![CDATA[4200000032201712018244009390]]></transaction_id>" +
				"</xml>"));

	}


	public static class XStreamWrapper extends XStream {

		public XStreamWrapper(HierarchicalStreamDriver hierarchicalStreamDriver) {
			super(hierarchicalStreamDriver);
		}

		@Override
		protected MapperWrapper wrapMapper(MapperWrapper next) {
			return new MapperWrapper(next) {
				@Override
				public boolean shouldSerializeMember(@SuppressWarnings("rawtypes") Class definedIn, String fieldName) {
					// 不能识别的节点，掠过。
					if (definedIn == Object.class) {
						return false;
					}
					return super.shouldSerializeMember(definedIn, fieldName);
				}
			};
		}
	}

}
