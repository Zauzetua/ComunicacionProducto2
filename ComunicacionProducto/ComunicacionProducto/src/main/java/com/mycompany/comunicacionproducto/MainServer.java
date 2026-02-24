/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.comunicacionproducto;

import org.glassfish.tyrus.server.Server;

/**
 *
 * @author jesus
 */
public class MainServer {

    public static void main(String[] args) {
        /*
         * Clase Main donde se instancia el servidor localhost para poder recibir las peticiones 
         * del MainClient
         */

        Server server = new Server("localhost", 8025, "/", null, ProductoServer.class);

        try {
            server.start();
            System.out.println("Servidor iniciado en ws://localhost:8025/productos");
            Thread.currentThread().join();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.stop();
        }
    }
}
