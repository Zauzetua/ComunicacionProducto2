package com.mycompany.comunicacionproducto.Models;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Clase Producto donde se representa el objeto completo del producto con sus
 * atributos
 * que permiten crear el producto
 */

public class Producto {

    private String clave; // 6 caracteres
    private String nombre; // 30, pq asi lo pide el problema
    private double precio;
    private int cantidad;
    private String marca; // 30
    private LocalDate fechaReg;

    // Constructor
    public Producto(String clave, String nombre, double precio,
            int cantidad, String marca, LocalDate fechaReg) {
        this.clave = clave;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.marca = marca;
        this.fechaReg = fechaReg;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public LocalDate getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(LocalDate fechaReg) {
        this.fechaReg = fechaReg;
    }

    /**
     * Metodo toString para poder serializar el producto a un formato de texto
     */
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return clave + "|"
                + nombre + "|"
                + precio + "|"
                + cantidad + "|"
                + marca + "|"
                + fechaReg.format(formatter);
    }

    /**
     * Metodo para poder serializar el producto a un formato binario con un formato definido
     * en bytes para cada atributo
     * @return
     */
    public byte[] toBinaryFormat() {

        ByteBuffer buffer = ByteBuffer.allocate(86);

        buffer.put(fixLength(clave, 6));
        buffer.put(fixLength(nombre, 30));
        buffer.putDouble(precio);
        buffer.putInt(cantidad);
        buffer.put(fixLength(marca, 30));

        long epoch = fechaReg.atStartOfDay()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();

        buffer.putLong(epoch);

        return buffer.array();
    }

    /**
     * Metodo fromtext para poder deserializar el producto apartir del string
     * formato definido por el metodo toString
     * 
     * @param data
     * @return
     */
    public static Producto fromText(String data) {
        String[] parts = data.split("\\|");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return new Producto(
                parts[0],
                parts[1],
                Double.parseDouble(parts[2]),
                Integer.parseInt(parts[3]),
                parts[4],
                LocalDate.parse(parts[5], formatter));
    }
    /**
     * Metodo fromBinary para deserializar el producto a apartir del formato binario 
     * definido por el metodo toBinaryFormat
     * @param data
     * @return
     */

    public static Producto fromBinary(byte[] data) {

        ByteBuffer buffer = ByteBuffer.wrap(data);

        byte[] claveBytes = new byte[6];
        buffer.get(claveBytes);

        byte[] nombreBytes = new byte[30];
        buffer.get(nombreBytes);

        double precio = buffer.getDouble();
        int cantidad = buffer.getInt();

        byte[] marcaBytes = new byte[30];
        buffer.get(marcaBytes);

        long epoch = buffer.getLong();

        LocalDate fecha = java.time.Instant.ofEpochMilli(epoch)
                .atZone(ZoneOffset.UTC)
                .toLocalDate();

        return new Producto(
                new String(claveBytes).trim(),
                new String(nombreBytes).trim(),
                precio,
                cantidad,
                new String(marcaBytes).trim(),
                fecha);
    }

    /**
     * Metodo fixLegth para ajustar el tamaño de los atributos string 
     * al formato definido en el metodo toBinaryFormat tomando el texto
     * como valor y el length de bytes que le asignamos a cada atributo
     * @param value
     * @param length
     * @return
     */
    private byte[] fixLength(String value, int length) {
        byte[] bytes = value.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[length];

        System.arraycopy(bytes, 0, result, 0,
                Math.min(bytes.length, length));

        return result;
    }

}
