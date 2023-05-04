import java.util.List;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task){
        Server selectedServer = null;
        int shortestTime = Integer.MAX_VALUE;
        for(Server server: servers){
            if(server.getExpectedTime() < shortestTime){
                selectedServer = server;
                shortestTime = server.getExpectedTime();
            }
        }

        if(selectedServer != null){
            selectedServer.addTasks(task);
        }
    }
}
