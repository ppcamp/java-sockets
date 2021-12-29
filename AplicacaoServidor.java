import java.net.*;
import java.io.*;
import java.util.*;

public class AplicacaoServidor {
    private ServerSocket servidor;
    private int porta;
    
    Socket sock1, sock2;
    
    Cliente cliente1, cliente2;
    
    public AplicacaoServidor(int porta) throws IOException {
        this.porta = porta;
        servidor = new ServerSocket(porta); 
    }

    public void esperaConexoes() throws IOException {

        sock1 = servidor.accept();
        System.out.println("Nova conexão com o cliente " +   
        sock1.getInetAddress().getHostAddress());
        
        cliente1 = new Cliente(sock1, this);
        cliente1.start();
     
     
        sock2 = servidor.accept();
        System.out.println("Nova conexão com o cliente " +   
        sock2.getInetAddress().getHostAddress());
        
        cliente2 = new Cliente(sock2, this);
        cliente2.start();
    
    }

    public void finaliza() throws IOException  {
        cliente1.finaliza();
        cliente2.finaliza();
        sock1.close();
        sock2.close();
        servidor.close();
    }
    
    public void enviaMensagemTodos(Mensagem msg) throws IOException  {
        cliente1.saida().writeObject(msg);
        cliente2.saida().writeObject(msg);
    }
    
    public int getPorta() {
        return porta;
    }

    public static void main(String[] args) throws IOException {
        boolean fim = false;
        AplicacaoServidor server = new AplicacaoServidor(12345);     
        System.out.println("Porta 12345 aberta!");
        
        System.out.println("Aguardando a conexão dos dois clientes...");    
        server.esperaConexoes();
        System.out.println("Clientes conectados!");
        
        Scanner teclado = new Scanner(System.in);
        String msg;
        
        do {
            System.out.println("Digite fim para sair: ");
            msg = teclado.nextLine();
            if (msg.equals("fim") || msg.equals("FIM"))
                fim = true;
        } while (!fim);
        
        teclado.close();
        server.finaliza();
        
    }
    
}

class Cliente extends Thread {
    private Socket sock;
    private boolean fim;
    private AplicacaoServidor server;
    private ObjectInputStream streamEntrada;
    private ObjectOutputStream streamSaida;

    
    public Cliente(Socket sock, AplicacaoServidor server) throws IOException  {
        this.sock = sock;
        this.server = server;
        fim = false;
        streamSaida = new ObjectOutputStream(sock.getOutputStream());
        streamEntrada = new ObjectInputStream(sock.getInputStream());
    }
    
    public void finaliza() {
        fim = true;
    }
    
    
    public ObjectInputStream entrada() {
        return streamEntrada;
    }

    public ObjectOutputStream saida() {
        return streamSaida;
    }

    
    public void run() {
        try {
            Mensagem msg;
            while (!fim) { 
            
                msg = (Mensagem) streamEntrada.readObject();
                System.out.println("Preparando...");
                server.enviaMensagemTodos(msg);
                System.out.println("Mensagem enviada para todos!");
            } 
            streamEntrada.close();
        } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            
        }
    }
}

