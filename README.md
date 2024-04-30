# Run the app
1) The easiest way to run the app is to download ``docker-compose.yml`` file and write `docker-compose up -d` in a command line.

2) Not the easiest way is to download the whole project, type ``mvn clean package`` then go to the target folder and type ``java java -DJWT_SECRET=*secret* -DJWT_EXPIRATION=*expiration* -jar manageclients-0.0.1.jar`` where *secret* is any string, *expiration* is any positive integer.
