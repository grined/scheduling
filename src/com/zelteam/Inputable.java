package com.zelteam;

import com.zelteam.model.Resource;
import com.zelteam.model.Site;
import com.zelteam.model.Task;

import java.util.List;

public interface Inputable {
    List<Site> getSites();

    List<Resource> getResources();

    List<Task> getTasks();
}
