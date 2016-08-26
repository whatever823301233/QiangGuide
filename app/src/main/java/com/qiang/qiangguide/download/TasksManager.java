package com.qiang.qiangguide.download;

import com.liulishuo.filedownloader.model.FileDownloadStatus;


/**
 * Created by Qiang on 2016/8/26.
 */
public class TasksManager {

    /*private final static class HolderClass {
        private final static TasksManager INSTANCE = new TasksManager();
    }

    public static TasksManager getImpl() {
        return HolderClass.INSTANCE;
    }

    private List<Museum> modelList;
    private List<FileDownloadQueueSet> queueSets;

    private SparseArray<FileDownloadQueueSet> taskSparseArray = new SparseArray<>();

    private TasksManager() {
        modelList = DBHandler.getInstance(null).getAllTasks();
        queueSets=new ArrayList<>();
        initDemo();
    }

    private void initDemo() {
        if (modelList.size() <= 0) {
            final int demoSize = Constant.BIG_FILE_URLS.length;
            for (int i = 0; i < demoSize; i++) {
                final String url = Constant.BIG_FILE_URLS[i];
                addTask(url);
            }
        }
    }

    public void addTaskForViewHolder(final BaseDownloadTask task) {
        taskSparseArray.put(task.getId(), task);
    }

    public void removeTaskForViewHolder(final int id) {
        taskSparseArray.remove(id);
    }

    *//*public void updateViewHolder(final int id, final TaskItemViewHolder holder) {
        final BaseDownloadTask task = taskSparseArray.get(id);
        if (task == null) {
            return;
        }

        task.setTag(holder);
    }*//*

    public void releaseTask() {
        taskSparseArray.clear();
    }

    private FileDownloadConnectListener listener;

    public void onCreate(final WeakReference<Activity> activityWeakReference) {
        if (!FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().bindService();
            registerServiceConnectionListener(activityWeakReference);
        }
    }

    private void registerServiceConnectionListener(final WeakReference<Activity>
                                                           activityWeakReference) {
        if (listener != null) {
            FileDownloader.getImpl().removeServiceConnectListener(listener);
        }

        listener = new FileDownloadConnectListener() {

            @Override
            public void connected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }

            @Override
            public void disconnected() {
                if (activityWeakReference == null
                        || activityWeakReference.get() == null) {
                    return;
                }

                activityWeakReference.get().postNotifyDataChanged();
            }
        };

        FileDownloader.getImpl().addServiceConnectListener(listener);
    }

    private void unregisterServiceConnectionListener() {
        FileDownloader.getImpl().removeServiceConnectListener(listener);
        listener = null;
    }

    public void onDestroy() {
        unregisterServiceConnectionListener();
        releaseTask();
    }

    public boolean isReady() {
        return FileDownloader.getImpl().isServiceConnected();
    }

    public Museum get(final int position) {
        return modelList.get(position);
    }

    public Museum getById(final int id) {
        for (Museum model : modelList) {
            if (model.getId() == id) {
                return model;
            }
        }

        return null;
    }

    *//**
     * @param status Download Status
     * @return has already downloaded
     * @see FileDownloadStatus
     *//*
    public boolean isDownloaded(final int status) {
        return status == FileDownloadStatus.completed;
    }

    public int getStatus(final int id, String path) {
        return FileDownloader.getImpl().getStatus(id, path);
    }

    public long getTotal(final int id) {
        return FileDownloader.getImpl().getTotal(id);
    }

    public long getSoFar(final int id) {
        return FileDownloader.getImpl().getSoFar(id);
    }

    public int getTaskCounts() {
        return modelList.size();
    }

    public Museum addTask(final String url) {
        return addTask(url, createPath(url));
    }

    public Museum addTask(final String url, final String path) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) {
            return null;
        }

        final int id = FileDownloadUtils.generateId(url, path);
        Museum model = getById(id);
        if (model != null) {
            return model;
        }
        final Museum newModel = dbHandler.addTask(url, path);
        if (newModel != null) {
            modelList.add(newModel);
        }

        return newModel;
    }

    public String createPath(final String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }

        return FileDownloadUtils.getDefaultSaveFilePath(url);
    }*/
    
}
