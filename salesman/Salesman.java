package salesman;

import java.io.*;
import java.util.*;

public class Salesman extends Person {
    private String email;
    private String password;

    public Salesman(String id, String name, String email, String phone, String password) {
        super(id, name, phone); 
        this.email = email;
        this.password = password;
}


    // Getters and setters
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setName(String name) { super.name = name; }
    public void setPhone(String phone) { super.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password;}

    @Override
    public String getProfile() {
        return super.getProfile() + "\nEmail: " + email;
    }


    public static List<Salesman> loadSalesmen(String filename) {
        List<Salesman> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    list.add(new Salesman(parts[0], parts[1], parts[2], parts[3], parts[4]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void saveSalesmen(String filename, List<Salesman> salesmen) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (Salesman s : salesmen) {
                pw.println(s.id + "," + s.name + "," + s.email + "," + s.phone + "," + s.password);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
