@Library('jenkins-shared-library@master') _
env['BUILDPOD_YAML'] = '''
apiVersion: v1
kind: Pod
metadata:
  labels:
    job: k8s-pipeline-build
spec:
  volumes:
  - name: docker-config
    secret:
      secretName: dockerhub-secret
      items:
        - key: .dockerconfigjson
          path: config.json
  - name: maven-cache-storage
    persistentVolumeClaim:
      claimName: cache-pvc
  containers:
  - name: maven
    image: maven:3.9.9-eclipse-temurin-17
    command:
    - sleep
    args:
    - 99d
    volumeMounts:
      - name: maven-cache-storage
        mountPath: /root/.m2
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    imagePullPolicy: Always
    command:
    - /busybox/cat
    tty: true
    volumeMounts:
      - name: docker-config
        mountPath: /kaniko/.docker
  - name: deploy
    image: alpine/helm:3.17.0
    command:
    - sleep
    args:
    - 99d
'''
env['DOCKER_REGISTRY'] = 'https://index.docker.io/v1/'
env['DOCKER_USERNAME'] = 'vivek8539'
runPipeline()
