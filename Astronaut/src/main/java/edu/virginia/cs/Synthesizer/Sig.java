package edu.virginia.cs.Synthesizer;

import java.io.Serializable;
import java.util.ArrayList;


public class Sig implements Serializable {
    int category = -1;  // 0: Class , 1: Association
    String sigName = "";
    ArrayList<String> attrSet = new ArrayList<String>();
    String id = "";
    String isAbstract = "";
    String parent = "";
    boolean hasParent = false;
    String src = "";
    String dst = "";
    String src_mul = "";
	String dst_mul = "";

    public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	public String getSigName() {
		return sigName;
	}
	public void setSigName(String sigName) {
		this.sigName = sigName;
	}
	public ArrayList<String> getAttrSet() {
		return attrSet;
	}
	public void setAttrSet(ArrayList<String> attrSet) {
		this.attrSet = attrSet;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsAbstract() {
		return isAbstract;
	}
	public void setIsAbstract(String isAbstract) {
		this.isAbstract = isAbstract;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public boolean isHasParent() {
		return hasParent;
	}
	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getDst() {
		return dst;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getSrc_mul() {
		return src_mul;
	}
	public void setSrc_mul(String src_mul) {
		this.src_mul = src_mul;
	}
	public String getDst_mul() {
		return dst_mul;
	}
	public void setDst_mul(String dst_mul) {
		this.dst_mul = dst_mul;
	}
}
