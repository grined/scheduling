package com.zelteam;

import com.zelteam.model.Resource;
import com.zelteam.model.Site;
import com.zelteam.model.Task;
import com.zelteam.util.Random;

import java.util.*;
import java.util.stream.Collectors;

public class InputBuilder {
    private final List<Site> sites;
    private final List<Resource> resources;
    private final List<Task> tasks;

    public InputBuilder() {
        sites = buildSites();
        resources = sites.stream()
                .flatMap(s -> s.getResources().stream())
                .collect(Collectors.toList());
        double slowestResource = resources.stream()
                .mapToDouble(Resource::getSpeed)
                .min()
                .orElse(1d);
        tasks = buildTasks(slowestResource);
    }

    public List<Site> getSites() {
        return sites;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Site> buildSites() {
        int sitesCount = Random.uniformInt(5,10).sample(); //from 5 till 10
        List<Site> sites = new ArrayList<>();
        while (sitesCount-->0) {
            sites.add(new Site(buildResources()));
        }
        return sites;
    }

    private List<Resource> buildResources() {
        int resourcesCount = Random.uniformInt(5,8).sample(); // from 5 till 8
        return Arrays.stream(Random.uniformReal(1, 7.5d).sample(resourcesCount)) //processor power 1 to 7.5
                .boxed()
                .map(Resource::new)
                .collect(Collectors.toList());
    }

    public List<Task> buildTasks(double slowestResource) {
        int tasksCount = Random.uniformInt(1000,5000).sample(); // from 1k till 5k
        List<Integer> interArrivalIntervals = Arrays.stream(Random.poisson(5).sample(tasksCount))//delays Poisson
                .boxed()
                .collect(Collectors.toList());
        List<Integer> startedTimes = new ArrayList<>(interArrivalIntervals.size());

        int time = 0;
        for (int i : interArrivalIntervals){
            time += i;
            startedTimes.add(time);
        }
        Iterator<Integer> currentTime = startedTimes.iterator();

        return Arrays.stream(Random.uniformReal(20, 500).sample(tasksCount)) //computation times
                .boxed()
                .map(d -> new Task(d, currentTime.next(), slowestResource))
                .collect(Collectors.toList());
    }
}
