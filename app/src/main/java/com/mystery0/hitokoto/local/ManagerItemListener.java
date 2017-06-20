package com.mystery0.hitokoto.local;

import com.mystery0.hitokoto.class_class.HitokotoGroup;


public interface ManagerItemListener
{
	void onItemClick(HitokotoGroup hitokotoGroup, int position);

	void onItemSelect(HitokotoGroup hitokotoGroup, int position, boolean checked);
}
