package com.study.net.rpc.serialization;

// 接口
public interface Serialization {

    byte[] serialize(Object output) throws Exception;

    Object deserialize(byte[] input, Class clazz) throws Exception;
}