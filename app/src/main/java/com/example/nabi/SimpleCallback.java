package com.example.nabi;

//public interface SimpleCallback {
//    void callback(Object data);
//}

// You could do it as well generic, that's what I do in my lib:
public interface SimpleCallback<T> {
    void callback(T data);
}
