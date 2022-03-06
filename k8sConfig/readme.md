# Config k8s

init docker, docker-compose(optional), minikube, kubectl
```bash
sudo apt-get update
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh
rm get-docker.sh
sudo usermod -aG docker $USER && newgrp docker

sudo curl -L "https://github.com/docker/compose/releases/download/1.29.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
sudo install minikube-linux-amd64 /usr/local/bin/minikube
rm minikube-linux-amd64

curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl

sudo echo "vm.max_map_count=262144" >> /etc/sysctl.conf
sudo sysctl -p

minikube config set memory 4096
minikube config set cpus 2
minikube config set driver none
sudo apt-get install -y conntrac
minikube start
```

terminal config(optional)
```bash
echo "source <(kubectl completion bash)" >> ~/.bashrc
source ~/.bashrc 

sudo apt-get install fish
mkdir -p ~/.config/fish/completions
cd ~/.config/fish
git clone https://github.com/evanlucas/fish-kubectl-completions
ln -s ../fish-kubectl-completions/completions/kubectl.fish completions/
```

init setup
```bash
kubectl create secret tls tls-secret -n search --cert=certificate.crt --key=private.key
kubectl apply -f namespace.yml
kubectl apply -f pv.yml
kubectl apply -f ingress
```

setup app related
```bash
kubectl apply -f search-service.yml
kubectl apply -f es-statful.yml
```

if you don't like statefulset, you can you pod with empty-dir or pvc volume
```bash
kubectl apply -f es-pod.yml

kubectl apply -f es-pod-v2-pvc.yml
kubectl apply -f es-pod-v2.yml
```

rolling update
```bash
kubectl set image deployment/search-service search-service=jordanleeeee/search-service:{{tag}} -n search
```

