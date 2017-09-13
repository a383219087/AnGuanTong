package com.cqj.test.wbd2_gwpy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

import com.cqj.test.wbd2_gwpy.UserInfoDao;
import com.cqj.test.wbd2_gwpy.CompanyInfoDao;
import com.cqj.test.wbd2_gwpy.RwInfoDao;
import com.cqj.test.wbd2_gwpy.CsInfoDao;
import com.cqj.test.wbd2_gwpy.SbInfoDao;
import com.cqj.test.wbd2_gwpy.JcbInfoDao;
import com.cqj.test.wbd2_gwpy.JcbDetailInfoDao;
import com.cqj.test.wbd2_gwpy.AqjcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.SbjcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.SbjcListInfoDao;
import com.cqj.test.wbd2_gwpy.YhfcInfoDao;
import com.cqj.test.wbd2_gwpy.YhfcCommitInfoDao;
import com.cqj.test.wbd2_gwpy.YhzgCommitInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * Master of DAO (schema version 5): knows all DAOs.
*/
public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 5;

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        UserInfoDao.createTable(db, ifNotExists);
        CompanyInfoDao.createTable(db, ifNotExists);
        RwInfoDao.createTable(db, ifNotExists);
        CsInfoDao.createTable(db, ifNotExists);
        SbInfoDao.createTable(db, ifNotExists);
        JcbInfoDao.createTable(db, ifNotExists);
        JcbDetailInfoDao.createTable(db, ifNotExists);
        AqjcCommitInfoDao.createTable(db, ifNotExists);
        SbjcCommitInfoDao.createTable(db, ifNotExists);
        SbjcListInfoDao.createTable(db, ifNotExists);
        YhfcInfoDao.createTable(db, ifNotExists);
        YhfcCommitInfoDao.createTable(db, ifNotExists);
        YhzgCommitInfoDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        UserInfoDao.dropTable(db, ifExists);
        CompanyInfoDao.dropTable(db, ifExists);
        RwInfoDao.dropTable(db, ifExists);
        CsInfoDao.dropTable(db, ifExists);
        SbInfoDao.dropTable(db, ifExists);
        JcbInfoDao.dropTable(db, ifExists);
        JcbDetailInfoDao.dropTable(db, ifExists);
        AqjcCommitInfoDao.dropTable(db, ifExists);
        SbjcCommitInfoDao.dropTable(db, ifExists);
        SbjcListInfoDao.dropTable(db, ifExists);
        YhfcInfoDao.dropTable(db, ifExists);
        YhfcCommitInfoDao.dropTable(db, ifExists);
        YhzgCommitInfoDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    /** WARNING: Drops all table on Upgrade! Use only during development. */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(UserInfoDao.class);
        registerDaoClass(CompanyInfoDao.class);
        registerDaoClass(RwInfoDao.class);
        registerDaoClass(CsInfoDao.class);
        registerDaoClass(SbInfoDao.class);
        registerDaoClass(JcbInfoDao.class);
        registerDaoClass(JcbDetailInfoDao.class);
        registerDaoClass(AqjcCommitInfoDao.class);
        registerDaoClass(SbjcCommitInfoDao.class);
        registerDaoClass(SbjcListInfoDao.class);
        registerDaoClass(YhfcInfoDao.class);
        registerDaoClass(YhfcCommitInfoDao.class);
        registerDaoClass(YhzgCommitInfoDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
