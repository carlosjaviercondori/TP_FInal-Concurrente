import java.io.*;
import java.util.regex.*;

public class VerificarInvariante {
    public static void main(String[] args) {
        String logPath = "petri_log.txt";
        boolean invarianteOk = true;
        Pattern pattern = Pattern.compile("P0:(\\d+).*P1:(\\d+).*P2:(\\d+)");
        try (BufferedReader br = new BufferedReader(new FileReader(logPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int p0 = Integer.parseInt(matcher.group(1));
                    int p1 = Integer.parseInt(matcher.group(2));
                    int p2 = Integer.parseInt(matcher.group(3));
                    if (p0 + p1 + p2 != 3) {
                        System.out.println("Invariante violado en línea: " + line.trim());
                        invarianteOk = false;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (invarianteOk) {
            System.out.println("¡El invariante de plaza se cumple en todo el log!");
        } else {
            System.out.println("Hubo violaciones del invariante.");
        }
    }
}
