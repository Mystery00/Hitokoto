package com.mystery0.hitokoto.test_source;

import com.mystery0.hitokoto.App;
import com.mystery0.hitokoto.class_class.Hitokoto;
import com.mystery0.hitokoto.class_class.HitokotoSource;
import com.mystery0.hitokoto.local.LocalConfigure;
import com.mystery0.tools.MysteryNetFrameWork.HttpUtil;
import com.mystery0.tools.MysteryNetFrameWork.ResponseListener;

public class TestSource
{
    public static void test(HitokotoSource hitokotoSource, final TestSourceListener listener)
    {
        final HttpUtil httpUtil = new HttpUtil(App.getContext());
        if (hitokotoSource.getSource().equals("online"))
        {
            switch (hitokotoSource.getMethod())
            {
                case 1://get
                    httpUtil.setRequestMethod(HttpUtil.RequestMethod.GET);
                    break;
                case 2://post
                    break;
            }
            httpUtil.setUrl(hitokotoSource.getAddress())
                    .setResponseListener(new ResponseListener()
                    {
                        @Override
                        public void onResponse(int i, String s)
                        {
                            if (i == 1)
                            {
                                try
                                {
                                    httpUtil.fromJson(s, Hitokoto.class);
                                    listener.result(true);
                                } catch (Exception e)
                                {
                                    listener.result(false);
                                }
                            } else
                            {
                                listener.result(false);
                            }
                        }
                    })
                    .open();
        } else if (hitokotoSource.getSource().equals("local"))
        {
            if (LocalConfigure.getRandom() == null)
            {
                listener.result(false);
            } else
            {
                listener.result(true);
            }
        } else if (hitokotoSource.getSource().equals("custom"))
        {
            listener.result(true);
        }
    }
}
