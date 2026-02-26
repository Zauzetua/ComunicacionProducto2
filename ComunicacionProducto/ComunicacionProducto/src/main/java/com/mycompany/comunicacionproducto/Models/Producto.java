package com.mycompany.comunicacionproducto.Models;

import com.mycompany.comunicacionproducto.LocalDateAdapter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.validation.*;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import javax.xml.XMLConstants;
import org.xml.sax.SAXException;

/**
 * Clase Producto donde se representa el objeto completo del producto con sus
 * atributos que permiten crear el producto
 */
@XmlRootElement(name = "producto")
@XmlAccessorType(XmlAccessType.FIELD)
public class Producto {

    private String clave; // 6 caracteres
    private String nombre; // 30, pq asi lo pide el problema
    private double precio;
    private int cantidad;
    private String marca; // 30

    @XmlJavaTypeAdapter(LocalDateAdapter.class)
    private LocalDate fechaReg;

    // Constructor vacio necesario para la serializacion XML
    public Producto() {
    }

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
     * Metodo para poder serializar el producto a un formato binario con un
     * formato definido en bytes para cada atributo
     *
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
     * Metodo fromBinary para deserializar el producto a apartir del formato
     * binario definido por el metodo toBinaryFormat
     *
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
     * Metodo fixLegth para ajustar el tamaño de los atributos string al formato
     * definido en el metodo toBinaryFormat tomando el texto como valor y el
     * length de bytes que le asignamos a cada atributo
     *
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

    /**
     * Metodo toXML para serializar el producto a formato XML utilizando JAXB
     *
     * @return String con el formato XML del producto
     * @throws Exception en caso de que ocurra un error durante la serializacion
     */
    public String toXML() throws Exception {
        JAXBContext context = JAXBContext.newInstance(Producto.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        StringWriter writer = new StringWriter();
        marshaller.marshal(this, writer);
        return writer.toString();
    }

    /**
     * Metodo fromXML para deserializar el producto a partir de un string con
     * formato XML
     *
     * @param xml String con el formato XML del producto
     * @return Producto deserializado a partir del XML
     * @throws Exception En caso de que ocurra un error durante la
     * deserializacion
     */
    public static Producto fromXML(String xml) throws Exception {

        JAXBContext context = JAXBContext.newInstance(Producto.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (Producto) unmarshaller.unmarshal(new StringReader(xml));
    }

    /**
     * Metodo que valida si el XML cumple con nuestro XSD
     * @param xmlString EL XML en String
     * @return true si es valido, false si no
     */
    public boolean validarXML(String xmlString) {
        try {
            // instancio la fabrica para esquemas W3C
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

            // por seguridad deshabilito el acceso a entidades externas para prevenir inyecciones XXE
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            // cargo mi archivo xsd directo de la raiz de mi proyecto
            File archivoXsd = new File("src/main/java/com/mycompany/comunicacionproducto/Models/producto.xsd");
            Schema schema = factory.newSchema(archivoXsd);

            // creo mi validador
            Validator validator = schema.newValidator();

            // ejecuto la validacion de mi xml recibido como string
            validator.validate(new StreamSource(new StringReader(xmlString)));

            // si paso la linea anterior, mi xml es valido y retorno true
            return true;

        } catch (IOException | SAXException e) {
            // mi xml no cumple con el esquema o fallo la lectura de mi archivo
            // e.printStackTrace(); 
            return false;
        }
    }

}
