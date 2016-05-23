package edu.virginia.cs.Synthesizer;

public class MetricValue {
    private Integer solutionNo = 0;
    private Integer TATI = 0;
    private Integer NCT = 0;
    private Integer NCRF = 0;
    private Integer ANV = 0;
    private Integer NIC = 0;
    private Integer NFK = 0;

    private String TATI_detail = "";
    private String NCT_detail = "";
    private String NCRF_detail = "";
    private String ANV_detail = "";
//    private String NIC_detail = "";

//    ArrayList<Integer> similarSolutions = new ArrayList<Integer>();

    public MetricValue(){
    }

    public MetricValue(int solutionNo){
        this.solutionNo = solutionNo;
    }

    public Integer getTATI() {
        return TATI;
    }

    public void setTATI(Integer TATI) {
        this.TATI = TATI;
    }

    public Integer getNCT() {
        return NCT;
    }

    public void setNCT(Integer NCT) {
        this.NCT = NCT;
    }

    public Integer getNCRF() {
        return NCRF;
    }

    public void setNCRF(Integer NCRF) {
        this.NCRF = NCRF;
    }

    public Integer getANV() {
        return ANV;
    }

    public void setANV(Integer ANV) {
        this.ANV = ANV;
    }

    public boolean shallowEquals(MetricValue m) {
        if (TATI.equals(m.getTATI()) &&
                NCT.equals(m.getNCT()) &&
                NCRF.equals(m.getNCRF()) &&
                ANV.equals(m.getANV()) &&
//                NIC.equals(m.getNIC()) &&
                NFK.equals(m.getNFK()) )
            return true;
        return false;
    }

    public boolean equals(MetricValue m) {
        if (TATI.equals(m.getTATI()) &&
                NCT.equals(m.getNCT()) &&
                NCRF.equals(m.getNCRF()) &&
                ANV.equals(m.getANV()) &&
//                NIC.equals(m.getNIC()) &&
                NFK.equals(m.getNFK()) &&
                TATI_detail.equals(m.TATI_detail) &&
                NCT_detail.equals(m.NCT_detail) &&
                NCRF_detail.equals(m.NCRF_detail) &&
                ANV_detail.equals(m.ANV_detail))
//                NIC_detail.equals(m.NIC_detail) )
            return true;
        return false;
    }

    public Integer getSolutionNo() {
        return solutionNo;
    }

    public void setSolutionNo(Integer solutionNo) {
        this.solutionNo = solutionNo;
    }

    public String getTATI_detail() {
        return TATI_detail;
    }

    public void setTATI_detail(String TATI_detail) {
        this.TATI_detail = TATI_detail;
    }

    public String getNCT_detail() {
        return NCT_detail;
    }

    public void setNCT_detail(String NCT_detail) {
        this.NCT_detail = NCT_detail;
    }

    public String getNCRF_detail() {
        return NCRF_detail;
    }

    public void setNCRF_detail(String NCRF_detail) {
        this.NCRF_detail = NCRF_detail;
    }

    public String getANV_detail() {
        return ANV_detail;
    }

    public void setANV_detail(String ANV_detail) {
        this.ANV_detail = ANV_detail;
    }

    public Integer getNIC() {
        return NIC;
    }

    public void setNIC(Integer NIC) {
        this.NIC = NIC;
    }

//    public String getNIC_detail() {
//        return NIC_detail;
//    }
//
//    public void setNIC_detail(String NIC_detail) {
//        this.NIC_detail = NIC_detail;
//    }

    public Integer getNFK() {
        return NFK;
    }

    public void setNFK(Integer NFK) {
        this.NFK = NFK;
    }
}
