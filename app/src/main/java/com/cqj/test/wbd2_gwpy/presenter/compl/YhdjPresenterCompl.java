package com.cqj.test.wbd2_gwpy.presenter.compl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.cqj.test.wbd2_gwpy.AqjcCommitInfo;
import com.cqj.test.wbd2_gwpy.CsInfo;
import com.cqj.test.wbd2_gwpy.CsInfoDao;
import com.cqj.test.wbd2_gwpy.JcbDetailInfo;
import com.cqj.test.wbd2_gwpy.JcbDetailInfoDao;
import com.cqj.test.wbd2_gwpy.JcbInfo;
import com.cqj.test.wbd2_gwpy.JcbInfoDao;
import com.cqj.test.wbd2_gwpy.R;
import com.cqj.test.wbd2_gwpy.RwInfo;
import com.cqj.test.wbd2_gwpy.RwInfoDao;
import com.cqj.test.wbd2_gwpy.SbInfo;
import com.cqj.test.wbd2_gwpy.SbInfoDao;
import com.cqj.test.wbd2_gwpy.activity.MyApplication;
import com.cqj.test.wbd2_gwpy.dao.SqliteOperator;
import com.cqj.test.wbd2_gwpy.presenter.IYhdjPresenter;
import com.cqj.test.wbd2_gwpy.util.StringUtil;
import com.cqj.test.wbd2_gwpy.util.UploadDataHelper;
import com.cqj.test.wbd2_gwpy.util.Utils;
import com.cqj.test.wbd2_gwpy.util.WebServiceUtil;

import org.kobjects.base64.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/3/8.
 */
public class YhdjPresenterCompl implements IYhdjPresenter {

    private IYhdjPresenter.View mView;
    private MyApplication myApp;
    private Context mContext;
    private CompositeSubscription mCompositeSubscription;
    private List<JcbDetailInfo> mJcbDetailList;
    private int mJcbDetailPage;
    private Subscription mCommitSubscription;

    public YhdjPresenterCompl(IYhdjPresenter.View pView, Activity activity) {
        this.mView = pView;
        this.myApp = (MyApplication) activity.getApplication();
        this.mContext = activity;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void getRwData() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<RwInfo>>() {
            @Override
            public void call(Subscriber<? super List<RwInfo>> subscriber) {
                if (!MyApplication.isConnection) {
                    RwInfoDao rwInfoDao = SqliteOperator.INSTANCE.getRwInfoDao(mContext);
                    subscriber.onNext(rwInfoDao.loadAll());
                    subscriber.onCompleted();
                    return;
                }
                ArrayList<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("TaskID", "0");
                map.put("TaskTitle", "默认安全检查任务");
                result.add(map);
                String keys2[] = {"ComID", "nStart", "eStart"};
                Object values2[] = {
                        Integer.parseInt(myApp.getComInfo().getCom_id()),
                        "2010-01-01T01:00:00.000",
                        "2015-12-31T01:00:00.000"};
                try {
                    ArrayList<HashMap<String, Object>> data = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getSafetyCheckTaskListFromCom", new String[]{
                                    "TaskTitle", "TaskID"},
                            WebServiceUtil.SAFE_URL);
                    result.addAll(data);
                    subscriber.onNext(parseRwInfo(result));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<RwInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("任务信息获取失败，请重试");
                    }

                    @Override
                    public void onNext(List<RwInfo> hashMaps) {
                        mView.getRwDataSuccess(hashMaps);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void getCsData() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<CsInfo>>() {
            @Override
            public void call(Subscriber<? super List<CsInfo>> pSubscriber) {
                if (!MyApplication.isConnection) {
                    CsInfoDao rwInfoDao = SqliteOperator.INSTANCE.getCsInfoDao(mContext);
                    pSubscriber.onNext(rwInfoDao.loadAll());
                    pSubscriber.onCompleted();
                    return;
                }
                ArrayList<HashMap<String, Object>> result;
                String keys2[] = {"Comid"};
                Object values2[] = {Integer.parseInt(myApp.getComInfo()
                        .getCom_id())};
                try {
                    result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getAllField", new String[]{"mplid",
                                    "mplname"}, WebServiceUtil.URL);
                    if (result == null || result.isEmpty()) {
                        result = new ArrayList<HashMap<String, Object>>();
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("mplid", "0");
                        map.put("mplname", "无");
                        result.add(map);
                    }
                    pSubscriber.onNext(parseCsInfo(result));
                } catch (Exception e) {
                    e.printStackTrace();
                    pSubscriber.onError(e);
                }
                pSubscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<CsInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("场所信息获取失败，请重试");
                    }

                    @Override
                    public void onNext(List<CsInfo> pCsInfos) {
                        mView.getCsDataSuccess(pCsInfos);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void getJcbData() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<JcbInfo>>() {
            @Override
            public void call(Subscriber<? super List<JcbInfo>> pSubscriber) {
                if (!MyApplication.isConnection) {
                    JcbInfoDao rwInfoDao = SqliteOperator.INSTANCE.getJcbInfoDao(mContext);
                    pSubscriber.onNext(rwInfoDao.loadAll());
                    pSubscriber.onCompleted();
                    return;
                }
                ArrayList<HashMap<String, Object>> result;
                try {
                    String keys[] = {"orgIDstr", "cDocID", "keyWord",
                            "cStart", "cEnd", "onlyInTitle", "cState",
                            "userId", "cType", "docTempId", "retstr"};
                    // System.out.println("orgidstr" +
                    // orgItem.get("Emid").toString());
                    Object values[] = {myApp.getComInfo().getOrg_idstr(),
                            0, "", "1900-01-01T00:00:00.850",
                            "2049-12-31T00:00:00.850", true, true, 0,
                            "安全检查表", 0, ""};
                    result = WebServiceUtil.getWebServiceMsg(keys, values,
                            "getCapacityDocument", new String[]{"cDocid",
                                    "cDocTitle"});

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("cDocid", "0");
                    map.put("cDocTitle", "无检查表");
                    if (result == null) {
                        result = new ArrayList<HashMap<String, Object>>();
                        result.add(map);
                    } else {
                        result.add(0, map);
                    }
                    pSubscriber.onNext(parseJcbInfo(result));
                } catch (Exception e) {
                    e.printStackTrace();
                    pSubscriber.onError(e);
                }
                pSubscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<JcbInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("检查表获取失败，请重试");
                    }

                    @Override
                    public void onNext(List<JcbInfo> pCsInfos) {
                        mView.getJcbDataSuccess(pCsInfos);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void getSbData(final String csId) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<SbInfo>>() {
            @Override
            public void call(Subscriber<? super List<SbInfo>> pSubscriber) {
                if (!MyApplication.isConnection) {
                    try {
                        ArrayList<SbInfo> result = new ArrayList<SbInfo>();
                        result.add(new SbInfo(null, "0", "无设备", "0", false));
                        SbInfoDao rwInfoDao = SqliteOperator.INSTANCE.getSbInfoDao(mContext);
                        QueryBuilder<SbInfo> qb = rwInfoDao.queryBuilder();
                        qb.where(SbInfoDao.Properties.Csid.eq(csId));
                        List<SbInfo> data = qb.list();
                        result.addAll(data);
                        pSubscriber.onNext(result);
                        pSubscriber.onCompleted();
                    } catch (Exception pE) {
                        pE.printStackTrace();
                        pSubscriber.onError(pE);
                    }
                    return;
                }
                ArrayList<HashMap<String, Object>> result;
                String keys2[] = {"cpid", "proclaid", "placeid", "kemid", "comid"};
                Object values2[] = {0, 0, Integer.parseInt(csId), 0, Integer.parseInt(myApp.getComInfo()
                        .getCom_id())};
                try {
                    result = new ArrayList<HashMap<String, Object>>();
                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("cpid", "0");
                    map.put("cproname", "无设备");
                    result.add(map);
                    ArrayList<HashMap<String, Object>> data = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getAllEquipment", new String[]{"cpid",
                                    "cproname", "num"}, WebServiceUtil.HUIWEI_URL, WebServiceUtil.HUIWEI_NAMESPACE);
                    result.addAll(data);
                    pSubscriber.onNext(parseSbInfo(result, csId));
                } catch (Exception e) {
                    e.printStackTrace();
                    pSubscriber.onError(e);
                }
                pSubscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SbInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.getMessage();
                        mView.toast("设备信息获取失败，请重试");
                    }

                    @Override
                    public void onNext(List<SbInfo> pCsInfos) {
                        mView.getSbDataSuccess(pCsInfos);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void getJcbDetail(final String jcbId) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<JcbDetailInfo>>() {
            @Override
            public void call(Subscriber<? super List<JcbDetailInfo>> pSubscriber) {
                if (!MyApplication.isConnection) {
                    try {
                        JcbDetailInfoDao rwInfoDao = SqliteOperator.INSTANCE.getJcbDetailInfoDao(mContext);
                        QueryBuilder<JcbDetailInfo> qb = rwInfoDao.queryBuilder();
                        qb.where(JcbDetailInfoDao.Properties.Jcb_id.eq(jcbId));
                        List<JcbDetailInfo> result = qb.list();
                        if (result == null || result.isEmpty()) {
                            result = new ArrayList<JcbDetailInfo>();
                        }
                        pSubscriber.onNext(result);
                        pSubscriber.onCompleted();
                    } catch (Exception pE) {
                        pE.printStackTrace();
                        pSubscriber.onError(pE);
                    }
                    return;
                }
                ArrayList<HashMap<String, Object>> result;
                try {
                    String keys2[] = {"SCheckID", "CDocID", "InduID", "FliedID",
                            "ProName"};
                    Object values2[] = {0, jcbId, 0, 0, ""};
                    result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getSafetyCheckList", new String[]{"oblititle",
                                    "odetail", "sCheckListtype", "induname",
                                    "inseName", "malName", "rAdvise"},
                            WebServiceUtil.SAFE_URL);
                    if (result == null || result.isEmpty()) {
                        result = new ArrayList<HashMap<String, Object>>();
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("oblititle", "无检查表项");
                        map.put("odetail", "无检查表项");
                        result.add(map);
                    }
                    pSubscriber.onNext(parseJcbDetailInfo(result, jcbId));
                } catch (Exception e) {
                    e.printStackTrace();
                    pSubscriber.onError(e);
                }
                pSubscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<JcbDetailInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.toast("检查项获取失败，请重试");
                    }

                    @Override
                    public void onNext(List<JcbDetailInfo> pCsInfos) {
                        mJcbDetailList = pCsInfos;
                        if (pCsInfos.isEmpty()) {
                            mView.changeJcbDetail(null, 0, pCsInfos.size());
                        } else {
                            mView.changeJcbDetail(pCsInfos.get(0), 1, pCsInfos.size());
                        }
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void getPrePage() {
        if (mJcbDetailList == null || mJcbDetailList.isEmpty()) {
            return;
        }
        if (mJcbDetailPage == 0) {
            mView.toast("没有上一条了");
            return;
        }
        mJcbDetailPage--;
        mView.changeJcbDetail(mJcbDetailList.get(mJcbDetailPage), mJcbDetailPage + 1, mJcbDetailList.size());
    }

    @Override
    public void getNextPage() {
        if (mJcbDetailList == null || mJcbDetailList.isEmpty()) {
            return;
        }
        if (mJcbDetailPage == mJcbDetailList.size() - 1) {
            mView.toast("没有下一条了");
            return;
        }
        mJcbDetailPage++;
        mView.changeJcbDetail(mJcbDetailList.get(mJcbDetailPage), mJcbDetailPage + 1, mJcbDetailList.size());
    }

    @Override
    public void getJcbDetail() {
        if (mJcbDetailList == null || mJcbDetailList.isEmpty()) {
            mView.setJcbDetailGone();
            return;
        }
        if (TextUtils.isEmpty(mJcbDetailList.get(mJcbDetailPage).getOblititle())) {
            mView.setJcbDetailGone();
        }
        StringBuffer sb = new StringBuffer();
        sb.append("标题：");
        sb.append(mJcbDetailList.get(mJcbDetailPage).getOblititle());
        sb.append("\n");
        sb.append("细则：");
        sb.append(mJcbDetailList.get(mJcbDetailPage).getOdetail());
        sb.append("\n");
        sb.append("检查表项针对的人的行为、设备、环境的因素：");
        sb.append(mJcbDetailList.get(mJcbDetailPage).getSCheckListtype());
        sb.append("\n");
        sb.append("行业类别：");
        sb.append(mJcbDetailList.get(mJcbDetailPage).getInduname());
        sb.append("\n");
        sb.append("伤害类别：");
        sb.append(mJcbDetailList.get(mJcbDetailPage).getInseName());
        sb.append("\n");
        sb.append("事故类别：");
        sb.append(mJcbDetailList.get(mJcbDetailPage).getMalName());
        sb.append("\n");
        sb.append("整改建议：");
        sb.append(mJcbDetailList.get(mJcbDetailPage).getRAdvise());
        sb.append("\n");
        mView.setJcbDetail(sb.toString());
    }

    @Override
    public boolean isCommitting() {
        return mCommitSubscription != null && !mCommitSubscription.isUnsubscribed();
    }

    @Override
    public void upload(final AqjcCommitInfo info) {
        if (!MyApplication.isConnection) {
            mView.toast("未联网，已保存！等待下次联网后再提示");
            SqliteOperator.INSTANCE.getCommitInfo(mContext).insertOrReplace(info);
            return;

        }
        mView.pendingDialog(R.string.yhdj_commit_hint);
        // 进行Base64编码
        mCommitSubscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> pSubscriber) {
                try {
                    ArrayList<HashMap<String, Object>> data = UploadDataHelper.uploadAqjc(info);
                    if (data == null || data.isEmpty()) {
                        pSubscriber.onNext("");
                    } else {
                        pSubscriber.onNext(StringUtil.noNull(data.get(0).get("retstr")));
                    }
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
                pSubscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.cancelDialog();
                        mView.commitStatus(false);
                    }

                    @Override
                    public void onNext(String pS) {
                        mView.cancelDialog();
                        mView.commitStatus(TextUtils.isEmpty(pS));
                        if (!TextUtils.isEmpty(pS)) {
                            mView.toast(pS);
                        }
                    }
                });

    }

    @Override
    public void cancel() {
        if (!mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getSssb(final int pSbid,final int comid ) {
        if (!MyApplication.isConnection) {
            mView.toast("此功能需联网！");
            return;
        }
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> pSubscriber) {
                try {
                    String keys2[] = {"cpid", "proclaid", "placeid", "kemid",
                            "comid"};
                    Object values2[] = {pSbid, 0, 0, 0, comid};
                    ArrayList<HashMap<String,Object>> result = WebServiceUtil.getWebServiceMsg(keys2, values2,
                            "getAllEquipment", new String[]{"mroomid"},
                            WebServiceUtil.HUIWEI_URL,WebServiceUtil.HUIWEI_NAMESPACE);
                    if(result.size()>0){
                        String placeId = String.valueOf(result.get(0).get("mroomid"));
                        pSubscriber.onNext(placeId);
                    }else{
                        pSubscriber.onError(new Throwable());
                    }
                } catch (Exception pE) {
                    pE.printStackTrace();
                }
                pSubscriber.onCompleted();

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        mView.cancelDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.cancelDialog();
                        mView.toast(R.string.ewm_failed);
                    }

                    @Override
                    public void onNext(String pS) {
                        mView.setPlaceId(pS);
                    }
                });
    }

    private List<RwInfo> parseRwInfo(List<HashMap<String, Object>> result) {
        List<RwInfo> rwInfoList = new ArrayList<RwInfo>();
        for (HashMap<String, Object> map : result) {
            RwInfo rwInfo = new RwInfo(null, StringUtil.noNull(map.get("TaskID")), StringUtil.noNull(map.get("TaskTitle")), false);
            rwInfoList.add(rwInfo);
        }
        SqliteOperator.INSTANCE.getRwInfoDao(mContext).deleteAll();
        SqliteOperator.INSTANCE.getRwInfoDao(mContext).insertOrReplaceInTx(rwInfoList);
        return rwInfoList;
    }

    private List<CsInfo> parseCsInfo(List<HashMap<String, Object>> result) {
        List<CsInfo> rwInfoList = new ArrayList<CsInfo>();
        for (HashMap<String, Object> map : result) {
            CsInfo rwInfo = new CsInfo(null, StringUtil.noNull(map.get("mplid")), StringUtil.noNull(map.get("mplname")), false);
            rwInfoList.add(rwInfo);
        }
        SqliteOperator.INSTANCE.getCsInfoDao(mContext).deleteAll();
        SqliteOperator.INSTANCE.getCsInfoDao(mContext).insertOrReplaceInTx(rwInfoList);
        return rwInfoList;
    }

    private List<SbInfo> parseSbInfo(List<HashMap<String, Object>> result, String csId) {
        List<SbInfo> rwInfoList = new ArrayList<SbInfo>();
        for (HashMap<String, Object> map : result) {
            String detail = "0".equals(StringUtil.noNull(map.get("cpid"))) ? "" : "(" + StringUtil.noNull(map.get("num")) + ")";
            SbInfo rwInfo = new SbInfo(null, StringUtil.noNull(map.get("cpid")),
                    String.format("%s%s", StringUtil.noNull(map.get("cproname")), detail), csId, false);
            rwInfoList.add(rwInfo);
        }
        SqliteOperator.INSTANCE.getSbInfoDao(mContext).deleteAll();
        SqliteOperator.INSTANCE.getSbInfoDao(mContext).insertOrReplaceInTx(rwInfoList);
        return rwInfoList;
    }

    private List<JcbInfo> parseJcbInfo(List<HashMap<String, Object>> result) {
        List<JcbInfo> rwInfoList = new ArrayList<JcbInfo>();
        for (HashMap<String, Object> map : result) {
            JcbInfo rwInfo = new JcbInfo(null, StringUtil.noNull(map.get("cDocid")), StringUtil.noNull(map.get("cDocTitle")), false);
            rwInfoList.add(rwInfo);
        }
        SqliteOperator.INSTANCE.getJcbInfoDao(mContext).deleteAll();
        SqliteOperator.INSTANCE.getJcbInfoDao(mContext).insertOrReplaceInTx(rwInfoList);
        return rwInfoList;
    }

    private List<JcbDetailInfo> parseJcbDetailInfo(List<HashMap<String, Object>> result, String jcbId) {
        List<JcbDetailInfo> rwInfoList = new ArrayList<JcbDetailInfo>();
        for (HashMap<String, Object> map : result) {
            JcbDetailInfo rwInfo = new JcbDetailInfo();
            rwInfo.setJcb_id(jcbId);
            rwInfo.setOblititle(StringUtil.noNull(map.get("oblititle")));
            rwInfo.setOdetail(StringUtil.noNull(map.get("odetail")));
            rwInfo.setSCheckListtype(StringUtil.noNull(map.get("sCheckListtype")));
            rwInfo.setInduname(StringUtil.noNull(map.get("induname")));
            rwInfo.setInseName(StringUtil.noNull(map.get("inseName")));
            rwInfo.setMalName(StringUtil.noNull(map.get("malName")));
            rwInfo.setRAdvise(StringUtil.noNull(map.get("rAdvise")));
            rwInfo.setIs_choose(false);
            rwInfoList.add(rwInfo);
        }
        JcbDetailInfoDao jcbDetailInfoDao = SqliteOperator.INSTANCE.getJcbDetailInfoDao(mContext);
        QueryBuilder qb = jcbDetailInfoDao.queryBuilder();
        qb.where(JcbDetailInfoDao.Properties.Jcb_id.eq(jcbId));
        if (qb.buildCount().count() == 0) {
            jcbDetailInfoDao.insertOrReplaceInTx(rwInfoList);
        }
        return rwInfoList;
    }
}
