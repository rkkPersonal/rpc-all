package com.study.net.rpc.util;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author Steven
 * @date 2023年01月22日 17:06
 */
public class SpiUtil {

    public static <T> T getInstance(String name, Class<T> objClass) {
        ServiceLoader<?> load = ServiceLoader.load(objClass, Thread.currentThread().getContextClassLoader());
        Iterator<?> iterator = load.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next.getClass().getName().equalsIgnoreCase(name)) {
                return (T) next;
            }
        }
        return null;
    }
}
