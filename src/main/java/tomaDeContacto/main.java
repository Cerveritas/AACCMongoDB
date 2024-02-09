package tomaDeContacto;

import com.mongodb.client.*;

import org.bson.Document;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class main {

    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);

        int opcion = 0;

        do {

            System.out.println("0 - Salir del programa");
            System.out.println("1 - ");
            System.out.println("2 - ");
            System.out.println(" ");
            System.out.println("INTRODUCE EL NUMERO: ");

            opcion = sc.nextInt();



            switch (opcion){

                case 0:
                    System.out.println("Gracias por usar el programa...");
                    break;

                case 1:
                    // Voy a visualizar lo insertado
                    insertarDocumentos();
                    break;

                case 2:
                    // Voy a visualizar lo insertado
                    visualizarColeccion();
                    break;

                case 3:
                    // Voy a psar los datos a un fichero
                    MongoDBToTextFile();
                    break;

                default:
                    System.out.println("Numero introducido incorrecto, vuelve a intentarlo");
            }



        } while (opcion != 0);




    }

    public static void insertarDocumentos(){
        // Establecer la conexion a MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            System.out.println("Conexion exitosa a MongoDB");

            // Selecciona la base de datos y la coleccion
            MongoDatabase database = mongoClient.getDatabase("miBaseDeDatosAACC");
            MongoCollection<Document> collection = database.getCollection("miColeccion");

            // Ejemplo de insercion de un documento
            Document document = new Document("Fecha", new Date());
            collection.insertOne(document);

            System.out.println("Documento insertado en la coleccion");

        } catch (Exception e){
            System.out.println("Error al conectar a MongoDB: " + e.getMessage());
        }
    }


    public static void visualizarColeccion(){
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            System.out.println("Conexion exitosa a MongoDB");

            // Selecciona la base de datos y la coleccion
            MongoDatabase database = mongoClient.getDatabase("miBaseDeDatosAACC");
            MongoCollection<Document> collection = database.getCollection("miColeccion");

            // Mostrar todos los documentos de la coleccion
            System.out.println("Documentos en la coleccion");

            // Obtener todos los documentos
            FindIterable<Document> documents = collection.find();

            // Iterar sobre los documentos e imprimirlos
            for (Document document : documents) {
                System.out.println(document.toJson());
            }

        } catch (Exception e){
            System.out.println("Error al conectar a MongoDB " + e.getMessage());
        }
    }

    public static void MongoDBToTextFile(){
        // Configura la conexion con tu base de datos MongoDB
        try (var mongoClient = MongoClients.create("mongodb://localhost:27017")) {

            // Selecciona la base de datos y la coleccion
            var database = mongoClient.getDatabase("miBaseDeDatos");
            var collection = database.getCollection("miColeccion");

            // Consulta todos los documentos de la coleccion
            try (MongoCursor<Document> cursor = collection.find().iterator()) {

                // Especifica la ruta del archivo de texto
                String filePath = "datos_exportados.txt";

                // Abre el archivo en modo escritura
                try (FileWriter writer = new FileWriter(filePath)) {

                    // Itera sobre los doucmentos y escribe cada uno en el archivo
                    while (cursor.hasNext()){
                        Document document = cursor.next();

                        // Puedes personalizar como se escriben los datos en el archivo segun tu estructura
                        writer.write(document.toJson());
                        writer.write("\n");
                    }

                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        }
    }
}
