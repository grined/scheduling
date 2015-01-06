package com.zelteam.model;

import java.util.List;

public class Site {
    private final List<Resource> resources;

    public Site(List<Resource> resources) {
        this.resources = resources;
    }

    public List<Resource> getResources() {
        return resources;
    }
}
