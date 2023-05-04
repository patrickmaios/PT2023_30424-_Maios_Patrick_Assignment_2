import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server selectedServer = null;
        int shortestQueue = Integer.MAX_VALUE;
        for(Server server: servers){
            if(server.getQueueSize() < shortestQueue){
                selectedServer = server;
                shortestQueue = server.getQueueSize();
            }
        }

        if(selectedServer != null){
            selectedServer.addTasks(task);
        }
    }
}
