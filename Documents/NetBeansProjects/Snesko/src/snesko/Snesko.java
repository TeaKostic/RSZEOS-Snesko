package snesko;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
                               
                String opcija = in.readLine(); // is used to read one line text at a time
                
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
            String novaRec = in.readLine(); 

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

        int ind = slucajanBroj(listaReci.size()); // used to get the number of elements in list

//            System.out.println(listaReci.size());
        System.out.println(listaReci.get(ind)); // used to return the element at a given index from array/list

        // Rec dobijena nasumicno
        String odabranaRec = listaReci.get(ind);

        try {
            
            List<Character> unetaSlova = new ArrayList<Character>(); // prazna lista, sad sledi punjenje
            int counter = 0;
            
             while(true){
                 
                prikaziRecZaPogadjanje(odabranaRec, unetaSlova);//znamo da je svakako 1 slovo taj string
                System.out.println("Unesite slovo za pogadjanje ili konacno resenje: ");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String unos = in.readLine();
                

                if(unos.matches("[АБВГДЂЕЖЗИЈКЛЉМНЊОПРСТЋУФХЦЧЏШабвгдђежзијклљмнњопрстћуфхцчџш]+")) {
                    unos = unos.toLowerCase();
                    if(unos.length() == 1){
                        if(!unetaSlova.contains(unos.charAt(0))) {    
                            unetaSlova.add(unos.charAt(0));
                            if(odabranaRec.contains(unos)){
                               if (krajIgrePobedaProvera(odabranaRec, unetaSlova)){
                                System.out.println("Igra je gotova! Pogodili ste trazenu rec: \"" + odabranaRec + "\"");
                                break;
                                } 
                            } else {
                                counter ++;
                                System.out.println("Brojac iznosi " + counter);
                                
                                // int brPogrSl = brojPogresnihSlova(odabranaRec, unetaSlova);
                                crtajSneska(counter);
                                
                                if (counter == 7) {
                                    System.out.println("Izgubili ste, igra je zavrsena!");
                                    break;
                                }
                            }

                        } else {
                            System.out.println("Slovo je vec uneto!");
                        }
                    } else {
                        if(unos.equals(odabranaRec)){
                            System.out.println("Igra je gotova! Pogodili ste trazenu rec: \"" + odabranaRec + "\"");
                            break;
                        } else {
                            //int brPogrSl = brojPogresnihSlova(odabranaRec, unetaSlova);
                            counter++; 
                            crtajSneska(counter);
                        }
                    }

                } else {
                    System.out.println("Pogresan unos! Dozvoljena su samo cirilicna slova!");
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
               continue; //нова итерација, dodavanje kroz svaku iteraciju crtice
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
    
    private void crtajSneska (int counter) {
       
         switch(counter) {
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

