package edu.virginia.cs.Uniq;


/**
 * Created by IntelliJ IDEA.
 * User: ct4ew
 * Date: 7/23/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class Pair<T> {
    private T first;
    private T second;

    public Pair() {
    }

    public Pair(T first, T second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return this.first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return this.second;
    }

    public void setSecond(T second) {
        this.second = second;
    }
}
