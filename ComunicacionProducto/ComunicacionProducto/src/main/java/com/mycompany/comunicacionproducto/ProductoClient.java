/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.comunicacionproducto;

import java.nio.ByteBuffer;

import com.mycompany.comunicacionproducto.Models.Producto;
import com.mycompany.comunicacionproducto.proto.ProductoProto;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;

/**
 * Se abre la sesion websocket para enviar los productos al servidor,
 * se envian en texto y binario
 * 
 * @author jesus
 */
@ClientEndpoint
public class ProductoClient {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    /**
     * Metodo para enviar el producto en formato texto
     * @param p Producto a enviar
     */
    public void sendText(Producto p) {
        String data = p.toString();
        session.getAsyncRemote().sendText(data);
    }

    /**
     * Metodo para enviar el producto en formato binario
     * @param p Producto a enviar
     */
    public void sendBinary(Producto p) {
        byte[] data = p.toBinaryFormat();
        session.getAsyncRemote().sendBinary(
                java.nio.ByteBuffer.wrap(data));
    }

    /**
     * Metodo para enviar el producto en formato XML
     * @param p Producto a enviar
     * @throws Exception si ocurre un error durante la serializacion a XML
     */
    public void sendXML(Producto p) throws Exception {
        String xmlData = p.toXML();
        session.getAsyncRemote().sendText(xmlData);
    }

    /**
     * Metodo para enviar el producto en formato protobuf
     * @param p Producto a enviar
     */
    public void sendProtobuf(Producto p) {

        byte[] data = toProtobuf(p);

        session.getAsyncRemote()
                .sendBinary(ByteBuffer.wrap(data));
    }

    /**
     * Metodo para convertir un producto a formato protobuf
     * @param p Producto a convertir
     * @return byte[] con el producto en formato protobuf
     */
    public byte[] toProtobuf(Producto p) {

        ProductoProto.Producto proto = ProductoProto.Producto.newBuilder()
                .setClave(p.getClave())
                .setNombre(p.getNombre())
                .setPrecio(p.getPrecio())
                .setCantidad(p.getCantidad())
                .setMarca(p.getMarca())
                .setFechaReg(p.getFechaReg().toString())
                .build();

        return proto.toByteArray();
    }
}