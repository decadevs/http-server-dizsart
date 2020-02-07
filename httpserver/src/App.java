import com.sun.net.httpserver.*;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class App{

        public static void main(String[] args) throws IOException {
            HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
            HttpContext context = server.createContext("/");
            HttpContext context1 = server.createContext("/json");
            context.setHandler(App::handleRequestHtml);
            context1.setHandler(App::handleRequestJson);
            server.start();
        }

        private static void handleRequestJson(HttpExchange exchange) throws IOException {
            Path path = Paths.get("src/json.txt");
            try(BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                String json = "{numbers:[123, 23, 223],text:[this is a text, this is another text]}";
                bw.write(json);
            }
                try(BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                    String response = br.readLine();
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }

            catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    private static void handleRequestHtml(HttpExchange exchange) throws IOException {
            String response = "<p> i am an HTML paragraph tag </p>";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }


