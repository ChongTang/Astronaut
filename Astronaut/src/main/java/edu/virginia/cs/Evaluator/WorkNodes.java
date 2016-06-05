package edu.virginia.cs.Evaluator;

import java.util.ArrayList;
import edu.virginia.cs.AppConfig;

/**
 * Created by Tang on 11/24/14.
 */
public class WorkNodes {
    private static ArrayList<Node> nodes = new ArrayList<Node>();

    public WorkNodes(){
        for(String name : AppConfig.getSparkSlaves()){
            String nodeAddr = name;
            Node n = new Node();
            n.setAddr(nodeAddr);
            nodes.add(n);
            System.out.println(nodeAddr);
        }
    }

    /* hard-coded version
    public WorkNodes(){
        for(int i = 2; i<50; i++){
            String nodeName = "centurion0";
            if(i<10) {
                nodeName = nodeName + "0" + String.valueOf(i);
            } else {
                nodeName = nodeName + String.valueOf(i);
            }
            String nodeAddr = nodeName + ".cs.virginia.edu";
            Node n = new Node();
            n.setAddr(nodeAddr);
            nodes.add(n);
            System.out.println(nodeAddr);
        }
    }*/

    public static Node getAnIdleNode(){
        for(Node n: nodes){
            if(n.getStatus() == Node.NodeStatus.IDLE){
                return n;
            }
        }
        return null;
    }

}
