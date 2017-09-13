package com.cqj.test.wbd2_gwpy;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import com.cqj.test.wbd2_gwpy.myinterface.IChooseItem;

/**
 * Entity mapped to table CS_INFO.
 */
public class CsInfo implements IChooseItem{

    private Long id;
    private String mplid;
    private String mplname;
    private Boolean is_choose;

    public CsInfo() {
    }

    public CsInfo(Long id) {
        this.id = id;
    }

    public CsInfo(Long id, String mplid, String mplname, Boolean is_choose) {
        this.id = id;
        this.mplid = mplid;
        this.mplname = mplname;
        this.is_choose = is_choose;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMplid() {
        return mplid;
    }

    public void setMplid(String mplid) {
        this.mplid = mplid;
    }

    public String getMplname() {
        return mplname;
    }

    public void setMplname(String mplname) {
        this.mplname = mplname;
    }

    public Boolean getIs_choose() {
        return is_choose;
    }

    public void setIs_choose(Boolean is_choose) {
        this.is_choose = is_choose;
    }

    @Override
    public String getItemName() {
        return mplname;
    }

    @Override
    public String getItemId() {
        return mplid;
    }

    @Override
    public String getCsId() {
        return null;
    }
}
