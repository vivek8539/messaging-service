@Library('jenkins-shared-library@master') _
env['BUILDPOD_YAML'] = '''
apiVersion: v1
kind: Pod
metadata:
  labels:
    job: k8s-pipeline-build
spec:
  containers:
  - name: maven
    image: maven:3.9.9-eclipse-temurin-17
    command:
    - sleep
    args:
    - 99d
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    command:
      - /busybox/sh
    args:
      - -c
      - cat
    tty: true
'''
env['DOCKER_REGISTRY'] = 'https://index.docker.io/v1/'
env['DOCKER_USERNAME'] = 'vivek8539'
runPipeline()
