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
'''
runPipeline()
