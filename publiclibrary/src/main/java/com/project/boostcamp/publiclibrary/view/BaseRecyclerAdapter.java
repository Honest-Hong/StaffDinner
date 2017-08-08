package com.project.boostcamp.publiclibrary.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.boostcamp.publiclibrary.data.BaseData;
import com.project.boostcamp.publiclibrary.object.BaseVH;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

/**
 * Created by Hong Tae Joon on 2017-08-04.
 */

public class BaseRecyclerAdapter<T extends BaseVH> extends RecyclerView.Adapter<T> {
    private Context context;
    private int layoutId;
    private Class<T> clazz;
    private ArrayList<BaseData> arrayList;

    public BaseRecyclerAdapter(Context context, int layoutId, Class<T> clazz) {
        this.context = context;
        this.layoutId = layoutId;
        this.clazz = clazz;
        this.arrayList = new ArrayList<>();
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            Constructor<T> constructor = clazz.getConstructor(View.class);
            return constructor.newInstance(LayoutInflater.from(context).inflate(layoutId, parent, false));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setData(ArrayList<BaseData> data) {
        this.arrayList = data;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.setupView(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
