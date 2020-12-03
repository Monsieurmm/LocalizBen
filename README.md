# LocalizBen
Une application permettant de scan le QRCode d'une benne et d'envoyer un SMS contenant les informations de la benne ainsi que les coordonnées GPS de celle-ci.
## Table of Contents
* [General info](#general-info)
* [Scenarii](#scenarii)
## General info
Dans un premier temps, l’utilisateur ouvre l’application.
Il atterrit sur une page lui permettant, via un bouton, de démarrer le processus de scan de QR Code, l'application ouvre alors l'appareil photo.
L'utilisateur oriente la caméra de manière à avoir le QR Code dans son champ de vision.
Une fois le QR Code scanné, l'appareil photo se ferme. Les données relatives à la benne ainsi que les coordonnées GPS sont ensuite envoyées par SMS.
## Scenarii
* En tant qu’utilisateur, je veux pouvoir exploiter l’appareil photo du téléphone afin de pouvoir scanner un QR Code.
* En tant qu’utilisateur, je veux pouvoir être en capacité extraire de ce QR Code les informations relatives à la benne scannée.
* En tant qu’utilisateur, je veux pouvoir être en capacité d’exploiter le GPS du téléphone afin de détecter mon emplacement.
* En tant qu’utilisateur, je veux pouvoir être en capacité d'envoyer un SMS à mon employeur contenant les informations de la benne et où elle se situe.
