import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Serveur
{
    public static void main(String[] args)
    {
        final ServerSocket serSoc;
        final Socket cliSoc;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in);
        
        try
        {
            serSoc = new ServerSocket(5000);
            cliSoc = serSoc.accept();
            out = new PrintWriter(cliSoc.getOutputStream());
            in = new BufferedReader(new InputStreamReader(cliSoc.getInputStream()));
            Thread envoi = new Thread(new Runnable()
            {
                String msg;
                @Override
                public void run()
                {
                    while (true)
                    {
                        msg = sc.nextLine();
                        out.println(msg);
                        out.flush();
                    }
                }
            });
            envoi.start();

            Thread recevoir = new Thread(new Runnable()
            {
                String msg;
                @Override
                public void run()
                {
                    try
                    {
                        msg = in.readLine();
                        // Tant que le client est connecté
                        while (msg != null)
                        {
                            System.out.println("Client : " + msg);
                            msg = in.readLine();
                        }
                        // Sortir de la boucle si le client a deconnecté
                        System.out.println("Client déconnecté");
                        // Fermer le flux et la session socket
                        out.close();
                        cliSoc.close();
                        serSoc.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            recevoir.start();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }   
    }
}