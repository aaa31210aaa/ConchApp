package utils;


import java.io.Serializable;

public class CustomResponse<T> implements Serializable {
    private static final long serialVersionUID = 5213230387175987834L;
    public String success;
    public int code;
    public String msg;
    public T data;

}
