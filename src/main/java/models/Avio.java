package models;

public class Avio {
    private Integer idAvio;
    private String model;
    private String compañia;

    public Avio() {
    }

    public Avio(Integer idAvio, String model, String compañia) {
        this.idAvio = idAvio;
        this.model = model;
        this.compañia = compañia;
    }

    public Integer getIdAvio() {
        return idAvio;
    }

    public void setIdAvio(Integer idAvio) {
        this.idAvio = idAvio;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCompañia() {
        return compañia;
    }

    public void setCompañia(String compañia) {
        this.compañia = compañia;
    }
}
