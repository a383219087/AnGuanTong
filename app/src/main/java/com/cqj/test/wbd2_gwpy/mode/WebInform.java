package com.cqj.test.wbd2_gwpy.mode;

import java.util.Date;
import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;

public class WebInform implements KvmSerializable {

	private String orgidStr;
	private Date dateStart;
	private Date dateEnd;
	private int topCount;
	private String docType;
	private String infoTitle;
	private String retStr;

	public String getOrgidStr() {
		return orgidStr;
	}

	public void setOrgidStr(String orgidStr) {
		this.orgidStr = orgidStr;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public int getTopCount() {
		return topCount;
	}

	public void setTopCount(int topCount) {
		this.topCount = topCount;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getInfoTitle() {
		return infoTitle;
	}

	public void setInfoTitle(String infoTitle) {
		this.infoTitle = infoTitle;
	}

	public String getRetStr() {
		return retStr;
	}

	public void setRetStr(String retStr) {
		this.retStr = retStr;
	}

	@Override
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		switch (arg0) {
		case 0:
			return getOrgidStr();
		case 1:
			return getDateStart();
		case 2:
			return getDateEnd();
		case 3:
			return getTopCount();
		case 4:
			return getDocType();
		case 5:
			return getInfoTitle();
		case 6:
			return getRetStr();
		default:
			break;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		// TODO Auto-generated method stub
		switch (arg0) {  
        case 0:  
            arg2.type = PropertyInfo.STRING_CLASS;  
            arg2.name = "orgidStr";  
            break;  
        case 1:  
            arg2.type = MarshalDate.DATE_CLASS;  
            arg2.name = "dateStart";  
            break;  
        case 2:  
            arg2.type = MarshalDate.DATE_CLASS;  
            arg2.name = "dateEnd";  
            break;
        case 3:  
            arg2.type = PropertyInfo.INTEGER_CLASS;
            arg2.name = "topCount";  
        case 4:  
            arg2.type = PropertyInfo.STRING_CLASS;
            arg2.name = "docType"; 
        case 5:  
            arg2.type = PropertyInfo.STRING_CLASS;
            arg2.name = "infoTitle"; 
        case 6:  
            arg2.type = PropertyInfo.STRING_CLASS;
            arg2.name = "retStr"; 
            break;
        default:  
            break;  
    }  
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		// TODO Auto-generated method stub
		switch (arg0) {  
        case 0:  
            setOrgidStr(arg1.toString());  
            break;  
        case 1:  
            setDateStart((Date) arg1);  
            break;  
        case 2:  
        	setDateEnd((Date) arg1);  
            break;
        case 3:
        	setTopCount((Integer) arg1);
        	break;
        case 4:
        	setDocType(arg1.toString());
        	break;
        case 5:
        	setInfoTitle(arg1.toString());
        	break;
        case 6:
        	setRetStr(arg1.toString());
        default:  
            break;  
    }  
	}

}
