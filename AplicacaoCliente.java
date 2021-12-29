import java.net.*;
import java.io.*;
import java.util.*;


public class AplicacaoCliente {

    private Socket servidor;
    private ObjectOutputStream streamSaida;
    private ObjectInputStream streamEntrada;
    private ThreadEntrada entrada; 
    private String nick;

    public AplicacaoCliente(String ip, int porta, String nick) throws UnknownHostException, IOException {
        this.nick = nick;
        servidor = new Socket(ip, porta);
        streamSaida = new ObjectOutputStream(servidor.getOutputStream());
        streamEntrada = new ObjectInputStream(servidor.getInputStream());
        entrada = new ThreadEntrada(streamEntrada);
        entrada.start();
    }

    public void enviaMensagem(Mensagem msg) throws IOException {
        streamSaida.writeObject(msg);
    }
    
    public void enviaMensagem(String msg) throws IOException {
        streamSaida.writeObject(new Mensagem(msg, nick));
    }
    
    public void enviaMensagem(String msg, String destinatario) throws IOException {
        streamSaida.writeObject(new Mensagem(msg, nick, destinatario));
    }
    
    public String getNick() {
        return nick;
    }
    
    public void finaliza() throws IOException {
        entrada.finaliza();
        streamSaida.close();
        streamEntrada.close();
        servidor.close();
    }
    
    public static void main(String[] args) 
           throws UnknownHostException, IOException {

        String msg = "", nick = "";
        boolean fim = false;
        
        InputStreamReader streamTeclado = new InputStreamReader(System.in);
        BufferedReader teclado = new BufferedReader(streamTeclado);

        System.out.println("Digite seu nickname: ");
        nick = teclado.readLine();
            
        AplicacaoCliente cliente = new AplicacaoCliente("127.0.0.1", 12345, nick);
        System.out.println(nick + " se conectou ao servidor!");
        
        
        
        do {
            System.out.println("Digite uma mensagem ou fim para sair: ");
            
            msg = teclado.readLine();
            
            if (msg.equals("fim") || msg.equals("FIM"))
                fim = true;    
            else             
                cliente.enviaMensagem(msg, nick);
        } while (!fim);
                
        cliente.finaliza();
    }
    
}

class ThreadEntrada extends Thread {

    private ObjectInputStream streamEntrada;
    private boolean fim;

    public ThreadEntrada(ObjectInputStream streamEntrada) {
        this.streamEntrada = streamEntrada;
        this.fim = false;
    }
    
    public void finaliza() {
        fim = true;
    }

    public void run() {
        Mensagem msg;
        try {
            while(!fim) {
                msg = (Mensagem) streamEntrada.readObject();
                System.out.println(msg.getRemetente() + ": " + msg.getMensagem());
            }
        } catch (Exception e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}

