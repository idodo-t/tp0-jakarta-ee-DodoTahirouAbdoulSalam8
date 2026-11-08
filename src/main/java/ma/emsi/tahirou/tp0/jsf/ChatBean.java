package ma.emsi.tahirou.tp0.jsf;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.emsi.tahirou.tp0.llm.GenerateurReponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Backing bean de la page index.xhtml.
 * Portée @ViewScoped : l'instance survit aux requêtes POST successives sur la même vue,
 * ce qui permet de conserver l'historique de la conversation.
 */
@Named("chatBean")
@ViewScoped
public class ChatBean implements Serializable {

    private String roleSysteme;
    private boolean roleSystemeChangeable = true;
    private String question;
    private String reponse;
    private final StringBuilder conversation = new StringBuilder();

    @Inject
    private FacesContext facesContext;

    @Inject
    private GenerateurReponse generateur;

    public ChatBean() {
    }

    public String getRoleSysteme() { return roleSysteme; }
    public void setRoleSysteme(String roleSysteme) { this.roleSysteme = roleSysteme; }

    public boolean isRoleSystemeChangeable() { return roleSystemeChangeable; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getReponse() { return reponse; }

    public String getConversation() { return conversation.toString(); }
    public void setConversation(String texte) { /* lecture seule : valeur ignorée */ }

    /** Liste des rôles proposés dans le menu déroulant. */
    public List<SelectItem> getRolesDisponibles() {
        List<SelectItem> roles = new ArrayList<>();
        for (GenerateurReponse.Role r : GenerateurReponse.Role.values()) {
            roles.add(new SelectItem(r.name(), r.getLibelle()));
        }
        return roles;
    }

    /** Traite la question : appelle le générateur et ajoute l'échange à l'historique. */
    public String envoyer() {
        // Deuxième garde-fou : la validation JSF (required) est déjà faite, mais on vérifie aussi côté bean.
        if (question == null || question.isBlank()) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Texte vide", "Entrez un texte à traiter."));
            return null;
        }
        if (roleSysteme == null || roleSysteme.isBlank()) {
            facesContext.addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rôle manquant", "Choisissez un rôle."));
            return null;
        }
        this.reponse = generateur.genererReponse(roleSysteme, question);
        afficherConversation();
        this.roleSystemeChangeable = false;
        this.question = "";
        return null;
    }

    /** Réinitialise la conversation en recréant une nouvelle vue (donc un nouveau bean @ViewScoped). */
    public String nouveauChat() {
        return "index?faces-redirect=true";
    }

    private void afficherConversation() {
        conversation.append("== Rôle : ").append(roleSysteme).append(" ==\n");
        conversation.append("Utilisateur : ").append(question).append("\n");
        conversation.append("Système : ").append(reponse).append("\n\n");
    }
}
