package histoire;
import personnages.Gaulois;
import villagegaulois.Etal;
import villagegaulois.Village;
import villagegaulois.VillageSansChefException;


public class ScenarioCasDegrade {

    public static void main(String[] args) {
        Etal etal = new Etal();
        etal.libererEtal();
        try {
            Gaulois asterix = new Gaulois("Astérix", 8);
            etal.acheterProduit(1, asterix);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        try {
            Village villageSansChef = new Village("Village Test", 10, 5);
            villageSansChef.afficherVillageois();
        } catch (VillageSansChefException e) {
            e.printStackTrace();
        }
        System.out.println("Fin du test");
        }

}
