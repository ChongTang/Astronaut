package edu.virginia.cs.Evaluator;

/**
 * Created by tang on 11/24/14.
 */
public class Node {
    private String addr = "";
    private NodeStatus status;

    public Node(){
        this.status = NodeStatus.IDLE;
    }

    public NodeStatus getStatus() {
        return status;
    }

    public void setStatus(NodeStatus status) {
        this.status = status;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public enum NodeStatus {
        USING,
        IDLE
    }

}
