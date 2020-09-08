package Evalutor;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;

import java.util.HashMap;
import java.util.Map;

// 管理docker-client
public class DockerClinetManager {
    public static final DockerClient client = getDockerClient();
    private DockerClinetManager(){

    }
    public static DockerClient getDockerClient(){
        if(client == null){
            return   new DefaultDockerClient("http://localhost:2375");
        }
        return client;
    }
}
