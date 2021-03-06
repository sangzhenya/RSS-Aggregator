package com.abitalo.www.rss_aggregator.helper;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.abitalo.www.rss_aggregator.constants.MessageWhat;
import com.abitalo.www.rss_aggregator.entity.RssSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindCallback;

/**
 * Created by sangz on 2016/6/11.
 */
public class RssSourceViewSearchHelper extends Thread {
    private boolean isRunning = true;
    private Handler handler = null;
    private String keyword;
    private Context context;

    public RssSourceViewSearchHelper(Context context, Handler handler, String keyword){
        this.context = context;
        this.handler = handler;
        this.keyword = keyword;
    }

    @Override
    public void run() {
        BmobQuery queryName = new BmobQuery("rss_source");
        BmobQuery queryURL = new BmobQuery("rss_source");
        queryURL.addWhereContains("rssUrl", keyword);
        queryName.addWhereContains("rssName", keyword);

        List<BmobQuery> bmobQueries = new ArrayList<>();
        bmobQueries.add(queryName);
        bmobQueries.add(queryURL);

        BmobQuery query = new BmobQuery("rss_source");
        query.or(bmobQueries);
        query.findObjects(context, new FindCallback() {
            @Override
            public void onSuccess(JSONArray jsonArray) {
                JSONObject jsonObject;
                ArrayList<RssSource> rssSources = new ArrayList<>();
                rssSources.add(new RssSource());
                assert jsonArray != null;
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        jsonObject = jsonArray.getJSONObject(i);
                        RssSource rssSource = new RssSource();
                        rssSource.setId(Integer.parseInt(jsonObject.get("id").toString()));
                        rssSource.setRssName(jsonObject.get("rssName").toString());
                        rssSource.setRssUrl(jsonObject.get("rssUrl").toString());
                        rssSource.setRssIcon(jsonObject.get("rssIcon").toString());
                        rssSources.add(rssSource);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("rssSources", rssSources);
                if (!isRunning){
                    return;
                }
                Message message = new Message();
                message.what = MessageWhat.RSS_SOURCE_LOAD_SUCCESS;
                message.setData(bundle);
                handler.sendMessage(message);

            }
            @Override
            public void onFailure(int arg0, String arg1) {
//                showToast("查询失败:" + arg1);
            }
        });
    }

    public void stopLoad(){
        isRunning = false;
    }
}
