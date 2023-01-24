package com.study.net.agent.service;

/**
 * @author Steven
 * @date 2023年01月22日 14:23
 */

public class IphoneServiceImpl implements IphoneService {
    @Override
    public String product(String component) {
        String s = "生产 ：" + component;
        System.out.println(s);
        return "生产 ：" + component;
    }
}
