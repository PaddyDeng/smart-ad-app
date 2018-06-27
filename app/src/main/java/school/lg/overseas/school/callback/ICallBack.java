package school.lg.overseas.school.callback;

public interface ICallBack<T> {
    void onSuccess(T t);

    void onFail();
}
