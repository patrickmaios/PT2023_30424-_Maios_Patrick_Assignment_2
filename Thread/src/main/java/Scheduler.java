import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    public List<Server> servers;
    private int maxNoServers;
    private int maxTaskPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoServers, int maxTaskPerServer, SelectionPolicy policy) {

        this.maxNoServers = maxNoServers;
        this.maxTaskPerServer = maxTaskPerServer;
        this.servers = new ArrayList<>();
        changeStrategy(policy);

        for(int i = 0; i < maxNoServers; i++){
            Server server = new Server(maxTaskPerServer);
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();

        }
    }


    public void changeStrategy(SelectionPolicy policy){
        if(policy == SelectionPolicy.SHORTEST_TIME){
            strategy = new ConcreteStrategyTime();
        }
        if(policy == SelectionPolicy.SHORTEST_QUEUE){
            strategy = new ConcreteStrategyQueue();
        }
    }

    public void dispatchTask(Task task){
        strategy.addTask(servers, task);
    }


}
