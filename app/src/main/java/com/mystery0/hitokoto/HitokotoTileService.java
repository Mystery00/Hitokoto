package com.mystery0.hitokoto;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

import com.mystery0.hitokoto.widget.WidgetConfigure;
import com.mystery0.hitokoto.widget.WidgetService;
import com.mystery0.tools.Logs.Logs;

@TargetApi(Build.VERSION_CODES.N)
public class HitokotoTileService extends TileService
{
    private static final String TAG = "HitokotoTileService";

    @Override
    public void onTileAdded()
    {
        Logs.i(TAG, "onTileAdded: ");
        Tile tile = getQsTile();
        if (!WidgetConfigure.getEnable())
        {
            tile.setState(Tile.STATE_UNAVAILABLE);
        }
        if (WidgetConfigure.getAutoRefresh())
        {
            tile.setState(Tile.STATE_ACTIVE);
        } else
        {
            tile.setState(Tile.STATE_INACTIVE);
        }
        tile.updateTile();
    }

    @Override
    public void onStartListening()
    {
        Logs.i(TAG, "onStartListening: ");
        Tile tile = getQsTile();
        if (!WidgetConfigure.getEnable())
        {
            tile.setState(Tile.STATE_UNAVAILABLE);
        }
        if (WidgetConfigure.getAutoRefresh())
        {
            tile.setState(Tile.STATE_ACTIVE);
        } else
        {
            tile.setState(Tile.STATE_INACTIVE);
        }

        tile.updateTile();
    }

    @Override
    public void onClick()
    {
        Logs.i(TAG, "onClick: ");
        Tile tile = getQsTile();
        if (tile.getState() == Tile.STATE_ACTIVE)
        {
            tile.setState(Tile.STATE_INACTIVE);
            WidgetConfigure.setAutoRefresh(false);
            stopService(new Intent(App.getContext(), WidgetService.class));
        } else
        {
            tile.setState(Tile.STATE_ACTIVE);
            WidgetConfigure.setAutoRefresh(true);
            startService(new Intent(App.getContext(), WidgetService.class));
        }

        tile.updateTile();
    }
}