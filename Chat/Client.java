import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class Client
{
    public static void main(String[] args)
    {
        final Socket cliSoc;
        final BufferedReader in;
        final PrintWriter out;
        final Scanner sc = new Scanner(System.in); // Pour lire à partir du clavier

        try
        {
            // Pour serveur et client sur même machine
                /* Les infos du serveur ( port et adresse IP ou nom d'hote)
                * 127.0.0.1 adresse local de la machine
                */

            // Pour serveur et client sur différentes machines
                /* Adresse IP de la machine serveur ( taper 'ipconfig' dans cmd pour trouver l'adresse ip de votre machine )
                * Serveur et client(s) doivent être connectés au même réseau wifi
                * Adresse IP change après déconnexion du serveur et client
                */

            cliSoc = new Socket("127.0.0.1",5000);

            // Flux pour envoyer
            out = new PrintWriter(cliSoc.getOutputStream());
            // Flux pour recevoir
            in = new BufferedReader(new InputStreamReader(cliSoc.getInputStream()));

            Thread envoyer = new Thread(new Runnable()
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
            envoyer.start();

            Thread recevoir = new Thread(new Runnable()
            {
                String msg;
                @Override
                public void run()
                {
                    try
                    {
                        msg = in.readLine();
                        while (msg != null)
                        {
                            System.out.println("Serveur : " + msg);
                            msg = in.readLine();
                        }
                        System.out.println("Serveur déconnecté");
                        out.close();
                        cliSoc.close();
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