# cahier_de_prepa_archiveur 1.0
Permet de télécharger toute l'arborescence d'un compte sur cahier de prépa.

__LE PROJET EN PYTHON NE SERA PAS MAINTENU, IL SERT DE TRACES, POSSIBLEMENT POUR QUE LES GENS LE FORKENT ET Y TRAVAILLENT__</br>
__LA SEULE PARTIE MAINTENUE EST DESORMAIS CELLE EN JAVA__

Ce projet est une version améliorée du projet de [Azuxul](https://github.com/Azuxul/cahier-de-prepa-downloader) dans la mesure ou son projet se contente de balayer les liens directs de 1 à 100 sur le site. Cependant, le sien est plus ergonomique car on utilise directement l'identifiant et le mot de passe. Ceci dit, je laisse mon inspiration ici.</br>

[Un projet similaire au mien en Rust que j'ai trouvé crée par piirios](https://github.com/piirios/cdp-parser).

Ces différentes rubriques ont à coeur de faire en sorte que tout le monde, même les non-spécialistes, soit capable d'utiliser cet outil.
Si vous vous y connaissez déjà vous pouvez ignorer les paragraphes ci-dessous, la documentation est dans le --help du jar.

### Comment fonctionne le programme ?

Il va simuler des requêtes qu'un utilisateur de cahier de prépa ferait. Pour se faire, il va utiliser vos identifiants et envoyer des requêtes à votre place.</br>
Il va explorer le site et essayer de recréer récursivement toute l'arborescence qu'il découvre en préservant le nommage des noms/chapitres/sections.
Il affiche tout ce qu'il trouve et intègre une "barre de chargement" assez primitive.

# Pré-requis

Il faut un JRE (Java Runtime Environment) 17.
Le projet a été __compilé pour Java 17__.

Les dépendances sont incluses dans l'archive jar.
Il suffit alors d'ouvrir l'archive.<br>
Dans un terminal:
```java -jar cdp.jar```
Ou alors, si votre système le permet, double-cliquer.

# Annexes

En cas de problème, n'hésitez pas à ouvrir un [ticket](https://github.com/Loatchi/cahier_de_prepa_archiveur/issues).</br>

Vous pouvez envoyer des pull requests directement; cependant il est toujours intéressant de parler de vos souhaits d'extension dans la rubrique issue avant.</br>

**Le code a été fait en une nuit, il n'est certainement pas simple à lire.**

## Possibilités d'extensions

- [X] Faire en sorte que le programme trouve tout seul CDP_SESSION ou CDP_SESSION_PERM
- [X] Utiliser IDENTIFIANT/MDP à la place des cookies (voir le fichier `jvm`)
- [X] Rajouter des couleurs à l'output du terminal
- [ ] Télécharger la page d'accueil
- [ ] Faire en sorte de toute mettre directement dans un zip/tar.gz
- [X] Créer une application graphique probablement en Java/Kotlin

 
