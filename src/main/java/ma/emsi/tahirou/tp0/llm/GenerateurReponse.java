package ma.emsi.tahirou.tp0.llm;

import jakarta.enterprise.context.Dependent;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

/**
 * Traitement personnel (bonus) : le "Laboratoire de texte".
 * Le rôle choisi décide du traitement appliqué au texte saisi :
 * analyse statistique, chiffrement de César ou inversion miroir.
 * Portée @Dependent : une instance est créée pour chaque bean qui l'injecte.
 */
@Dependent
public class GenerateurReponse implements Serializable {

    public enum Role {
        ANALYSEUR("Analyseur : statistiques du texte"),
        CESAR("Chiffreur : code de César (décalage 3)"),
        MIROIR("Miroir : texte inversé");

        private final String libelle;

        Role(String libelle) { this.libelle = libelle; }

        public String getLibelle() { return libelle; }
    }

    public String genererReponse(String role, String texte) {
        Role r;
        try {
            r = Role.valueOf(role);
        } catch (IllegalArgumentException e) {
            return "Rôle inconnu : " + role;
        }
        return switch (r) {
            case ANALYSEUR -> analyser(texte);
            case CESAR -> cesar(texte, 3);
            case MIROIR -> new StringBuilder(texte).reverse().toString();
        };
    }

    private String analyser(String texte) {
        String[] mots = texte.trim().split("\\s+");
        long lettres = texte.chars().filter(Character::isLetter).count();
        long voyelles = sansAccents(texte).toLowerCase(Locale.ROOT).chars()
                .filter(c -> "aeiouy".indexOf(c) >= 0).count();
        String plusLong = Arrays.stream(mots)
                .map(m -> m.replaceAll("[^\\p{L}\\p{N}'-]", ""))
                .max(Comparator.comparingInt(String::length)).orElse("");
        String nettoye = sansAccents(texte).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        boolean palindrome = !nettoye.isEmpty() && new StringBuilder(nettoye).reverse().toString().equals(nettoye);
        return String.format("%d caractère(s), %d mot(s), %d lettre(s) dont %d voyelle(s). "
                        + "Mot le plus long : « %s ». Palindrome : %s.",
                texte.length(), mots.length, lettres, voyelles, plusLong, palindrome ? "oui" : "non");
    }

    private String cesar(String texte, int decalage) {
        StringBuilder sb = new StringBuilder();
        for (char c : texte.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char) ('a' + (c - 'a' + decalage) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                sb.append((char) ('A' + (c - 'A' + decalage) % 26));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private String sansAccents(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
    }
}
