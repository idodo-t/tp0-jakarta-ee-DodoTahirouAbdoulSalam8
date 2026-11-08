# Commandes Git conseillées (un commit + push par étape)

git init
git add . && git commit -m "Etat initial de l'application"
git branch -M main
git remote add origin https://github.com/<ton-compte>/tp0-tahirou.git
git push -u origin main

# Puis, au fil des étapes (si tu refais le TP étape par étape dans IntelliJ) :
git commit -am "Ajout de la page index.xhtml (JSF)"            && git push
git commit -am "Ajout de la dépendance PrimeFaces 16.0.0"        && git push
git commit -am "Ajout du backing bean ChatBean (@ViewScoped)"    && git push
git commit -am "Ajout de GenerateurReponse (@Dependent)"         && git push
git commit -am "Page d'accueil index.xhtml dans web.xml"         && git push
git commit -am "Tests OK"                                        && git push
git commit -am "Traitement personnel : Laboratoire de texte"     && git push
