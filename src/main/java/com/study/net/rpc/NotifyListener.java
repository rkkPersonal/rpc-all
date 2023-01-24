package com.study.net.rpc;

import java.net.URI;
import java.util.Set;

public interface NotifyListener {
    void notify(Set<URI> uris);
}