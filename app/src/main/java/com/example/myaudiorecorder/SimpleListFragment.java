package com.example.myaudiorecorder;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import androidx.fragment.app.ListFragment;

import com.example.myaudiorecorder.model.FileRepository;
import com.example.myaudiorecorder.presenter.MyPresenter;

public class SimpleListFragment extends ListFragment {

    private FileRepository mRep;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setListAdapter(setAdapter(mRep));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
//        myPresenter=new MyPresenter();
        setListAdapter(setAdapter(mRep));
//adapter.notifyDataSetChanged();
        return view;

    }

    public ArrayAdapter setAdapter(FileRepository mRep) {
        mRep = new FileRepository();
        mRep.updateListOfFiles();
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                R.layout.list_item, R.id.file_name, mRep.getListOfFiles());
        return adapter;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }


}
//}
//        ListAdapter adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1, listOfString.getListOfNames());


//     bindList(this.getListView(),new FileRepository());

//        setListAdapter(adapter);
//        return adapter;