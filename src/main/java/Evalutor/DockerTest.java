package Evalutor;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.LogStream;
import com.spotify.docker.client.messages.*;
import com.spotify.docker.client.messages.swarm.Config;

import java.util.List;

public class DockerTest {
    public static void main(String[] args) throws Exception {
        DockerClient client = new DefaultDockerClient("http://localhost:2375");
        List<Image> images = client.listImages(DockerClient.ListImagesParam.allImages());
        List<Container> containers = client.listContainers(DockerClient.ListContainersParam.allContainers());
        for (Container container : containers) {
            System.out.println(container.state());
            System.out.println(container.imageId());
            System.out.println(container.toString());

        }
        ContainerConfig config = ContainerConfig.builder()
                .image("ubuntu")
                .cmd("sh", "-c", "while :; do sleep 1; done")
                .build();
        ContainerCreation creation = client.createContainer(
                config);
        String id = creation.id();
        client.startContainer(id);// 启动容器
        final String[] command = {"sh", "-c", "ls -a"};
        final ExecCreation execCreation = client.execCreate(
                id, command, DockerClient.ExecCreateParam.attachStdout(),
                DockerClient.ExecCreateParam.attachStderr());
        final LogStream output = client.execStart(execCreation.id());
        final String execOutput = output.readFully();
        System.out.println(execOutput);

        client.killContainer(id);
        client.removeContainer(id);//删除容器
        client.close();
    }
}
