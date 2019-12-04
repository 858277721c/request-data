package com.sd.lib.reqdata;

public abstract class BaseRequestDataTask<T> implements RequestDataTask<T>
{
    private State mState = State.None;
    private OnStateChangeCallback mOnStateChangeCallback;

    @Override
    public final State getState()
    {
        return mState;
    }

    @Override
    public final void setOnStateChangeCallback(OnStateChangeCallback callback)
    {
        mOnStateChangeCallback = callback;
    }

    /**
     * 设置状态
     *
     * @param state
     */
    protected final void setState(State state)
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

    @Override
    public void execute(ExecuteCallback<T> callback)
    {
        executeImpl(callback);
    }

    protected abstract void executeImpl(ExecuteCallback<T> callback);
}
