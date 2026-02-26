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
    @OnMessage
    public void onTextMessage(String message, Session session) {

        long start = System.nanoTime();

        try {

            if (esXML(message)) {
                Producto validador = new Producto();

                boolean esValido = validador.validarXML(message);

                Producto p = Producto.fromXML(message);

                long end = System.nanoTime();
                System.out.println("------------XML-----------");
                if (esValido) {
                    System.out.println("El XML cumple con XSD");
                    System.out.println("Producto recibido (XML): " + p);
                    System.out.println("Peso bytes: " + message.getBytes().length);
                    System.out.println("Tiempo procesamiento: " + (end - start) + " ns");
                } else {
                    System.out.println("El XML no cumple con XSD");
                }

            } else {
                Producto p = Producto.fromText(message);

                long end = System.nanoTime();
                System.out.println("---------TEXTO--------------");
                System.out.println("Producto recibido (texto): " + p);
                System.out.println("Peso bytes: " + message.getBytes().length);
                System.out.println("Tiempo procesamiento: " + (end - start) + " ns");
            }
            System.out.println("-----------------------");

        } catch (Exception e) {
            System.out.println("Error procesando mensaje: " + e.getMessage());
        }
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

    private boolean esXML(String message) {
        return message.trim().startsWith("<");
    }

}
