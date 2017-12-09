package com.cqj.test.wbd2_gwpy.util;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.HashMap;

public class WebServiceUtil {

	public static final String WEBSERVICE_NAMESPACE = "http://wisebus.com/";
	public static final String HUIWEI_URL ="http://www.huiweioa.com/5VCommon.asmx";
	public static final String HUIWEI_5VIN_URL="http://www.huiweioa.com/5VInformPublish.asmx";
	public static final String HUIWEI_NAMESPACE ="http://www.huiweioa.com/";
	public static final String URL = "http://www.wisebus.com/5VCommon.asmx";
	public static final String SAFE_URL = "http://www.wisebus.com/5VSafetyProduction.asmx";
	public static final String HUIWEI_SAFE_URL = "http://www.huiweioa.com/5VSafetyProduction.asmx";
	public static final String PART_DUTY_URL = "http://www.wisebus.com/5VHumanResource.asmx";
	public static final String putURL = "http://www.wisebus.com/5VInformPublish.asmx";
	public static final String putWEBSERVICE_NAMESPACE = "http://tempuri.org/";
	public static final String IMAGE_URLPATH = "http://huiweioa.chinasafety.org/";

	public static void putWebServiceMsg(String[] keys, Object[] values,
			String methodName) throws Exception {
		String actionUrl = putWEBSERVICE_NAMESPACE + methodName;
		SoapObject so = new SoapObject(putWEBSERVICE_NAMESPACE, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(putURL, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
			String resultStr = result.toString();
			System.out.println("result:" + resultStr);
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
		}
	}

	public static String putWebServiceMsg(String[] keys, Object[] values,
			String methodName, String url) throws Exception {
		String actionUrl = WEBSERVICE_NAMESPACE + methodName;
		SoapObject so = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(url, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
			String resultStr = result.toString();
			int start = resultStr.indexOf("e{UploadFileResult=")
					+ "e{UploadFileResult=".length();
			int end = resultStr.indexOf(";");
			resultStr = resultStr.substring(start, end);
			System.out.println("result:" + resultStr);
			return resultStr;
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
			return "";
		}
	}

	public static String putWebServiceMsg(String[] keys, Object[] values,
										  String methodName, String url,String nameSpace) throws Exception {
		String actionUrl = nameSpace + methodName;
		SoapObject so = new SoapObject(nameSpace, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(url, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
			String resultStr = result.toString();
			int start = resultStr.indexOf("e{UploadFileResult=")
					+ "e{UploadFileResult=".length();
			int end = resultStr.indexOf(";");
			resultStr = resultStr.substring(start, end);
			System.out.println("result:" + resultStr);
			return resultStr;
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
			return "";
		}
	}

	public static ArrayList<HashMap<String, Object>> getWebServiceMsg(
			String[] keys, Object[] values, String methodName) throws Exception {
		String actionUrl = WEBSERVICE_NAMESPACE + methodName;
		SoapObject so = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		MarshalFloat marshaldDouble = new MarshalFloat();
		marshaldDouble.register(envelope);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(URL, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
		}
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
		if (result != null) {
			String resultStr = "";
			try {
				resultStr = result.toString();
				System.out.println("result:" + resultStr);
				int endInt = resultStr.lastIndexOf("};");
				resultStr = resultStr.substring(endInt + 2,
						resultStr.length() - 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SoapObject detail = (SoapObject) result.getProperty(0);
			SoapObject mstr = (SoapObject) detail.getProperty(1);
			if (mstr.getPropertyCount() > 0) {
				SoapObject mstr2 = (SoapObject) mstr.getProperty(0);
				for (int i = 0; i < mstr2.getPropertyCount(); i++) {
					String str_result = mstr2.getProperty(i).toString();
					HashMap<String, Object> map = parseData(str_result,
							resultStr);
					if (map != null) {
						datas.add(map);
					}
				}
			} else {
				HashMap<String, Object> map = parseData("", resultStr);
				if (map != null) {
					datas.add(map);
				}
			}
		}
		return datas;
	}

	public static ArrayList<HashMap<String, Object>> getWebServiceMsg(
			String[] keys, Object[] values, String methodName, String url,String nameSpace)
			throws Exception {
		String actionUrl = nameSpace + methodName;
		SoapObject so = new SoapObject(nameSpace, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		MarshalFloat marshaldDouble = new MarshalFloat();
		marshaldDouble.register(envelope);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(url, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
		}
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
		if (result != null) {
			String resultStr = "";
			try {
				resultStr = result.toString();
				System.out.println("result:" + resultStr);
				int endInt = resultStr.lastIndexOf("};");
				resultStr = resultStr.substring(endInt + 2,
						resultStr.length() - 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				SoapObject detail = (SoapObject) result.getProperty(0);
				SoapObject mstr = (SoapObject) detail.getProperty(1);
				if (mstr.getPropertyCount() > 0) {
					SoapObject mstr2 = (SoapObject) mstr.getProperty(0);
					for (int i = 0; i < mstr2.getPropertyCount(); i++) {
						String str_result = mstr2.getProperty(i).toString();
						HashMap<String, Object> map = parseData(str_result,
								resultStr);
						if (map != null) {
							datas.add(map);
						}
					}
				} else {
					HashMap<String, Object> map = parseData("", resultStr);
					if (map != null) {
						datas.add(map);
					}
				}
			} catch (Exception e) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("result", "ok");
				datas.add(map);
			}
		}
		return datas;
	}

	public static ArrayList<HashMap<String, Object>> getWebServiceMsg(
			String[] keys, Object[] values, String methodName, String url)
			throws Exception {
		return getWebServiceMsg(keys,values,methodName,url,WEBSERVICE_NAMESPACE);
	}

	public static ArrayList<HashMap<String, Object>> getWebServiceMsg(
			String[] keys, Object[] values, String methodName,
			String[] getParams_Keys) throws Exception {
		String actionUrl = WEBSERVICE_NAMESPACE + methodName;
		SoapObject so = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(URL, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
		}
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
		if (result != null) {
			String resultStr = "";
			try {
				resultStr = result.toString();
				System.out.println("result:" + resultStr);
				int endInt = resultStr.lastIndexOf("};");
				resultStr = resultStr.substring(endInt + 2,
						resultStr.length() - 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SoapObject detail = (SoapObject) result.getProperty(0);
			SoapObject mstr = (SoapObject) detail.getProperty(1);
			if (mstr.getPropertyCount() > 0) {
				SoapObject mstr2 = (SoapObject) mstr.getProperty(0);
				for (int i = 0; i < mstr2.getPropertyCount(); i++) {
					SoapObject items = (SoapObject) mstr2.getProperty(i);
					HashMap<String, Object> maps = new HashMap<String, Object>();
					for (int j = 0; j < getParams_Keys.length; j++) {
						try {
							maps.put(getParams_Keys[j],
									items.getProperty(getParams_Keys[j])
											.toString());
						} catch (Exception e) {
							continue;
						}
					}
					datas.add(maps);
				}
			}
		}
		return datas;
	}

	public static ArrayList<HashMap<String, Object>> getWebServiceMsg(
			String[] keys, Object[] values, String methodName,
			String[] getParams_Keys, String url) throws Exception {
		String actionUrl = WEBSERVICE_NAMESPACE + methodName;
		SoapObject so = new SoapObject(WEBSERVICE_NAMESPACE, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		MarshalFloat marshaldDouble = new MarshalFloat();
		marshaldDouble.register(envelope);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(url, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
		}
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
		if (result != null) {
			String resultStr = "";
			try {
				resultStr = result.toString();
				System.out.println("result:" + resultStr);
				int endInt = resultStr.lastIndexOf("};");
				resultStr = resultStr.substring(endInt + 2,
						resultStr.length() - 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
			SoapObject detail = (SoapObject) result.getProperty(0);
			SoapObject mstr = (SoapObject) detail.getProperty(1);
			if (mstr.getPropertyCount() > 0) {
				SoapObject mstr2 = (SoapObject) mstr.getProperty(0);
				for (int i = 0; i < mstr2.getPropertyCount(); i++) {
					SoapObject items = (SoapObject) mstr2.getProperty(i);
					HashMap<String, Object> maps = new HashMap<String, Object>();
					for (int j = 0; j < getParams_Keys.length; j++) {
						try {
							maps.put(getParams_Keys[j],
									items.getProperty(getParams_Keys[j])
											.toString());
						} catch (Exception e) {
							continue;
						}
					}
					datas.add(maps);
				}
			}
			}catch(Exception e){
			}
		}
		return datas;
	}

	public static ArrayList<HashMap<String, Object>> getWebServiceMsg(
			String[] keys, Object[] values, String methodName,
			String[] getParams_Keys, String url,String nameSpace) throws Exception {
		String actionUrl = nameSpace + methodName;
		SoapObject so = new SoapObject(nameSpace, methodName);
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				so.addProperty(keys[i], values[i]);
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		MarshalFloat marshaldDouble = new MarshalFloat();
		marshaldDouble.register(envelope);
		envelope.bodyOut = so;
		envelope.encodingStyle = "UTF-8";
		envelope.dotNet = true;
		HttpTransportSE ht = new HttpTransportSE(url, 5000);
		ht.call(actionUrl, envelope);
		SoapObject result = null;
		try {
			result = (SoapObject) envelope.bodyIn;
		} catch (Exception e) {
			SoapFault soaF = (SoapFault) envelope.bodyIn;
			System.out.println("FaultString:" + soaF.faultstring);
		}
		ArrayList<HashMap<String, Object>> datas = new ArrayList<HashMap<String, Object>>();
		if (result != null) {
			String resultStr = "";
			try {
				resultStr = result.toString();
				System.out.println("result:" + resultStr);
				int endInt = resultStr.lastIndexOf("};");
				resultStr = resultStr.substring(endInt + 2,
						resultStr.length() - 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			try{
				SoapObject detail = (SoapObject) result.getProperty(0);
				SoapObject mstr = (SoapObject) detail.getProperty(1);
				if (mstr.getPropertyCount() > 0) {
					SoapObject mstr2 = (SoapObject) mstr.getProperty(0);
					for (int i = 0; i < mstr2.getPropertyCount(); i++) {
						SoapObject items = (SoapObject) mstr2.getProperty(i);
						HashMap<String, Object> maps = new HashMap<String, Object>();
						for (int j = 0; j < getParams_Keys.length; j++) {
							try {
								maps.put(getParams_Keys[j],
										items.getProperty(getParams_Keys[j])
												.toString());
							} catch (Exception e) {
								continue;
							}
						}
						datas.add(maps);
					}
				}
			}catch(Exception e){
			}
		}
		return datas;
	}

	private static HashMap<String, Object> parseData(String str_result,
			String outStr) {
		HashMap<String, Object> data = new HashMap<String, Object>();
		if (StringUtil.isEmpty(str_result)) {
			if (StringUtil.isEmpty(outStr)) {
				return null;
			}
			str_result = outStr;
		} else {
			int start = str_result.indexOf("{");
			int end = str_result.lastIndexOf("}");
			str_result = str_result.substring(start + 1, end);
			str_result = str_result + outStr;
		}

		String[] temps = str_result.split(";");
		for (int i = 0; i < temps.length; i++) {
			String res = temps[i].trim();
			if (StringUtil.isNotEmpty(res)) {
				String[] keyvalues = res.split("=");
				if (keyvalues.length > 1) {
					if ((keyvalues[1].indexOf("{") != -1)) {
						keyvalues[1] = keyvalues[1].replace("{", "#");
						keyvalues[1] = keyvalues[1].replace("}", "$");
					}
					data.put(keyvalues[0].trim(), keyvalues[1].trim());
				}
			}
		}
		return data;
	}
}
