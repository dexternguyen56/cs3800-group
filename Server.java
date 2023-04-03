import java.io.*; 
import java.net.*; 

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server listening on port 5000");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String request = inFromClient.readLine();
                System.out.println("Request: " + request);
                String time = TimeClient.getCurrentTime();
                OutputStream outToClient = clientSocket.getOutputStream();
                if ((request.contains("/time") && !request.contains("?")) || request.contains("/time?zone=all")) {
                    outToClient.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outToClient.write("Content-Type: text/html\r\n\r\n".getBytes());
                    String gmtTime = convertTime(time, "GMT");
                    String estTime = convertTime(time, "EST");
                    String pstTime = convertTime(time, "PST");
                    
                    String html = "<html><body><h1>GMT: "+ gmtTime+ "</h1>"
                                + "<h1>EST: " + estTime + "</h1>"
                                + "<h1>PST: " + pstTime + "</h1>"
                                + "</body></html>";
                    
                    outToClient.write(html.getBytes());
                } else if (request.contains("/time?zone=est")) {
                    
                    outToClient.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outToClient.write("Content-Type: text/html\r\n\r\n".getBytes());
                    String estTime = convertTime(time, "EST");
                    String html = "<html><body><h1>EST: "+ estTime+ "</h1>";
                    outToClient.write(html.getBytes()); 

                } 
                else if(request.contains("/time?zone=pst")){
                    outToClient.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outToClient.write("Content-Type: text/html\r\n\r\n".getBytes());
                    String pstTime = convertTime(time, "PST");
                    String html = "<html><body><h1>PST: "+ pstTime+ "</h1>";
                    outToClient.write(html.getBytes());
                }
                else {
                    outToClient.write("HTTP/1.1 400 Bad Request\r\n".getBytes());
                    outToClient.write("Content-Type: text/html\r\n\r\n".getBytes());
                    String output = "<html><body><h1>Invalid Request</body></h1>";
                    outToClient.write(output.getBytes());
                }
                
                outToClient.flush();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String convertTime(String time, String form){
        int month = Integer.parseInt(time.substring(9,11)) ;
        int year = Integer.parseInt(time.substring(6,8));
        int day = Integer.parseInt(time.substring(12,14));
        int hour = Integer.parseInt(time.substring(15,17));
        int minutes = Integer.parseInt(time.substring(18,20));
        boolean am = true;
        String res = "";
        if(form.equals("EST")){
            hour -=4;
            if(hour<0){
                day--;
                hour = 12-Math.abs(hour);
                am = false;
            }
            if(hour>12){
                am = false;
                hour-=12;
            }
        }
        else if(form.equals("PST")){
            hour-=7;
            if(hour<0){
                day--;
                hour = 12-Math.abs(hour);
                am = false;
            }
            if(hour>12){
                am = false;
                hour -=12;
            }
        }
        else if(form.equals("GMT")){
            if(hour>12){
                hour-=12;
                am = false;
            }
        }
        else{
            return "";
        }
        if(hour==0){
            hour = 12;
        }
        if(minutes<10){
        res += month + "/" + day + "/" +year + ", "  + hour+ ":" + "0" + minutes;
        }
        else{
            res += month + "/" + day + "/" +year + ", "  + hour+ ":" + minutes;
        }
        if(am){
            res += " AM";
        }
        else{
            res += " PM";
        }
        return res;
    }
}
