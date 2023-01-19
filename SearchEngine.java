import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> stringList = new ArrayList<String>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            String output = "";
            if(stringList.size() == 0)
                output = "none";
            for(int i=0; i<stringList.size(); i++)
                output += stringList.get(i) + "\r\n";

            return String.format("Results:\r\n%s", output);

        }else {
            System.out.println("Path: " + url.getPath());
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {

                    stringList.add(parameters[1]);

                    String output = "";
                    for(int i=0; i<stringList.size(); i++)
                        output += stringList.get(i) + "\r\n";

                    return String.format("%s added to list! It now contains:\r\n%s", parameters[1], output);
                }
            }
            else if (url.getPath().contains("/search")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {

                    String matchingStrings = "";
                    for(int i=0; i<stringList.size(); i++)
                        if(stringList.get(i).contains(parameters[1]))
                            matchingStrings += stringList.get(i) + "\r\n";
    
                    return String.format("Strings containing %s:\r\n%s", parameters[1], matchingStrings);
                }
            }
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
