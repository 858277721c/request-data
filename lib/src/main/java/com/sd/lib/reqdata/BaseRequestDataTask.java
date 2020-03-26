package com.sd.lib.reqdata;

public abstract class BaseRequestDataTask<T> implements RequestDataTask<T>
{
    private volatile State mState = State.None;
    private OnStateChangeCallback mOnStateChangeCallback;
    private ExecuteCallback<T> mRealExecuteCallback;

    @Override
    public final State getState()
    {
        return mState;
    }

    @Override
    public synchronized final void setOnStateChangeCallback(OnStateChangeCallback callback)
    {
        mOnStateChangeCallback = callback;
    }

    /**
     * 设置状态
     *
     * @param state
     */
    protected synchronized final void setState(State state)
    {
        if (state == null)
            throw new IllegalArgumentException("state is null");

        final State old = mState;
        if (old != state)
        {
            mState = state;
            onStateChanged(old, mState);

            if (mOnStateChangeCallback != null)
                mOnStateChangeCallback.onStateChanged(old, mState);
        }
    }

    protected void onStateChanged(State oldState, State newState)
    {
    }

    protected final ExecuteCallback<T> getExecuteCallback()
    {
        return mInternalExecuteCallback;
    }

    private final ExecuteCallback<T> mInternalExecuteCallback = new ExecuteCallback<T>()
    {
        @Override
        public void onSuccess(T data)
        {
            synchronized (BaseRequestDataTask.this)
            {
                setState(State.Success);
                if (mRealExecuteCallback != null)
                    mRealExecuteCallback.onSuccess(data);
            }
        }

        @Override
        public void onError(int code, String desc)
        {
            synchronized (BaseRequestDataTask.this)
            {
                setState(State.Error);
                if (mRealExecuteCallback != null)
                    mRealExecuteCallback.onError(code, desc);
            }
        }
    };

    @Override
    public synchronized void execute(ExecuteCallback<T> callback)
    {
        mRealExecuteCallback = callback;
        executeImpl();
    }

    @Override
    public synchronized final void cancel()
    {
        mRealExecuteCallback = null;
        cancelImpl();
    }

    protected abstract void executeImpl();

    protected abstract void cancelImpl();
}
