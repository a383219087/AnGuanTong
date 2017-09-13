package com.cqj.test.wbd2_gwpy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.cqj.test.wbd2_gwpy.SbjcCommitInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SBJC_COMMIT_INFO.
*/
public class SbjcCommitInfoDao extends AbstractDao<SbjcCommitInfo, Long> {

    public static final String TABLENAME = "SBJC_COMMIT_INFO";

    /**
     * Properties of entity SbjcCommitInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property CPID = new Property(1, Integer.class, "CPID", false, "CPID");
        public final static Property CpName = new Property(2, String.class, "cpName", false, "CP_NAME");
        public final static Property CDate = new Property(3, String.class, "cDate", false, "C_DATE");
        public final static Property CEmid = new Property(4, Integer.class, "cEmid", false, "C_EMID");
        public final static Property REmid = new Property(5, Integer.class, "rEmid", false, "R_EMID");
        public final static Property CRemark = new Property(6, String.class, "cRemark", false, "C_REMARK");
        public final static Property CState = new Property(7, String.class, "cState", false, "C_STATE");
        public final static Property CheckDate = new Property(8, java.util.Date.class, "checkDate", false, "CHECK_DATE");
    };


    public SbjcCommitInfoDao(DaoConfig config) {
        super(config);
    }
    
    public SbjcCommitInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SBJC_COMMIT_INFO' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'CPID' INTEGER," + // 1: CPID
                "'CP_NAME' TEXT," + // 2: cpName
                "'C_DATE' TEXT," + // 3: cDate
                "'C_EMID' INTEGER," + // 4: cEmid
                "'R_EMID' INTEGER," + // 5: rEmid
                "'C_REMARK' TEXT," + // 6: cRemark
                "'C_STATE' TEXT," + // 7: cState
                "'CHECK_DATE' INTEGER);"); // 8: checkDate
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SBJC_COMMIT_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SbjcCommitInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer CPID = entity.getCPID();
        if (CPID != null) {
            stmt.bindLong(2, CPID);
        }
 
        String cpName = entity.getCpName();
        if (cpName != null) {
            stmt.bindString(3, cpName);
        }
 
        String cDate = entity.getCDate();
        if (cDate != null) {
            stmt.bindString(4, cDate);
        }
 
        Integer cEmid = entity.getCEmid();
        if (cEmid != null) {
            stmt.bindLong(5, cEmid);
        }
 
        Integer rEmid = entity.getREmid();
        if (rEmid != null) {
            stmt.bindLong(6, rEmid);
        }
 
        String cRemark = entity.getCRemark();
        if (cRemark != null) {
            stmt.bindString(7, cRemark);
        }
 
        String cState = entity.getCState();
        if (cState != null) {
            stmt.bindString(8, cState);
        }
 
        java.util.Date checkDate = entity.getCheckDate();
        if (checkDate != null) {
            stmt.bindLong(9, checkDate.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SbjcCommitInfo readEntity(Cursor cursor, int offset) {
        SbjcCommitInfo entity = new SbjcCommitInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1), // CPID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // cpName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // cDate
            cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4), // cEmid
            cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5), // rEmid
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // cRemark
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // cState
            cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)) // checkDate
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SbjcCommitInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCPID(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setCpName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCDate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCEmid(cursor.isNull(offset + 4) ? null : cursor.getInt(offset + 4));
        entity.setREmid(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setCRemark(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCState(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setCheckDate(cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SbjcCommitInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SbjcCommitInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
