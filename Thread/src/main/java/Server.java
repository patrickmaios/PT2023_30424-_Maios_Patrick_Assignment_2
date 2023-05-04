import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private int maxTaskPerService;
    private boolean isEmpty = false;
    private boolean stop=true;

    public Server(int maxTaskPerService) {
        this.maxTaskPerService = maxTaskPerService;
        tasks = new LinkedBlockingQueue<>(maxTaskPerService);
        waitingPeriod = new AtomicInteger(0);

    }

    public void addTasks(Task newTask){
        try{
            tasks.put(newTask);
            waitingPeriod.addAndGet(newTask.getServiceTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        System.out.println("Start server");
        while(stop) {
            try {
                if(!tasks.isEmpty()) {
                    Task task = tasks.peek();
                    if(task.getServiceTime() == 0) {
                        tasks.remove();
                        task = tasks.peek();
                        Thread.sleep(250);
                    }
                    else {
                        Thread.sleep(250);
                    }
                    if(task!=null) {
                        task.setServiceTime(task.getServiceTime() - 1);
                        waitingPeriod.set(waitingPeriod.get() - 1);
                    }
                }else {
                    //Thread.sleep(1000);
                }
            }  catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public int getExpectedTime(){
        int time = waitingPeriod.get();
        for(Task task : tasks){
            time += task.getServiceTime();
        }
        return time;
    }

    public int getQueueSize(){
        return tasks.size();
    }
    public int getWaitingPeriod(){
        return waitingPeriod.get();
    }

}
