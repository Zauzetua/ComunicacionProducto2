package com.mycompany.comunicacionproducto;

import com.mycompany.comunicacionproducto.Models.Producto;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

/**
 *
 * @author jesus
 */
@ServerEndpoint("/productos")
public class ProductoServer {

    private static int contador = 0;

    @OnMessage
    public void onTextMessage(String message, Session session) {
        long start = System.nanoTime();

        Producto p = Producto.fromText(message);

        long end = System.nanoTime();
        System.out.println("---------TEXTO--------------");

        System.out.println("Producto recibido (texto): " + p.toString());
        System.out.println("Peso bytes: " + message.getBytes().length);
        System.out.println("Tiempo procesamiento: " + (end - start) + " ns");
        contador++;
        System.out.println("Recibido #" + contador);
        System.out.println("-----------------------");
    }

    @OnMessage
    public void onBinaryMessage(byte[] data, Session session) {
        long start = System.nanoTime();

        Producto p = Producto.fromBinary(data);

        long end = System.nanoTime();

        System.out.println("------------BINARIO-----------");
        System.out.println("Producto recibido (binario): " + p.toString());
        System.out.println("Peso bytes: " + data.length);
        System.out.println("Tiempo procesamiento: " + (end - start) + " ns");
    }
}
