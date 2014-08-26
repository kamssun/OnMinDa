package com.newthread.android.manager;

/**
 * Created by Administrator on 2014/8/26.
 */
public class LibraryBookRemindManger implements IRemindService {
    private LibraryBookRemindManger() {
    }


    private static class LibraryBookRemindMangerHolder {
        private static LibraryBookRemindManger instance = new LibraryBookRemindManger();
    }

    public LibraryBookRemindManger getInstance() {
        return LibraryBookRemindMangerHolder.instance;
    }

    @Override
    public void init() {

    }

    @Override
    public void openAllRemind() {

    }

    @Override
    public void closeALlRemind() {

    }

    @Override
    public void openSeletedRemind() {

    }

    @Override
    public void closeSeletedRemind() {

    }
}
