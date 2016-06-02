package edu.virginia.cs.Synthesizer;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ct4ew
 * Date: 7/23/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class CodeNamePair implements Serializable {
    private String first;
    private String second;

    public CodeNamePair() {

    }

    public CodeNamePair(String first, String second) {
        this.first = first;
        this.second = second;
    }

    public String getFirst() {
        return this.first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return this.second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

}



