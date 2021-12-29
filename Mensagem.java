import java.io.*;

public class Mensagem implements Serializable {
    private String msg;
    private String remetente;
    private String destinatario;
    
    public Mensagem() {
        this.msg = "";
        this.remetente = "";
        this.destinatario = "";
    }
    
    public Mensagem(String msg, String remetente) {
        this.msg = msg;
        this.remetente = remetente;
        this.destinatario = "";
    }

    public Mensagem(String msg, String remetente, String destinatario) {
        this.msg = msg;
        this.remetente = remetente;
        this.destinatario = destinatario;
    }
    
    public String getMensagem() {
        return msg;
    }
    
    public void setMensagem(String msg) {
        this.msg = msg;
    }
    
    public String getRemetente() {
        return remetente;
    }
    
    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    

}
