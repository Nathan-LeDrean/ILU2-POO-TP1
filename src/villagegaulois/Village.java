package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
    private String nom;
    private Chef chef;
    private Gaulois[] villageois;
    private int nbVillageois = 0;
    private Marche marche;

    public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
        this.nom = nom;
        this.villageois = new Gaulois[nbVillageoisMaximum];
        this.marche = new Marche(nbEtals);
    }

    public String getNom() {
        return nom;
    }

    public void setChef(Chef chef) {
        this.chef = chef;
    }

    public void ajouterHabitant(Gaulois gaulois) {
        if (nbVillageois < villageois.length) {
            villageois[nbVillageois] = gaulois;
            nbVillageois++;
        }
    }

    public Gaulois trouverHabitant(String nomGaulois) {
        if (chef != null && nomGaulois.equals(chef.getNom())) {
            return chef;
        }
        for (int i = 0; i < nbVillageois; i++) {
            Gaulois gaulois = villageois[i];
            if (gaulois.getNom().equals(nomGaulois)) {
                return gaulois;
            }
        }
        return null;
    }

    public String afficherVillageois() throws VillageSansChefException {
        if (chef == null) {
            throw new VillageSansChefException("Le village ne possède pas de chef !");
        }
        StringBuilder chaine = new StringBuilder();
        if (nbVillageois < 1) {
            chaine.append("Il n'y a encore aucun habitant au village du chef ")
                  .append(chef.getNom()).append(".\n");
        } else {
            chaine.append("Au village du chef ").append(chef.getNom())
                  .append(" vivent les légendaires gaulois :\n");
            for (int i = 0; i < nbVillageois; i++) {
                chaine.append("- ").append(villageois[i].getNom()).append("\n");
            }
        }
        return chaine.toString();
    }

    // --- Méthodes du Village interagissant avec Marche ---

    public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
        StringBuilder chaine = new StringBuilder();
        chaine.append(vendeur.getNom()).append(" cherche un endroit pour vendre ")
              .append(nbProduit).append(" ").append(produit).append(".\n");
        int indiceEtal = marche.trouverEtalLibre();
        if (indiceEtal != -1) {
            marche.utiliserEtal(indiceEtal, vendeur, produit, nbProduit);
            chaine.append("Le vendeur ").append(vendeur.getNom())
                  .append(" vend des ").append(produit)
                  .append(" à l'étal n°").append(indiceEtal + 1).append(".\n");
        } else {
            chaine.append("Il n'y a plus d'étal libre au marché pour ")
                  .append(vendeur.getNom()).append(".\n");
        }
        return chaine.toString();
    }

    public String rechercherVendeursProduit(String produit) {
        StringBuilder chaine = new StringBuilder();
        Etal[] etalsProduit = marche.trouverEtals(produit);
        if (etalsProduit.length == 0) {
            chaine.append("Il n'y a pas de vendeur qui propose des ")
                  .append(produit).append(" au marché.\n");
        } else if (etalsProduit.length == 1) {
            chaine.append("Seul le vendeur ").append(etalsProduit[0].getVendeur().getNom())
                  .append(" propose des ").append(produit).append(" au marché.\n");
        } else {
            chaine.append("Les vendeurs qui proposent des ").append(produit).append(" sont :\n");
            for (Etal etal : etalsProduit) {
                chaine.append(etal.getVendeur().getNom()).append("\n");
            }
        }
        return chaine.toString();
    }

    public Etal rechercherEtal(Gaulois vendeur) {
        return marche.trouverVendeur(vendeur);
    }

    public String partirVendeur(Gaulois vendeur) {
        Etal etal = marche.trouverVendeur(vendeur);
        if (etal != null) {
            return etal.libererEtal();
        }
        return vendeur.getNom() + " n'a pas d'étal au marché.\n";
    }

    public String afficherMarche() {
        StringBuilder chaine = new StringBuilder();
        chaine.append("Le marché du village \"").append(nom)
              .append("\" possède plusieurs étals :\n");
        chaine.append(marche.afficherMarche());
        return chaine.toString();
    }

    // --- Classe interne Marche ---
    // 'private' car réservée à Village, et 'static' car elle n'accède pas aux champs d'instance de Village.
    private static class Marche {
        private Etal[] etals;

        public Marche(int nbEtals) {
            this.etals = new Etal[nbEtals];
            for (int i = 0; i < nbEtals; i++) {
                this.etals[i] = new Etal();
            }
        }

        public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
            etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
        }

        public int trouverEtalLibre() {
            for (int i = 0; i < etals.length; i++) {
                if (!etals[i].isEtalOccupe()) {
                    return i;
                }
            }
            return -1;
        }

        public Etal[] trouverEtals(String produit) {
            int nbEtalsTrouves = 0;
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
                    nbEtalsTrouves++;
                }
            }
            Etal[] resultat = new Etal[nbEtalsTrouves];
            int index = 0;
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.contientProduit(produit)) {
                    resultat[index] = etal;
                    index++;
                }
            }
            return resultat;
        }

        public Etal trouverVendeur(Gaulois gaulois) {
            for (Etal etal : etals) {
                if (etal.isEtalOccupe() && etal.getVendeur().equals(gaulois)) {
                    return etal;
                }
            }
            return null;
        }

        public String afficherMarche() {
            StringBuilder chaine = new StringBuilder();
            int nbEtalVide = 0;
            for (Etal etal : etals) {
                if (etal.isEtalOccupe()) {
                    chaine.append(etal.afficherEtal());
                } else {
                    nbEtalVide++;
                }
            }
            if (nbEtalVide > 0) {
                chaine.append("Il reste ").append(nbEtalVide)
                      .append(" étals non utilisés dans le marché.\n");
            }
            return chaine.toString();
        }
    }
}