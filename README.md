# make-keystore

>  📦 Utility wrapper around the Java keystore cli

I needed a way to create truststores on-the-fly in Kubernetes init-containers.

`make-keystore` creates PKCS12 keystore files composed of a variable amount of input.

## Usage

The binary is self-documented. Run `make-keystore --help` for help.

## Installation

Download a binary from the [latest release](https://github.com/axelrindle/make-keystore/releases/latest)

===== OR =====

Use the [provided container image](https://github.com/users/axelrindle/packages/container/package/make-keystore)

## Examples

### Container

<details>
<summary><code>Docker</code></summary>

```shell
docker run --rm -it \
  --name generate-truststore \
  -v "$(pwd):/mnt" \
  ghcr.io/axelrindle/make-keystore \
    create \
    --input=/etc/ssl/certs \
    --output=/mnt/truststore.p12 \
    --password=foobar
```

</details>

### Kubernetes Init-Container

<details>
<summary><code>pod.yaml</code></summary>

```yaml
apiVersion: v1
kind: Pod
metadata:
  name: java-application
spec:
  initContainers:
  - name: generate-truststore
    image: ghcr.io/axelrindle/make-keystore:latest
    imagePullPolicy: Always
    args:
    - create
    - --input=/etc/ssl/certs
    - --input=/mnt/k8s-root-ca
    - --password=changeme
    - --output=/mnt/truststore/truststore.p12
    volumeMounts:
    - mountPath: /mnt/k8s-root-ca
      name: k8s-root-ca
    - name: truststore
      mountPath: /mnt/truststore
  containers:
  - name: java-application
    image: org/java-application
    env:
      - name: TRUSTSTORE
        value: /mnt/truststore/truststore.p12
    volumeMounts:
    - name: truststore
      mountPath: /mnt/truststore
      readOnly: true
    resources:
      requests:
        cpu: 128m
        memory: 512Mi
      limits:
        cpu: "2"
        memory: 2048Mi
  volumes:
  - name: k8s-root-ca
    configMap:
      name: kube-root-ca.crt
      items:
      - key: ca.crt
        path: k8s-root-ca.pem
  - name: truststore
    emptyDir: {}
```

</details>

## License

[MIT](LICENSE)
