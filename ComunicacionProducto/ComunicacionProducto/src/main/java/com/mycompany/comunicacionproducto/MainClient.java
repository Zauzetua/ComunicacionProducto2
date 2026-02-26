package com.mycompany.comunicacionproducto;

import com.mycompany.comunicacionproducto.Models.Producto;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.WebSocketContainer;

import java.net.URI;
import java.time.LocalDate;

/**
 *
 * @author jesus
 */
public class MainClient {

    /*
        Clase MainClient donde se pone productClient como el endpoint y se conecta al servidor localhost 
        para poder hacer la peticion de los productos y poder hacer la serializacion de el objeto en texto y binario.
     */
    public static void main(String[] args) throws Exception {

        WebSocketContainer container
                = ContainerProvider.getWebSocketContainer();

        ProductoClient client = new ProductoClient();

        //endpoint del servidor al que nos conectaremos
        container.connectToServer(
                client,
                new URI("ws://localhost:8025/productos")
        );

        //Creacion del objeto producto
        Producto p = new Producto(
                "ABC123",
                "SuperLaptopGamingProMax2026",
                25000.50,
                10,
                "LenovoCorporationInternational",
                LocalDate.now()
        );
        // esperamos la conexion...
        Thread.sleep(1000);

        client.sendText(p);
        Thread.sleep(1000); // esperamos el envio del texto antes de enviar el binario
        client.sendBinary(p);
        Thread.sleep(1000);
        for (int i = 0; i < 3; i++) {
            client.sendXML(p);
        }

        // esperaremos el envio antes de cerrar la conexion 
        Thread.sleep(2000);
    }
}
