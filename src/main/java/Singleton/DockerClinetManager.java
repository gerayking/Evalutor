package Singleton;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;

import java.util.HashMap;
import java.util.Map;

// 管理docker-client
public class DockerClinetManager {
    public static  DockerClient client;
    private DockerClinetManager(){

    }
    public static DockerClient getDockerClient(){
        if(client==null){
            client=new DefaultDockerClient("http://localhost:2375");
        }
        return client;
    }
}
