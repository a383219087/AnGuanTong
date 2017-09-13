package com.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoCreateEntityManager {

    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(5, "com.cqj.test.wbd2_gwpy");
//      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
//      Schema schema = new Schema(1, "me.itangqi.bean");
//      schema.setDefaultJavaPackageDao("me.itangqi.dao");

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();

        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        addUserInfo(schema);
        createComInfo(schema);
        createRwInfo(schema);
        createCsInfo(schema);
        createSbInfo(schema);
        createJcbInfo(schema);
        createJcbDetailInfo(schema);
        createAqjcCommitInfo(schema);
        createSbxcInfo(schema);
        createSbxcListInfo(schema);
        createYhfcInfo(schema);
        createYhfcCommitInfo(schema);
        createYhzgCommitInfo(schema);
        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
        new DaoGenerator().generateAll(schema, "./app/src/main/java-gen");
    }

    /**
     * @param schema
     */
    private static void addUserInfo(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity userInfo = schema.addEntity("UserInfo");
        // 你也可以重新给表命名
        // userInfo.setTableName("NODE");

        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        userInfo.addIdProperty();
        userInfo.addIntProperty("uid").notNull();
        userInfo.addStringProperty("login_name").notNull();
        userInfo.addStringProperty("login_pwd");
        userInfo.addStringProperty("name");
        userInfo.addStringProperty("nick_name");
        userInfo.addStringProperty("phone_number");
        userInfo.addStringProperty("xuliehao");
        userInfo.addStringProperty("tongdao");
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        userInfo.addBooleanProperty("is_login");
        userInfo.addDateProperty("date");
    }

    private static void createComInfo(Schema schema) {
        /**
         * private String orgidStr;
         private String orgid;
         private String comname;
         private String comFullName;
         private String Emid;
         private String comId;
         */
        Entity comInfo = schema.addEntity("CompanyInfo");
        comInfo.addIdProperty();
        comInfo.addStringProperty("com_id");
        comInfo.addStringProperty("emid");
        comInfo.addStringProperty("com_fullname");
        comInfo.addStringProperty("com_name");
        comInfo.addStringProperty("org_id");
        comInfo.addStringProperty("org_idstr");
        comInfo.addBooleanProperty("is_choose");
    }

    private static void createRwInfo(Schema schema) {
        Entity rwInfo = schema.addEntity("RwInfo");
        rwInfo.addIdProperty().primaryKey().autoincrement();
        rwInfo.addStringProperty("partid");
        rwInfo.addStringProperty("partname");
        rwInfo.addBooleanProperty("is_choose");
    }

    private static void createCsInfo(Schema schema) {
        Entity rwInfo = schema.addEntity("CsInfo");
        rwInfo.addIdProperty().primaryKey().autoincrement();
        rwInfo.addStringProperty("mplid");
        rwInfo.addStringProperty("mplname");
        rwInfo.addBooleanProperty("is_choose");
    }

    private static void createSbInfo(Schema schema) {
        Entity rwInfo = schema.addEntity("SbInfo");
        rwInfo.addIdProperty().primaryKey().autoincrement();
        rwInfo.addStringProperty("sbid");
        rwInfo.addStringProperty("sbname");
        rwInfo.addStringProperty("csid");
        rwInfo.addBooleanProperty("is_choose");
    }

    private static void createJcbInfo(Schema schema) {
        Entity rwInfo = schema.addEntity("JcbInfo");
        rwInfo.addIdProperty().primaryKey().autoincrement();
        rwInfo.addStringProperty("jcbid");
        rwInfo.addStringProperty("jcbname");
        rwInfo.addBooleanProperty("is_choose");
    }

    private static void createJcbDetailInfo(Schema pSchema) {
        Entity detail = pSchema.addEntity("JcbDetailInfo");
        detail.addIdProperty().primaryKey().autoincrement();
        /**
         * "oblititle",
         "odetail", "sCheckListtype", "induname",
         "inseName", "malName", "rAdvise"
         */
        detail.addStringProperty("jcb_id");
        detail.addStringProperty("oblititle");
        detail.addStringProperty("odetail");
        detail.addStringProperty("sCheckListtype");
        detail.addStringProperty("induname");
        detail.addStringProperty("inseName");
        detail.addStringProperty("malName");
        detail.addStringProperty("rAdvise");
        detail.addBooleanProperty("is_choose");
    }

    private static void createAqjcCommitInfo(Schema pSchema) {
        /**
         * "checkDate", "recEmid", "dLimit",
         "hDetail", "dScheme", "hGrade", "lEmid",
         "dCost", "imgPatch", "objOrganizationID",
         "Taskid", "FliedID", "objPartid", "SetStr",
         "retstr"
         */
        Entity info = pSchema.addEntity("AqjcCommitInfo");
        info.addIdProperty().primaryKey().autoincrement();
        info.addStringProperty("imagePath");
        info.addStringProperty("checkDate");
        info.addIntProperty("recEmid");
        info.addStringProperty("dLimit");
        info.addStringProperty("hDetail");
        info.addStringProperty("dScheme");
        info.addStringProperty("hGrade");
        info.addIntProperty("lEmid");
        info.addFloatProperty("dCost");
        info.addIntProperty("objOrganizationID");
        info.addIntProperty("Taskid");
        info.addStringProperty("TaskName");
        info.addStringProperty("CsName");
        info.addIntProperty("FliedID");
        info.addIntProperty("objPartid");
        info.addStringProperty("SetStr");
    }

    private static void createSbxcInfo(Schema pSchema) {
/**
 * "CPID", "cDate", "cEmid", "rEmid",
 "cRemark", "cState"
 */
        Entity info = pSchema.addEntity("SbjcCommitInfo");
        info.addIdProperty().primaryKey().autoincrement();
        info.addIntProperty("CPID");
        info.addStringProperty("cpName");
        info.addStringProperty("cDate");
        info.addIntProperty("cEmid");
        info.addIntProperty("rEmid");
        info.addStringProperty("cRemark");
        info.addStringProperty("cState");
        info.addDateProperty("checkDate");
    }

    private static void createSbxcListInfo(Schema pSchema) {
/**
 * "prodname",
 "sccheckname", "sccheckdate",
 "sccheckstat", "sccheckdetail"
 */
        Entity info = pSchema.addEntity("SbjcListInfo");
        info.addIdProperty().primaryKey().autoincrement();
        info.addIntProperty("cpid");
        info.addStringProperty("prodname");
        info.addStringProperty("sccheckname");
        info.addStringProperty("sccheckdate");
        info.addStringProperty("sccheckstat");
        info.addStringProperty("sccheckdetail");
    }

    private static void createYhfcInfo(Schema pSchema) {
        /**
         * [hTroubleID]隐患ID,[ checkDate]检查日期,[ limitDate]整改时限,
         * [ troubleGrade]隐患等级,[ checkObject]检查对歇象单位名称,
         * [ finishDate]整改完成日期,[ safetyTrouble]隐患描述,
         * [ actionOrgName]执行检查的单位 [ esCost]预估费用,
         * [ dightCost]实际整改费用,[ LiabelEmid]责任人雇员ID ,
         * [ LiabelName]整改责任人姓名,[ areaName]区划名称,[ induName]行业名称,[reviewDate]复查日期
         */
        Entity info = pSchema.addEntity("YhfcInfo");
        info.addIdProperty().primaryKey().autoincrement();
        info.addStringProperty("hTroubleID");
        info.addBooleanProperty("isYhzg");
        info.addStringProperty("checkDate");
        info.addStringProperty("limitDate");
        info.addStringProperty("troubleGrade");
        info.addStringProperty("checkObject");
        info.addStringProperty("finishDate");
        info.addStringProperty("safetyTrouble");
        info.addStringProperty("actionOrgName");
        info.addStringProperty("esCost");
        info.addStringProperty("dightCost");
        info.addStringProperty("LiabelEmid");
        info.addStringProperty("LiabelName");
        info.addStringProperty("areaName");
        info.addStringProperty("induName");
        info.addStringProperty("reviewDate");
    }

    private static void createYhzgInfo(Schema pSchema) {
        /**
         * [hTroubleID]隐患ID,[ checkDate]检查日期,[ limitDate]整改时限,
         * [ troubleGrade]隐患等级,[ checkObject]检查对歇象单位名称,
         * [ finishDate]整改完成日期,[ safetyTrouble]隐患描述,
         * [ actionOrgName]执行检查的单位 [ esCost]预估费用,
         * [ dightCost]实际整改费用,[ LiabelEmid]责任人雇员ID ,
         * [ LiabelName]整改责任人姓名,[ areaName]区划名称,[ induName]行业名称,[reviewDate]复查日期
         */
        Entity info = pSchema.addEntity("YhzgInfo");
        info.addIdProperty().primaryKey().autoincrement();
        info.addStringProperty("hTroubleID");
        info.addStringProperty("checkDate");
        info.addStringProperty("limitDate");
        info.addStringProperty("troubleGrade");
        info.addStringProperty("checkObject");
        info.addStringProperty("finishDate");
        info.addStringProperty("safetyTrouble");
        info.addStringProperty("actionOrgName");
        info.addStringProperty("esCost");
        info.addStringProperty("dightCost");
        info.addStringProperty("LiabelEmid");
        info.addStringProperty("LiabelName");
        info.addStringProperty("areaName");
        info.addStringProperty("induName");
        info.addStringProperty("reviewDate");
    }

    private static void createYhfcCommitInfo(Schema pSchema) {
        Entity info = pSchema.addEntity("YhfcCommitInfo");
        info.addIdProperty().primaryKey().autoincrement();
        info.addIntProperty("troubleid");
        info.addStringProperty("troubleName");
        info.addStringProperty("reviewEmids");
        info.addStringProperty("reviewRemark");
        info.addStringProperty("reviewDate");
        info.addStringProperty("dightedImgPath");
    }

    private static void createYhzgCommitInfo(Schema pSchema) {
        /**
         * troubleid
         finishedDate
         results
         evalstr
         factCost
         Imgpath
         */
        Entity info = pSchema.addEntity("YhzgCommitInfo");
        info.addIdProperty().primaryKey().autoincrement();
        info.addIntProperty("troubleid");
        info.addStringProperty("troubleName");
        info.addStringProperty("finishedDate");
        info.addStringProperty("results");
        info.addStringProperty("evalstr");
        info.addStringProperty("factCost");
        info.addStringProperty("Imgpath");
    }

}
