package ua.od.radio.pozitivefm.data.callback;

public interface DataCallback<T> {
    void onEmit(T data);

    void onCompleted();

    void onError(Throwable throwable);
}
