out_path="../out/production/IscTorrent"

port=$(($1 + 8080))
host="localhost"

javac -d $out_path IscTorrent.java
java -cp $out_path IscTorrent $host $port