package com.cqj.test.wbd2_gwpy.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.cqj.test.wbd2_gwpy.AqjcCommitInfo;
import com.cqj.test.wbd2_gwpy.YhfcCommitInfo;
import com.cqj.test.wbd2_gwpy.YhzgCommitInfo;

import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/4/30.
 */
public class UploadDataHelper {
    private UploadDataHelper() {

    }

    public static ArrayList<HashMap<String, Object>> uploadYhzg(YhzgCommitInfo info)throws Exception{
        String imageResult = UploadDataHelper.getUploadedImageId(info.getImgpath());
        String[] keys = new String[]{"troubleid", "finishedDate",
                "results", "evalstr", "factCost", "imgpath"};
        Object[] values = new Object[]{info.getTroubleid(), info.getFinishedDate(),
                info.getResults(), info.getEvalstr(), info.getFactCost(), imageResult};
        return WebServiceUtil.getWebServiceMsg(keys, values,
                "setHiddenTroubleDighted", new String[]{},
                WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
    }

    public static ArrayList<HashMap<String, Object>> uploadYhfc(YhfcCommitInfo info)throws Exception{
        String imageResult = UploadDataHelper.getUploadedImageId(info.getDightedImgPath());
        String[] keys = new String[]{"troubleId", "reviewEmids",
                "reviewRemark", "reviewDate", "dightedImgPath"};
        Object[] values = new Object[]{info.getTroubleid(), info.getReviewEmids(),
                info.getReviewRemark(), info.getReviewDate(), imageResult};
        return WebServiceUtil.getWebServiceMsg(keys, values,
                "setHiddenTroubleReview", new String[]{},
                WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
    }

    public static ArrayList<HashMap<String, Object>> uploadAqjc(AqjcCommitInfo pFinalInfo) throws Exception {
        String filePaths = pFinalInfo.getImagePath();
        String imageResult = getUploadedImageId(filePaths);
        ArrayList<HashMap<String, Object>> data;
//                    if (info.getTaskid() == 0) {
        String keys3[] = {"checkDate", "recEmid", "dLimit",
                "hDetail", "dScheme", "hGrade", "lEmid",
                "dCost", "imgPatch", "objOrganizationID",
                /*"Taskid", "FliedID", "objPartid", "SetStr",*/
                "retstr"};
        Object[] values3 = {pFinalInfo.getCheckDate(),
                pFinalInfo.getRecEmid(),
                pFinalInfo.getDLimit(), pFinalInfo.getHDetail(), pFinalInfo.getDScheme(),
                pFinalInfo.getHGrade(), pFinalInfo.getLEmid(), pFinalInfo.getDCost(),
                imageResult, pFinalInfo.getObjOrganizationID(),
                /*info.getTaskid(), info.getFliedID(), info.getObjPartid(),
                info.getSetStr(), */""};
        data = WebServiceUtil.getWebServiceMsg(keys3, values3,
                "AddHiddenTroubleSimple", new String[]{},
                WebServiceUtil.HUIWEI_SAFE_URL, WebServiceUtil.HUIWEI_NAMESPACE);
//                    } else {
//                        String keys3[] = {"checkDate", "recEmid", "dLimit",
//                                "hDetail", "dScheme", "hGrade", "lEmid",
//                                "dCost", "imgPatch", "objOrganizationID",
//                                "Taskid", "FliedID", "objPartid", "SetStr",
//                                "retstr"};
//                        Object[] values3 = {info.getCheckDate(),
//                                info.getRecEmid(),
//                                info.getDLimit(), info.getHDetail(), info.getDScheme(),
//                                info.getHGrade(), info.getLEmid(), info.getDCost(),
//                                result, info.getObjOrganizationID(),
//                                info.getTaskid(), info.getFliedID(), info.getObjPartid(),
//                                info.getSetStr(), ""};
//                        data = WebServiceUtil.getWebServiceMsg(keys3, values3,
//                                "AddHiddenTroubleFull", new String[]{},
//                                WebServiceUtil.SAFE_URL);
//                    }
        return data;
    }

    public static String getNowDate() {
        Calendar instance = Calendar.getInstance();
        int month = instance.get(Calendar.MONTH) + 1;
        String monthStr = "";
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = month + "";
        }
        int day = instance.get(Calendar.DAY_OF_MONTH);
        String dayStr = "";
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = day + "";
        }
        return instance.get(Calendar.YEAR) + "-"
                + monthStr + "-" + dayStr + "T00:00:00.000";
    }

    /**
     * 上传图片
     *
     * @param pFilePaths 文件本地地址串，用“,”隔开
     * @return 上传成功后结果，用";"隔开
     * @throws Exception
     */
    public static String getUploadedImageId(String pFilePaths) throws Exception {
        StringBuilder result = new StringBuilder();
        String[] pathArr = pFilePaths.split(",");
        for (String path : pathArr) {
            if (!TextUtils.isEmpty(path) && !"null".equals(path)) {

                Bitmap b = null;
                try {
                    File file = new File(path);
                    String fileName = file.getName();
                    String uploadBuffer;

                    if (!fileName.endsWith(".mp3")) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        b = BitmapFactory.decodeFile(path);
                        b.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        uploadBuffer = new String(Base64.encode(baos
                                .toByteArray())); // 进行Base64编码
                    } else {
                        byte[] fileData = Utils.getBytes(file);
                        uploadBuffer = new String(Base64.encode(fileData)); // 进行Base64编码
                    }

                    //upSaveFile
                    /// 保存文件到远程服务器
                    /// </summary>
                    /// <param name="FileByteArray">待转换字节数组</param>
                    /// <param name="FileLength">字节长度</param>
                    /// <param name="SaveToUrl">保存路径</param>
                    /// <returns>返回是否执行成功</returns>
                    //public bool upSaveFile(byte[] FileByteArray, int FileLength, string SaveToUrl)
                    //UploadFile
                    //String[] keys = {"fileBytesstr", "fileName"};
                    String methodName = "UploadFile";
                    String[] keys = {"fileBytesstr", "fileName"};
                    Object[] values = {uploadBuffer, fileName};
                    System.out.println("fileName:" + fileName);
                    String serverPath = WebServiceUtil.putWebServiceMsg(keys, values,
                            methodName, WebServiceUtil.URL);
                    result.append(serverPath);
                    result.append(",");
                } finally {
                    if (b != null) {
                        b.recycle();
                    }
                }
            }
        }
        if(result.length()>0){
            return result.substring(0,result.length()-1);
        }
        return result.toString();
    }
}
