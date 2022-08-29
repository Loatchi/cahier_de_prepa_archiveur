# cahier_de_prepa_archiveur 1.0
Permet de télécharger toute l'arborescence d'un compte sur cahier de prépa.

Le tutoriel fonctionne toujours, cependant les sources utilisées sont celles dans le répertoire `py` car un projet d'interface graphique a été commencé en Java.
Pour les personnes voulant utiliser Java et non pas Python, le projet est relativement similaire. Le Jar est dans les releases. A noter que dans la version Java, il n'est plus nécessaire de chercher le cookie CDP_SESSION_PERM mais on utilise directement l'identifiant et le mot de passe.

__LE PROJET EN PYTHON NE SERA PAS MAINTENUE, IL SERT DE TRACES, POSSIBLEMENT POUR QUE LES GENS LE FORKENT ET Y TRAVAILLENT__</br>
__LA SEULE PARTIE MAINTENUE EST DESORMAIS CELLE EN JAVA__

Ce projet est une version améliorée du projet de [Azuxul](https://github.com/Azuxul/cahier-de-prepa-downloader) dans la mesure ou son projet se contente de balayer les liens directs de 1 à 100 sur le site. Cependant, le sien est plus ergonomique car on utilise directement l'identifiant et le mot de passe. Ceci dit, je laisse mon inspiration ici.</br>

[Un projet similaire au mien en Rust que j'ai trouvé crée par piirios](https://github.com/piirios/cdp-parser).

Ces différentes rubriques ont à coeur de faire en sorte que tout le monde, même les non-spécialistes, soit capable d'utiliser cet outil.
Si vous vous y connaissez déjà vous pouvez ignorer les paragraphes ci-dessous, la documentation est déjà inscrite dans le help du fichier python.

### Comment fonctionne le programme ?

Il va simuler des requêtes qu'un utilisateur de cahier de prépa ferait. Pour se faire, il va utiliser vos identifiants et envoyer des requêtes à votre place.</br>
Il va explorer le site et essayer de recréer récursivement toute l'arborescence qu'il découvre en préservant le nommage des noms/chapitres/sections.
Il affiche tout ce qu'il trouve et intègre une "barre de chargement" assez primaire du type 34/3293.

# Pré-requis

Pour utiliser l'application il faut un interpréteur Python. La dernière version de l'interpréteur devrait fonctionner (min version 3.10).
Faites bien attention à ne pas utiliser un outil de développement intégré (Spyder ou Edupython).
Pour vérifier la version de votre interpréteur, depuis un terminal:
`python --version`

Il faut `anytree` à télécharger en faisant dans un terminal:
```
pip install anytree
```

Si pip n'est pas reconnu, il faut l'installer.

#### Windows

L'interprétateur se télécharge depuis ce [site](https://www.python.org/downloads/). Il suffit de suivre le menu qui s'affiche pendant l'installation.

#### Linux

Généralement une version de python est déjà présente dans votre distribution, si elle n'est pas à jour vous pouvez suivre la documentation de votre distribution pour que ce soit le cas. 

# Téléchargement

#### Linux

Pour télécharger, décompresser et rendre executable les fichiers sources:
```
wget https://github.com/Loatchi/cahier_de_prepa_archiveur/archive/refs/tags/cahier_de_prepa.tar.gz
tar xpf cahier_de_prepa.tar.gz
chmod u+x cahier_de_prepa_archiveur-cahier_de_prepa/cdp_dl.py
```

#### Windows

Il faut télécharger le [zip](https://github.com/Loatchi/cahier_de_prepa_archiveur/releases/tag/cahier_de_prepa) et ensuite l'extraire.

# Utilisation

#### Windows

Win + R et taper "cmd" et ensuite Entrée.
Pour executer le programme:
```
python E:\\chemin\vers\le\fichier\cdp_dl.py
```

#### Linux

```
/chemin/vers/cdp_dl.py
```

# Derniers préparatifs

Quelque soit votre système d'exploitation, le programme a besoin d'avoir vos identifiants cahier de prépas. Il les récupère via un cookie; exactement comme ceux qui nous permettent d'être connecté à gmail même après avoir éteint notre navigateur.
*  __Il faut vous rendre sur votre site cahier de prépa à vous et vous authentifier avec "Se Souvenir de Moi".__

Ce qui vous permettra d'ouvrir le menu développeur de votre navigateur:

##### Navigateurs basés sur Chromium (Brave, Edge, Chrome, Opera, Vivaldi, Chromium)

*  Faites Ctrl + Shift + I, cela ouvrira le menu développeur.
*  Allez dans la section "Application" puis dans la sous-section "Cookies".
*  Cliquez sur le déroulant `https://cahier-de-prepa.fr/...`
*  Gardez la valeur de CDP_SESSION_PERM

##### Navigateurs basés sur Firefox

Je n'ai pas de navigateur avec la même base que celle de Firefox. Tout commit pour vider ce trou est la bienvenue !

# Extraire toute votre archive cahier de prépa !
 
Désormais on sait éxecuter un fichier et on connait le cookie associé à votre compte !</br>
Le compte y est !</br>

Il reste simplement à dire au programme dans quelle classe vous êtes et quel est votre cookie qui vous identifie !</br>
```
python3 chemin/vers/cdp_dl.py --class CLASSE --cookie CDP_SESSION_PERM_COOKIE
```

En remplaçant "CLASSE" par la partie qui identifie votre classe dans cahier de prépa, c'est CLASSE dans:
`https://cahier-de-prepa.fr/CLASSE/`</br>

Et en remplaçant "CDP_SESSION_PERM_COOKIE" par la valeur du cookie récupéré tout à l'heure !</br>

Le programme peut prendre jusqu'à 20/30 min, il va créer un répertoire nommé "CDP-CLASSE" où CLASSE est la donnée renseignée précédemment. Dans ce répertoire se trouvera tout ce qu'il a téléchargé. Pour l'instant, le programme ne télécharge pas les fichiers de la page d'accueil.

# Annexes

En cas de problème, n'hésitez pas à ouvrir un [ticket](https://github.com/Loatchi/cahier_de_prepa_archiveur/issues).</br>

Vous pouvez envoyer des pull request directement; cependant il est toujours intéressant de parler de vos souhaits d'extension dans la rubrique issue avant.</br>

**Le code a été fait en une nuit, il n'est certainement pas simple à lire.**

## Possibilités d'extensions

*  Faire en sorte que le programme trouve tout seul CDP_SESSION ou CDP_SESSION_PERM
*  Utiliser IDENTIFIANT/MDP à la place des cookies
*  Rajouter des couleurs à l'output du terminal
*  Télécharger la page d'accueil
*  Faire en sorte de toute mettre directement dans un zip/tar.gz
*  Créer une application graphique probablement en Java/Kotlin

 
