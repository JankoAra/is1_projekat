package http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpRequestUtil {

    //podesavanje URI i vrste zahteva, vraca odgovor na zahtev
    public static HttpResponse sendRequest(int actionNumber, Map<String, String> parameters) {
        String url = null;
        String method = null;
        HttpResponse response = null;
        switch (actionNumber) {
            case 1:
                //kreiranje grada
                url = "http://localhost:8080/centralni_server_aplikacija/api/mesto/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 2:
                //kreiranje korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/korisnik/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 3:
                //promena email-a za korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/korisnik/email";
                method = "PUT";
                //response = sendPutRequest(url, parameters);
                break;
            case 4:
                //promena mesta korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/korisnik/mesto";
                method = "PUT";
                //response = sendPutRequest(url, parameters);
                break;
            case 5:
                //kreiranje kategorije
                url = "http://localhost:8080/centralni_server_aplikacija/api/kategorija/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 6:
                //kreiranje video snimka
                url = "http://localhost:8080/centralni_server_aplikacija/api/video/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 7:
                //promena naziva snimka
                url = "http://localhost:8080/centralni_server_aplikacija/api/video/promeniNaziv";
                method = "PUT";
                //response = sendPutRequest(url, parameters);
                break;
            case 8:
                //dodavanje kategorije snuimku
                url = "http://localhost:8080/centralni_server_aplikacija/api/video/dodajKategoriju";
                method = "PUT";
                //response = sendPutRequest(url, parameters);
                break;
            case 9:
                //kreiranje paketa
                url = "http://localhost:8080/centralni_server_aplikacija/api/paket/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 10:
                //promena cene paketa
                url = "http://localhost:8080/centralni_server_aplikacija/api/paket/cena";
                method = "PUT";
                //response = sendPutRequest(url, parameters);
                break;
            case 11:
                //kreiranje pretplate korisnika na paket
                url = "http://localhost:8080/centralni_server_aplikacija/api/pretplata/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 12:
                //kreiranje gledanja snimka od strane korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/gledanje/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 13:
                //kreiranje ocene snimka od korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/ocena/create";
                method = "POST";
                //response = sendPostRequest(url, parameters);
                break;
            case 14:
                //menjanje ocene snimka od korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/ocena/promeni";
                method = "PUT";
                //response = sendPutRequest(url, parameters);
                break;
            case 15:
                //brisanje ocene snimka od korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/ocena/brisanje";
                method = "DELETE";
                //response = sendDeleteRequest(url, parameters);
                break;
            case 16:
                //brisanje video snimka od strane autora
                url = "http://localhost:8080/centralni_server_aplikacija/api/video/brisanje";
                method = "DELETE";
                //response = sendDeleteRequest(url, parameters);
                break;
            case 17:
                //dohvatanje svih mesta
                url = "http://localhost:8080/centralni_server_aplikacija/api/mesto/sve";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 18:
                //dohvatanje svih korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/korisnik/sve";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 19:
                //dohvatanje svih kategorija
                url = "http://localhost:8080/centralni_server_aplikacija/api/kategorija/sve";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 20:
                //dohvatanje svih video snimaka
                url = "http://localhost:8080/centralni_server_aplikacija/api/video/sve";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 21:
                //dohvatanje kategorija za odredjeni snimak
                url = "http://localhost:8080/centralni_server_aplikacija/api/video/kategorije";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 22:
                //dohvatanje svih paketa
                url = "http://localhost:8080/centralni_server_aplikacija/api/paket/sve";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 23:
                //dohvatanje svih pretplata za korisnika
                url = "http://localhost:8080/centralni_server_aplikacija/api/pretplata";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 24:
                //dohvatanje svih gledanja za snimak
                url = "http://localhost:8080/centralni_server_aplikacija/api/gledanje";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            case 25:
                //dohvatanje svih ocena za snimak
                url = "http://localhost:8080/centralni_server_aplikacija/api/ocena";
                method = "GET";
                //response = sendGetRequest(url, parameters);
                break;
            default:
                // Default case
                break;
        }
        response = sendRequestAndReceiveResponse(url, method, parameters);
        return response;
    }

    public static HttpResponse sendRequestAndReceiveResponse(String url, String method, Map<String, String> parameters) {
        try {
            URL urlObject = null;
            if (method.equals("GET") || method.equals("DELETE")) {
                //postavljanje query parametara
                StringBuilder urlString = new StringBuilder(url);
                if (parameters != null && !parameters.isEmpty()) {
                    urlString.append("?");
                    boolean firstParam = true;
                    for (Map.Entry<String, String> entry : parameters.entrySet()) {
                        String paramName;
                        String paramValue;
                        try {
                            paramName = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
                            paramValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                        } catch (UnsupportedEncodingException e) {
                            // Handle encoding exception, if needed
                            e.printStackTrace();
                            continue; // Skip this parameter and proceed to the next one
                        }
                        if (firstParam) {
                            firstParam = false;
                            urlString.append(paramName).append("=").append(paramValue);
                        } else {
                            urlString.append("&").append(paramName).append("=").append(paramValue);
                        }
                    }
                }
                urlObject = new URL(urlString.toString());
            } else {
                urlObject = new URL(url);
            }
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestMethod(method);
            if (method.equals("POST") || method.equals("PUT")) {
                connection.setDoOutput(true);

                // Set the content type to form data
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                // Create the form parameters
                StringBuilder sb = new StringBuilder();
                boolean firstParam = true;
                for (Map.Entry<String, String> entry : parameters.entrySet()) {
                    String paramName;
                    String paramValue;
                    try {
                        paramName = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
                        paramValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
                    } catch (UnsupportedEncodingException e) {
                        // Handle encoding exception, if needed
                        e.printStackTrace();
                        continue; // Skip this parameter and proceed to the next one
                    }
                    if (firstParam) {
                        firstParam = false;
                        sb.append(paramName).append("=").append(paramValue);
                    } else {
                        sb.append("&").append(paramName).append("=").append(paramValue);
                    }
                }
                String formData = sb.toString();

                // Get the output stream from the connection
                try (OutputStream outputStream = connection.getOutputStream()) {
                    // Write the form parameters to the output stream
                    outputStream.write(formData.getBytes(StandardCharsets.UTF_8));
                }
            }
            int responseCode = connection.getResponseCode();
            String responseMessage = connection.getResponseMessage();

            // Read the response from the server
            StringBuilder response = new StringBuilder();
            // Read the input stream based on the response code
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Process the content
                        response.append(line).append("\n");
                    }
                }
            } else {
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        // Process the error content
                        response.append(line).append("\n");
                    }
                }
            }
            connection.disconnect();
            return new HttpResponse(responseCode, response.toString(), responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HttpResponse(500, "Greska pri slanju http zahteva", "Greska pri slanju http zahteva");
    }

//    public static HttpResponse sendPostRequest(String url, Map<String, String> parameters) {
//        try {
//            URL urlObject = new URL(url);
//
//            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
//
//            connection.setRequestMethod("POST");
//
//            // Enable input/output streams
//            connection.setDoOutput(true);
//
//            // Set the content type to form data
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//            // Create the form parameters
//            StringBuilder sb = new StringBuilder();
//            boolean firstParam = true;
//            for (Map.Entry<String, String> entry : parameters.entrySet()) {
//                String paramName;
//                String paramValue;
//                try {
//                    paramName = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
//                    paramValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
//                } catch (UnsupportedEncodingException e) {
//                    // Handle encoding exception, if needed
//                    e.printStackTrace();
//                    continue; // Skip this parameter and proceed to the next one
//                }
//                if (firstParam) {
//                    firstParam = false;
//                    sb.append(paramName).append("=").append(paramValue);
//                } else {
//                    sb.append("&").append(paramName).append("=").append(paramValue);
//                }
//            }
//            String formData = sb.toString();
//
//            // Get the output stream from the connection
//            try (OutputStream outputStream = connection.getOutputStream()) {
//                // Write the form parameters to the output stream
//                outputStream.write(formData.getBytes(StandardCharsets.UTF_8));
//            }
//
//            int responseCode = connection.getResponseCode();
//            String responseMessage = connection.getResponseMessage();
//
//            // Read the response from the server
//            StringBuilder response = new StringBuilder();
//            // Read the input stream based on the response code
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // Process the content
//                        response.append(line).append("\n");
//                    }
//                }
//            } else {
//                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
//                    String line;
//                    while ((line = errorReader.readLine()) != null) {
//                        // Process the error content
//                        response.append(line).append("\n");
//                    }
//                }
//            }
//            connection.disconnect();
//            return new HttpResponse(responseCode, response.toString(), responseMessage);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(MestoRequests.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MestoRequests.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public static HttpResponse sendPutRequest(String url, Map<String, String> parameters) {
//        try {
//            URL urlObject = new URL(url);
//
//            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
//
//            connection.setRequestMethod("PUT");
//
//            // Enable input/output streams
//            connection.setDoOutput(true);
//
//            // Set the content type to form data
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//            // Create the form parameters
//            StringBuilder sb = new StringBuilder();
//            boolean firstParam = true;
//            for (String key : parameters.keySet()) {
//                String paramName;
//                if (firstParam) {
//                    firstParam = false;
//                    paramName = key + "=";
//                } else {
//                    paramName = "&" + key + "=";
//                }
//                sb.append(paramName);
//                sb.append(URLEncoder.encode(parameters.get(key), StandardCharsets.UTF_8.toString()));
//            }
//            String formData = sb.toString();
//
//            // Get the output stream from the connection
//            try (OutputStream outputStream = connection.getOutputStream()) {
//                // Write the form parameters to the output stream
//                outputStream.write(formData.getBytes(StandardCharsets.UTF_8));
//            }
//
//            int responseCode = connection.getResponseCode();
//            String responseMessage = connection.getResponseMessage();
//
//            // Read the response from the server
//            StringBuilder response = new StringBuilder();
//            // Read the input stream based on the response code
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // Process the content
//                        response.append(line).append("\n");
//                    }
//                }
//            } else {
//                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
//                    String line;
//                    while ((line = errorReader.readLine()) != null) {
//                        // Process the error content
//                        response.append(line).append("\n");
//                    }
//                }
//            }
//            connection.disconnect();
//            return new HttpResponse(responseCode, response.toString(), responseMessage);
//        } catch (MalformedURLException ex) {
//            Logger.getLogger(MestoRequests.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(MestoRequests.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return null;
//    }
//
//    public static HttpResponse sendDeleteRequest(String url, Map<String, String> parameters) {
//        try {
//            // Construct the URL with query parameters
//            StringBuilder urlString = new StringBuilder(url);
//            if (parameters != null && !parameters.isEmpty()) {
//                urlString.append("?");
//                boolean firstParam = true;
//                for (Map.Entry<String, String> entry : parameters.entrySet()) {
//                    String paramName;
//                    String paramValue;
//                    try {
//                        paramName = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
//                        paramValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
//                    } catch (UnsupportedEncodingException e) {
//                        // Handle encoding exception, if needed
//                        e.printStackTrace();
//                        continue; // Skip this parameter and proceed to the next one
//                    }
//                    if (firstParam) {
//                        firstParam = false;
//                        urlString.append(paramName).append("=").append(paramValue);
//                    } else {
//                        urlString.append("&").append(paramName).append("=").append(paramValue);
//                    }
//                }
//            }
//
//            URL urlObject = new URL(urlString.toString());
//
//            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
//
//            connection.setRequestMethod("DELETE");
//
//            int responseCode = connection.getResponseCode();
//            String responseMessage = connection.getResponseMessage();
//
//            // Read the response from the server
//            StringBuilder response = new StringBuilder();
//            // Read the input stream based on the response code
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // Process the content
//                        response.append(line).append("\n");
//                    }
//                }
//            } else {
//                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
//                    String line;
//                    while ((line = errorReader.readLine()) != null) {
//                        // Process the error content
//                        response.append(line).append("\n");
//                    }
//                }
//            }
//            connection.disconnect();
//            return new HttpResponse(responseCode, response.toString(), responseMessage);
//        } catch (MalformedURLException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//    public static HttpResponse sendGetRequest(String url, Map<String, String> parameters) {
//        try {
//            // Construct the URL with query parameters
//            StringBuilder urlString = new StringBuilder(url);
//            if (parameters != null && !parameters.isEmpty()) {
//                urlString.append("?");
//                boolean firstParam = true;
//                for (Map.Entry<String, String> entry : parameters.entrySet()) {
//                    String paramName;
//                    String paramValue;
//                    try {
//                        paramName = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString());
//                        paramValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString());
//                    } catch (UnsupportedEncodingException e) {
//                        // Handle encoding exception, if needed
//                        e.printStackTrace();
//                        continue; // Skip this parameter and proceed to the next one
//                    }
//                    if (firstParam) {
//                        firstParam = false;
//                        urlString.append(paramName).append("=").append(paramValue);
//                    } else {
//                        urlString.append("&").append(paramName).append("=").append(paramValue);
//                    }
//                }
//            }
//
//            URL urlObject = new URL(urlString.toString());
//
//            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
//
//            connection.setRequestMethod("GET");
//
//            int responseCode = connection.getResponseCode();
//            String responseMessage = connection.getResponseMessage();
//
//            // Read the response from the server
//            StringBuilder response = new StringBuilder();
//            // Read the input stream based on the response code
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // Process the content
//                        response.append(line).append("\n");
//                    }
//                }
//            } else {
//                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()))) {
//                    String line;
//                    while ((line = errorReader.readLine()) != null) {
//                        // Process the error content
//                        response.append(line).append("\n");
//                    }
//                }
//            }
//            connection.disconnect();
//            return new HttpResponse(responseCode, response.toString(), responseMessage);
//        } catch (MalformedURLException ex) {
//            ex.printStackTrace();
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return null;
//    }
}
