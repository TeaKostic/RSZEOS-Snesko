package snesko;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Snesko {
    Random rnd;
    Snesko(){
        // ovde kreiramo novi objekat klase Random koji cemo koristiti kasnije 
        // u bilo kojoj metodi klase Snesko sa rnd.nextInt() ili neke slicne metode
        rnd = new Random();
    }
    
    /**
     * Vraca slucajan broj u opsegu 0 - max
     * Napomena: Stavio sam da je ova metoda private jer nema smisla koristiti
     * je van ove klase - npr iz klase Igra kao snesko.slucajanBroj(10). 
     * Ova metoda je samo interna metoda koja se koristi u ostalim metodama 
     * klase Snesko - tek cemo govoriti o private, public i ostalim metodama
     * @param max maksimalna vrednost slucajnog broja koja moze biti vracena
     * @return slucajan broj 0-max
     */
    private int slucajanBroj(int max){
        int ret = rnd.nextInt(max); //+1 zato sto nextInt vraca 0 - vrednost_parametra - 1
        return ret;
    }
    /**
     *  Ova funkcija ce biti pozvana iz klase Igra
     *  Moj savet (nije obavezno) je da dodajete novu metodu u ovoj klasi (slicnu metodi igraj)
     *  za svaki ili barem za vecinu delova programa opisanih u dokumentu 
     *  sa tekstom zadatka (meni, citanje fajla dictionary.txt i biranje reci,
     *  unos slova/reci od strane igraca, ...)
     */
    void igraj(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in)); //
        
            OUTER:
            while (true) {
                System.out.println("Opcije:");
                System.out.println("1. Igraj igru");
                System.out.println("2. Dodaj reč");
                System.out.println("0. Izlaz");
                System.out.println("Odaberi opciju: ");
                String opcija = in.readLine();
                
                switch (opcija) {
                    case "1":
                        novaIgra();
                        break;
                    case "2":
                        dodajNovuRec();
                        break;
                    case "0":
                        break OUTER;
                    default:
                        System.out.println("Pogresan unos");
                        break;
                }
            }
            
        } catch (IOException e) {
            System.out.println("Doslo je do greske prilikom unosa");
        }
        
    }
    
    void dodajNovuRec() {
        try {
           List<String> listaReci = ucitajReciUListu();

            if (listaReci == null) {
                return;
            }

            System.out.println("Unesite rec koju zelite da dodate: ");
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String novaRec = in.readLine(); // citanje sa ulaza

            boolean postoji = false;
            for(String r : listaReci) {
                if (r.equals(novaRec)) {
                 postoji = true;
                 break;
                }
            }

            if (!postoji) {
                listaReci.add(novaRec);

                PrintWriter out = new PrintWriter(new OutputStreamWriter(
                            new FileOutputStream("dictionary.txt", true), StandardCharsets.UTF_8));
                
                out.println(novaRec);
                out.close();
                
                System.out.println("Rec " + novaRec + " je dodata u recnik!");
            } else {
                System.out.println("Dodavanje neuspesno! Rec \"" + novaRec + "\" vec postoji!");
            } 
        } catch (Exception e) {
            System.out.println("Greska prilikom dodavanja nove reci");
        }
    }
    
    List<String> ucitajReciUListu() {
        try {
            BufferedReader in = new BufferedReader(
                                new InputStreamReader(new FileInputStream("dictionary.txt"), "UTF8")); 
       
            List<String> listaReci = new ArrayList<>();
            String rec;
            while ((rec = in.readLine()) != null) {
                listaReci.add(rec);
            }
            
            return listaReci;
        } catch (Exception e) {
            System.out.println("Doslo je do greske prilikom citanja iz fajla!");
            return null;
        }
    }
    
    void novaIgra() {
        
        List<String> listaReci = ucitajReciUListu();
        
        if (listaReci == null) {
            return;
        }

        int ind = slucajanBroj(listaReci.size());

//            System.out.println(listaReci.size());
        System.out.println(listaReci.get(ind));

        // Rec dobijena nasumicno
        String odabranaRec = listaReci.get(ind);

        try {
            
            List<Character> unetaSlova = new ArrayList<Character>(); // prazna lista, sad sledi punjenje
                    
             while(true){
                 
                prikaziRecZaPogadjanje(odabranaRec, unetaSlova);//znamo da je svakako 1 slovo taj string
                System.out.println("Unesite slovo koje zelite da dodate: ");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String novoSlovo = in.readLine();
                

                if(novoSlovo.matches("[АБВГДЂЕЖЗИЈКЛЉМНЊОПРСТЋУФХЦЧЏШабвгдђежзијклљмнњопрстћуфхцчџш]")) {
                    if(!unetaSlova.contains(novoSlovo.charAt(0))) {    
                        unetaSlova.add(novoSlovo.charAt(0));
                        if(odabranaRec.contains(novoSlovo)){
                           if (krajIgrePobedaProvera(odabranaRec, unetaSlova)){
                            System.out.println("Igra je gotova! Pogodili ste trazenu rec: \"" + odabranaRec + "\"");
                            break;
                            } 
                        } else {
                            int brPogrSl = brojPogresnihSlova(odabranaRec, unetaSlova);
                            crtajSneska(brPogrSl);
                            if (brPogrSl == 7) {
                                System.out.println("Izgubili ste igra je zavrsena!");
                                break;
                            }
                        }
                        
                    } else {
                        System.out.println("Slovo je vec uneto");
                    }
                }
                
            }
        } catch (Exception e) {
            System.out.println("greska prilikom upisa dodatog slova");
        }
      
    }
    
    void prikaziRecZaPogadjanje(String odabranaRec, List<Character> unetaSlova) {   
        int duzinaOdabraneReci = odabranaRec.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < duzinaOdabraneReci; i++) {
           if(unetaSlova.contains(odabranaRec.charAt(i))) { // dobijam koje je slovo na tom indeksu, npr charAt(0)=r, charAt(1)=e.. reka
               sb.append(odabranaRec.charAt(i)); //append je nadogradnja stringa, dodajemo crticu ili slovu kroz iteraciju
               continue; //нова итерација
           } 
           sb.append("_ ");
        }
        System.out.println(sb.toString());
        
    }
    
    private boolean krajIgrePobedaProvera(String odabranaRec, List<Character> unetaSlova){
        int duzinaOdabraneReci = odabranaRec.length();
      
        for (int i = 0; i < duzinaOdabraneReci; i++) {
            if(!unetaSlova.contains(odabranaRec.charAt(i))){
                return false; // posle 1 slova koje ne valja, izlazimo iz petlje
            }
        }
        return true;
    }  
    
    private int brojPogresnihSlova(String odabranaRec, List<Character> unetaSlova){
        int brojPogresnihSlova = 0;
        for (Character c : unetaSlova) { // iteriram kroz sva uneta slova i za svako provera dal je u odabranu rec
            if(odabranaRec.indexOf(c) == -1){ // znaci da ne postoji
                brojPogresnihSlova++;
            }
        }
        return brojPogresnihSlova;
    }
    
    private void crtajSneska (int brojPogresnihSlova) {
       
         switch(brojPogresnihSlova) {
             case 1 :  System.out.print("\n  (`^'^'`)\n" +
                                         "  `======'\n");
                       break;
             case 2 : System.out.print("\n   (`^^')\n" +
                                        "  (`^'^'`)\n" +
                                        "  `======'\n");
                       break;
             case 3 : System.out.println("\n    (  )\n" +
                                        "   (`^^')\n" +
                                        "  (`^'^'`)\n" +
                                        "  `======'\n");
                       break;             
             case 4 : System.out.println("\n    (  )\n" +
                                        ">--(`^^')\n" +
                                        "  (`^'^'`)\n" +
                                        "  `======'\n");
                       break;    
             case 5 : System.out.println("\n    (  )___/\n" +
                                         ">--(`^^')\n" +
                                         "  (`^'^'`)\n" +
                                         "  `======'");
                      break;
             case 6 : System.out.println("\n    ('')___/\n" +
                                        ">--(`^^')\n" +
                                        "  (`^'^'`)\n" +
                                        "  `======'"); 
                      break;
             case 7 : System.out.println("\n   _|==|_  \n" +
                                        "    ('')___/\n" +
                                        ">--(`^^')\n" +
                                        "  (`^'^'`)\n" +
                                        "  `======'\n");    
                      break;
             default : break;
         }
    }
}

