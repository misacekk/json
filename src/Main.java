import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        Path cesta = Path.of("data", "pizzeria.json");
        String obsah = Files.readString(cesta);

        JSONObject root = new JSONObject(obsah);
        JSONArray pizzy = root.getJSONArray("pizzy");

        System.out.println("-------MENU-------");
        while(true){
            System.out.println("1. JÍDELNÍ LÍSTEK");
            System.out.println("2. NEJLEVNĚJŠÍ PIZZA");
            System.out.println("3. NEJDRAŽŠÍ PIZZA");
            System.out.println("4. PRŮMĚRNÁ CENA ZA PIZZU");
            System.out.println("5. PIZZA V CENOVÉM ROZMEZÍ");
            System.out.println("6. PIZZA S URČITOU INGREDIENCÍ");
            System.out.println("7. PIZZA OD NEJLEVNĚJŠÍ PO NEJDRAŽŠÍ");
            System.out.println("8. VYTVOŘ SI SVOU PIZZU");
            System.out.println("9. VÝPIS DVOU NAHODNÝCH PIZZ A URČENÍ LEVNĚJŠÍ");
            int vyber = sc.nextInt();
            sc.nextLine();
            switch(vyber) {
                case 1:
                    for (int i = 0; i < pizzy.length(); i++) {
                        JSONObject pizza = pizzy.getJSONObject(i);
                        System.out.println(pizza.getString("nazev") + " – " + pizza.getInt("cena") + " Kč");
                    }
                    break;
                case 2:
                    String nejlevnejsiJmeno = "";
                    int nejlC = 1000000;
                    for (int i = 0; i < pizzy.length(); i++) {
                        JSONObject pizza = pizzy.getJSONObject(i);
                        if (pizza.getInt("cena") < nejlC) {
                            nejlC = pizza.getInt("cena");
                            nejlevnejsiJmeno = pizza.getString("nazev");
                        }
                    }
                    System.out.println("Nejlevnější pizza: " + nejlevnejsiJmeno + " cena: " + nejlC + " Kč");
                    break;
                case 3:
                    String nejdrazsiJmeno = "";
                    int nejdC = 0;
                    for (int i = 0; i < pizzy.length(); i++) {
                        JSONObject pizza = pizzy.getJSONObject(i);
                        if (pizza.getInt("cena") > nejdC) {
                            nejdC = pizza.getInt("cena");
                            nejdrazsiJmeno = pizza.getString("nazev");
                        }
                    }
                    System.out.println("Nejdražší pizza: " + nejdrazsiJmeno + " cena: " + nejdC + " Kč");
                    break;
                case 4:
                    int soucet = 0;
                    for (int i = 0; i < pizzy.length(); i++) {
                        soucet += pizzy.getJSONObject(i).getInt("cena");
                    }
                    System.out.println("Průměrná cena za pizzu: " + (soucet / pizzy.length()) + " Kč");
                    break;
                case 5:
                    System.out.println("Zadejte nejnižší cenu pizz");
                    int int1 = sc.nextInt();
                    System.out.println("Zadejte nejvyšší cenu pizz");
                    int int2 = sc.nextInt();
                    for (int i = 0; i < pizzy.length(); i++) {
                        JSONObject pizza = pizzy.getJSONObject(i);
                        int cena = pizza.getInt("cena");
                        if (cena >= int1 && cena <= int2) {
                        System.out.println(pizza.getString("nazev") + " – " + pizza.getInt("cena") + " Kč");
                        }
                    }
                    break;
                case 6:
                    System.out.println("Zadejte ingredienci v pizze:");
                    String hledanaIngredience = sc.nextLine();
                    for (int i = 0; i < pizzy.length(); i++) {
                        JSONObject pizza = pizzy.getJSONObject(i);
                        JSONArray ingrediencePole = pizza.getJSONArray("ingredience");
                        boolean pizzaObsahujeIngredience = false;
                        for (int j = 0; j < ingrediencePole.length(); j++) {
                            String aktualniIngredience = ingrediencePole.getString(j);
                            if (aktualniIngredience.equalsIgnoreCase(hledanaIngredience)) {
                                pizzaObsahujeIngredience = true;
                                break;
                            }
                        }
                        if (pizzaObsahujeIngredience) {
                            System.out.println(pizza.getString("nazev") + " – " + pizza.getInt("cena") + " Kč");
                        }
                    }
                    break;
                case 7:
                    List<JSONObject> seznamPizz = new ArrayList<>();
                    for (int i = 0; i < pizzy.length(); i++) {
                        seznamPizz.add(pizzy.getJSONObject(i));
                    }
                    seznamPizz.sort((pizza1, pizza2) -> Integer.compare(pizza1.getInt("cena"), pizza2.getInt("cena")));
                    for (JSONObject pizza : seznamPizz) {
                        System.out.println(pizza.getString("nazev") + " – " + pizza.getInt("cena") + " Kč");
                    }
                    break;
                case 8:
                    sc.nextLine();
                    System.out.println("Napiš název:");
                    String nzv = sc.nextLine().trim();

                    System.out.println("Napiš 3 ingredience (každou potvrď Enterem):");
                    String ing1 = sc.nextLine().trim();
                    String ing2 = sc.nextLine().trim();
                    String ing3 = sc.nextLine().trim();

                    System.out.println("Napiš cenu:");
                    int cn = sc.nextInt();

                    JSONArray noveIngredience = new JSONArray();
                    noveIngredience.put(ing1);
                    noveIngredience.put(ing2);
                    noveIngredience.put(ing3);

                    JSONObject novaPizza = new JSONObject();
                    novaPizza.put("nazev", nzv);
                    novaPizza.put("ingredience", noveIngredience);
                    novaPizza.put("cena", cn);

                    pizzy.put(novaPizza);

                    System.out.println("Pizza \"" + nzv + "\" byla úspěšně přidána!");
                    break;

                case 9:
                    Random rd = new Random();
                    int nahodnyIndex1 = rd.nextInt(pizzy.length());
                    int nahodnyIndex2 = rd.nextInt(pizzy.length());

                    while (nahodnyIndex1 == nahodnyIndex2) {
                        nahodnyIndex2 = rd.nextInt(pizzy.length());
                    }

                    JSONObject nahodnaPizza1 = pizzy.getJSONObject(nahodnyIndex1);
                    JSONObject nahodnaPizza2 = pizzy.getJSONObject(nahodnyIndex2);

                    int cena1 = nahodnaPizza1.getInt("cena");
                    int cena2 = nahodnaPizza2.getInt("cena");

                    System.out.println("--- POROVNÁNÍ DVOU NÁHODNÝCH PIZZ ---");
                    System.out.println("1. Vylosovaná: " + nahodnaPizza1.getString("nazev") + " (" + cena1 + " Kč)");
                    System.out.println("2. Vylosovaná: " + nahodnaPizza2.getString("nazev") + " (" + cena2 + " Kč)");
                    System.out.println("------------------------------------");

                    if (cena1 < cena2) {
                        System.out.println("Výhodnější tip pro vás: " + nahodnaPizza1.getString("nazev") + " za " + cena1 + " Kč");
                    } else if (cena2 < cena1) {
                        System.out.println("Výhodnější tip pro vás: " + nahodnaPizza2.getString("nazev") + " za " + cena2 + " Kč");
                    } else {
                        System.out.println("Obě pizzy stojí stejně (" + cena1 + " Kč), vyberte si podle chuti!");
                    }
                    break;
            }
        }
    }
}