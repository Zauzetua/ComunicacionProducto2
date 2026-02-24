/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.comunicacionproducto;

import com.mycompany.comunicacionproducto.Models.Producto;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

/**
 * Se abre la sesion  websocket para enviar los productos al servidor,
 * se envian en texto y binario
 * @author jesus
 */
@ClientEndpoint
public class ProductoClient {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    public void sendText(Producto p) {
        String data = p.toString();
        session.getAsyncRemote().sendText(data);
    }

    public void sendBinary(Producto p) {
        byte[] data = p.toBinaryFormat();
        session.getAsyncRemote().sendBinary(
            java.nio.ByteBuffer.wrap(data)
        );
    }
}