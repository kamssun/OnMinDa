package com.newthread.android.clock;


public enum TimeTaskMeta {
    test(Test.class),
    testService(TestService.class),
    couserBroadcast("com.newthread.android.action.CourserRemind"),
    libraryBookRunOut("com.newthread.android.action.LibraryBookRunOut");
    private Class<?> task;
    private String action;

    private TimeTaskMeta(Class<?> activity) {
        this.task = activity;
    }

    private TimeTaskMeta(String action) {
        this.action = action;
    }

    public Class<?> getStartClass() {
        return task;
    }

    public String getAction() {
        return action;
    }
}
