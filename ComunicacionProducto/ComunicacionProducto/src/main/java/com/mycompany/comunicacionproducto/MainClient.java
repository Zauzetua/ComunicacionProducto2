/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
        int N = 3;
        int M = 3;
        for (int i = 0; i < N; i++) {
            client.sendText(p);
        }
        for (int i = 0; i < M; i++) {
            client.sendBinary(p);
        }
        // esperaremos el envio antes de cerrar la conexion 
        Thread.sleep(2000);
    }
}
