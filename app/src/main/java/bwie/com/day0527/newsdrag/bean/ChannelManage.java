package bwie.com.day0527.newsdrag.bean;

import android.database.SQLException;
import android.util.Log;

import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bwie.com.day0527.Bean;
import bwie.com.day0527.newsdrag.dao.ChannelDao;
import bwie.com.day0527.newsdrag.db.SQLHelper;


public class ChannelManage {
    public static ChannelManage channelManage;
    /**
     * 默认的用户选择频道列表
     */
    public static List<ChannelItem> defaultUserChannels;
    /**
     * 默认的其他频道列表
     */
    public static List<ChannelItem> defaultOtherChannels;
    static List<String> all = new ArrayList<>();
    private ChannelDao channelDao;
    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    static {
        requestData();
        defaultUserChannels = new ArrayList<ChannelItem>();
        defaultOtherChannels = new ArrayList<ChannelItem>();
    /*    for (int i=1;i<=10;i++){
            defaultUserChannels.add(new ChannelItem(i,all.get(i),i,1));
        }

      for (int i=11;i<=20;i++){
          defaultOtherChannels.add(new ChannelItem(i,all.get(i),i,0));
      }*/
        defaultUserChannels.add(new ChannelItem(1, "QQ", 1, 1));
        defaultUserChannels.add(new ChannelItem(2, "微信", 2, 1));
        defaultUserChannels.add(new ChannelItem(3, "腾讯手机管家—清理垃圾防骚扰", 3, 1));
        defaultUserChannels.add(new ChannelItem(4, "QQ浏览器-WiFi新闻动漫直播", 4, 1));
        defaultUserChannels.add(new ChannelItem(5, "QQ音乐", 5, 1));
        defaultUserChannels.add(new ChannelItem(6, "QQ空间", 6, 1));
        defaultUserChannels.add(new ChannelItem(7, "腾讯新闻", 7, 1));
        defaultUserChannels.add(new ChannelItem(8, "腾讯视频", 1, 1));
        defaultUserChannels.add(new ChannelItem(9, "QQ阅读", 2, 1));
        defaultUserChannels.add(new ChannelItem(10, "腾讯微视", 3, 1));
        defaultOtherChannels.add(new ChannelItem(11, "Q立方桌面", 4, 0));
        defaultOtherChannels.add(new ChannelItem(12, "QQ邮箱", 5, 0));
        defaultOtherChannels.add(new ChannelItem(13, "水印相机", 6, 0));
        defaultOtherChannels.add(new ChannelItem(14, "财付通", 7, 0));
        defaultOtherChannels.add(new ChannelItem(15, "微云", 11, 0));
        defaultOtherChannels.add(new ChannelItem(16, "自选股(腾讯炒股票软件)", 8, 0));
        defaultOtherChannels.add(new ChannelItem(17, "天天酷跑", 9, 0));
        defaultOtherChannels.add(new ChannelItem(18, "天天飞车-乱斗季", 10, 0));
        defaultOtherChannels.add(new ChannelItem(19, "欢乐斗地主（腾讯）", 11, 0));
        defaultOtherChannels.add(new ChannelItem(20, "节奏大师", 11, 0));
    }

    private ChannelManage(SQLHelper paramDBHelper) throws SQLException {
        if (channelDao == null)
            channelDao = new ChannelDao(paramDBHelper.getContext());
        // NavigateItemDao(paramDBHelper.getDao(NavigateItem.class));
        return;
    }

    /**
     * 初始化频道管理类
     *
     * @param paramDBHelper
     * @throws SQLException
     */
    public static ChannelManage getManage(SQLHelper dbHelper) throws SQLException {
        if (channelManage == null)
            channelManage = new ChannelManage(dbHelper);
        return channelManage;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllChannel() {
        channelDao.clearFeedTable();
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<ChannelItem> getUserChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"1"});
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            List<ChannelItem> list = new ArrayList<ChannelItem>();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        initDefaultChannel();
        return defaultUserChannels;
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
     */
    public List<ChannelItem> getOtherChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"0"});
        List<ChannelItem> list = new ArrayList<ChannelItem>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultOtherChannels;
        return (List<ChannelItem>) cacheList;
    }

    /**
     * 保存用户频道到数据库
     *
     * @param userList
     */
    public void saveUserChannel(List<ChannelItem> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ChannelItem channelItem = (ChannelItem) userList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(Integer.valueOf(1));
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存其他频道到数据库
     *
     * @param otherList
     */
    public void saveOtherChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ChannelItem channelItem = (ChannelItem) otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(Integer.valueOf(0));
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 初始化数据库内的频道数据
     */
    private void initDefaultChannel() {
        Log.d("deleteAll", "deleteAll");
        deleteAllChannel();
        saveUserChannel(defaultUserChannels);
        saveOtherChannel(defaultOtherChannels);
    }


    private static void requestData() {


        RequestParams parms = new RequestParams("http://mapp.qzone.qq.com/cgi-bin/mapp/mapp_subcatelist_qq?yyb_cateid=-10&categoryName=%E8%85%BE%E8%AE%AF%E8%BD%AF%E4%BB%B6&pageNo=1&pageSize=20&type=app&platform=touch&network_type=unknown&resolution=412x732");
        x.http().get(parms, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
                StringBuffer buffer = new StringBuffer(result);
                String string = buffer.substring(0, result.length() - 1);

                Gson gson = new Gson();
                Bean bean = gson.fromJson(string, Bean.class);

                List<Bean.AppBean> list = bean.getApp();
                for (int i = 0; i < list.size(); i++) {
                    all.add(list.get(i).getName());
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("fase");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
