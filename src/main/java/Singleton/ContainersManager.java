package Singleton;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.Container;
import org.apache.commons.lang.ObjectUtils;

import java.util.List;

public class ContainersManager {
    public static synchronized Container getCainer() throws DockerException, InterruptedException {
        DockerClient dockerClient = DockerClinetManager.getDockerClient();
        List<Container> containers = dockerClient.listContainers(DockerClient.ListContainersParam.allContainers());
        for (Container container : containers) {
            if(container.state().equals("running"))continue;
            dockerClient.startContainer(container.id());
            return container;
        }
        return null;
    }
}
