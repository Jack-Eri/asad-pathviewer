# ASAD - Lab 2  - Path Viewer

> Nohan Budry & Vincent Rubin

## Utilisation

Pour démarré le projet, il faut dabort lancé le docker compose. Depuis un terminal dans le même dossier que le fichier `docker-compose.yml`, utilisez la command suivante.

```
docker compose up --scale asad-data=3 --scale asad-computation=3 -d --build
```

- **--scale** permet de choisir le nombre d'instance pour les couches de donnée et de calcul.
- **-d** permet de lancer les containers en arrière plan. Il peut être omis pour voir les logs.
- **--build** permet de forcer la création des images des containers de donnée et de calcul. Il peut ètre omis et les images seront build seulement si elle ne sont pas déjà existantes.

Pour arrêter les containers vous pouvez faire `docker compose stop`. Pour les supprimmer de l'ordinateur, vous pouvez faire `docker compose down` ou `docker compose rm`. La commande `docker compose ps -a` permet de vois les containers existants.

Une fois les containers lancé, les deux API HTTP sont disponibles avec les url suivante:

- `http://localhost:8080/data` 
- `http://localhost:8080/computation` 

Vous pouvez entrer ces liens dans un navigateur et ajouter le chemin `/health` pour voir s'ils ont bien été démarrée. Vous verrez aussi le load balancer en action. 

Maintenant vous pouvez exécuter le path viewer. Pour cela, déplacé-vous vers le fichier jar:

`cd asad-visualization/target/`

Une fois dans se dossier, vous pouvez lance le programme:

`java -jar path-viewer.jar`

### Notes

- Pour une question de simplicité les données de la base de données ne sont pas stocké sur l'ordinateur mais simplement dans le container. Tant que le container n'est pas supprimmer les données resteront disponibles.
- Il est possible que le "reverse proxy" (Traefik) crash de temps en temps. Il doit y avoir un problème de configuration à quelque part mais noun n'avions pas eu le temps de le résoudre. Si cela arrive, vous pouvez arrêter, supprimmer et redémarré les containers avec les commandes vu précédemment.