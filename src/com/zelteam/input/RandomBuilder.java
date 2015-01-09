package com.zelteam.input;

import com.zelteam.model.Resource;
import com.zelteam.model.Site;
import com.zelteam.model.Task;
import com.zelteam.util.Random;

import java.util.*;
import java.util.stream.Collectors;

public class RandomBuilder implements Inputable {
    public static final int SITES_LOWER_COUNT = 1;
    public static final int SITES_UPPER_COUNT = 1;
    public static final int RESOURCES_LOWER_COUNT = 5;
    public static final int RESOURCES_UPPER_COUNT = 8;
    public static final double RESOURCES_LOWER_POWER = 1d;
    public static final double RESOURCES_UPPER_POWER = 7.5d;
    public static final int TASKS_LOWER_COUNT = 1000;
    public static final int TASKS_UPPER_COUNT = 5000;
    public static final int COMPUTATION_LOWER_TIME = 20;
    public static final int COMPUTATION_UPPER_TIME = 500;
    private final List<Site> sites;
    private final List<Resource> resources;
    private final List<Task> tasks;

    public RandomBuilder() {
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

    @Override
    public List<Site> getSites() {
        return sites;
    }

    @Override
    public List<Resource> getResources() {
        return resources;
    }

    @Override
    public List<Task> getTasks() {
        return tasks;
    }

    public List<Site> buildSites() {
        int sitesCount = Random.uniformInt(SITES_LOWER_COUNT, SITES_UPPER_COUNT).sample(); //from 5 till 10
        List<Site> sites = new ArrayList<>();
        while (sitesCount-->0) {
            sites.add(new Site(buildResources()));
        }
        return sites;
    }

    private List<Resource> buildResources() {
        int resourcesCount = Random.uniformInt(RESOURCES_LOWER_COUNT, RESOURCES_UPPER_COUNT).sample(); // from 5 till 8
        return Arrays.stream(Random.uniformReal(RESOURCES_LOWER_POWER, RESOURCES_UPPER_POWER).sample(resourcesCount))
                .boxed()
                .map(Resource::new)
                .collect(Collectors.toList());
    }

    public List<Task> buildTasks(double slowestResource) {
        int tasksCount = Random.uniformInt(TASKS_LOWER_COUNT,TASKS_UPPER_COUNT).sample(); // from 1k till 5k
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

        return Arrays.stream(Random.uniformReal(COMPUTATION_LOWER_TIME, COMPUTATION_UPPER_TIME)
                .sample(tasksCount)) //computation times
                .boxed()
                .map(d -> new Task(d, currentTime.next(), slowestResource))
                .collect(Collectors.toList());
    }
}
