package com.zelteam.input;

import com.zelteam.model.Resource;
import com.zelteam.model.Site;
import com.zelteam.model.Task;
import com.zelteam.util.Random;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SDSC0Input implements Inputable {
    public static final int SITES_LOWER_COUNT =  1;
    public static final int SITES_UPPER_COUNT = SITES_LOWER_COUNT;
    public static final int RESOURCES_LOWER_COUNT = 144;
    public static final int RESOURCES_UPPER_COUNT = RESOURCES_LOWER_COUNT;
    public static final int CPU_POWER = 8;

    private final List<Site> sites;
    private final List<Resource> resources;
    private final List<Task> tasks;

    public SDSC0Input() throws IOException {
        sites = buildSites();
        resources = sites.stream()
                .flatMap(s -> s.getResources().stream())
                .collect(Collectors.toList());
        tasks = buildTasks();
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
        return IntStream.generate(()-> CPU_POWER).limit(resourcesCount)
                .boxed()
                .map(Resource::new)
                .collect(Collectors.toList());
    }

    public List<Task> buildTasks() throws IOException {
        BufferedReader fileReader = new BufferedReader(new FileReader("input" + File.separator + "SDSC0.txt"));

        final String delimiter = "\t";
        List<Task> taskList = fileReader.lines()
                .map(line -> {
                    String[] tokens = line.split(delimiter);
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd--HH:mm:ss");

                    long arrivalTime = 0;
                    long computationSize = 0;
                    try {
                        arrivalTime = df.parse(tokens[16]).getTime();
                        long time = df.parse(tokens[17]).getTime();
                        computationSize = df.parse(tokens[18]).getTime() - (time>arrivalTime?time:arrivalTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return new Task(computationSize, arrivalTime, 1);
                })
                .sorted((t1, t2) -> ((Double) t1.getAppearTime()).compareTo(t2.getAppearTime()))
                .collect(Collectors.toList());
        double firstTime = taskList.get(0).getAppearTime();
        return taskList.stream()
                .map(t-> new Task(t.getComputationalSize(), t.getAppearTime() - firstTime, CPU_POWER))
                .collect(Collectors.toList());
    }
}
