package com.sd.lib.reqdata;

public interface RequestDataTask<T>
{
    State getState();

    void setOnStateChangeCallback(OnStateChangeCallback callback);

    void execute(ExecuteCallback<T> callback);

    void cancel();

    interface ExecuteCallback<T>
    {
        void onSuccess(T data);

        void onError(int code, String desc);
    }

    /**
     * 状态变化回调
     */
    interface OnStateChangeCallback
    {
        void onStateChanged(State oldState, State newState);
    }

    enum State
    {
        None,
        Executing,
        Success,
        Error,
    }
}
