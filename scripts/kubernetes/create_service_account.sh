#!/bin/bash

# Create kubeconfig file for one user and one or multi namespaces
#
echo "USER: $1"
echo "NAMESPACES: $2"

function help() {
  echo "The first parameter must be the user name, the second is the namespace."
  exit 1
}

if [ -z $1 ]; then
  echo "first parameter, username, is not set"
  help
fi
if [ -z $2 ]; then
  echo "second parameter, namespace(s), is not set"
  help
fi

user_account=$1
main_namespace=$2

echo "the service account $user_account will be created on the namespace $main_namespace"
kubectl apply -f - <<EOF
apiVersion: v1
kind: ServiceAccount
metadata:
  name: ${user_account}-sa
  namespace: $main_namespace
EOF
kubectl apply -f - <<EOF
apiVersion: v1
kind: Secret
type: kubernetes.io/service-account-token
metadata:
  name: ${user_account}-token
  namespace: $main_namespace
  annotations:
    kubernetes.io/service-account.name: ${user_account}-sa
EOF

echo "configure namespace $main_namespace"
kubectl apply -f - <<EOF
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  namespace: $main_namespace
  name: ${user_account}-role
rules:
- apiGroups:
  - ""
  - extensions
  - apps
  - networking.k8s.io
  - longhorn.io
  - autoscaling
  - batch
  resources:
  - deployments
  - replicasets
  - pods
  - services
  - ingresses
  - volumes
  - replicationcontrollers
  - daemonsets
  - statefulsets
  - horizontalpodautoscalers
  - cronjobs
  - jobs
  - persistentvolumeclaims
  - configmaps
  - namespaces
  - secrets
  - pods/attach
  - pods/exec
  - pods/log
  - pods/portforward
  - roles
  verbs:
  - '*'
---
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: ${user_account}-${main_namespace}-role-binding
  namespace: $main_namespace
subjects:
- kind: ServiceAccount
  name: ${user_account}-sa
  namespace: $main_namespace
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: ${user_account}-role
EOF

server=$( kubectl config view --minify --raw -o jsonpath='{.clusters[].cluster.server}' | sed 's/"//' )
ca=$( kubectl --namespace $main_namespace get secret/${user_account}-token -o jsonpath='{.data.ca\.crt}' )
token=$( kubectl --namespace $main_namespace get secret/${user_account}-token -o jsonpath='{.data.token}' | base64 --decode )
clusterName=k8s-cluster

echo "
apiVersion: v1
kind: Config
clusters:
  - name: ${clusterName}
    cluster:
      certificate-authority-data: ${ca}
      server: ${server}
contexts:
  - name: ${user_account}-sa@${clusterName}
    context:
      cluster: ${clusterName}
      namespace: $main_namespace
      user: ${user_account}-sa
users:
  - name: ${user_account}-sa
    user:
      token: ${token}
current-context: ${user_account}-sa@${clusterName}
" >> ${user_account}-${main_namespace}.yaml
