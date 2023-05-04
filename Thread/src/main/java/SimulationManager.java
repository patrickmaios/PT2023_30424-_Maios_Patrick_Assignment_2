import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class SimulationManager implements Runnable{

    public int time_limit = 60;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int numberOfServices = 2;
    public int numberOfClients = 4;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    private Scheduler scheduler;
    private SimulationManager frame;
    private List<Task> generatedTasks;
    private static File file;

    public SimulationManager() throws IOException {
        //TODO Initialize UI
        generateRandomTask();
        scheduler = new Scheduler(numberOfServices, numberOfClients, selectionPolicy);
        file = new File("output.txt");
        try{
            FileWriter fw =new FileWriter("output.txt", false);
            PrintWriter pw = new PrintWriter(fw, false);
            pw.flush();
            pw.close();
            fw.close();
        }catch (Exception exception){
            exception.printStackTrace();
        }

    }

    public void generateRandomTask(){
        generatedTasks = new ArrayList<>();
        Random random = new Random();
        int upper_serv = 10;
        int upper_arriv = 40;
        int i;
        for(i=0; i < numberOfClients; i++){
            int arrival_rand = 2 + random.nextInt(upper_arriv)%28;
            int service_rand = 2 + random.nextInt(upper_serv)%2;

            generatedTasks.add(new Task(i, arrival_rand, service_rand));
        }
        generatedTasks.sort(Comparator.comparingInt(task -> task.arrivalTime));
    }

    public void filePrint(int currentTime, List<Task> tasks, int numberOfServices) {
        try {
            BufferedWriter buff = new BufferedWriter(new FileWriter(file, true));
            buff.write("Time: " + currentTime);
            buff.newLine();
            buff.write("Clients: ");
            for(Task task : tasks){
                buff.write("("+task.getId()+", "+task.getArrivalTime()+", "+ task.getServiceTime() + ")");
            }
            buff.newLine();
            for(int i=0; i< numberOfServices; i++)
                buff.write("Queue " + i + ": " + scheduler.servers.get(i).getTasks() + "\n");
            buff.flush();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public boolean finish(List<Task> generatedTasks, Scheduler scheduler){
        if(generatedTasks.isEmpty()){
            for(int i = 0; i< numberOfServices; i++){
                if(!scheduler.servers.get(i).getTasks().isEmpty()){
                    return true;
                }
            }
        }
        else{
            return true;
        }
        return false;
    }

    @Override
    public void run(){
        int currentTime = 0;
        while(currentTime < time_limit && finish(generatedTasks, scheduler)){
            System.out.println("Time " + currentTime + "\n");
            System.out.println("Waiting clients: " + generatedTasks + "\n");



            while(generatedTasks.size()!=0 && currentTime == generatedTasks.get(0).arrivalTime){
                scheduler.dispatchTask(generatedTasks.get(0));
                generatedTasks.remove(0);
            }
            for(int i=0; i< numberOfServices; i++)
                System.out.println("Queue " + i + ": " + scheduler.servers.get(i).getTasks() + "\n");
            filePrint(currentTime, generatedTasks, numberOfServices);
            currentTime++;
            try {

                Thread.sleep(250);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }

    public static void main(String[] args) throws IOException {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();


    }
}
