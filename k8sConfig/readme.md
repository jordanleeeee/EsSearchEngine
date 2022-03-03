# Config k8s

start minikube
```bash
minikube start --kubernetes-version=v1.22.2 --driver=none
```

init secret
```bash
kubectl create secret tls tls-secret -n search --cert=certificate.crt --key=private.key 
```

init setup
```bash
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

