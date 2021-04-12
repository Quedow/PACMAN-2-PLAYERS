# PACMAN-2-PLAYERS
# Introduction :
C'est dans le cadre d'un projet scolaire (en duo) de 2ème année d'école d'ingénieurs que nous nous sommes lancé dans la programmation d'un Pac-Man jouable à 2 joueurs sur le même ordinateur. Il reprend les mêmes mécaniques que le Pac-Man classique à la différence que dans celui-ci, il faudra faire attention aux collisions entre joueurs qui font perdre des vies, au bonus qui ne transforme pas obligatoirement le joueur en mode berseker mais qui peut aussi donner une vie supplémentaire ou un bonus de score. Les différentes sorties de map ne mènent pas obligatoirement à la sortie opposée : c'est alátoire.

# À disposition :
Si vous souhaitez regarder ou modifier le code source celui-ci est disponible sous le nom de dossier "PACMAN2P". Le jeu a été codé en JavaFx sur Intellij. Si vous souhaitez uniquement jouer au jeu, il vous suffit de télécharger uniquement le dossier "PACMAN2P (build)".

# Fonctionnalités :
1. Vous avez la possibilité de personnaliser la map.
2. Vous pouvez télécharger des skins sur internet pour personnaliser votre jeu (.gif pour le joueur classique et .png pour le joueur berseker et les ennemis). Les noms des images doivent impérativement rester le même que l'original.
3. Dans le menu principal, vous pouvez choisir de jouer à 1 ou 2 joueurs ainsi que le nombre d'ennemis.

# Règle de création d'une Map :
Afin de créer votre map vous devez créer un fichier texte qui portera le nom du fichier contenant la map de base "PACMAN_MAP.txt". Je vous conseille de conserver la map de base soit sous un autre nom ou dans un autre dossier afin de pouvoir la récupérer à tout moment. Lorsque vous commencerez à composer votre map, vous devrez respecter le code suivant :
- Mettre un "0" pour le spawn des ennemis (obligatoire),
- Mettre un "1" pour les murs,
- Mettre un "2" pour les chemins contenant une bille de score,
- Mettre un "3" pour les chemins vides,
- Mettre un "4" pour les chemins contenant un bonus.

# Les bonus :
Il y aura 3 types de bonus qui seront générés aléatoirement selon des probabilités prédéfinies :
- Mode berserk : 3/5;
- Une vie supplémentaire : 1/5;
- 200 points de score supplémentaires : 1/5.
