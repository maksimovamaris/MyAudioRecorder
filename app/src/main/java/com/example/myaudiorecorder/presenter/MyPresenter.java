package com.example.myaudiorecorder.presenter;


import com.example.myaudiorecorder.model.FileRepository;

public class MyPresenter {

    private FileRepository myRepository;
    private MyView mView;

    public MyPresenter(MyView view) {
        myRepository = new FileRepository();
        mView = view;

    }

    public void updateList() {
        myRepository.updateListOfFiles();
        mView.onUpdate();
    }


    public interface MyView {
        void onUpdate();
    }

}
