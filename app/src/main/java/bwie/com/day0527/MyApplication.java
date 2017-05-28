package bwie.com.day0527;

import android.app.Application;

import org.xutils.DbManager;
import org.xutils.x;

import bwie.com.day0527.newsdrag.db.SQLHelper;

/**
 * Created by $USER_NAME on 2017/5/27.
 */

public class MyApplication extends Application {
    private static MyApplication mAppApplication;
    private SQLHelper sqlHelper;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        mAppApplication = this;
        getDaoConfig();



    }

    public  static DbManager.DaoConfig daoConfig;
    public static DbManager.DaoConfig getDaoConfig(){
        if(daoConfig==null){
            daoConfig=new DbManager.DaoConfig()
                    .setDbVersion(1)
                    .setDbName("tt")//设置数据库的名字
                    .setAllowTransaction(true)
                    .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                        @Override
                        public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                        }
                    });
        }
        return daoConfig;
    }

    /** 获取Application */
    public static MyApplication getApp() {
        return mAppApplication;
    }

    /** 获取数据库Helper */
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(mAppApplication);
        return sqlHelper;
    }

    /** 摧毁应用进程时候调用 */
    public void onTerminate() {
        if (sqlHelper != null)
            sqlHelper.close();
        super.onTerminate();
    }

    public void clearAppCache() {
    }
}
