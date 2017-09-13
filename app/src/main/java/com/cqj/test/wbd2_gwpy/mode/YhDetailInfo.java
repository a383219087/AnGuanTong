package com.cqj.test.wbd2_gwpy.mode;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class YhDetailInfo {
    private String checkDate;
    private String limitDate;
    private String troubleGrade;
    private String checkObject;
    private String finishDate;
    private String dightScheme;
    private String dResult;
    private String safetyTrouble;
    private String actionComname;
    private String imgPatch;
    private String reviewDate;
    private String reviewDetail;
    private String reviewNames;
    private String esCost;
    private String dightCost;
    private String liabelMasterName;
    private String liabelEmid;
    private String areaName;
    private String DightedImgPaths;
    private String LiabelMasterMPhone;

    public String getCheckDate() {
        if (!TextUtils.isEmpty(checkDate) && checkDate.length() > 10) {
            checkDate = checkDate.substring(0, 10);
        }
        return checkDate;
    }

    public void setCheckDate(String pCheckDate) {
        checkDate = pCheckDate;
    }

    public String getLimitDate() {
        if (!TextUtils.isEmpty(limitDate) && limitDate.length() > 10) {
            limitDate = limitDate.substring(0, 10);
        }
        return limitDate;
    }

    public void setLimitDate(String pLimitDate) {
        limitDate = pLimitDate;
    }

    public String getTroubleGrade() {
        return troubleGrade;
    }

    public void setTroubleGrade(String pTroubleGrade) {
        troubleGrade = pTroubleGrade;
    }

    public String getCheckObject() {
        return checkObject;
    }

    public void setCheckObject(String pCheckObject) {
        checkObject = pCheckObject;
    }

    public String getFinishDate() {
        if (!TextUtils.isEmpty(finishDate) && finishDate.length() > 10) {
            finishDate = finishDate.substring(0, 10);
        }
        return finishDate;
    }

    public void setFinishDate(String pFinishDate) {
        finishDate = pFinishDate;
    }

    public String getDightScheme() {
        return dightScheme;
    }

    public void setDightScheme(String pDightScheme) {
        dightScheme = pDightScheme;
    }

    public String getdResult() {
        return dResult;
    }

    public void setdResult(String pDResult) {
        dResult = pDResult;
    }

    public String getSafetyTrouble() {
        return safetyTrouble;
    }

    public void setSafetyTrouble(String pSafetyTrouble) {
        safetyTrouble = pSafetyTrouble;
    }

    public String getActionComname() {
        return actionComname;
    }

    public void setActionComname(String pActionComname) {
        actionComname = pActionComname;
    }

    public List<String> getImgPatch() {
        List<String> imgList = new ArrayList<String>();
        String[] imgArr = imgPatch.split("[|]");
        if (imgArr.length > 0) {
            for (String str : imgArr) {
                if (TextUtils.isEmpty(str)) {
                    continue;
                }
                str =str.replace("../","");
                imgList.add(String.format("http://www.chinasafety.org/%s", str));
            }
        }
        return imgList;
    }

    public void setImgPatch(String pImgPatch) {
        imgPatch = pImgPatch;
    }

    public String getReviewDate() {
        if (!TextUtils.isEmpty(reviewDate) && reviewDate.length() > 10) {
            reviewDate = reviewDate.substring(0, 10);
        }
        return reviewDate;
    }

    public void setReviewDate(String pReviewDate) {
        reviewDate = pReviewDate;
    }

    public String getReviewDetail() {
        return reviewDetail;
    }

    public void setReviewDetail(String pReviewDetail) {
        reviewDetail = pReviewDetail;
    }

    public String getReviewNames() {
        return reviewNames;
    }

    public void setReviewNames(String pReviewNames) {
        reviewNames = pReviewNames;
    }

    public String getEsCost() {
        return esCost;
    }

    public void setEsCost(String pEsCost) {
        esCost = pEsCost;
    }

    public String getDightCost() {
        return dightCost;
    }

    public void setDightCost(String pDightCost) {
        dightCost = pDightCost;
    }

    public String getLiabelMasterName() {
        return liabelMasterName;
    }

    public void setLiabelMasterName(String pLiabelMasterName) {
        liabelMasterName = pLiabelMasterName;
    }

    public String getLiabelEmid() {
        return liabelEmid;
    }

    public void setLiabelEmid(String pLiabelEmid) {
        liabelEmid = pLiabelEmid;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String pAreaName) {
        areaName = pAreaName;
    }

    public List<String> getDightedImgPaths() {
        List<String> imgList = new ArrayList<String>();
        String[] imgArr = DightedImgPaths.split("[|]");
        if (imgArr.length > 0) {
            for (String str : imgArr) {
                if (TextUtils.isEmpty(str)) {
                    continue;
                }
                str =str.replace("../","");
                imgList.add(String.format("http://www.chinasafety.org/%s", str));
            }
        }
        return imgList;
    }

    public void setDightedImgPaths(String pDightedImgPaths) {
        DightedImgPaths = pDightedImgPaths;
    }

    public String getLiabelMasterMPhone() {
        return LiabelMasterMPhone;
    }

    public void setLiabelMasterMPhone(String pLiabelMasterMPhone) {
        LiabelMasterMPhone = pLiabelMasterMPhone;
    }
}
