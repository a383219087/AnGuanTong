package com.cqj.test.wbd2_gwpy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.cqj.test.wbd2_gwpy.AqjcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.CompanyInfoDao;
import com.cqj.test.wbd2_gwpy.CsInfoDao;
import com.cqj.test.wbd2_gwpy.DaoMaster;
import com.cqj.test.wbd2_gwpy.DaoSession;
import com.cqj.test.wbd2_gwpy.JcbDetailInfoDao;
import com.cqj.test.wbd2_gwpy.JcbInfoDao;
import com.cqj.test.wbd2_gwpy.RwInfoDao;
import com.cqj.test.wbd2_gwpy.SbInfoDao;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfo;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.SbjcListInfoDao;
import com.cqj.test.wbd2_gwpy.UserInfoDao;
import com.cqj.test.wbd2_gwpy.YhfcCommitInfo;
import com.cqj.test.wbd2_gwpy.YhfcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.YhfcInfo;
import com.cqj.test.wbd2_gwpy.YhfcInfoDao;
import com.cqj.test.wbd2_gwpy.YhzgCommitInfoDao;
import com.cqj.test.wbd2_gwpy.activity.MyApplication;

/**
 *
 * Created by Administrator on 2016/3/7.
 */
public enum  SqliteOperator {
    INSTANCE;

    private UserInfoDao mUserInfoDao;
    private CompanyInfoDao mCompanyInfoDao;
    private RwInfoDao mRwInfoDao;
    private CsInfoDao mCsInfoDao;
    private JcbInfoDao mJcbInfoDao;
    private JcbDetailInfoDao mJcbDetailInfoDao;
    private SbInfoDao mSbInfoDao;
    private AqjcCommitInfoDao mCommitInfoDao;
    private SbjcCommitInfoDao mSbCommitDao;
    private SbjcListInfoDao mSbjcListInfoDao;
    private YhfcInfoDao mYhfcInfoDao;
    private YhfcCommitInfoDao mYhfcCommitInfoDao;
    private YhzgCommitInfoDao mYhzgCommitInfoDao;

    public SbjcListInfoDao getSbjcListInfoDao(Context context){
        if(mSbjcListInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mSbjcListInfoDao =sDaoSession.getSbjcListInfoDao();
        }
        return mSbjcListInfoDao;
    }

    public SbjcCommitInfoDao getSbCommitDao(Context context){
        if(mSbCommitDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mSbCommitDao =sDaoSession.getSbjcCommitInfoDao();
        }
        return mSbCommitDao;
    }

    public UserInfoDao getUserInfoDao(Context context){
        if(mUserInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mUserInfoDao =sDaoSession.getUserInfoDao();
        }
        return mUserInfoDao;
    }

    public CompanyInfoDao getCompanyInfoDao(Context context){
        if(mCompanyInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mCompanyInfoDao =sDaoSession.getCompanyInfoDao();
        }
        return mCompanyInfoDao;
    }

    public RwInfoDao getRwInfoDao(Context context){
        if(mRwInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mRwInfoDao =sDaoSession.getRwInfoDao();
        }
        return mRwInfoDao;
    }

    public CsInfoDao getCsInfoDao(Context context){
        if(mCsInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mCsInfoDao =sDaoSession.getCsInfoDao();
        }
        return mCsInfoDao;
    }

    public SbInfoDao getSbInfoDao(Context context){
        if(mSbInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mSbInfoDao =sDaoSession.getSbInfoDao();
        }
        return mSbInfoDao;
    }

    public JcbInfoDao getJcbInfoDao(Context context){
        if(mJcbInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mJcbInfoDao =sDaoSession.getJcbInfoDao();
        }
        return mJcbInfoDao;
    }

    public JcbDetailInfoDao getJcbDetailInfoDao(Context context){
        if(mJcbDetailInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mJcbDetailInfoDao =sDaoSession.getJcbDetailInfoDao();
        }
        return mJcbDetailInfoDao;
    }

    public YhfcInfoDao getYhfcInfoDao(Context context){
        if(mYhfcInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mYhfcInfoDao =sDaoSession.getYhfcInfoDao();
        }
        return mYhfcInfoDao;
    }

    public AqjcCommitInfoDao getCommitInfo(Context context){
        if(mCommitInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mCommitInfoDao =sDaoSession.getAqjcCommitInfoDao();
        }
        return mCommitInfoDao;
    }

    public YhfcCommitInfoDao getYhfcCommitInfo(Context context){
        if(mYhfcCommitInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mYhfcCommitInfoDao =sDaoSession.getYhfcCommitInfoDao();
        }
        return mYhfcCommitInfoDao;
    }

    public YhzgCommitInfoDao getYhzgCommitInfo(Context context){
        if(mYhzgCommitInfoDao ==null){
            DaoMaster.DevOpenHelper helper =new DaoMaster.DevOpenHelper(context.getApplicationContext(),MyApplication.OFFLINE_DB,null);
            SQLiteDatabase sqLiteDatabase =helper.getWritableDatabase();
            DaoMaster daoMaster =new DaoMaster(sqLiteDatabase);
            DaoSession sDaoSession = daoMaster.newSession();
            mYhzgCommitInfoDao =sDaoSession.getYhzgCommitInfoDao();
        }
        return mYhzgCommitInfoDao;
    }


}
