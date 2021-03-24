mvn clean install -Dmaven.test.skip=true
sudo docker build -t parent .
sudo docker stop parent
sudo docker rm parent
sudo docker run -d --name parent -p 80:8080 parent
