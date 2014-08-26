package com.newthread.android.clock;

public class TimeTask {
    private String time = "";
    private String taskName = "";
    private String requestCode = "";
    private String type; //只能是 activity ,service , broadcast
    private String repetTimeMills;

    public void setRepetTimeMills(String repetTimeMills) {
        this.repetTimeMills = repetTimeMills;
    }

    public String getRepetTimeMills() {
        return repetTimeMills;
    }

    public void setRepetTime(String repetTimeMills) {
        this.repetTimeMills = repetTimeMills;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(String requestCode) {
        this.requestCode = requestCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * @param time     example:201404301420
     * @param taskName example:test.class 注意一定要是Intent能启动的任务 必须要到
     *                 TimeTaskMeta和manifest里面注册  如:Activity Service Broadcast
     */
    public TimeTask(String time, String taskName, String type) {
        this.time = time;
        this.taskName = taskName;
        this.type = type;
        this.repetTimeMills = null;
    }

    public TimeTask(String time, String taskName, String type, String repetTimeMills) {
        this.time = time;
        this.taskName = taskName;
        this.type = type;
        this.repetTimeMills = repetTimeMills;
    }

    /**
     * 默认activity型
     *
     * @param time
     * @param taskName
     */
    public TimeTask(String time, String taskName) {
        this(time, taskName, "activity");
    }

    /**
     * 一定要有默认构造函数
     */
    public TimeTask() {

    }
}
