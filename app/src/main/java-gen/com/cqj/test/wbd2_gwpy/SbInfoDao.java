package com.cqj.test.wbd2_gwpy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.cqj.test.wbd2_gwpy.SbInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SB_INFO.
*/
public class SbInfoDao extends AbstractDao<SbInfo, Long> {

    public static final String TABLENAME = "SB_INFO";

    /**
     * Properties of entity SbInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Sbid = new Property(1, String.class, "sbid", false, "SBID");
        public final static Property Sbname = new Property(2, String.class, "sbname", false, "SBNAME");
        public final static Property Csid = new Property(3, String.class, "csid", false, "CSID");
        public final static Property Is_choose = new Property(4, Boolean.class, "is_choose", false, "IS_CHOOSE");
    };


    public SbInfoDao(DaoConfig config) {
        super(config);
    }
    
    public SbInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SB_INFO' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'SBID' TEXT," + // 1: sbid
                "'SBNAME' TEXT," + // 2: sbname
                "'CSID' TEXT," + // 3: csid
                "'IS_CHOOSE' INTEGER);"); // 4: is_choose
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SB_INFO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SbInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String sbid = entity.getSbid();
        if (sbid != null) {
            stmt.bindString(2, sbid);
        }
 
        String sbname = entity.getSbname();
        if (sbname != null) {
            stmt.bindString(3, sbname);
        }
 
        String csid = entity.getCsid();
        if (csid != null) {
            stmt.bindString(4, csid);
        }
 
        Boolean is_choose = entity.getIs_choose();
        if (is_choose != null) {
            stmt.bindLong(5, is_choose ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SbInfo readEntity(Cursor cursor, int offset) {
        SbInfo entity = new SbInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sbid
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sbname
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // csid
            cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0 // is_choose
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SbInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSbid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSbname(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCsid(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setIs_choose(cursor.isNull(offset + 4) ? null : cursor.getShort(offset + 4) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SbInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SbInfo entity) {
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
